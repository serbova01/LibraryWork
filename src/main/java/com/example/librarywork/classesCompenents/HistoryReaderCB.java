package com.example.librarywork.classesCompenents;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

/**
 * Класс История читателей экземпляра книги со свойствами <b>dateIssue</b>, <b>dateReturn</b> и <b>subsName</b>.
 * <p>
 * Данный класс позволяет описать экземпляр истории читателей экземпляра книги с заданным датой выдачи, датой возврата и имя читателя
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class HistoryReaderCB {
    /** Поле даты выдачи */
    private Date dateIssue;
    /** Поле даты возврата */
    private Date dateReturn;
    /** Поле имени читателя */
    private StringProperty subsName;
    /**
     * Конструктор – создание нового экземпляра с заданными параметрами для каталожной карточки
     * @param dateIssue строковое представление даты выдачи
     * @param dateReturn строковое представление даты возврата
     * @param subsName имени читателя
     */
    public HistoryReaderCB(Date dateIssue, Date dateReturn,String subsName){
        this.dateIssue = dateIssue;
        this.dateReturn = dateReturn;
        this.subsName = new SimpleStringProperty(subsName);
    }
    /**
     * Функция получения значение поля {@link HistoryReaderCB#dateIssue} для таблицы
     * @return возвращает дата выдачи
     */
    public StringProperty strDateIssueProperty() {
        return new SimpleStringProperty(dateIssue.toString());
    }
    /**
     * Функция получения значение поля {@link HistoryReaderCB#dateReturn} для таблицы
     * @return возвращает дата возврата
     */
    public StringProperty strDateReturnProperty() {
        return new SimpleStringProperty(dateReturn.toString());
    }
    /**
     * Функция получения значение поля {@link HistoryReaderCB#subsName} для таблицы
     * @return возвращает имя читателя
     */
    public StringProperty subsNameProperty() {
        return subsName;
    }
}
