package com.example.librarywork.classesCompenents;

/**
 * Класс Издание со свойствами <b>id</b>, <b>description</b>, <b>yearPubl</b>, <b>countPage</b>, <b>image</b>,
 * <b>ISBN</b>, <b>bookId</b>, <b>publHouseName</b> и <b>price</b>.
 * <p>
 * Данный класс позволяет описать экземпляр издания книги с заданным идентификатором издания, описанием книги,
 * годом издания, количеством страниц, название обложки, номером ISBN, идентификатором произведения,
 * названием издательства и ценой
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class Edition {
    /** Поле идентификатора издания */
    private int id;
    /** Поле описания книги */
    private String description;
    /** Поле года издания */
    private int yearPubl;
    /** Поле количества страниц */
    private int countPage;
    /** Поле названия обложки */
    private String image;
    /** Поле номера ISBN */
    private String ISBN;
    /** Поле идентификатор произведения */
    private int bookId;
    /** Поле названия произведения */
    private String bookName;
    /** Поле названия издательства */
    private String publHouseName;
    /** Поле цены */
    private double price;
    /**
     * Конструктор – создание нового экземпляра с заданными параметрами добавления и изменения в БД
     * @param id идентификатор издания
     * @param description описание книги
     * @param yearPubl года издания
     * @param countPage количество страниц
     * @param image название обложки
     * @param ISBN номер ISBN
     * @param bookId идентификатор произведения
     * @param bookName название произведения
     * @param publHouseName название издательства издания
     * @param price цена
     */
    public Edition(int id, String description, int yearPubl, int countPage, String image, String ISBN, int bookId,
                   String bookName, String publHouseName, double price) {
        this.id = id;
        this.description = description;
        this.yearPubl = yearPubl;
        this.countPage = countPage;
        this.image = image;
        this.ISBN = ISBN;
        this.bookId = bookId;
        this.bookName = bookName;
        this.publHouseName = publHouseName;
        this.price = price;
    }
    /**
     * Функция получения записи наименования каталожной карточки
     * @return возвращает строку
     */
    @Override
    public String toString(){
        return id + " - " + bookName + " - " + ISBN + " (" + yearPubl + ")";
    }
    /**
     * Функция получения значение поля {@link Edition#id}
     * @return возвращает идентификатор издания
     */
    public int getId() {
        return id;
    }
    /**
     * Функция изменения идентификатора издания {@link Edition#id}
     * @param id идентификатор издания
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Функция получения значение поля {@link Edition#description}
     * @return возвращает описание книги
     */
    public String getDescription() {
        return description;
    }
    /**
     * Функция получения значение поля {@link Edition#yearPubl}
     * @return возвращает год издания
     */
    public int getYearPubl() {
        return yearPubl;
    }
    /**
     * Функция получения значение поля {@link Edition#countPage}
     * @return возвращает количество страниц
     */
    public int getCountPage() {
        return countPage;
    }
    /**
     * Функция получения значение поля {@link Edition#image}
     * @return возвращает название обложки
     */
    public String getImage() {
        return image;
    }
    /**
     * Функция получения значение поля {@link Edition#bookId}
     * @return возвращает идентификатор произведения
     */
    public int getBookId() {
        return bookId;
    }
    /**
     * Функция получения значение поля {@link Edition#bookName}
     * @return возвращает название произведения
     */
    public String getBookName() {
        return bookName;
    }
    /**
     * Функция получения значение поля {@link Edition#publHouseName}
     * @return возвращает название издательства издания
     */
    public String getPublHouseName() {
        return publHouseName;
    }
    /**
     * Функция получения значение поля {@link Edition#ISBN}
     * @return возвращает номер ISBN
     */
    public String getISBN() {
        return ISBN;
    }
    /**
     * Функция получения значение поля {@link Edition#price}
     * @return возвращает цену
     */
    public double getPrice() {
        return price;
    }

}
