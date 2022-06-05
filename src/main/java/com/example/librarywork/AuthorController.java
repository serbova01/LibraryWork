package com.example.librarywork;

import com.example.librarywork.classesCompenents.Author;
import com.example.librarywork.classesCompenents.Edition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Класс контроллера для добавления и редактирования автора.
 * <p>
 * Данный класс позволяет добавлять и редактировать автора.
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class AuthorController {
    /** Текстовое поле для фамилии автора */
    public TextField tfFirstName;
    /** Текстовое поле для имени автора */
    public TextField tfLastName;
    /** Текстовое поле для отчества автора */
    public TextField tfMiddleName;
    /** Родительский контроллер при добавлении данных */
    @FXML
    InsertController InsertPanelController;
    /** Данные автора при редактировании данных */
    private Author authorEdit;
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
    /** CheckInfo для проверок текстовых полей */
    private CheckInfo checkInfo;
    /** Окно приложения */
    private Stage dialogStage;
    /**
     * Функция изменения окна приложения {@link AuthorController#dialogStage}
     * @param addStage окно прриложения
     */
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }
    /**
     * Функция изменения Автора для редактирования {@link AuthorController#authorEdit}
     * @param author экземпляр класса Автор для редактирования данных
     */
    public void setAuthorEdit(Author author){
        this.authorEdit = author;
        showAuthor();
    }
    /**
     * Функция изменения родительского контроллера {@link AuthorController#InsertPanelController}
     * @param insertPanelController класс контроллера
     */
    public void setInsertPanelController(InsertController insertPanelController) {
        InsertPanelController = insertPanelController;
    }
    /**
     * Функция для вывода данных для редактирования в текстовые поля {@link AuthorController#tfFirstName},
     * {@link AuthorController#tfLastName} и {@link AuthorController#tfMiddleName}
     */
    private void showAuthor() {
        tfFirstName.setText(authorEdit.getFirstName());
        tfLastName.setText(authorEdit.getLastName());
        tfMiddleName.setText(authorEdit.getMiddleName());
    }
    /**
     * Функция осуществления подключения к БД и инициализация поля {@link AuthorController#statement}
     * @return состояние подключения
     */
    public boolean connectDB(){
        checkInfo = new CheckInfo();
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
     * Функция добавления или редактирования данных
     */
    public void onSave(ActionEvent actionEvent) {
        if (connectDB()){
            try {
                if (checkInfo.checkInfoIsLetter(tfFirstName.getText()) && checkInfo.checkInfoIsLetter(tfLastName.getText())){
                    String middleName = " ";
                    if (tfMiddleName!=null) middleName=tfMiddleName.getText();
                    if (authorEdit == null){
                        int rowInsertAuthor = statement.executeUpdate("Insert into authors (FirstName, LastName, MiddleName) " +
                                "values ('" + tfFirstName.getText() +"', '"+ tfLastName.getText() +"', '"+ middleName +"')");
                        if (rowInsertAuthor > 0){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Автор успешно добавлен.", ButtonType.OK);
                            alert.showAndWait();
                            InsertPanelController.selectFormCreateB();
                        }
                    }
                    else{
                        int rowUpdateAuthor = statement.executeUpdate("Update authors set FirstName = '"+tfFirstName.getText()+"', " +
                                "LastName = '"+tfLastName.getText()+"', MiddleName = '"+middleName+"' " +
                                "where id = "+ authorEdit.getId());
                        if (rowUpdateAuthor > 0){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Данные об авторе изменены.", ButtonType.OK);
                            alert.showAndWait();
                        }
                    }
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Поля Фамилия и Имя не должны содержать цифры\n " +
                            "или символы не являющихся частью алфавита.", ButtonType.OK);
                    alert.showAndWait();
                }

            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Автор с такими ФИО уже есть в системе.",
                        ButtonType.OK);
                alert.showAndWait();
            }
        }
    }
    /**
     * Функция закрытия окна
     */
    public void onCancel(ActionEvent actionEvent) {
        if (authorEdit==null){
            InsertPanelController.authorPanel.setVisible(false);
            tfClear();
        }else{
            dialogStage.close();
        }
    }
    /**
     * Функция очищения текстовых полей {@link AuthorController#tfFirstName}, {@link AuthorController#tfLastName} и
     * {@link AuthorController#tfMiddleName}.
     */
    public void tfClear() {
        tfFirstName.clear();
        tfLastName.clear();
        tfMiddleName.clear();
    }
}
