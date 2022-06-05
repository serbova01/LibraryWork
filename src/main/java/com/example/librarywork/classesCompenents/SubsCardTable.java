package com.example.librarywork.classesCompenents;

import javafx.beans.property.*;

/**
 * Класс Запись в абонементной карточке со свойствами <b>id</b>, <b>inventoryNumber</b>, <b>department</b>, <b>book</b>,
 * <b>dateIssue</b>, <b>dateReturn</b>, <b>bookId</b>, <b>authors</b> и <b>issue</b>.
 * <p>
 * Данный класс позволяет описать экземпляр записи в абонементной карточке с заданным идентификатором, инвентарным номером
 * выданного экземляра, отделом, названием произведения, датой выдачи, датой возврата, идентификатором произведения, списком
 * авторов произведения и обозначение актуальности записи.
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class SubsCardTable {
    /** Поле идентификатора */
    private int id;
    /** Поле инвентарного номера */
    private IntegerProperty inventoryNumber;
    /** Поле отдела */
    private StringProperty department;
    /** Поле названия произведения */
    private StringProperty book;
    /** Поле даты выдачи */
    private StringProperty dateIssue;
    /** Поле даты возврата */
    private StringProperty dateReturn;
    /** Поле идентификатора произведения */
    private int bookId;
    /** Поле списка авторов произведения */
    private StringProperty authors;
    /** Поле обозначения актуальности записи */
    private SimpleBooleanProperty issue;
    /**
     * Конструктор – создание нового экземпляра с заданными параметрами для каталожной карточки
     * @param id идентификатор
     * @param inventoryNumber инвентарный номер экземпляра книги
     * @param department  отдел
     * @param dateIssue строковое представление даты выдачи
     * @param dateIssue строковое представление даты выдачи
     * @param book название произведения
     * @param bookId идентификатор произведения
     * @param authors список авторов произведения
     * @param issue обозначение актуальности записи
     */
    public SubsCardTable(int id, int inventoryNumber, String department, String book,String dateIssue,String dateReturn, int bookId,
                         String authors, boolean issue) {
        this.id =id;
        this.inventoryNumber = new SimpleIntegerProperty(inventoryNumber);
        this.department = new SimpleStringProperty(department);
        this.dateIssue = new SimpleStringProperty(dateIssue);
        this.dateReturn = new SimpleStringProperty(dateReturn);
        this.book = new SimpleStringProperty(book);
        this.bookId = bookId;
        this.authors = new SimpleStringProperty(authors);
        this.issue = new SimpleBooleanProperty(issue);
    }
    /**
     * Функция получения значение поля {@link SubsCardTable#id}
     * @return возвращает идентификатор
     */
    public int getId() {
        return id;
    }
    /**
     * Функция изменения идентификатора {@link SubsCardTable#id}
     * @param id идентификатор
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Функция получения значение поля {@link SubsCardTable#inventoryNumber}
     * @return возвращает инвентарный номер экземпляра выданной книги
     */
    public int getInventoryNumber() {
        return inventoryNumber.get();
    }
    /**
     * Функция получения значение поля {@link SubsCardTable#inventoryNumber} для таблицы
     * @return возвращает инвентарный номер
     */
    public IntegerProperty inventoryNumberProperty() {
        return inventoryNumber;
    }
    /**
     * Функция получения значение поля {@link SubsCardTable#department}
     * @return возвращает отдел
     */
    public String getDepartment() {
        return department.get();
    }
    /**
     * Функция получения значение поля {@link SubsCardTable#department} для таблицы
     * @return возвращает отдел
     */
    public StringProperty departmentProperty() {
        return department;
    }
    /**
     * Функция получения значение поля {@link SubsCardTable#issue}
     * @return возвращает обозначение актуальности записи
     */
    public boolean isIssue() {
        return issue.get();
    }
    /**
     * Функция получения значение поля {@link SubsCardTable#issue} для таблицы
     * @return возвращает обозначение актуальности записи
     */
    public SimpleBooleanProperty issueProperty() {
        return issue;
    }
    /**
     * Функция получения значение поля {@link SubsCardTable#dateIssue} для таблицы
     * @return возвращает дату выдачи
     */
    public StringProperty dateIssueProperty() {
        return dateIssue;
    }
    /**
     * Функция получения значение поля {@link SubsCardTable#dateReturn} для таблицы
     * @return возвращает дату возврата
     */
    public StringProperty dateReturnProperty() {
        return dateReturn;
    }
    /**
     * Функция изменения идентификатора издания {@link SubsCardTable#issue}
     * @param issue обозначение актуальности записи
     */
    public void setIssue(boolean issue) {
        this.issue.set(issue);
    }
    /**
     * Функция получения значение строки "'Список авторов' - 'Название книги'" для таблицы
     * @return возвращает строку
     */
    public StringProperty authorsBookNameProperty() {
        String authorsStr = this.authors.get();
        String bookName = this.book.get();
        String authorsBookName = authorsStr + " - " + bookName;
        return new SimpleStringProperty(authorsBookName);
    }
    /**
     * Функция получения значение строки "'Главный автор', 'Название книги'"
     * @return возвращает строку для записи в файл
     */
    public String getAuthorsBookName() {
        String authorsBookName = authors.get().split(", ")[0] + ", " + this.book.get();
        return authorsBookName;
    }
}
