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

public class IssueController {
    public TableView<CopyBookTable> tvListCB;
    public TableColumn<CopyBookTable, Integer> tcCBId;
    public TableColumn<CopyBookTable, String> tcCBBook;
    public TableColumn<CopyBookTable, String> tcCBEdition;
    public TableColumn<CopyBookTable, Boolean> tcChooseItem;

    private Stage dialogStage;
    private SubscriberTable subscriber;
    ObservableList<CopyBookTable> listCopiesBook;
    final String DB_URL = "jdbc:mysql://localhost:3306/library?useSSL=false";;
    final String LOGIN = "root";
    final String PASSWORD = "root_root";
    public Statement statement;
    private Connection connection;
    private CopyBookTable selectCopyBook;

    public void setSubscriber(SubscriberTable subscriber){
        this.subscriber = subscriber;
    }
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }
    @FXML
    void initialize(){
        if (connectDB()){
            selectData("where copiesBook.Status = 'в наличии' ");
        }
    }

    private void selectData(String whereQuery) {
        try {
            listCopiesBook = FXCollections.observableArrayList();
            ResultSet rsSelect = statement.executeQuery("Select * from CopiesBook " +
                    "join Editions on editions.Id = copiesBook.EditionId " +
                    "join Books on books.Id = editions.BookId " +
                    "join PublishingHouses on PublishingHouses.Id = editions.PublHouseId " + whereQuery);
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
            tvListCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CopyBookTable>() {
                @Override
                public void changed(ObservableValue<? extends CopyBookTable>
                                            observableValue, CopyBookTable student, CopyBookTable t1) {
                    if (t1 != null) {
                        selectCopyBook = t1;
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
        System.out.println(query);
        if (idsCB.size()>0){
            try {
                int rowInsertHR = statement.executeUpdate(query.toString());
                if (rowInsertHR > 0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Книги выданы.", ButtonType.OK);
                    alert.showAndWait();
                }
                selectData("where copiesBook.Status = 'в наличии'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void onCancel(ActionEvent actionEvent) {
        selectData("where copiesBook.Status = 'в наличии'");
    }
}
