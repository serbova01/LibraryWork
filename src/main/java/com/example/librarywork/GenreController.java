package com.example.librarywork;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.sql.*;

/**
 * Класс контроллера для добавления жанра.
 * <p>
 * Данный класс позволяет добавлять жанр.
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class GenreController {
    /** Текстовое поле для названия жанра */
    public TextField tfNameGenre;
    /** Родительский контроллер при добавлении данных */
    @FXML
    InsertController InsertPanelController;
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
    /**
     * Функция очищения текстовых полей {@link GenreController#tfNameGenre}.
     */
    public void tfClear(){
        this.tfNameGenre.clear();
    }
    /**
     * Функция изменения родительского контроллера {@link AuthorController#InsertPanelController}
     * @param insertPanelController класс контроллера
     */
    public void setInsertPanelController(InsertController insertPanelController) {
        InsertPanelController = insertPanelController;
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
     * Функция добавления данных и формирование нового отдела
     */
    public void onSave(ActionEvent actionEvent) {
        if (connectDB()){

            try {
                if (checkInfo.checkInfoIsLetter(tfNameGenre.getText())){
                    String department = newDepartment();
                    int rowInsertGenre = statement.executeUpdate("Insert into genres (Name, Department) " +
                            "values ('" + tfNameGenre.getText() +"', '"+ department +"')");
                    if (rowInsertGenre > 0){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Жанр успешно добавлен.", ButtonType.OK);
                        alert.showAndWait();
                        InsertPanelController.selectFormCreateB();
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Поле Жанр должно содержать только\n " +
                            "символы алфавита.", ButtonType.OK);
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Такой жанр уже есть в системе.", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }
    /**
     * Функция получения нового отдела по первой букве названия жанра
     * @return новый отдел
     */
    String newDepartment(){
        String newGenre = tfNameGenre.getText();
        int maxNumDepart = 0;
        try {
            ResultSet rsGenresTable = statement.executeQuery("Select * from Genres");
            String genre = "";
            if (rsGenresTable.getRow()>0){
                while (rsGenresTable.next()){
                    genre = "" + rsGenresTable.getString("Name").charAt(0);
                    if (genre.equals(newGenre.charAt(0))){
                        int numDepartment = Integer.valueOf(rsGenresTable.getString("Department").charAt(1));
                        if (numDepartment > maxNumDepart) maxNumDepart = numDepartment;
                    }
                }
                return "" + newGenre.charAt(0) + maxNumDepart;
            }
            else return "" + newGenre.charAt(0) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     * Функция закрытия окна
     */
    public void onCancel(ActionEvent actionEvent) {
        InsertPanelController.genrePanel.setVisible(false);
        tfNameGenre.clear();
    }
}
