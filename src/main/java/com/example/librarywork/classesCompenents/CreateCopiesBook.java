package com.example.librarywork.classesCompenents;

import javafx.beans.property.*;

/**
 * Класс Запись привоза книг со свойствами <b>count</b>, <b>nameBook</b>, <b>editionId</b>, <b>editionPHName</b> и <b>ISBN</b>.
 * <p>
 * Данный класс позволяет описать экземпляр записи привоза книг с заданным количеством, названием произведения,
 * идентификатором издания, названием издательства издания, номером ISBN
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class CreateCopiesBook {
    /** Поле количества */
    private IntegerProperty count;
    /** Поле названия произведения */
    private StringProperty nameBook;
    /** Поле идентификатором издания */
    private IntegerProperty editionId;
    /** Поле названия издательства издания */
    private StringProperty editionPHName;
    /** Поле номера ISBN */
    private StringProperty ISBN;
    /**
     * Конструктор – создание нового экземпляра с заданными параметрами
     * @param count количество привезенных книг
     * @param nameBook названия произведения
     * @param editionId идентификатор издания
     * @param editionPHName название издательства издания
     * @param ISBN номер ISBN
     */
    public CreateCopiesBook(int count, String nameBook, int editionId, String editionPHName, String ISBN){
        this.count = new SimpleIntegerProperty(count);
        this.nameBook = new SimpleStringProperty(nameBook);
        this.editionId = new SimpleIntegerProperty(editionId);
        this.editionPHName = new SimpleStringProperty(editionPHName);
        this.ISBN = new SimpleStringProperty(ISBN);
    }
    /**
     * Функция получения значение поля {@link CreateCopiesBook#ISBN} для таблицы
     * @return возвращает номер ISBN
     */
    public StringProperty ISBNProperty() {
        return ISBN;
    }
    /**
     * Функция получения значение поля {@link CreateCopiesBook#count}
     * @return возвращает количество привезенных книг
     */
    public int getCount() {
        return count.get();
    }
    /**
     * Функция получения значение поля {@link CreateCopiesBook#count} для таблицы
     * @return возвращает количество привезенных книг
     */
    public IntegerProperty countProperty() {
        return count;
    }
    /**
     * Функция получения значение поля {@link CreateCopiesBook#nameBook} для таблицы
     * @return возвращает название произведения
     */
    public StringProperty nameBookProperty() {
        return nameBook;
    }
    /**
     * Функция получения значение поля {@link CreateCopiesBook#editionId}
     * @return возвращает идентификатор издания
     */
    public int getEditionId() {
        return editionId.get();
    }
    /**
     * Функция получения значение поля {@link CreateCopiesBook#editionPHName} для таблицы
     * @return возвращает название издательства издания
     */
    public StringProperty editionPHNameProperty() {
        return editionPHName;
    }
}
