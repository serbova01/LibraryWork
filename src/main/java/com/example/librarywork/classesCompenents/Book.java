package com.example.librarywork.classesCompenents;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Класс Произведение со свойствами <b>id</b>, <b>name</b>, <b>genre</b>, <b>department</b>, <b>authors</b>,
 * <b>authorsFullName</b> и <b>count</b>.
 * <p>
 * Данный класс позволяет описать экземпляр произведения с заданным идентификатором, названием, жанром, отделом,
 * списком авторов (кроткие имена), списком авторов (полные имена), количеством экземпляров
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class Book {
    /** Поле идентификатора */
    private IntegerProperty id;
    /** Поле названия */
    private StringProperty name;
    /** Поле жанра */
    private StringProperty genre;
    /** Поле отдела */
    private StringProperty department;
    /** Поле списка авторов (короткие имена) */
    private StringProperty authors;
    /** Поле списка авторов (полные имена)а */
    private String authorsFullName;
    /** Поле количества экземпляров */
    private IntegerProperty count;
    /**
     * Конструктор – создание нового экземпляра с заданными параметрами для списка произведений
     * @param id идентификатора произведения
     * @param name названия произведения
     * @param authors списка авторов (короткие имена) произведения
     */
    public Book(int id, String name, String authors){
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.authors = new SimpleStringProperty(authors);
    }
    /**
     * Конструктор – создание нового экземпляра с заданными параметрами для каталога книг
     * @param id идентификатора произведения
     * @param name названия произведения
     * @param genre жанра произведения
     * @param department отдела произведения
     * @param authors списка авторов (короткие имена) произведения
     * @param fullNameAuthors списка авторов (полные имена) произведения
     * @param count количества экземпляров произведения
     */
    public Book(int id, String name, String genre, String department, String authors, String fullNameAuthors, int count){
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.genre = new SimpleStringProperty(genre);
        this.department = new SimpleStringProperty(department);
        this.authors = new SimpleStringProperty(authors);
        this.authorsFullName = fullNameAuthors;
        this.count = new SimpleIntegerProperty(count);
    }
    /**
     * Функция получения значение поля {@link Book#id}
     * @return возвращает идентификатор произведения
     */
    public int getId() {
        return id.get();
    }
    /**
     * Функция изменения идентификатора автора {@link Book#id}
     * @param id идентификатор произведения
     */
    public void setId(int id) {
        this.id.set(id);
    }
    /**
     * Функция получения значение поля {@link Book#name}
     * @return возвращает название произведения
     */
    public String getName() {
        return name.get();
    }
    /**
     * Функция получения значение поля {@link Book#name} для таблицы
     * @return возвращает название произведения
     */
    public StringProperty nameProperty() {
        return name;
    }
    /**
     * Функция получения значение поля {@link Book#genre}
     * @return возвращает жанр произведения
     */
    public String getGenre() {
        return genre.get();
    }
    /**
     * Функция получения значение поля {@link Book#genre} для таблицы
     * @return возвращает жанра произведения
     */
    public StringProperty genreProperty() {
        return genre;
    }
    /**
     * Функция получения значение поля {@link Book#department} для таблицы
     * @return возвращает отдела произведения
     */
    public StringProperty departmentProperty() {
        return department;
    }
    /**
     * Функция получения значение поля {@link Book#authors}
     * @return возвращает список авторов (короткие имена) произведения
     */
    public String getAuthors() {
        return authors.get();
    }
    /**
     * Функция получения значение поля {@link Book#authors} для таблицы
     * @return возвращает список авторов (короткие имена) произведения
     */
    public StringProperty authorsProperty() {
        return authors;
    }
    /**
     * Функция изменения идентификатора автора {@link Book#authors}
     * @param authors список авторов (короткие имена) произведения
     */
    public void setAuthors(String authors) {
        this.authors.set(authors);
    }
    /**
     * Функция получения значение поля {@link Book#count} для таблицы
     * @return возвращает количество экземпляров произведения
     */
    public IntegerProperty countProperty() {
        return count;
    }
    /**
     * Функция изменения идентификатора автора {@link Book#count}
     * @param count количество экземпляров произведения
     */
    public void setCount(int count) {
        this.count.set(count);
    }
    /**
     * Функция получения значение поля {@link Book#authorsFullName}
     * @return возвращает список авторов (полные имена) произведения
     */
    public String getAuthorsFullName() {
        return authorsFullName;
    }
    /**
     * Функция изменения идентификатора автора {@link Book#authorsFullName}
     * @param authorsFullName список авторов (полные имена) произведения
     */
    public void setAuthorsFullName(String authorsFullName) {
        this.authorsFullName = authorsFullName;
    }
}
