package com.example.librarywork;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

/**
 * Класс контроллера для добавления издательства.
 * <p>
 * Данный класс позволяет добавлять издательство.
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class InsertPHController {
    /** Текстовое поле для названия издательства*/
    public TextField tfNamePH;
    /** Контейнер последовательности названий городов */
    public Spinner<String> sCity;
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
    /** Родительский контроллер при добавлении данных */
    @FXML
    InsertController InsertPanelController;
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

        checkInfo = new CheckInfo();
        return true;
    }
    /**
     * Функция инициализации класса и заполнение контейнера последовательности  {@link InsertPHController#sCity}
     */
    @FXML
    void initialize(){
        ObservableList<String> cities = FXCollections.observableArrayList("Москва", "Смоленск", "Санкт-Петербург",
                "Ростов-на-Дону", "Ярославль", "Иркутск", "Аксай", "Обнинск", "Волгоград", "Рязань");
        SpinnerValueFactory<String> citiesSVF = new SpinnerValueFactory.ListSpinnerValueFactory<String>(cities);
        citiesSVF.setValue("Москва");
        sCity.setValueFactory(citiesSVF);
    }
    /**
     * Функция добавления данных
     */
    public void onSave(ActionEvent actionEvent) {
        if (connectDB()){
           if(checkInfo.checkInfoIsLetter(tfNamePH.getText())){
               try {
                   int row = statement.executeUpdate("Insert into PublishingHouses (NamePH, Place) " +
                           "values ('"+ tfNamePH.getText() + "', '" + sCity.getValue() + "')");
                   if (row > 0){
                       Alert alert = new Alert(Alert.AlertType.INFORMATION, "Издательство успешно добавлено.", ButtonType.OK);
                       alert.showAndWait();
                       InsertPanelController.selectFormCreateEd();
                   }
               } catch (SQLException e) {
                   Alert alert = new Alert(Alert.AlertType.INFORMATION, "Такое издательство уже есть в системе.", ButtonType.OK);
                   alert.showAndWait();
               }
           } else{
             Alert alert = new Alert(Alert.AlertType.WARNING , "Поле Название издательства должно содержать\n" +
                     " символы алфавита.", ButtonType.OK);
             alert.showAndWait();
           }
        }
    }
    /**
     * Функция закрытия окна
     */
    public void onCancel(ActionEvent actionEvent) {
        InsertPanelController.publHousePanel.setVisible(false);
        tfNamePH.clear();
    }
}
