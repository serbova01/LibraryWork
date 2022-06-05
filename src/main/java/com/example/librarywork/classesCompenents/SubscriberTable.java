package com.example.librarywork.classesCompenents;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

/**
 * Класс Абонент со свойствами <b>subsNumber</b>, <b>firstName</b>, <b>secondName</b>, <b>middleName</b>,
 * <b>phoneNumber</b>, <b>dateRegistration</b> и <b>address</b>.
 * <p>
 * Данный класс позволяет описать экземпляр абонента с заданным абонементным номером, фамилией, именем, отчеством,
 * номером телефона, датой регистрации и адресом.
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class SubscriberTable {
    /** Поле абонементного номера */
    private IntegerProperty subsNumber;
    /** Поле фамилии */
    private StringProperty firstName;
    /** Поле имени */
    private StringProperty secondName;
    /** Поле отчества */
    private StringProperty middleName;
    /** Поле номера телефона */
    private StringProperty phoneNumber;
    /** Поле даты регистрации */
    private Date dateRegistration;
    /** Поле адреса */
    private StringProperty address;
    /**
     * Конструктор – создание нового экземпляра с заданными параметрами
     * @param subsNumber абонементный номера бонента
     * @param firstName фамилия абонента
     * @param secondName  имя абонента
     * @param middleName  отчество абонента
     * @param phoneNumber номер телефона абонента
     * @param dateRegistration строковое представление даты регистрации абонента
     * @param address адрес абонента
     */
    public SubscriberTable(int subsNumber, String firstName, String secondName, String middleName, String phoneNumber,
                           Date dateRegistration, String address){
        this.subsNumber = new SimpleIntegerProperty(subsNumber);
        this.firstName = new SimpleStringProperty(firstName);
        this.secondName = new SimpleStringProperty(secondName);
        this.middleName = new SimpleStringProperty(middleName);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.dateRegistration = dateRegistration;
        this.address = new SimpleStringProperty(address);
    }
    /**
     * Функция получения значение поля {@link SubscriberTable#subsNumber}
     * @return возвращает абонементный номер
     */
    public int getSubsNumber() {
        return subsNumber.get();
    }
    /**
     * Функция получения значение поля {@link SubscriberTable#subsNumber} для таблицы
     * @return возвращает абонементный номер
     */
    public IntegerProperty subsNumberProperty() {
        return subsNumber;
    }
    /**
     * Функция получения значение поля {@link SubscriberTable#firstName}
     * @return возвращает фамилию абонента
     */
    public String getFirstName() {
        return firstName.get();
    }
    /**
     * Функция получения значение поля {@link SubscriberTable#firstName} для таблицы
     * @return возвращает фамилию абонента
     */
    public StringProperty firstNameProperty() {
        return firstName;
    }
    /**
     * Функция получения значение поля {@link SubscriberTable#secondName}
     * @return возвращает имя абонента
     */
    public String getSecondName() {
        return secondName.get();
    }
    /**
     * Функция получения значение поля {@link SubscriberTable#firstName} для таблицы
     * @return возвращает фамилию абонента
     */
    public StringProperty secondNameProperty() {
        return secondName;
    }
    /**
     * Функция получения значение поля {@link SubscriberTable#middleName}
     * @return возвращает отчество абонента
     */
    public String getMiddleName() {
        return middleName.get();
    }
    /**
     * Функция получения значение поля {@link SubscriberTable#middleName} для таблицы
     * @return возвращает отчество абонента
     */
    public StringProperty middleNameProperty() {
        return middleName;
    }
    /**
     * Функция изменения идентификатора {@link SubscriberTable#middleName}
     * @param middleName отчество абонента
     */
    public void setMiddleName(String middleName) {
        this.middleName.set(middleName);
    }
    /**
     * Функция получения значение поля {@link SubscriberTable#phoneNumber}
     * @return возвращает номер телефона абонента
     */
    public String getPhoneNumber() {
        return phoneNumber.get();
    }
    /**
     * Функция получения значение поля {@link SubscriberTable#phoneNumber} для таблицы
     * @return возвращает номер телефона абонента
     */
    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }
    /**
     * Функция получения значение поля {@link SubscriberTable#dateRegistration} для таблицы
     * @return возвращает дату регистрации в строковом представлении
     */
    public StringProperty strDateRegProperty() {
        return new SimpleStringProperty(dateRegistration.toString());
    }
    /**
     * Функция получения значение поля {@link SubscriberTable#address}
     * @return возвращает адрес абонента
     */
    public String getAddress() {
        return address.get();
    }
    /**
     * Функция получения значение поля {@link SubscriberTable#address} для таблицы
     * @return возвращает адрес абонента
     */
    public StringProperty addressProperty() {
        return address;
    }
}
