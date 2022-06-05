package com.example.librarywork.classesCompenents;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Класс История читателей произведения со свойствами <b>inventoryNumber</b>, <b>strDateIssue</b>, <b>strDateReturn</b> и
 * <b>subsName</b>.
 * <p>
 * Данный класс позволяет описать экземпляр истории читателей произведения с заданным инвентарным номером экземпляра книги,
 * строковое представление даты выдачи, строковое представление даты возврата и имя читателя
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class HistoryReaderB{
    /** Поле инвентарного номера */
    private IntegerProperty inventoryNumber;
    /** Поле строкового представления даты выдачи */
    private StringProperty strDateIssue;
    /** Поле строкового представления даты возврата */
    private StringProperty strDateReturn;
    /** Поле имени читателя */
    private StringProperty subsName;
    /**
     * Конструктор – создание нового экземпляра с заданными параметрами для каталожной карточки
     * @param inventoryNumber инвентарный номер
     * @param strDateIssue строковое представление даты выдачи
     * @param strDateReturn строковое представление даты возврата
     * @param subsName имени читателя
     */
    public HistoryReaderB(int inventoryNumber, String strDateIssue, String strDateReturn, String subsName) {
        this.inventoryNumber = new SimpleIntegerProperty(inventoryNumber);
        this.strDateIssue = new SimpleStringProperty(strDateIssue);
        this.strDateReturn = new SimpleStringProperty(strDateReturn);
        this.subsName = new SimpleStringProperty(subsName);
    }
    /**
     * Функция получения значение поля {@link HistoryReaderB#inventoryNumber} для таблицы
     * @return возвращает инвентарный номер
     */
    public IntegerProperty inventoryNumberProperty() {
        return inventoryNumber;
    }
    /**
     * Функция получения значение поля {@link HistoryReaderB#strDateIssue} для таблицы
     * @return возвращает строковое представление даты выдачи
     */
    public StringProperty strDateIssueProperty() {
        return strDateIssue;
    }
    /**
     * Функция получения значение поля {@link HistoryReaderB#strDateReturn} для таблицы
     * @return возвращает  строковое представление даты возврата
     */
    public StringProperty strDateReturnProperty() {
        return strDateReturn;
    }
    /**
     * Функция получения значение поля {@link HistoryReaderB#subsName} для таблицы
     * @return возвращает имя читателя
     */
    public StringProperty subsNameProperty() {
        return subsName;
    }


}
