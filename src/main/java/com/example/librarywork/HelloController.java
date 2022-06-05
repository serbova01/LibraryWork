package com.example.librarywork;

import com.example.librarywork.classesCompenents.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.Locale;

/**
 * Класс контроллера работы основного окна приложения.
 * <p>
 * Данный класс осуществлять основную работу программы.
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class HelloController {
    /** Текстовое поле для поиска по названию произведения в перечне книг */
    public TextField tfFindBook;
    /** Таблица перечня книг */
    public TableView tvListBooks;
    /** Столбец инвентарного номера экземпляра книги */
    public TableColumn<CopyBookTable, Integer> tcCBId;
    /** Столбец названия произведения экземпляра книги */
    public TableColumn<CopyBookTable, String> tcCBBook;
    /** Столбец названия издательства экземпляра книги */
    public TableColumn<CopyBookTable, String> tcCBEdition;
    /** Столбец статуса экземпляра книги */
    public TableColumn<CopyBookTable, String> tcCBStatus;
    /** Таблицы истории читателей выбранного экземпляра книги */
    public TableView tvHistoryRCB;
    /** Столбец даты выдачи */
    public TableColumn<HistoryReaderCB, String> tcCBDateIssue;
    /** Столбец даты возврата */
    public TableColumn<HistoryReaderCB, String> tcCBDateReturn;
    /** Столбец имени читателя */
    public TableColumn<HistoryReaderCB, String> tcCBSubscriber;
    /** Текстовое поле подробной информации о выбранном экземпляре книги */
    public TextArea taAllInfCB;
    /** Таблица католога книг */
    public TableView tvCatalog;
    /** Столбец названия произведения */
    public TableColumn<Book, String> tcBookName;
    /** Столбец жанра произведения */
    public TableColumn<Book, String> tcBGenre;
    /** Столбец отдела произведения */
    public TableColumn<Book, String> tcBDepartment;
    /** Столбец списка авторов произведения */
    public TableColumn<Book, String> tcBAuthors;
    /** Столбец количества экземпляров произведения */
    public TableColumn<Book, Integer> tcBCount;
    /** Таблица историй читателей произведения */
    public TableView tvHistoryRB;
    /** Столбец инвентарного номера экземпляра книги */
    public TableColumn<HistoryReaderB, Integer> tcBInvNum;
    /** Столбец даты выдачи экземпляра книги */
    public TableColumn<HistoryReaderB, String> tcBDateIssue;
    /** Столбец даты возврата экземпляра книги */
    public TableColumn<HistoryReaderB, String> tcBDateReturn;
    /** Столбец имени читателя экземпляра книги */
    public TableColumn<HistoryReaderB, String> tcBSubscriber;
    /** Таблица абонентов */
    public TableView tvListSubs;
    /** Столбец абонементного номера */
    public TableColumn<SubscriberTable, Integer> tcSId;
    /** Столбец фамилии абонента */
    public TableColumn<SubscriberTable, String> tcSFirstName;
    /** Столбец имени абонента */
    public TableColumn<SubscriberTable, String> tcSSecondName;
    /** Столбец отчества абонента */
    public TableColumn<SubscriberTable, String> tcSMiddleName;
    /** Столбец номера телефона абонента */
    public TableColumn<SubscriberTable, String> tcSPhoneNum;
    /** Столбец даты регистрации абонента */
    public TableColumn<SubscriberTable, String> tcSDateReg;
    /** Столбец адреса абонента */
    public TableColumn<SubscriberTable, String> tcSAddress;
    /** Поле адреса базы данных */
    final String DB_URL = "jdbc:mysql://localhost:3306/library?useSSL=false";
    /** Поле логина для подключения к БД */
    final String LOGIN = "root";
    /** Поле пароля для подключения к БД */
    final String PASSWORD = "root_root";
    /** Поле для вывода изображения обложки */
    public ImageView ivImage;
    /** Элемент управления переключениями на статус 'в наличии' */
    public RadioMenuItem rimIsStatus;
    /** Элемент управления переключениями на статус 'списаны' */
    public RadioMenuItem rimWritingOffStatus;
    /** Элемент управления переключениями на статус 'выданы' */
    public RadioMenuItem rimIssueStatus;
    /** Элемент управления переключениями на вывод всех данных */
    public RadioMenuItem rimAllStatus;
    /** Текстовое поле для поиска по названию произведения в каталоге книг */
    public TextField tfFindNameBook;
    /** Текстовое поле для поиска по списку авторов произведения в каталоге книг */
    public TextField tfFindAuthor;
    /** Текстовое поле для поиска по названию жанра в каталоге книг */
    public TextField tfFindGenre;
    /** Текстовое поле для поиска по абонементному номеру в списке абонентов */
    public TextField tfFindSubsNumber;
    /** Текстовое поле для поиска по номеру  телефона в списке абонентов */
    public TextField tfFindPhoneNumber;
    /** Statement для выполнения SQL запросов */
    public Statement statement;
    /** Connection для подключения к БД */
    private Connection connection;
    /** Список экземпляров книг */
    private ObservableList<CopyBookTable> listBook;
    /** Список произведений */
    private ObservableList<Book> catalog;
    /** Список абонентов */
    private ObservableList<SubscriberTable> listSubs;
    /** Список историй читателя для выбранного экземпляра книги */
    private ObservableList<HistoryReaderCB> historyReaders;
    /** Список историй читателя выбранного произведения */
    private ObservableList<HistoryReaderB> historyReadersB;
    /** Выбранный экземпляр книги */
    private CopyBookTable selectCopyBook;
    /** Идентификатор издания выбранного экземпляра книги */
    private int editionIdForEdit;
    /** Выбранный абонент */
    private SubscriberTable selectSubscriber;
    /** Выбранное произведение */
    private Book selectBook;
    /**
     * Функция инициализации класса, проведения подключения к БД и вывода данных в таблицы
     */
    @FXML
    void initialize() {
        if(connectDB()){
            selectDataTables();
        }
    }
    /**
     * Функция вывода данных в таблицы {@link HelloController#tvListBooks}, {@link HelloController#tvCatalog} и
     * {@link HelloController#tvListSubs}
     */
    public void selectDataTables(){
        try {
            listBook = FXCollections.observableArrayList();
            catalog = FXCollections.observableArrayList();
            listSubs = FXCollections.observableArrayList();
            tvListSubs.getItems().clear();
            tvCatalog.getItems().clear();
            tvListSubs.getItems().clear();
            ResultSet resultSet = statement.executeQuery("Select * from CopiesBook " +
                    "join Editions on copiesbook.EditionId = editions.Id " +
                    "join PublishingHouses on editions.publhouseID = publishinghouses.Id " +
                    "join Books on editions.BookId = books.Id " +
                    "join Books_Authors on books.Id = books_authors.BookId " +
                    "join Authors on books_authors.AuthorId = authors.Id");
            while (resultSet.next()) {
                StringBuilder stringBuilder = ShortName(resultSet.getString("FirstName"),
                        resultSet.getString("LastName"), resultSet.getString("MiddleName"));
                this.listBook.add(new CopyBookTable(resultSet.getInt("CopiesBook.Id"),
                        resultSet.getString("Name").concat(stringBuilder.toString()),
                        resultSet.getString("NamePH"), resultSet.getString("Status")));
            }
            for(int i=0; i<listBook.size(); i++){
                int id = listBook.get(i).getInventoryNumber();
                StringBuilder strAuthors = new StringBuilder();
                for(int i1=i+1; i1<listBook.size(); i1++){
                    if (listBook.get(i1).getInventoryNumber() == id){
                        strAuthors.append(", ");
                        strAuthors.append(listBook.get(i1).getBook().split(":")[1]);
                        listBook.remove(i1);
                    }
                }
                String oldStr = listBook.get(i).getBook();
                listBook.get(i).setBook(oldStr.concat(strAuthors.toString()));
            }
            tcCBId.setCellValueFactory(cellData ->
                    cellData.getValue().inventoryNumberProperty().asObject());
            tcCBBook.setCellValueFactory(copybookStringCellDataFeatures
                    -> copybookStringCellDataFeatures.getValue().bookProperty());
            tcCBEdition.setCellValueFactory(copybookStringCellDataFeatures
                    -> copybookStringCellDataFeatures.getValue().publHouseProperty());
            tcCBStatus.setCellValueFactory(copybookStringCellDataFeatures
                    -> copybookStringCellDataFeatures.getValue().statusProperty());
            tvListBooks.setItems(listBook);
            tvListBooks.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CopyBookTable>() {
                @Override
                public void changed(ObservableValue<? extends CopyBookTable>
                                            observableValue, CopyBookTable student, CopyBookTable t1) {
                    if (t1 != null) {
                        showCopyBook(t1);
                        selectCopyBook = t1;
                    }
                }
            });
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Error select data");
        }
        try {
            ResultSet resultSet = statement.executeQuery("Select * from Books" +
                    " join Genres on books.GenreId = Genres.Id " +
                    "join Books_Authors on books.Id = books_authors.BookId " +
                    "join Authors on books_authors.AuthorId = authors.Id");
            System.out.println("Select data Books");
            while (resultSet.next()) {
                StringBuilder stringBuilder = ShortName(resultSet.getString("FirstName"),
                        resultSet.getString("LastName"), resultSet.getString("MiddleName"));
                String fullNameAuthor = resultSet.getString("FirstName") + " " +
                resultSet.getString("LastName")+" "+ resultSet.getString("MiddleName");
                int id = resultSet.getInt("Books.Id");
                Book book = new Book(resultSet.getInt("Books.Id"),
                        resultSet.getString("Name"), resultSet.getString("Genres.Name"),
                        resultSet.getString("Department"), stringBuilder.toString().split(":")[1],
                        fullNameAuthor, 0);
                this.catalog.add(book);
            }
            for(int i=0; i<catalog.size(); i++){
                int id = catalog.get(i).getId();
                System.out.println("catalog.get(i).getId() = " + catalog.get(i).getId());
                StringBuilder strAuthors = new StringBuilder();
                strAuthors.append(catalog.get(i).getAuthors());
                String fullNameAuthors = catalog.get(i).getAuthorsFullName();
                for(int i1=i+1; i1<catalog.size();){
                    if (catalog.get(i1).getId() == id){
                        strAuthors.append(", ");
                        strAuthors.append(catalog.get(i1).getAuthors());
                        fullNameAuthors+= ", " + catalog.get(i1).getAuthorsFullName();
                        catalog.remove(i1);
                    }
                    else i1++;
                }
                catalog.get(i).setAuthors(strAuthors.toString());
                catalog.get(i).setAuthorsFullName(fullNameAuthors);
            }
            for(Book book : catalog){
                resultSet = statement.executeQuery("Select count(*) from CopiesBook " +
                        "join Editions on copiesbook.EditionId = editions.Id " +
                        "join Books on editions.BookId = books.Id " +
                        "where Status <> 'списано' and books.Id = " + book.getId());
                resultSet.next();
                book.setCount(resultSet.getInt("count(*)"));
            }
            tcBookName.setCellValueFactory(bookStringCellDataFeatures
                    -> bookStringCellDataFeatures.getValue().nameProperty());
            tcBGenre.setCellValueFactory(bookStringCellDataFeatures
                    -> bookStringCellDataFeatures.getValue().genreProperty());
            tcBDepartment.setCellValueFactory(bookStringCellDataFeatures
                    -> bookStringCellDataFeatures.getValue().departmentProperty());
            tcBAuthors.setCellValueFactory(bookStringCellDataFeatures
                    -> bookStringCellDataFeatures.getValue().authorsProperty());
            tcBCount.setCellValueFactory(cellData ->
                    cellData.getValue().countProperty().asObject());
            tvCatalog.setItems(catalog);

            tvCatalog.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {
                @Override
                public void changed(ObservableValue<? extends Book>
                                            observableValue, Book student, Book t1) {
                    if (t1 != null) {
                        selectBook = t1;
                        System.out.println("selectBook: " + selectBook);
                        System.out.println("t1: " + t1);
                        showBook(t1);
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Error select data");
        }
        try {
            ResultSet resultSet = statement.executeQuery("Select * from Subscribers");
            while (resultSet.next()) {
                Date date = resultSet.getDate("DateRegistration");
                StringBuilder address = new StringBuilder();
                address.append(resultSet.getString("Street"));
                address.append(", ");
                address.append(resultSet.getString("House"));
                if (!resultSet.getString("Flat").equals("") && !resultSet.getString("Flat").equals(" ")){
                    address.append(", ");
                    address.append(resultSet.getString("Flat"));
                }
                this.listSubs.add(new SubscriberTable(resultSet.getInt("Id"), resultSet.getString("FirstName"),
                        resultSet.getString("SecondName"), resultSet.getString("MiddleName"),
                        resultSet.getString("PhoneNumber"), date, address.toString()));
            }
            tcSId.setCellValueFactory(cellData ->
                    cellData.getValue().subsNumberProperty().asObject());
            tcSFirstName.setCellValueFactory(subscriberStringCellDataFeatures
                    -> subscriberStringCellDataFeatures.getValue().firstNameProperty());
            tcSSecondName.setCellValueFactory(subscriberStringCellDataFeatures
                    -> subscriberStringCellDataFeatures.getValue().secondNameProperty());
            tcSMiddleName.setCellValueFactory(subscriberStringCellDataFeatures
                    -> subscriberStringCellDataFeatures.getValue().middleNameProperty());
            tcSPhoneNum.setCellValueFactory(subscriberStringCellDataFeatures
                    -> subscriberStringCellDataFeatures.getValue().phoneNumberProperty());
            tcSDateReg.setCellValueFactory(subscriberStringCellDataFeatures
                    -> subscriberStringCellDataFeatures.getValue().strDateRegProperty());
            tcSAddress.setCellValueFactory(subscriberStringCellDataFeatures
                    -> subscriberStringCellDataFeatures.getValue().addressProperty());
            tvListSubs.setItems(listSubs);
            tvListSubs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SubscriberTable>() {
                @Override
                public void changed(ObservableValue<? extends SubscriberTable>
                                            observableValue, SubscriberTable subscriber, SubscriberTable t1) {
                    if (t1 != null) {
                        selectSubscriber = t1;
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Error select data");
        }

    }
    /**
     * Функция получения короткого имени автора из списка авторов произведения, по которому составляется каталожная карточка
     * @param firstName фамилия автора
     * @param secondName имя автора
     * @param middleName отчество автора
     * @return короткое имя автора
     */
    private StringBuilder ShortName(String firstName,String secondName,String middleName){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(": ");
        stringBuilder.append(firstName);
        stringBuilder.append(" ");
        stringBuilder.append(secondName.charAt(0));
        stringBuilder.append(".");
        if (!middleName.equals("") && !middleName.equals(" ")){
            stringBuilder.append(middleName.charAt(0));
            stringBuilder.append(".");
        }
        return stringBuilder;
    }
    /**
     * Функция заполнения таблицы историй читателя выбранного экземпляра книги {@link HelloController#tvHistoryRCB}, текстового
     * поля {@link HelloController#taAllInfCB} и вывода изображения обложки в {@link HelloController#ivImage}
     * @param copyBookTable выбранный экземляр книги
     */
    void showCopyBook(CopyBookTable copyBookTable){
        int rowCount = 0;
        try{
            historyReaders = FXCollections.observableArrayList();
            tvHistoryRCB.getItems().clear();
            ResultSet resultSet = statement.executeQuery("Select * from HistoriesReader " +
                    "join Subscribers on historiesReader.SubscriberId = Subscribers.Id " +
                    "Where historiesReader.CopyBookId = " + copyBookTable.getInventoryNumber());
            rowCount = resultSet.getRow();
            while (resultSet.next()) {
                StringBuilder stringBuilder = ShortName(resultSet.getString("FirstName"),
                        resultSet.getString("SecondName"), resultSet.getString("MiddleName"));
                Date dateIssue = resultSet.getDate("DateIssue");
                Date dateReturn = resultSet.getDate("DateReturn");
                StringBuilder subscriber = new StringBuilder();
                subscriber.append(resultSet.getInt("Subscribers.Id"));
                subscriber.append(" ");
                subscriber.append(ShortName(resultSet.getString("FirstName"),
                        resultSet.getString("SecondName"),resultSet.getString("MiddleName")));
                this.historyReaders.add(new HistoryReaderCB(dateIssue, dateReturn, subscriber.toString()));
            }
            tcCBDateIssue.setCellValueFactory(historyReaderCBStringCellDataFeatures
                    -> historyReaderCBStringCellDataFeatures.getValue().strDateIssueProperty());
            tcCBDateReturn.setCellValueFactory(historyReaderCBStringCellDataFeatures
                    -> historyReaderCBStringCellDataFeatures.getValue().strDateReturnProperty());
            tcCBSubscriber.setCellValueFactory(historyReaderCBStringCellDataFeatures
                    -> historyReaderCBStringCellDataFeatures.getValue().subsNameProperty());
            tvHistoryRCB.setItems(historyReaders);

            resultSet = statement.executeQuery("Select * from CopiesBook " +
                    "join Editions on copiesbook.EditionId = editions.Id " +
                    "join PublishingHouses on editions.publhouseID = publishinghouses.Id " +
                    "join Books on editions.BookId = books.Id " +
                    "join Books_Authors on books.Id = books_authors.BookId " +
                    "join Authors on books_authors.AuthorId = authors.Id " +
                    "join Genres on books.GenreId = Genres.Id " +
                    "Where CopiesBook.Id = " + copyBookTable.getInventoryNumber());
            resultSet.next();
            StringBuilder addingInf = new StringBuilder();
            addingInf.append("Произведение: "); addingInf.append(resultSet.getString("Books.Name"));
            addingInf.append("\n");addingInf.append("Жанр: "); addingInf.append(resultSet.getString("Genres.Name"));
            addingInf.append("\n");addingInf.append("Отдел: "); addingInf.append(resultSet.getString("Department"));
            addingInf.append("\n");addingInf.append("Описание: "); addingInf.append(resultSet.getString("Discription"));
            addingInf.append("\n");addingInf.append("Автор(-ы): "); addingInf.append(copyBookTable.getBook().split(":")[1]);
            addingInf.append("\n");addingInf.append("Издательство: "); addingInf.append(resultSet.getString("NamePH"));
            addingInf.append("\n");addingInf.append("Место издательства: "); addingInf.append(resultSet.getString("Place"));
            addingInf.append("\n");addingInf.append("Год издания: "); addingInf.append(resultSet.getString("YearPublication"));
            addingInf.append("\n");addingInf.append("Число страниц: "); addingInf.append(resultSet.getString("CountPage"));
            addingInf.append("\n");addingInf.append("ISBN: "); addingInf.append(resultSet.getString("ISBN"));
            taAllInfCB.setText(addingInf.toString());

            ivImage.setImage(null);
            if (resultSet.getString("Image")!=" " && resultSet.getString("Image")!=null &&
                    resultSet.getString("Image")!="" ){
                ImageBook imageBook = new ImageBook();
                ivImage.setImage(imageBook.LoadImage(resultSet.getString("Image")));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error select historyReaderCB " + rowCount);
        }
    }
    /**
     * Функция заполнения таблицы историй читателя выбранного произведения {@link HelloController#tvHistoryRB}
     * @param book выбранное произведение
     */
    void showBook(Book book){
        int rowCount = 0;
        try{
            historyReadersB = FXCollections.observableArrayList();
            tvHistoryRB.getItems().clear();
            ResultSet resultSet = statement.executeQuery("Select * from HistoriesReader " +
                    "join Subscribers on historiesReader.SubscriberId = Subscribers.Id " +
                    "join CopiesBook on historiesReader.CopyBookId = CopiesBook.Id " +
                    "join Editions on CopiesBook.EditionId = Editions.Id " +
                    "join Books on Editions.BookId = Books.Id " +
                    "Where Books.Id = " + book.getId());
            while (resultSet.next()) {
                Date dateIssue = resultSet.getDate("DateIssue");
                Date dateReturn = resultSet.getDate("DateReturn");
                StringBuilder subscriber = new StringBuilder();
                subscriber.append(resultSet.getInt("Subscribers.Id"));
                subscriber.append(" ");
                subscriber.append(ShortName(resultSet.getString("FirstName"),
                        resultSet.getString("SecondName"),resultSet.getString("MiddleName")));
                this.historyReadersB.add(new HistoryReaderB(resultSet.getInt("CopiesBook.Id"),
                        dateIssue.toString(), dateReturn.toString(), subscriber.toString()));
            }
            tcBInvNum.setCellValueFactory(cellData ->
                    cellData.getValue().inventoryNumberProperty().asObject());
            tcBDateIssue.setCellValueFactory(historyReaderBStringCellDataFeatures
                    -> historyReaderBStringCellDataFeatures.getValue().strDateIssueProperty());
            tcBDateReturn.setCellValueFactory(historyReaderBStringCellDataFeatures
                    -> historyReaderBStringCellDataFeatures.getValue().strDateReturnProperty());
            tcBSubscriber.setCellValueFactory(historyReaderBStringCellDataFeatures
                    -> historyReaderBStringCellDataFeatures.getValue().subsNameProperty());
            tvHistoryRB.setItems(historyReadersB);
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error select historyReaderCB" + rowCount);
        }
    }
    /**
     * Функция осуществления подключения к БД и инициализация поля {@link HelloController#statement}
     * @return состояние подключения
     */
    public boolean connectDB(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loading succesfull.");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Driver loading failed!");
            return false;

        }
        try{
            connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
            //устанавливаем соединение
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Connection error!");
            return false;
        }
        return true;
    }
    /**
     * Функция отображения на форме таблицы историй читателя выбранного экземпляра книги {@link HelloController#tvHistoryRCB}
     */
    public void onOpenHRCopyBook(ActionEvent actionEvent) {
        tvHistoryRCB.setVisible(true);
        tvHistoryRCB.setPrefHeight(200);
    }
    /**
     * Функция скрытия на форме таблицы историй читателя выбранного экземпляра книги {@link HelloController#tvHistoryRCB}
     */
    public void onCloseHRCopyBook(ActionEvent actionEvent) {
        tvHistoryRCB.setVisible(false);
        tvHistoryRCB.setPrefHeight(40);
    }
    /**
     * Функция отображения на форме таблицы историй читателя выбранного произведения {@link HelloController#tvHistoryRB}
     */
    public void onOpenHRBook(ActionEvent actionEvent) {
        tvHistoryRB.setVisible(true);
        tvHistoryRB.setPrefHeight(200);
    }
    /**
     * Функция скрытия на форме таблицы историй читателя выбранного произведения {@link HelloController#tvHistoryRB}
     */
    public void onCloseHRBook(ActionEvent actionEvent) {
        tvHistoryRB.setVisible(false);
        tvHistoryRB.setPrefHeight(40);
    }
    /**
     * Функция списания выбраного экземпляра книги {@link HelloController#selectCopyBook}
     */
    public void onWrittingOffCB(ActionEvent actionEvent) {
        if (selectCopyBook!=null){
            try {
                int row = statement.executeUpdate("Update CopiesBook set Status = 'списана' " +
                        "where id = " + selectCopyBook.getInventoryNumber());
                if (row>0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Экземпляр "+ selectCopyBook.getInventoryNumber() +
                            " списан.", ButtonType.OK);
                    alert.showAndWait();
                    selectDataTables();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Функция редактирования издания выбранного экземпляра книги {@link HelloController#selectCopyBook} по
     * {@link HelloController#editionIdForEdit}
     */
    public void onUpdateCB(ActionEvent actionEvent) {
        if (selectCopyBook != null){
            if(!selectCopyBook.getStatus().equals("списана")){
                try {
                    ResultSet resultSet = statement.executeQuery("Select editionId from CopiesBook " +
                            "where id = " + selectCopyBook.getInventoryNumber());
                    resultSet.next();
                    editionIdForEdit = resultSet.getInt(1);
                    System.out.println("editionIdForEdit: " + editionIdForEdit);
                    showDialog("insertCB.fxml", "Редактирование сведений об издании.", "EditEd");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                Alert alert =new Alert(Alert.AlertType.WARNING, "Списаный экземпляр нельзя редактировать.", ButtonType.OK);
                alert.showAndWait();
            }
        }
        else {
            Alert alert =new Alert(Alert.AlertType.INFORMATION, "Для редактирования выберете экземпляр книги.", ButtonType.OK);
            alert.showAndWait();
        }
    }
    /**
     * Функция открытия окна для добавления экземпляров книг
     */
    public void onCreateCB(ActionEvent actionEvent) {
        showDialog("insertCB.fxml", "Привоз книг", "CreateCB");
    }
    /**
     * Функция открытия заданного окна с заданным заголовком и параметром отображаемых данных. После закрытия вызвваемого окна
     * данные обновляются.
     * @param resource название fxml-разметки вызываемого окна
     * @param title заголовок вызываемого окна
     * @param parameter параметр отображаемых данных в вызввемом окне
     */
    private void showDialog(String resource, String title, String parameter) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(
                HelloApplication.class.getResource(resource)
        );
        Parent page = null;
        try {
            System.out.println("resource: " + resource);
            page = loader.load();
            Stage addStage = new Stage();
            addStage.setTitle(title);
            addStage.initModality(Modality.WINDOW_MODAL);
            addStage.initOwner(HelloApplication.getPrimaryStage());
            Scene scene = new Scene(page);
            addStage.setScene(scene);
            switch (parameter){
                case "CreateCB":{
                    InsertController controller = loader.getController();
                    controller.setAddStage(addStage);
                    controller.setParameter(parameter);
                    addStage.showAndWait();
                    break;
                }
                case "EditB":{
                    InsertController controller = loader.getController();
                    controller.setBookEdit(selectBook);
                    controller.setAddStage(addStage);
                    controller.setParameter(parameter);
                    addStage.showAndWait();
                    break;
                }
                case "EditEd":{
                    InsertController controller = loader.getController();
                    controller.setAddStage(addStage);
                    controller.setEditionEdit(editionIdForEdit);
                    controller.setParameter(parameter);
                    addStage.showAndWait();
                    break;
                }
                case "CatalogCards":{
                    CatalogCardsController controller = loader.getController();
                    controller.setAddStage(addStage);
                    addStage.showAndWait();
                    break;
                }
                case "CreateS":{
                    SubscriberController controller = loader.getController();
                    controller.setAddStage(addStage);
                    addStage.showAndWait();
                    break;
                }
                case "EditSubs":{
                    SubscriberController controller = loader.getController();
                    controller.setAddStage(addStage);
                    controller.setSubscriberEdit(selectSubscriber);
                    addStage.showAndWait();
                    break;
                }
                case "Issue":{
                    IssueController controller = loader.getController();
                    controller.setAddStage(addStage);
                    controller.setSubscriber(selectSubscriber);
                    addStage.showAndWait();
                    break;
                }
                case "SubsCard":{
                    SubsCardController controller = loader.getController();
                    controller.setAddStage(addStage);
                    controller.setSubscriber(selectSubscriber);
                    addStage.showAndWait();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        selectDataTables();
    }
    /**
     * Функция открытия окна работы с каталожными карточками.
     */
    public void onOpenCatalogCards(ActionEvent actionEvent) {
        showDialog("catalogCards.fxml", "Каталожные карты", "CatalogCards");
    }
    /**
     * Функция открытия окна редактирования данных о выбранном произведении {@link HelloController#selectBook}.
     */
    public void onEditBook(ActionEvent actionEvent) {
        showDialog("insertCB.fxml", "Редактирование данных о произведении.", "EditB");
    }
    /**
     * Функция открытия окна добавления нового абонента.
     */
    public void onCreateSubscriber(ActionEvent actionEvent) {
        showDialog("insert.fxml", "Добавление абонента", "CreateS");
    }
    /**
     * Функция открытия окна редактирования данных о выбранном абоненте {@link HelloController#selectSubscriber}.
     */
    public void onEditSubscriber(ActionEvent actionEvent) {
        if (selectSubscriber != null){
            showDialog("insert.fxml", "Редактирование данных об абаненте " + selectSubscriber.getSubsNumber(),
                    "EditSubs");
        }
    }
    /**
     * Функция открытия окна для выдачи книги выбранному абоненту {@link HelloController#selectSubscriber}.
     */
    public void onIssueCopyBook(ActionEvent actionEvent) {
        if (selectSubscriber!= null){
            showDialog("issueCopyBook.fxml", "Выдача книги абоненту: " + selectSubscriber.getSubsNumber() + " " +
                    selectSubscriber.getFirstName(), "Issue");
        }
    }
    /**
     * Функция открытия окна работы с абонементной карточкой выбранного абонента {@link HelloController#selectSubscriber}.
     */
    public void onOpenSubsCard(ActionEvent actionEvent) {
        if (selectSubscriber != null){
            showDialog("subsCards.fxml", "Абонементная карточка "+ selectSubscriber.getSubsNumber() + " " +
                    selectSubscriber.getFirstName(), "SubsCard");
        }
    }
    /**
     * Функция фильтрации записей таблицы перечня книг {@link HelloController#tvListBooks} по заданному параметру заданного
     * столбца.
     * @param parameter текст для фильтрации
     * @param nameColumn название столбца для фильтрации
     */
    private void filtrationTableCB(String parameter, String nameColumn){
        ObservableList<CopyBookTable> list = FXCollections.observableArrayList(tvListBooks.getItems());
        if (nameColumn.equals("Status")){
            for(int i=0; i<list.size();){
                if (!list.get(i).getStatus().equals(parameter)){
                    list.remove(i);
                }else i++;
            }
        }else{
            for(int i=0; i<list.size();){
                if (!list.get(i).getBook().toLowerCase(Locale.ROOT).contains(parameter.toLowerCase(Locale.ROOT))){
                    list.remove(i);
                }else i++;
            }
        }
        tvListBooks.setItems(list);
    }
    /**
     * Функция фильтрации записей таблицы каталога книг {@link HelloController#tvCatalog} по заданному параметру заданного
     * столбца.
     * @param parameter текст для фильтрации
     * @param nameColumn название столбца для фильтрации
     */
    private void filtrationCatalog(String parameter, String nameColumn){
        ObservableList<Book> list = FXCollections.observableArrayList(tvCatalog.getItems());
        if (nameColumn.equals("Genre")){
            for(int i=0; i<list.size();){
                if (!list.get(i).getGenre().toLowerCase(Locale.ROOT).contains(parameter.toLowerCase(Locale.ROOT))){
                    list.remove(i);
                }else i++;
            }
        }else{
            if (nameColumn.equals("Author")){
                for(int i=0; i<list.size();){
                    if (!list.get(i).getAuthors().toLowerCase(Locale.ROOT).contains(parameter.toLowerCase(Locale.ROOT))){
                        list.remove(i);
                    }else i++;
                }
            }
            else{
                for(int i=0; i<list.size();){
                    if (!list.get(i).getName().contains(parameter)){
                        list.remove(i);
                    }else i++;
                }
            }
        }
        tvCatalog.setItems(list);
    }
    /**
     * Функция фильтрации записей таблицы абонентов {@link HelloController#tvListSubs} по заданному параметру заданного
     * столбца.
     * @param parameter текст для фильтрации
     * @param nameColumn название столбца для фильтрации
     */
    private void filtrationListSubs(String parameter, String nameColumn){
        ObservableList<SubscriberTable> list = FXCollections.observableArrayList(tvListSubs.getItems());
        if (nameColumn.equals("PhoneNum")){
            for(int i=0; i<list.size();){
                if (!list.get(i).getPhoneNumber().equals(parameter)){
                    list.remove(i);
                }else i++;
            }
        }else{
            for(int i=0; i<list.size();){
                if (list.get(i).getSubsNumber() != Integer.valueOf(parameter)){
                    list.remove(i);
                }else i++;
            }
        }
        tvListSubs.setItems(list);
    }

    /**
     * Функция фильтрации записей таблицы перечня книг {@link HelloController#tvListBooks} по статусу с использованием элементов
     * управления переключениями {@link HelloController#rimAllStatus}, {@link HelloController#rimIsStatus},
     * {@link HelloController#rimIssueStatus} и {@link HelloController#rimWritingOffStatus}.
     */
    public void onFiltrationOfStatus(ActionEvent actionEvent) {
        selectDataTables();
        if (rimAllStatus.isSelected()) selectDataTables();
        else{
            if(rimIsStatus.isSelected()) filtrationTableCB("в наличии", "Status");
            else{
                if (rimIssueStatus.isSelected()) filtrationTableCB("выдана", "Status");
                else filtrationTableCB("списана", "Status");
            }
        }
    }
    /**
     * Функция фильтрации записей таблицы перечня книг {@link HelloController#tvListBooks} по названию произведения
     * {@link HelloController#tfFindBook}
     */
    public void onFiltrationOfBook(ActionEvent actionEvent) {
        if (tfFindBook != null){
            selectDataTables();
            filtrationTableCB(tfFindBook.getText(), "Book");
        }
    }
    /**
     * Функция фильтрации записей таблицы каталога книг {@link HelloController#tvCatalog} по названию произведения
     * {@link HelloController#tfFindNameBook}
     */
    public void onFiltrationOfNameBook(ActionEvent actionEvent) {
        selectDataTables();
        if(tfFindNameBook!=null) filtrationCatalog(tfFindNameBook.getText(), "NameBook");
    }
    /**
     * Функция фильтрации записей таблицы каталога книг {@link HelloController#tvCatalog} по фамилии автора
     * {@link HelloController#tfFindAuthor}
     */
    public void onFiltrationOfAuthor(ActionEvent actionEvent) {
        selectDataTables();
        if(tfFindAuthor!=null) filtrationCatalog(tfFindAuthor.getText(), "Author");
    }
    /**
     * Функция фильтрации записей таблицы каталога книг {@link HelloController#tvCatalog} по названию жанра
     * {@link HelloController#tfFindGenre}
     */
    public void onFiltrationOfGenre(ActionEvent actionEvent) {
        selectDataTables();
        if(tfFindGenre!=null) filtrationCatalog(tfFindGenre.getText(), "Genre");
    }
    /**
     * Функция фильтрации записей таблицы абонентов {@link HelloController#tvListSubs} по абонементному номеру
     * {@link HelloController#tfFindSubsNumber}
     */
    public void onFiltrationOfSubsNum(ActionEvent actionEvent) {
        selectDataTables();
        if (tfFindSubsNumber!=null) filtrationListSubs(tfFindSubsNumber.getText(), "SubsNum");
    }
    /**
     * Функция фильтрации записей таблицы абонентов {@link HelloController#tvListSubs} по номеру телефона
     * {@link HelloController#tfFindPhoneNumber}
     */
    public void onFiltrationOfPhoneNum(ActionEvent actionEvent) {
        selectDataTables();
        if (tfFindPhoneNumber!=null) filtrationListSubs(tfFindPhoneNumber.getText(), "PhoneNum");
    }
    /**
     * Функция открытия окна для работы с абонементной карточкой выбранного абонента {@link HelloController#selectSubscriber}.
     * Реализация данной функции проходит при двойном щелчке по записи в таблице абонентов {@link HelloController#tvListSubs}.
     */
    public void onClickedSubscriber(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount()==2){
            if (selectSubscriber != null){
                showDialog("subsCards.fxml", "Абонементная карточка "+ selectSubscriber.getSubsNumber() + " " +
                        selectSubscriber.getFirstName(), "SubsCard");
            }
        }
    }
    /**
     * Функция обновления данных в таблицах {@link HelloController#tvListBooks}, {@link HelloController#tvCatalog} и
     * {@link HelloController#tvListSubs}.
     */
    public void onSelectAll(ActionEvent actionEvent) {
        selectDataTables();
    }
}