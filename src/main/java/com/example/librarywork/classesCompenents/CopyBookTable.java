package com.example.librarywork.classesCompenents;

import javafx.beans.property.*;

/**
 * Класс Экземпляра книги со свойствами <b>inventoryNumber</b>, <b>status</b>, <b>book</b>, <b>publHouse</b> и <b>issue</b>.
 * <p>
 * Данный класс позволяет описать экземпляр экземпляра книги с заданным инвентарным номером, статусом, названием произведения,
 * названием издательства, обозначением выдачи книги
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class CopyBookTable {
    /** Поле инвентарным номером */
    private IntegerProperty inventoryNumber;
    /** Поле статуса */
    private StringProperty status;
    /** Поле названия произведения */
    private StringProperty book;
    /** Поле названия издательства */
    private StringProperty publHouse;
    /** Поле обозначения выдачи книги */
    private SimpleBooleanProperty issue;
    /**
     * Конструктор – создание нового экземпляра с заданными параметрами для перечня книг
     * @param inventoryNumber инвентарного номера экземпляра книги
     * @param book названия произведения экземпляра книги
     * @param publHouse название издательства экземпляра книги
     * @param status статус экземпляра книги
     */
    public CopyBookTable(int inventoryNumber, String book, String publHouse, String status){
        this.inventoryNumber = new SimpleIntegerProperty(inventoryNumber);
        this.book = new SimpleStringProperty(book);
        this.publHouse = new SimpleStringProperty(publHouse);
        this.status = new SimpleStringProperty(status);
        this.issue = new SimpleBooleanProperty(false);
    }
    /**
     * Функция получения значение поля {@link CopyBookTable#inventoryNumber}
     * @return возвращает инвентарный номер экземпляра книги
     */
    public int getInventoryNumber() {
        return inventoryNumber.get();
    }
    /**
     * Функция получения значение поля {@link CopyBookTable#inventoryNumber} для таблицы
     * @return возвращает инвентарный номер экземпляра книги
     */
    public IntegerProperty inventoryNumberProperty() {
        return inventoryNumber;
    }
    /**
     * Функция получения значение поля {@link CopyBookTable#status}
     * @return возвращает статус экземпляра книги
     */
    public String getStatus() {
        return status.get();
    }
    /**
     * Функция получения значение поля {@link CopyBookTable#status} для таблицы
     * @return возвращает статус экземпляра книги
     */
    public StringProperty statusProperty() {
        return status;
    }
    /**
     * Функция получения значение поля {@link CopyBookTable#book}
     * @return возвращает название произведения экземпляра книги
     */
    public String getBook() {
        return book.get();
    }
    /**
     * Функция получения значение поля {@link CopyBookTable#book} для таблицы
     * @return возвращает название произведения экземпляра книги
     */
    public StringProperty bookProperty() {
        return book;
    }
    /**
     * Функция изменения идентификатора автора {@link CopyBookTable#book}
     * @param book название произведения экземпляра книги
     */
    public void setBook(String book) {
        this.book.set(book);
    }
    /**
     * Функция получения значение поля {@link CopyBookTable#publHouse} для таблицы
     * @return возвращает название издательства экземпляра книги
     */
    public StringProperty publHouseProperty() {
        return publHouse;
    }
    /**
     * Функция получения значение поля {@link CopyBookTable#issue}
     * @return возвращает обозначение выдачи экземпляра книги
     */
    public boolean isIssue() {
        return issue.get();
    }
    /**
     * Функция изменения идентификатора автора {@link CopyBookTable#book}
     * @param issue обозначение выдачи экземпляра книги
     */
    public void setIssue(boolean issue) {
        this.issue.set(issue);
    }
}
