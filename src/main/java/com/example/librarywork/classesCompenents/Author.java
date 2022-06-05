package com.example.librarywork.classesCompenents;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Класс Автор со свойствами <b>id</b>, <b>firstName</b>, <b>lastName</b> и <b>middleName</b>.
 * <p>
 * Данный класс позволяет описать экземпляр автора с заданными ФИО
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class Author {
    /** Поле идентификатора */
    private IntegerProperty id;
    /** Поле фамилии */
    private StringProperty firstName;
    /** Поле имени */
    private StringProperty lastName;
    /** Поле отчества */
    private StringProperty middleName;
    /**
     * Конструктор – создание нового экземпляра с заданными параметрами
     * @param id идентификатора автора
     * @param firstName фамилии автора
     * @param lastName имени автора
     * @param middleName отчества автора
     */
    public Author(int id, String firstName, String lastName, String middleName){
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.middleName = new SimpleStringProperty(middleName);
    }
    /**
     * Функция получения строкового представления ФИО автора
     * @return возвращает строку
     */
    @Override
    public String toString(){
        return this.firstName.get() + " " + this.lastName.get() + " " + this.middleName.get();
    }
    /**
     * Функция получения значение поля {@link Author#id}
     * @return возвращает идентификатор автора
     */
    public int getId() {
        return id.get();
    }
    /**
     * Функция изменения идентификатора автора {@link Author#id}
     * @param id идентификатор автора
     */
    public void setId(int id) {
        this.id.set(id);
    }
    /**
     * Функция получения значение поля {@link Author#firstName}
     * @return возвращает фамилию автора
     */
    public String getFirstName() {
        return firstName.get();
    }
    /**
     * Функция получения значение поля {@link Author#lastName}
     * @return возвращает имя автора
     */
    public String getLastName() {
        return lastName.get();
    }
    /**
     * Функция получения значение поля {@link Author#middleName}
     * @return возвращает отчество автора
     */
    public String getMiddleName() {
        return middleName.get();
    }
}
