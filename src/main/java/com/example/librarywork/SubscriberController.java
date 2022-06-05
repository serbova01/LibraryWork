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

public class SubscriberController {
    public TextField tfSFirstName;
    public TextField tfSSecondName;
    public TextField tfSMiddleName;
    public TextField tfSPhoneNum;
    public TextField tfSStreet;
    public TextField tfSHouse;
    public TextField tfSFlat;

    final String DB_URL1 = "jdbc:mysql://localhost:3306/library?useSSL=false";;
    final String LOGIN1 = "root";
    final String PASSWORD1 = "root_root";
    public Statement statement1;
    private Connection connection1;
    private Stage dialogStage;
    private CheckInfo checkInfo;
    private SubscriberTable subscriber;

    @FXML
    void initialize(){
        if (connectDB()){
            checkInfo = new CheckInfo();
        }
    }

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

    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }
    public void setSubscriberEdit(SubscriberTable subscriber) {
        this.subscriber = subscriber;
        showSubscriber();
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

    public void onAddSubscriber(ActionEvent actionEvent) {
        String house = " ";
        System.out.println("checkInfoIsLetter(tfSStreet.getText()): " + checkInfo.checkInfoIsLetter(tfSStreet.getText()));
        System.out.println("checkInfoIsLetterOrDigit(house): " + checkInfo.checkInfoIsLetterOrDigit(house));
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
