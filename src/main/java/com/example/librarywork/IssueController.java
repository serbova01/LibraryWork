package com.example.librarywork;

import com.example.librarywork.classesCompenents.CopyBookTable;
import com.example.librarywork.classesCompenents.SubscriberTable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.*;
import java.time.LocalDate;

/**
 * Класс для выдачи книги абоненту.
 * <p>
 * Данный класс позволяет выдать книгу.
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class IssueController {
    /** Таблица экземпляров книг в наличии */
    public TableView<CopyBookTable> tvListCB;
    /** Столбец инвентарного номера */
    public TableColumn<CopyBookTable, Integer> tcCBId;
    /** Столбец названия произведения */
    public TableColumn<CopyBookTable, String> tcCBBook;
    /** Столбец названия издательства */
    public TableColumn<CopyBookTable, String> tcCBEdition;
    /** Столбец выбран/не выбран */
    public TableColumn<CopyBookTable, Boolean> tcChooseItem;
    /** Окно приложения */
    private Stage dialogStage;
    /** Абонент, которому выдается книга */
    private SubscriberTable subscriber;
    /** Список экземпляров книг в наличии */
    ObservableList<CopyBookTable> listCopiesBook;
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
    /**
     * Функция изменения абонента, которому выдается книга {@link IssueController#subscriber}
     * @param subscriber абонент
     */
    public void setSubscriber(SubscriberTable subscriber){
        this.subscriber = subscriber;
    }
    /**
     * Функция изменения окна приложения {@link IssueController#dialogStage}
     * @param addStage окно прриложения
     */
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }
    /**
     * Функция инициализации класса и заполнение таблицы {@link IssueController#tvListCB} и списка
     * {@link IssueController#listCopiesBook}
     */
    @FXML
    void initialize(){
        if (connectDB()){
            selectData();
        }
    }
    /**
     * Функция заполнения таблицы {@link IssueController#tvListCB} и списка
     * {@link IssueController#listCopiesBook}
     */
    private void selectData() {
        try {
            listCopiesBook = FXCollections.observableArrayList();
            ResultSet rsSelect = statement.executeQuery("Select * from CopiesBook " +
                    "join Editions on editions.Id = copiesBook.EditionId " +
                    "join Books on books.Id = editions.BookId " +
                    "join PublishingHouses on PublishingHouses.Id = editions.PublHouseId " + "where copiesBook.Status = 'в наличии' ");
            while(rsSelect.next()){
                CopyBookTable copyBook = new CopyBookTable(rsSelect.getInt("CopiesBook.Id"),
                        rsSelect.getString("Books.Name"), rsSelect.getString("NamePH"),
                        rsSelect.getString("Status"));
                listCopiesBook.add(copyBook);
            }
            tcCBId.setCellValueFactory(cellData ->
                    cellData.getValue().inventoryNumberProperty().asObject());
            tcCBBook.setCellValueFactory(copybookStringCellDataFeatures
                    -> copybookStringCellDataFeatures.getValue().bookProperty());
            tcCBEdition.setCellValueFactory(copybookStringCellDataFeatures
                    -> copybookStringCellDataFeatures.getValue().publHouseProperty());
            tcChooseItem.setCellFactory( copy -> new CheckBoxTableCell());
            tcChooseItem.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CopyBookTable, Boolean>,
                                ObservableValue<Boolean>>() {

                @Override
                public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<CopyBookTable, Boolean> param) {
                    CopyBookTable person = param.getValue();

                    SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(person.isIssue());

                    // Note: singleCol.setOnEditCommit(): Not work for
                    // CheckBoxTableCell.

                    // When "Single?" column change.
                    booleanProp.addListener(new ChangeListener<Boolean>() {

                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                                            Boolean newValue) {
                            person.setIssue(newValue);
                        }
                    });
                    return booleanProp;
                }
            });

            tcChooseItem.setCellFactory(new Callback<TableColumn<CopyBookTable, Boolean>, //
                    TableCell<CopyBookTable, Boolean>>() {
                @Override
                public TableCell<CopyBookTable, Boolean> call(TableColumn<CopyBookTable, Boolean> p) {
                    CheckBoxTableCell<CopyBookTable, Boolean> cell = new CheckBoxTableCell<CopyBookTable, Boolean>();
                    cell.setAlignment(Pos.CENTER);
                    return cell;
                }
            });
            tvListCB.setItems(listCopiesBook);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Функция осуществления подключения к БД и инициализация поля {@link IssueController#statement}
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
     * Функция выдачи выбранных экземпляров из таблицы {@link IssueController#tvListCB} и сохранение данных в БД.
     */
    public void onIssueCB(ActionEvent actionEvent) {
        ObservableList<Integer> idsCB = FXCollections.observableArrayList();
        for(CopyBookTable copyBookTable : tvListCB.getItems()){
            if (copyBookTable.isIssue()){
                idsCB.add(copyBookTable.getInventoryNumber());
            }
        }
        StringBuilder query = new StringBuilder();
        query.append("Insert into HistoriesReader (DateIssue, DateReturn, CopyBookId, SubscriberId, Relevance) values ");
        for(int i=0; i<idsCB.size();i++){
            query.append("('"+ Date.valueOf(LocalDate.now()) +"', '"+Date.valueOf(LocalDate.now().plusMonths(1))+"', " +
                    + idsCB.get(i) +", "+subscriber.getSubsNumber()+", true)");
            if (i+1 != idsCB.size()) query.append(", ");
            try {
                statement.executeUpdate("Update CopiesBook Set Status = 'выдана' where Id = " + idsCB.get(i));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (idsCB.size()>0){
            try {
                int rowInsertHR = statement.executeUpdate(query.toString());
                if (rowInsertHR > 0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Книги выданы.", ButtonType.OK);
                    alert.showAndWait();
                }
                selectData();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    /**
     * Функция обнуления всех выбранных экземпляров.
     */
    public void onCancel(ActionEvent actionEvent) {
        selectData();
    }
}
