package com.example.librarywork;

import com.example.librarywork.classesCompenents.SubscriberTable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

/**
 * Класс для добавления и редактирования данных абонента.
 * <p>
 * Данный класс добавить нового абонента или отредактировать данные текущего абонента.
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class SubscriberController {
    /** Текстовое поле фамилии абонента */
    public TextField tfSFirstName;
    /** Текстовое поле имени абонента */
    public TextField tfSSecondName;
    /** Текстовое поле отчества абонента */
    public TextField tfSMiddleName;
    /** Текстовое поле номера телефона абонента */
    public TextField tfSPhoneNum;
    /** Текстовое поле улицы проживания */
    public TextField tfSStreet;
    /** Текстовое поле дома проживания */
    public TextField tfSHouse;
    /** Текстовое поле квартиры проживания */
    public TextField tfSFlat;
    /** Адрес базы данных */
    final String DB_URL1 = "jdbc:mysql://localhost:3306/library?useSSL=false";
    /** Логин для подключения к БД */
    final String LOGIN1 = "root";
    /** Пароль для подключения к БД */
    final String PASSWORD1 = "root_root";
    /** Statement для выполнения SQL запросов */
    public Statement statement1;
    /** Connection для подключения к БД */
    private Connection connection1;
    /** Окно приложения */
    private Stage dialogStage;
    /** CheckInfo для проверок текстовых полей */
    private CheckInfo checkInfo;
    /** Абонент, данные которого нужно редактировать */
    private SubscriberTable subscriber;
    /**
     * Функция инициализации класса и {@link SubscriberController#checkInfo}
     */
    @FXML
    void initialize(){
        if (connectDB()){
            checkInfo = new CheckInfo();
        }
    }
    /**
     * Функция заполнения тестовых полей {@link SubscriberController#tfSFirstName}, {@link SubscriberController#tfSSecondName},
     * {@link SubscriberController#tfSMiddleName}, {@link SubscriberController#tfSPhoneNum},
     * {@link SubscriberController#tfSStreet}, {@link SubscriberController#tfSHouse}, {@link SubscriberController#tfSFlat}
     * данными {@link SubscriberController#subscriber}
     */
    private void showSubscriber() {
        tfSFirstName.setText(subscriber.getFirstName());
        tfSSecondName.setText(subscriber.getSecondName());
        tfSMiddleName.setText(subscriber.getMiddleName());
        tfSPhoneNum.setText(subscriber.getPhoneNumber());
        String[] address = subscriber.getAddress().split(", ");
        tfSStreet.setText(address[0]);
        tfSHouse.setText(address[1]);
        if (address.length>2) tfSFlat.setText(address[2]);
    }
    /**
     * Функция изменения окна приложения {@link SubscriberController#dialogStage}
     * @param addStage окно прриложения
     */
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }
    /**
     * Функция изменения абонента для редактирования {@link SubscriberController#subscriber}
     * @param subscriber абонент
     */
    public void setSubscriberEdit(SubscriberTable subscriber) {
        this.subscriber = subscriber;
        showSubscriber();
    }
    /**
     * Функция осуществления подключения к БД и инициализация поля {@link SubscriberController#statement1}
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
            connection1 = DriverManager.getConnection(DB_URL1, LOGIN1, PASSWORD1);
            //устанавливаем соединение
            statement1 = connection1.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Connection error!");
            return false;
        }
        return true;
    }
    /**
     * Функция проверки данных перед сохранение изменений в БД
     */
    public void onAddSubscriber(ActionEvent actionEvent) {
        String house = " ";
        if (tfSHouse != null) house = tfSHouse.getText();
        if (checkInfo.checkInfoIsLetter(tfSFirstName.getText()) && checkInfo.checkInfoIsLetter(tfSSecondName.getText())
                && checkInfo.checkInfoPhoneNumber(tfSPhoneNum.getText()) && checkInfo.checkInfoIsLetter(tfSStreet.getText())
                && checkInfo.checkInfoIsLetterOrDigit(house)){
            String middleName = " ";
            if (tfSMiddleName != null) middleName = tfSMiddleName.getText();
            if (middleName.length()<2 | checkInfo.checkInfoIsLetter(middleName)){
                if (subscriber == null) insertSubscriber();
                else {
                    tfSHouse.setText(house);
                    subscriber.setMiddleName(middleName);
                    editSubscriber();
                }

            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Поле Отчество должно содержать символы алфавита.",
                        ButtonType.OK);
                alert.showAndWait();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "Поля Фамилия, Имя, Телефон, Улица, Дом\n" +
                    " должны быть заполнены корректными данными. \n" +
                    "Поле Телефон должно иметь такой формат: \n" +
                    "8 (***)-***-**-** или +7 (***)-***-**-**", ButtonType.OK);
            alert.showAndWait();
        }
    }
    /**
     * Функция сохранения изменений в редактируемом абоненте {@link SubscriberController#subscriber}
     */
    private void editSubscriber() {
        try {
            String flat = " ";
            if (tfSFlat!=null) flat=tfSFlat.getText();
            int row = statement1.executeUpdate("Update Subscribers Set FirstName = '" + tfSFirstName.getText() +"', " +
                    "SecondName = '"+tfSSecondName.getText()+"', MiddleName = '"+subscriber.getMiddleName()+"', PhoneNumber = " +
                    "'" +tfSPhoneNum.getText()+ "', Street = '"+ tfSStreet.getText()+"', House = '"+tfSHouse.getText()+"', " +
                    "Flat = '"+flat+"' where id = " + subscriber.getSubsNumber());
            if (row>0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Данные об абоненте успешно изменены.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Функция добавления данных в БД.
     */
    private void insertSubscriber() {
        try {
            String flat = " ";
            if (tfSFlat!=null) flat=tfSFlat.getText();
            int row = statement1.executeUpdate("Insert into subscribers (FirstName, SecondName, MiddleName, PhoneNumber, " +
                    "DateRegistration, Street, House, Flat) values ('"+ tfSFirstName.getText() + "', '" +
                    tfSSecondName.getText() + "', '" + tfSMiddleName.getText() + "', '" + tfSPhoneNum.getText() + "'," +
                    " '" + Date.valueOf(LocalDate.now()) + "', '" + tfSStreet.getText() + "', '" + tfSHouse.getText() + "', " +
                    "'" + flat + "')");
            if(row>0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Абонент успешно добавлен.");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Абонент с таким номером телефона уже есть в системе.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Функция очищения тестовых полей {@link SubscriberController#tfSFirstName}, {@link SubscriberController#tfSSecondName},
     * {@link SubscriberController#tfSMiddleName}, {@link SubscriberController#tfSPhoneNum}, {@link SubscriberController#tfSStreet},
     * {@link SubscriberController#tfSHouse} и {@link SubscriberController#tfSFlat}
     */
    public void onCancel(ActionEvent actionEvent) {
        tfSHouse.clear();
        tfSStreet.clear();
        tfSPhoneNum.clear();
        tfSMiddleName.clear();
        tfSSecondName.clear();
        tfSFlat.clear();
        tfSFirstName.clear();
    }

}
