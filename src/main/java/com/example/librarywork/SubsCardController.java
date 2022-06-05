package com.example.librarywork;

import com.example.librarywork.classesCompenents.CopyBookTable;
import com.example.librarywork.classesCompenents.SubsCardTable;
import com.example.librarywork.classesCompenents.SubscriberTable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

/**
 * Класс для работы с абонементной карточкой и формуляром читателя.
 * <p>
 * Данный класс позволяет скачать текстовый файл абонементной карточки и формуляра читателя, продлить срок возврата выданных книг
 *  и оформить возврат.
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class SubsCardController {
    /** Таблица записей выдачи абоненту книг */
    public TableView<SubsCardTable> tvSubsCard;
    /** Столбец инвентарного номера */
    public TableColumn<SubsCardTable, Integer> tcInvNum;
    /** Столбец даты выдачи */
    public TableColumn<SubsCardTable, String> tcDateIssue;
    /** Столбец даты возврата */
    public TableColumn<SubsCardTable, String> tcDateReturn;
    /** Столбец отдела */
    public TableColumn<SubsCardTable, String> tcDepartment;
    /** Столбец авторов и заголовка книги */
    public TableColumn<SubsCardTable, String> tcAuthorsBookName;
    /** Столбец актуальности записи */
    public TableColumn<SubsCardTable, Boolean> tcRelevance;
    /** Текстовое поле для данных формуляра читателя */
    public TextArea taDataSubs;
    /** Абонент */
    private SubscriberTable subscriber;
    /** Окно приложения */
    private Stage dialogStage;
    /** Адрес базы данных */
    final String DB_URL = "jdbc:mysql://localhost:3306/library?useSSL=false";
    /** Логин для подключения к БД */
    final String LOGIN = "root";
    /** Пароль для подключения к БД */
    final String PASSWORD = "root_root";
    /** Statement для выполнения SQL запросов */
    public Statement statement;
    /** Connection для подключения к БД */
    private Connection connection;
    /** Список записей абонементной карточки */
    ObservableList<SubsCardTable> listRecordsSC;
    /** Список записей, по которым будет оформлен возврат */
    ObservableList<SubsCardTable> listBooksForReturn;
    /**
     * Функция изменения абонента, которому выдается книга {@link SubsCardController#subscriber}
     * @param subscriber абонент
     */
    public void setSubscriber(SubscriberTable subscriber){
        this.subscriber = subscriber;
        selectData();
    }
    /**
     * Функция изменения окна приложения {@link SubsCardController#dialogStage}
     * @param addStage окно прриложения
     */
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }
    /**
     * Функция инициализации класса
     */
    @FXML
    void initialize(){
        connectDB();
    }
    /**
     * Функция заполнения формуляра читателя {@link SubsCardController#taDataSubs} и абонементной карточки
     * {@link SubsCardController#tvSubsCard}
     */
    private void selectData() {
        try {
            listRecordsSC = FXCollections.observableArrayList();
            listBooksForReturn = FXCollections.observableArrayList();
            ResultSet rsDataSubs = statement.executeQuery("Select * from Subscribers where id = "+subscriber.getSubsNumber());
            rsDataSubs.next();
            StringBuilder dataSubs = new StringBuilder();
            dataSubs.append("№\t\t" + rsDataSubs.getInt("Id"));
            dataSubs.append("\nФамилия\t\t" + rsDataSubs.getString("FirstName"));
            dataSubs.append("\nИмя, Отчество\t\t" + rsDataSubs.getString("SecondName")
                    + " "+ rsDataSubs.getString("MiddleName"));
            StringBuilder address = new StringBuilder();
            address.append(rsDataSubs.getString("Street")); address.append(", ");
            address.append(rsDataSubs.getString("House"));
            if (rsDataSubs.getString("Flat").length()>0) {
                address.append(", ");
                address.append(rsDataSubs.getString("Flat"));
            }
            dataSubs.append("\nДомашний адрес\t\t" + address.toString());
            dataSubs.append("\nСостоит читателем библиотеки с\t\t" + rsDataSubs.getDate("DateRegistration"));

            taDataSubs.setText(dataSubs.toString());
            rsDataSubs.close();

            ResultSet rsSubsNum = statement.executeQuery("Select * from HistoriesReader " +
                    "join CopiesBook on CopiesBook.Id = historiesReader.CopyBookId " +
                    "join Editions on Editions.Id = CopiesBook.EditionId " +
                    "join Books on Books.Id = Editions.BookId " +
                    "join Genres on Genres.Id = Books.GenreId " +
                    "join Books_Authors on Books_Authors.BookId = Books.Id " +
                    "join Authors on Authors.Id = Books_Authors.AuthorId " +
                    "where HistoriesReader.SubscriberId = " + subscriber.getSubsNumber());
            while (rsSubsNum.next()){
                SubsCardTable subsCardTable = new SubsCardTable(rsSubsNum.getInt("HistoriesReader.Id"),
                        rsSubsNum.getInt("CopiesBook.Id"), rsSubsNum.getString("Department"),
                        rsSubsNum.getString("Books.Name"), rsSubsNum.getDate("DateIssue").toString(),
                        rsSubsNum.getDate("DateReturn").toString(), rsSubsNum.getInt("BookId"),
                        ShortName(rsSubsNum.getString("FirstName"), rsSubsNum.getString("LastName"),
                                rsSubsNum.getString("MiddleName")).toString(),
                        rsSubsNum.getBoolean("Relevance"));
                listRecordsSC.add(subsCardTable);
            }
            tcInvNum.setCellValueFactory(cellData ->
                    cellData.getValue().inventoryNumberProperty().asObject());
            tcDepartment.setCellValueFactory(subsCardTableStringCellDataFeatures
                    -> subsCardTableStringCellDataFeatures.getValue().departmentProperty());
            tcDateIssue.setCellValueFactory(subsCardTableStringCellDataFeatures
                    -> subsCardTableStringCellDataFeatures.getValue().dateIssueProperty());
            tcDateReturn.setCellValueFactory(subsCardTableStringCellDataFeatures
                    -> subsCardTableStringCellDataFeatures.getValue().dateReturnProperty());
            tcAuthorsBookName.setCellValueFactory(subsCardTableStringCellDataFeatures
                    -> subsCardTableStringCellDataFeatures.getValue().authorsBookNameProperty());
            tcRelevance.setCellFactory( copy -> new CheckBoxTableCell());
            tcRelevance.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SubsCardTable, Boolean>,
                    ObservableValue<Boolean>>() {
                @Override
                public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<SubsCardTable, Boolean> param) {
                    SubsCardTable person = param.getValue();

                    SimpleBooleanProperty booleanProp = person.issueProperty();
                    booleanProp.addListener(new ChangeListener<Boolean>() {

                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                                            Boolean newValue) {
                            if (!newValue){
                                person.setIssue(newValue);
                                listBooksForReturn.add(person);
                            }
                        }
                    });
                    return booleanProp;
                }
            });

            tvSubsCard.setItems(listRecordsSC);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Функция осуществления подключения к БД и инициализация поля {@link SubsCardController#statement}
     * @return состояние подключения
     */
    public boolean connectDB(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loading succesfull.");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Driver loading failed!");
            return false;

        }
        try{
            connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Connection error!");
            return false;
        }
        return true;
    }
    /**
     * Функция получения короткого имени автора из списка авторов произведения, экземпляр которого есть в абонементной карточке
     * @param firstName фамилия автора
     * @param lastName имя автора
     * @param middleName отчество автора
     * @return короткое имя автора
     */
    private StringBuilder ShortName(String firstName, String lastName, String middleName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(firstName);
        stringBuilder.append(" ");
        stringBuilder.append(lastName.charAt(0));
        stringBuilder.append(".");
        if (!middleName.equals("") && !middleName.equals(" ")){
            stringBuilder.append(middleName.charAt(0));
            stringBuilder.append(".");
        }
        return stringBuilder;
    }
    /**
     * Функция сохранения файла с заданным названием и текстом
     * @param title название файла
     * @param text текст
     */
    private void downloadFile(String title, String text){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберете папку для сохранения");
        fileChooser.setInitialFileName(title);
        File file = fileChooser.showSaveDialog(dialogStage);
        if (file != null) {
            try(FileWriter writer = new FileWriter(file.getParent() + "/"+ title + ".txt", false)){
                writer.write(text);
                writer.flush();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Файл создан.", ButtonType.OK);
                alert.showAndWait();
            }
            catch(IOException ex){
                System.out.println(ex.getMessage());
            }
        }
    }
    /**
     * Функция сохранения абонементной карточки.
     */
    public void onLoadSubsCard(ActionEvent actionEvent) {
        StringBuilder text = new StringBuilder();
        text.append(String.format("%-10s %-10s %-40s %-10s\n", "Инв. номер","|  Отдел","|  Автор и заглавие", "|  Состояние"));
        for (SubsCardTable subsCard : listRecordsSC){
            String relevance = "";
            if (subsCard.isIssue()) relevance = "выдана";
            else relevance = "возвращена";
            text.append(String.format("%-10s %-10s %-40s %-10s\n", subsCard.getInventoryNumber(),
                    "|  " + subsCard.getDepartment(), "|  " + subsCard.getAuthorsBookName(), "|  " + relevance));
        }
        downloadFile("Абонементная карточка "+ subscriber.getSubsNumber() +" - "+
                        ShortName(subscriber.getFirstName(), subscriber.getSecondName(), subscriber.getMiddleName()),
                text.toString());
    }
    /**
     * Функция сохранения формуляра читателя.
     */
    public void onLoadDataSubs(ActionEvent actionEvent) {
        downloadFile("Формуляр читателя " + subscriber.getSubsNumber() +" - "+
                        ShortName(subscriber.getFirstName(), subscriber.getSecondName(), subscriber.getMiddleName()),
                taDataSubs.getText());
    }
    /**
     * Функция продления срока выдачи экземпляров книги, запись о которых еще актуальна, на 1 месяц.
     */
    public void onEditDateReturn(ActionEvent actionEvent) {
        if (tvSubsCard.getItems().size()>0){
            try {
                int rowUpdateDateR = statement.executeUpdate("Update HistoriesReader set DateReturn = " +
                        "DATE_ADD(DateReturn, INTERVAL 1 MONTH ) where SubscriberId = "+ subscriber.getSubsNumber() +
                        " and Relevance");
                if (rowUpdateDateR>0){
                    new Alert(Alert.AlertType.INFORMATION, "Продлено " + rowUpdateDateR + " книг.", ButtonType.OK).showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            selectData();
        }
    }
    /**
     * Функция открытия окна выдачи книги.
     */
    public void onOpenIssue(ActionEvent actionEvent) {
        if (subscriber!= null){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(
                    HelloApplication.class.getResource("issueCopyBook.fxml")
            );
            Parent page = null;
            try {
                page = loader.load();
                Stage addStage = new Stage();
                addStage.setTitle("Выдача книги абоненту: " + subscriber.getSubsNumber() + " " +
                        subscriber.getFirstName());
                addStage.initModality(Modality.WINDOW_MODAL);
                addStage.initOwner(HelloApplication.getPrimaryStage());
                Scene scene = new Scene(page);
                addStage.setScene(scene);
                IssueController controller = loader.getController();
                controller.setAddStage(addStage);
                controller.setSubscriber(subscriber);
                addStage.showAndWait();
                selectData();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    /**
     * Функция оформления возврата.
     */
    public void onReturnBooks(ActionEvent actionEvent) {
        if (listBooksForReturn.size()>0){
            try {
                int rsUpdateHR = 0;
                for(int i = 0; i<listBooksForReturn.size();i++){
                     rsUpdateHR += statement.executeUpdate("Update HistoriesReader set Relevance = false " +
                            "where id = " + listBooksForReturn.get(i).getId());
                     int rsUpdateCB = statement.executeUpdate("Update CopiesBook set Status = 'в наличии' " +
                             "where id = " + listBooksForReturn.get(i).getInventoryNumber());
                }
                if (rsUpdateHR>0) {
                    listBooksForReturn.clear();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Возврат оформлен.", ButtonType.OK);
                    alert.showAndWait();
                    selectData();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Для оформления возврата отметьте нужные книги.",
                    ButtonType.OK);
            alert.showAndWait();
        }
    }
}
