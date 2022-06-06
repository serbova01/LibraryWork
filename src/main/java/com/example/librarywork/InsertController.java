package com.example.librarywork;

import com.example.librarywork.classesCompenents.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * Класс контроллера, реализующий привоз книг, добавление и редактирование произведений и изданий.
 * <p>
 * Данный класс осуществлять основную работу по добавлению или редактированию произведения или издания.
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class InsertController {
    /** Элемент управления списком произведений */
    public ListView lvBooks;
    /** Элемент управления списком изданий выбранного произведения */
    public ListView lvEditions;
    /** Элемент управления количеством экземпляров выбранного издания */
    public Slider sCount;
    /** Таблица с данными о привезенных книгах одного издания */
    public TableView<CreateCopiesBook> tvAddingCB;
    /** Столбец названия произведения */
    public TableColumn<CreateCopiesBook, String> tcNameBook;
    /** Столбец названия издательства */
    public TableColumn<CreateCopiesBook, String> tcPublHouse;
    /** Столбец номера ISBN */
    public TableColumn<CreateCopiesBook, String> tcISBN;
    /** Столбец количества привезенных книг */
    public TableColumn<CreateCopiesBook, Integer> tcCount;
    /** Выпадающий список издательств */
    public ComboBox cbPublHouses;
    /** Текстовое поле номера ISBN */
    public TextField tfISBN;
    /** Текстовое поле описания книги */
    public TextField tfDiscription;
    /** Текстовое поле года издания */
    public TextField tfYearPubl;
    /** Текстовое поле цены */
    public TextField tfPrice;
    /** Элемент для вывода изображения обложки */
    public ImageView ivImage;
    /** Текстовое поле названия произведения */
    public TextField tfNameBook;
    /** Выпадающий список жанров */
    public ComboBox cbGenre;
    /** Элемент управления списком авторов произведения */
    public ListView lvAuthorsBook;
    /** Панель компановки элементов управления для привоза книг */
    public Pane pCreateCB;
    /** Панель компановки элементов управления для работы с изданиями */
    public Pane pCreateEditions;
    /** Панель компановки элементов управления для работы с произведениями */
    public Pane pCreateBooks;
    /** Метка отображающая текущее значение {@link InsertController#sCount} */
    public Label lValueSlider;
    /** Элемент управления списком авторов */
    public ListView lvAuthors;
    /** Панель компановки для отображения fxml-разметки для добавления жанра */
    public VBox genrePanel;
    /** Панель компановки для отображения fxml-разметки для работы с данными автора */
    public VBox authorPanel;
    /** Панель компановки для отображения fxml-разметки для добавления издательства */
    public VBox publHousePanel;
    /** Текстовое поле количества страниц издания */
    public TextField tfCountPages;
    public HBox pInsertPH;
    /** Окно приложения */
    private Stage dialogStage;
    /** Параметр отображения данных */
    private String parameter;
    /** Список произведений */
    private ObservableList<Book> booksListViews;
    /** Адрес базы данных */
    final String DB_URL1 = "jdbc:mysql://localhost:3306/library?useSSL=false";
    /** Логин для подключения к БД */
    final String LOGIN1 = "root";
    /** Пароль для подключения к БД */
    final String PASSWORD1 = "root_root";
    /** Statement для выполнения SQL запросов */
    public Statement statement1;
    /** Connection для подключения к БД */
    private Connection connection1;
    /** Список изданий по выбранному произведению */
    private ObservableList<String> listEditions;
    /** Список записей о привезенныз книгах */
    private ObservableList<CreateCopiesBook> listCB;
    /** Выбранное произведение */
    private Book selectBook;
    /** Выбранное издание */
    private String selectEdition;
    /** Выбранная запись о привезенных книгах */
    private CreateCopiesBook selectCB;
    /** Список жанров */
    private ObservableList<String> listGenres;
    /** Список авторов */
    private ObservableList<Author> listAuthors;
    /** Список авторов произведения */
    private ObservableList<Author> listAuthorsBook;
    /** Идентификатор произведения для которого добавляется издание */
    private int editionBookId;
    /** Редактируемое издание */
    private Edition editionEdit;
    /** CheckInfo для проверок текстовых полей */
    private CheckInfo checkInfo;
    /** Редактируемое произведение */
    private Book bookEdit;
    /** Загруженный файл */
    private File fileImage;
    /** Контроллер для добавления жанра */
    @FXML
    private GenreController genrePanelController;
    /** Контроллер для работы с данными автора */
    @FXML
    private AuthorController authorPanelController;
    /** Контроллер для добавления издательства */
    @FXML
    private InsertPHController publHousePanelController;
    /** Выбранный автор */
    private Author selectAuthor;
    /**
     * Функция изменения окна приложения {@link InsertController#dialogStage}
     * @param addStage окно прриложения
     */
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }
    /**
     * Функция изменения идентификатора произведения, для которого добавляется издание {@link InsertController#editionBookId}
     * @param id идентификатор произведения
     */
    public void setEditionBookId(int id){editionBookId=id;}
    /**
     * Функция изменения редактируемого издания {@link InsertController#editionEdit}
     * @param id идентификатор издания
     */
    public void setEditionEdit(int id){
        if (connectDB()){
            try {
                ResultSet resultSet = statement1.executeQuery("Select * from Editions " +
                        "join PublishingHouses on publishingHouses.Id = editions.PublHouseId " +
                                "join Books on Books.Id = editions.BookId "+
                        "where editions.id = " + id);
                resultSet.next();
                this.editionEdit = new Edition(resultSet.getInt("Editions.Id"),
                        resultSet.getString("Discription"),
                        resultSet.getInt("YearPublication"), resultSet.getInt("CountPage"),
                        resultSet.getString("Image"), resultSet.getString("ISBN"),
                        resultSet.getInt("BookId"), resultSet.getString("Books.Name"),
                        resultSet.getString("NamePH"), resultSet.getDouble("Price"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            showEdition();
        }
     }
    /**
     * Функция изменения редактируемого произведения {@link InsertController#bookEdit}
     * @param selectBook произведение
     */
    public void setBookEdit(Book selectBook) {
        this.bookEdit = selectBook;
    }
    /**
     * Функция изменения параметра отображения данных {@link InsertController#parameter}.
     * @param parameter параметр отображения данных
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
        if (this.connectDB()){
            this.selectData();
        }
    }
    /**
     * Функция заполняющая панель компановки {@link InsertController#pCreateBooks}
     */
    private void showBook() {
        if (connectDB()){
            tfNameBook.setText(bookEdit.getName());
            cbGenre.setValue(bookEdit.getGenre());
            String[] fullNamesAuthors = bookEdit.getAuthorsFullName().split(", ");
            listAuthorsBook = FXCollections.observableArrayList();
            for (String fullNameAuthor : fullNamesAuthors){
                String[] namesAuthor = fullNameAuthor.split(" ");
                String middleName = "";
               if (namesAuthor.length>2) middleName = namesAuthor[2];
                try {
                    ResultSet rsIdAuthor = statement1.executeQuery("Select id from Authors where FirstName = " +
                            "'"+namesAuthor[0]+"'and LastName = '"+ namesAuthor[1] +"'and MiddleName = '"+middleName+"'");
                    rsIdAuthor.next();
                    listAuthorsBook.add(new Author(rsIdAuthor.getInt(1), namesAuthor[0], namesAuthor[1],
                            middleName));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            ObservableList<String> items = FXCollections.observableArrayList();
            for(Author author : listAuthorsBook){
                items.add(author.getFirstName() + " " + author.getLastName() + " " + author.getMiddleName());
            }
            lvAuthorsBook.setItems(items);
        }
    }
    /**
     * Функция заполняющая панель компановки {@link InsertController#pCreateEditions}
     */
    private void showEdition(){
        tfCountPages.setText(String.valueOf(this.editionEdit.getCountPage()));
        tfISBN.setText(editionEdit.getISBN());
        tfYearPubl.setText(String.valueOf(editionEdit.getYearPubl()));
        tfDiscription.setText(editionEdit.getDescription());
        tfPrice.setText(String.valueOf(editionEdit.getPrice()));
        cbPublHouses.setValue(editionEdit.getPublHouseName());
        if (editionEdit.getImage()!=""){
            ImageBook imageBook = new ImageBook();
            Image img = imageBook.LoadImage(editionEdit.getImage());
            //File file = new File("src/main/resources/images/boocover.jpg");
            ivImage.setImage(img);
        }
    }
    /**
     * Функция осуществления подключения к БД и инициализация поля {@link InsertController#statement1}
     * @return состояние подключения
     */
    public boolean connectDB(){
        checkInfo =new CheckInfo();
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
    /**
     * Функция вывода данных в одну из панелей компановки {@link InsertController#pCreateCB},
     * {@link InsertController#pCreateBooks} или {@link InsertController#pCreateEditions} в зависимости от
     * {@link InsertController#parameter}.
     */
    private void selectData(){
        pCreateBooks.setVisible(false);
        pCreateCB.setVisible(false);
        pCreateEditions.setVisible(false);
        cbPublHouses.getItems().clear();
        lvAuthors.getItems().clear();
        lvAuthorsBook.getItems().clear();
        cbGenre.getItems().clear();
        switch (this.parameter){
            case "CreateCB":{
                selectFormCreateCB();
                break;
            }
            case "CreateB":{
                selectFormCreateB();
                genrePanelController.setInsertPanelController(this);
                authorPanelController.setInsertPanelController(this);
                authorPanel.setVisible(false);
                genrePanel.setVisible(false);
                break;
            }
            case "EditB":{
                selectFormCreateB();
                showBook();
                genrePanelController.setInsertPanelController(this);
                authorPanelController.setInsertPanelController(this);
                authorPanel.setVisible(false);
                genrePanel.setVisible(false);
                break;
            }
            case "CreateEd":{
                publHousePanelController.setInsertPanelController(this);
                selectFormCreateEd();
                break;
            }
            case "EditEd":{
                publHousePanelController.setInsertPanelController(this);
                editionBookId = editionEdit.getBookId();
                selectFormCreateEd();
                break;
            }
        }
    }
    /**
     * Функция вывода данных в панель компановки {@link InsertController#pCreateBooks}.
     */
    public void selectFormCreateB(){
        try {
            listGenres = FXCollections.observableArrayList();
            listAuthors = FXCollections.observableArrayList();
            listAuthorsBook = FXCollections.observableArrayList();
            pCreateBooks.setVisible(true);
            ResultSet rsGenres = statement1.executeQuery("SELECT Name from Genres");
            while (rsGenres.next()){
                listGenres.add(rsGenres.getString("Genres.Name"));
            }
            cbGenre.setItems(listGenres);

            ResultSet rsAuthors = statement1.executeQuery("SELECT * from Authors");
            while (rsAuthors.next()){
                Author author = new Author(rsAuthors.getInt("Id"), rsAuthors.getString("FirstName"),
                        rsAuthors.getString("LastName"), rsAuthors.getString("MiddleName"));
                listAuthors.add(author);
            }
            ObservableList<String> items = FXCollections.observableArrayList();
            listAuthors.forEach(author -> items.add(author.toString()));
            lvAuthors.setItems(items);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Функция вывода данных в панель компановки {@link InsertController#pCreateEditions}.
     */
    public void selectFormCreateEd() {
        try {
            ObservableList<String> items = FXCollections.observableArrayList();
            pCreateEditions.setVisible(true);
            ResultSet rsPublHouses = statement1.executeQuery("SELECT * from PublishingHouses");
            while (rsPublHouses.next()){
                items.add(rsPublHouses.getString("NamePH"));
            }
            cbPublHouses.setItems(items);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Функция вывода данных в панель компановки {@link InsertController#pCreateCB}.
     */
    void selectFormCreateCB(){
        try {
            booksListViews = FXCollections.observableArrayList();
            listEditions = FXCollections.observableArrayList();
            listCB = FXCollections.observableArrayList();
            pCreateCB.setVisible(true);
            tcNameBook.setCellValueFactory(createCopiesBookStringCellDataFeatures
                    -> createCopiesBookStringCellDataFeatures.getValue().nameBookProperty());
            tcISBN.setCellValueFactory(createCopiesBookStringCellDataFeatures
                    -> createCopiesBookStringCellDataFeatures.getValue().ISBNProperty());
            tcPublHouse.setCellValueFactory(createCopiesBookStringCellDataFeatures
                    -> createCopiesBookStringCellDataFeatures.getValue().editionPHNameProperty());
            tcCount.setCellValueFactory(cellData ->
                    cellData.getValue().countProperty().asObject());
            tvAddingCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CreateCopiesBook>() {
                @Override
                public void changed(ObservableValue<? extends CreateCopiesBook>
                                            observableValue, CreateCopiesBook book, CreateCopiesBook t1) {
                    if (t1 != null) {
                        selectCB = t1;
                        }
                    }
                }
            );

            ResultSet rsBooks = statement1.executeQuery("Select * from Books " +
                    "join Books_Authors on books.Id = books_authors.BookId " +
                    "join Authors on books_authors.AuthorId = authors.Id");

            while (rsBooks.next()) {
                StringBuilder stringBuilder = ShortName(rsBooks.getString("FirstName"),
                        rsBooks.getString("LastName"), rsBooks.getString("MiddleName"));
                Book booksListView = new Book(rsBooks.getInt("Id"),
                        rsBooks.getString("Name"), stringBuilder.toString());
                int i = containsNameBook(booksListView.getId(), booksListView.getName());
                if (i!=-1){
                    StringBuilder strAuthors = new StringBuilder();
                    strAuthors.append(booksListViews.get(i).getAuthors());
                    strAuthors.append(", ");
                    strAuthors.append(booksListView.getAuthors());
                    booksListViews.get(i).setAuthors(strAuthors.toString());

                } else {
                    this.booksListViews.add(booksListView);
                }

            }
            ObservableList<String> items = FXCollections.observableArrayList();
            for (Book book : booksListViews) {
                String str = book.getName() + " - " + book.getAuthors();
                items.add(str);
            }
            lvBooks.setItems(items);
            lvBooks.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String>
                                            observableValue, String book, String t1) {
                    if (t1 != null) {
                        String[] name_authors = t1.split(" - ");
                        Book findBook = findToNameAuthorsBook(name_authors[0], name_authors[1]);
                        if (findBook != null){
                            int id = findBook.getId();
                            editionBookId = findBook.getId();
                            selectBook = findBook;
                            lvEditions.getItems().clear();
                            try {
                                ResultSet rsEditions = statement1.executeQuery("Select * from Editions " +
                                        "join PublishingHouses on editions.PublHouseId = publishingHouses.id " +
                                        "where editions.BookId = " + String.valueOf(id));
                                while (rsEditions.next()){
                                    listEditions.add(rsEditions.getString("NamePH") + " - " +
                                            rsEditions.getString("ISBN") + " - " +
                                            rsEditions.getInt("YearPublication"));
                                }
                                lvEditions.setItems(listEditions);

                                MultipleSelectionModel<String> langsSelectionModel =
                                        lvEditions.getSelectionModel();
                                langsSelectionModel.selectedItemProperty()
                                        .addListener(new ChangeListener<String>(){
                                            public void changed(ObservableValue<? extends String>
                                                                        changed, String oldValue, String newValue){
                                                selectEdition= newValue;
                                            }
                                        });
                            } catch (SQLException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                }
            });
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Функция открытия окна формы добавления произведения
     */
    public void onOpenInsertB(ActionEvent actionEvent) {
        editionBookId = 0;
        showDialog("insertCB.fxml", "Добавление произведения","CreateB");
    }
    /**
     * Функция открытия окна формы добавления издания выбранному произведению {@link InsertController#editionBookId}.
     */
    public void onOpenInsertEd(ActionEvent actionEvent) {
        if (editionBookId > 0){
            showDialog("insertCB.fxml", "Добавление издания","CreateEd");
        }
    }
    /**
     * Функция отображения панели компановки для добавления издательства {@link InsertController#publHousePanel}.
     */
    public void onOpenPanelPH(ActionEvent actionEvent) {
        pInsertPH.setVisible(true);
    }
    /**
     * Функция отображения панели компановки для добавления жанра {@link InsertController#genrePanel}.
     */
    public void onOpenPanelAddingG(ActionEvent actionEvent) {
        genrePanel.setVisible(true);
    }
    /**
     * Функция отображения панели компановки для добавления автора {@link InsertController#authorPanel}.
     */
    public void onOpenPanelAddingA(ActionEvent actionEvent) {
        authorPanel.setVisible(true);
    }
    /**
     * Функция добавления записи о привезенных книгах в таблицу {@link InsertController#tvAddingCB}.
     */
    public void onAddingTable(ActionEvent actionEvent) {
        if (sCount.getValue() > 0 && selectBook !=null && selectEdition!=null){
            String[] edition = selectEdition.split(" - ");
            try {
                ResultSet rsSelectEdition = statement1.executeQuery("Select * from Editions " +
                        "join PublishingHouses on editions.PublHouseId = publishingHouses.id " +
                        "where ISBN = '" + edition[1] + "' and publishingHouses.NamePH = '" + edition[0] +
                        "' and YearPublication =  '"+ edition[2] + "'");
                rsSelectEdition.next();
                int editionId = rsSelectEdition.getInt("Editions.Id");
                CreateCopiesBook copiesBook = new CreateCopiesBook(Integer.valueOf(lValueSlider.getText()),
                        selectBook.getName(), editionId, edition[0], edition[1]);
                listCB.add(copiesBook);
                tvAddingCB.setItems(listCB);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        else{
            getMessage(Alert.AlertType.WARNING, "Для добавления информации в таблицу выберете произведение и\n издание, " +
                    "а также выставьие количество добавляемых экземпляров.");
        }
    }
    /**
     * Функция добавления указанное количество экземпляров книг из {@link InsertController#tvAddingCB}.
     */
    public void onAddCB(ActionEvent actionEvent) {
        if(listCB.size()>0){
            for(CreateCopiesBook copyBook : listCB){
                String sqlQuery = "insert into copiesbook (Status, EditionId) values ";
                for(int i=0; i<copyBook.getCount(); i++){
                    String partQuery = "(\"в наличии\", " + copyBook.getEditionId() +")";
                    sqlQuery+=partQuery;
                    if (i+1 != copyBook.getCount()) sqlQuery += ", ";
                }
                try {
                    int rows = statement1.executeUpdate(sqlQuery);
                    if (rows > 0){
                        statement1.executeUpdate("Update editions set CountCopies = CountCopies + "+ copyBook.getCount()+
                                " where id = "+ copyBook.getEditionId() +"");
                        getMessage(Alert.AlertType.INFORMATION, "Книги успешно добавлены.");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            tvAddingCB.getItems().clear();
        }
    }
    /**
     * Функция очищения {@link InsertController#tvAddingCB} и {@link InsertController#sCount}.
     */
    public void onClear(ActionEvent actionEvent) {
    tvAddingCB.getItems().clear();
    sCount.setValue(0.0);
    }
    /**
     * Функция проверки и сохранения данных об издании.
     */
    public void onSaveEdition(ActionEvent actionEvent) {
        if (editionEdit==null){
            if (checkInfo.checkInfoISBN(tfISBN.getText()) && cbPublHouses.getValue() != null &&
            ivImage.getImage() != null){
                if(checkInfo.checkInfoIsNumeric(tfCountPages.getText()) && checkInfo.checkInfoIsFloat(tfPrice.getText())){
                    if (tfYearPubl.getText().length() == 4 && checkInfo.checkInfoIsNumeric(tfYearPubl.getText())){
                        try {
                            ResultSet rsPublHouseId = statement1.executeQuery("Select * from PublishingHouses " +
                                    "where NamePH = '"+ cbPublHouses.getValue() +"'");
                            rsPublHouseId.next();
                            int publHouseId = rsPublHouseId.getInt("Id");
                            String[] pathImage = ivImage.getImage().getUrl().split("/");
                            ImageBook imageBook = new ImageBook();
                            imageBook.SaveImage(fileImage);
                            if (editionBookId>0){
                                String sql = "Insert into editions (Discription, YearPublication, " +
                                        "CountPage, Image, BookId, PublHouseId, ISBN, CountCopies, Price) values " +
                                        "('" + tfDiscription.getText() + "', " + tfYearPubl.getText() + ", "+
                                        tfCountPages.getText() + ", '" + pathImage[pathImage.length-1] + "', "+
                                        editionBookId + ", " + publHouseId + ", '" + tfISBN.getText() + "', 0," +
                                        tfPrice.getText() + ")";
                                int row = statement1.executeUpdate(sql);
                                if (row > 0){
                                    getMessage(Alert.AlertType.INFORMATION, "Издание было успешно добавлено.");
                                }
                            }
                        } catch (SQLException e) {
                            getMessage(Alert.AlertType.ERROR, "Издание с таким ISBN уже есть в системе..");
                        }
                    }
                    else getMessage(Alert.AlertType.WARNING, "Поле Год публикации должно содержать\n 4-хзначное число.");
                } else getMessage(Alert.AlertType.WARNING, "Поля Кол-во страниц и Цена должны содержать \n " +
                        "только целые и вещественные числа\n (пример 00.0) соответственно.");
            }
            else getMessage(Alert.AlertType.WARNING, "Для добавления издания нужно загрузить изображение и\n " +
                    "заполнить поля Издательство и ISBN корректными\n данными.");
        }
        else{
            if (checkInfo.checkInfoISBN(tfISBN.getText()) && !cbPublHouses.getValue().equals(null) &&
                    ivImage.getImage() != null){
                if(checkInfo.checkInfoIsNumeric(tfCountPages.getText()) && checkInfo.checkInfoIsFloat(tfPrice.getText())){
                    if (tfYearPubl.getText().length() == 4 && checkInfo.checkInfoIsNumeric(tfYearPubl.getText())){
                        try {
                            ResultSet rsPublHouseId = statement1.executeQuery("Select * from PublishingHouses " +
                                    "where NamePH = '"+ cbPublHouses.getValue() +"'");
                            rsPublHouseId.next();

                            int publHouseId = rsPublHouseId.getInt("Id");
                            String[] pathImage = ivImage.getImage().getUrl().split("/");
                            ImageBook imageBook = new ImageBook();
                            imageBook.SaveImage(fileImage);
                            if (editionBookId>0){

                                int row = statement1.executeUpdate("UPDATE Editions set Discription = '" +
                                        tfDiscription.getText()+"', " + " YearPublication = "+ tfYearPubl.getText() +
                                        ", CountPage = "+tfCountPages.getText()+", Image = " + "'"+
                                        pathImage[pathImage.length-1]+"', ISBN = '"+tfISBN.getText()+"', BookId = "+
                                        editionEdit.getBookId()+", PublHouseId = "+publHouseId+ ", Price = " +
                                        tfPrice.getText() +" where id = " + editionEdit.getId());
                                if (row > 0){
                                    getMessage(Alert.AlertType.INFORMATION, "Издание было успешно изменено.");
                                    dialogStage.close();
                                }
                            }
                        } catch (SQLException e) {
                            getMessage(Alert.AlertType.ERROR, "Издание с таким ISBN уже есть в системе.");
                        }
                    }
                    else getMessage(Alert.AlertType.WARNING, "Поле Год публикации должно содержать\n 4-хзначное число.");
                } else getMessage(Alert.AlertType.WARNING, "Поля Кол-во страниц и Цена должны содержать \n " +
                        "только целые и вещественные числа\n (пример 00.0) соответственно.");
            }
            else getMessage(Alert.AlertType.WARNING, "Для добавления издания нужно загрузить изображение и\n " +
                    "заполнить поля Издательство и ISBN корректными\n данными.");
        }
    }
    /**
     * Функция очищения панели {@link InsertController#pCreateBooks}.
     */
    public void onCancel(ActionEvent actionEvent) {
        clearBookPanel();
    }
    /**
     * Функция проверки и сохранения данных об произведении.
     */
    public void onSaveBook(ActionEvent actionEvent) {

        if (lvAuthorsBook.getItems().size()>0 && tfNameBook!=null && cbGenre.getValue()!=null){
            try {
                String genre = cbGenre.getSelectionModel().getSelectedItem().toString();
                ResultSet rsBookDulicate = statement1.executeQuery("Select * from books where books.Name = '" +
                        tfNameBook.getText() + "'");
                ObservableList<Integer> booksId = FXCollections.observableArrayList();
                int rowBookDubl = rsBookDulicate.getRow();
                while (rsBookDulicate.next()){
                    booksId.add(rsBookDulicate.getInt("Id"));
                }
                ObservableList<Integer> authorsId = FXCollections.observableArrayList();
                for(Author author: listAuthorsBook){
                    try {
                        ResultSet rsAuthorId = statement1.executeQuery("SELECT Id from Authors where FirstName = '" +
                                author.getFirstName() + "' and LastName = '" + author.getLastName() + "' and " +
                                "MiddleName = '" + author.getMiddleName() + "'");
                        rsAuthorId.next();
                        authorsId.add(rsAuthorId.getInt("Id"));
                    }catch (SQLException e){
                        System.out.println(e.getMessage());
                    }
                }
                boolean isDublicate = false;
                if (rowBookDubl==0){
                    if (bookEdit == null) insertDataBook(genre, authorsId);
                    else updateDataBook(genre, authorsId);
                }
                else{
                    for (int bookId : booksId){
                        for (int authorId : authorsId){
                            try{
                                ResultSet rsFindDuplicate = statement1.executeQuery("SELECT * from Books_Authors " +
                                        "where BookId = "+ bookId + " and AuthorId = " + authorId);
                                if (rsFindDuplicate.getRow()>0){
                                    isDublicate=true;
                                    getMessage(Alert.AlertType.ERROR, "В системе уже есть произведение с таким названием и\n " +
                                            "автором." + findToId(authorId, listAuthorsBook));
                                    break;
                                }
                            }catch (SQLException e){
                                System.out.println(e.getMessage());
                            }
                        }
                        if (isDublicate) {
                            break;
                        }
                    }
                    if (!isDublicate){
                        if (bookEdit==null) insertDataBook(genre, authorsId);
                        else updateDataBook(genre, authorsId);
                    }
                }


            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        else getMessage(Alert.AlertType.WARNING, "Для добавления произведения нужно ввести корректное название книги, " +
                "выбрать жанр и автора(-ов)!");
    }
    /**
     * Функция удаления выбранной записи {@link InsertController#selectCB} о привезенных книгах из таблицы
     * {@link InsertController#tvAddingCB}.
     */
    public void onRemovingTable(ActionEvent actionEvent){
        listCB.remove(selectCB);
        tvAddingCB.setItems(listCB);
    }
    /**
     * Функция добавления выделенной записи из {@link InsertController#lvAuthors} в
     * {@link InsertController#lvAuthorsBook} при двойном клике по элементу.
     */
    public void onClickedAuthorAdd(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2){
            //ObservableList<String> items = lvAuthorsBook.getItems();
            Author author = findToName(lvAuthors.getSelectionModel().getSelectedItem().toString());
            if (author!= null && !lvAuthorsBook.getItems().contains(author.toString()))
            {
                listAuthorsBook.add(author);
            }
            ObservableList<String> items = FXCollections.observableArrayList();
            listAuthorsBook.forEach(item->items.add(item.toString()));
            lvAuthorsBook.setItems(items);
        }
        selectAuthor = findToName(lvAuthors.getSelectionModel().getSelectedItem().toString());
    }
    /**
     * Функция удаления выделенной записи из {@link InsertController#lvAuthorsBook} при двойном клике по элементу.
     */
    public void onClickedAuthorDel(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2){
            ObservableList<String> items = lvAuthorsBook.getItems();
            Author author = findToName(lvAuthorsBook.getSelectionModel().getSelectedItem().toString());
            if (author!= null)
            {
                for(int i=0;i<listAuthorsBook.size();i++){
                    if (listAuthorsBook.get(i).toString().equals(author.toString())){
                        listAuthorsBook.remove(i);
                        items.remove(author.toString());
                    }
                }

            }
            lvAuthorsBook.setItems(items);
        }
    }
    /**
     * Функция сохранения и загрузки изображения в {@link InsertController#ivImage}.
     */
    public void onLoadImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        FileChooser.ExtensionFilter filter1 =
                new FileChooser.ExtensionFilter("All image files", "*.png",
                        "*.jpg", "*.gif");
        FileChooser.ExtensionFilter filter2 =
                new FileChooser.ExtensionFilter("JPEG files", "*.jpg");
        FileChooser.ExtensionFilter filter3 =
                new FileChooser.ExtensionFilter("PNG image Files",
                        "*.png");
        FileChooser.ExtensionFilter filter4 =
                new FileChooser.ExtensionFilter("GIF image Files",
                        "*.gif");
        fileChooser.getExtensionFilters()
                .addAll(filter1, filter2, filter3, filter4);
        File file = fileChooser.showOpenDialog(dialogStage);
        if (file != null) {
            fileImage = file;
            Image img = new Image(file.toURI().toString());
            ivImage.setImage(img);
        }
    }
    /**
     * Функция записи значения {@link InsertController#sCount} в {@link InsertController#lValueSlider}.
     */
    public void onChangeValue(MouseEvent mouseEvent) {
        int value = (int) Math.round(sCount.getValue());
        lValueSlider.setText(String.valueOf(value));
    }
    /**
     * Функция изменения данных произведения {@link InsertController#bookEdit}.
     * @param genre название жанра
     * @param authorsId список идентификаторов авторов произведения
     */
    private void updateDataBook(String genre, ObservableList<Integer> authorsId){
        try {
            ResultSet rsGenreId = statement1.executeQuery("Select Id from genres where genres.Name = '" + genre +"'");
            rsGenreId.next();
            int genreID = rsGenreId.getInt("Id");
            int rowBooks = statement1.executeUpdate("Update Books set Name = '" + tfNameBook.getText() +"', " +
                    "GenreId = "+ genreID+ " where id = "+ bookEdit.getId());
            rsGenreId.close();
            int rowDelBooksAuthors = statement1.executeUpdate("Delete from Books_Authors where BookId = " +
                    bookEdit.getId());
            StringBuilder sqlQuery = new StringBuilder();
            sqlQuery.append("Insert into Books_Authors (BookId, AuthorId) values ");
            for (int i=0; i<authorsId.size(); i++){
                sqlQuery.append("("+bookEdit.getId()+", "+ authorsId.get(i)+")");
                if (i+1 != authorsId.size()) sqlQuery.append(", ");
            }
            int rowInsertBooksAuthors = statement1.executeUpdate(sqlQuery.toString());
            if (rowBooks>0 && rowInsertBooksAuthors>0){
                getMessage(Alert.AlertType.INFORMATION, "Произведение успешно изменено.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    /**
     * Функция добавления произведения.
     * @param genre название жанра
     * @param authorsId список идентификаторов авторов произведения
     */
    private void insertDataBook(String genre, ObservableList<Integer> authorsId){
        try {
            ResultSet rsGenreId = statement1.executeQuery("Select Id from genres where genres.Name = '" + genre +"'");
            rsGenreId.next();
            int genreID = rsGenreId.getInt("Id");
            int rowBooks = statement1.executeUpdate("Insert into books (Name, GenreId) values ('"+ tfNameBook.getText() +
                    "', "+ genreID +")");
            rsGenreId.close();
            if (rowBooks >0){
                ResultSet rsBookId = statement1.executeQuery("Select max(Id) from books");
                rsBookId.next();
                int bookId = rsBookId.getInt(1);
                String sqlInsertBookAuthor="Insert into books_authors (BookId, AuthorId) values ";
                StringBuilder secondPartSQL = new StringBuilder();
                for (int i=0; i<authorsId.size(); i++){
                    secondPartSQL.append("(" + bookId +", "+ authorsId.get(i) +")");
                    if (i+1!=authorsId.size()) secondPartSQL.append(", ");
                }
                sqlInsertBookAuthor += secondPartSQL.toString();
                editionBookId = bookId;
                int rowB_A = statement1.executeUpdate(sqlInsertBookAuthor);
                clearBookPanel();
                if(rowBooks>0 && rowB_A>0){
                    getMessage(Alert.AlertType.INFORMATION, "Произведение добавлено");
                }


            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Функция вывода сообщения в виде модального диалогового окна с заданным типом окна и текстом сообщения.
     * @param type тип окна
     * @param text текст сообщения
     */
    void getMessage(Alert.AlertType type, String text){
        Alert alert = new Alert(type, text, ButtonType.OK);
        alert.showAndWait();
    }
    /**
     * Функция открытия заданного окна с заданным заголовком и параметром отображаемых данных. После закрытия вызвваемого окна
     * данные обновляются.
     * @param resource название fxml-разметки вызываемого окна
     * @param title заголовок вызываемого окна
     * @param parameter параметр отображаемых данных в вызввемом окне
     */
    private void showDialog(String resource, String title, String parameter) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(
                HelloApplication.class.getResource(resource)
        );
        Parent page = null;
        try {
            page = loader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Stage addStage = new Stage();
        addStage.setTitle(title);;
        addStage.initModality(Modality.WINDOW_MODAL);
        addStage.initOwner(HelloApplication.getPrimaryStage());
        Scene scene = null;
        switch (parameter){
            case "CreateB":{
                scene = new Scene(page, 800,515);
                addStage.setScene(scene);
                InsertController controller = loader.getController();
                controller.setAddStage(addStage);
                controller.setParameter(parameter);
                addStage.showAndWait();
                break;
            }
            case "CreateEd":{
                scene = new Scene(page, 800,600);
                addStage.setScene(scene);
                InsertController controller = loader.getController();
                controller.setAddStage(addStage);
                controller.setParameter(parameter);
                controller.setEditionBookId(editionBookId);
                addStage.showAndWait();
                break;
            }
            case "EditA":{
                scene = new Scene(page, 250,270);
                addStage.setScene(scene);
                AuthorController controller = loader.getController();
                controller.setAddStage(addStage);
                controller.setAuthorEdit(selectAuthor);
                addStage.showAndWait();
                break;
            }
        }
        selectData();
    }
    /**
     * Функция возращения автора из списка {@link InsertController#lvAuthors} по ФИО.
     * @param name ФИО автора для поиска
     * @return автора
     */
    Author findToName(String name){
        String[] names = name.split(" ");
        for(Author author : listAuthors){
            if (author.getFirstName().equals(names[0]) && author.getLastName().equals(names[1])){
                if (names.length>2 && author.getMiddleName().equals(names[2]))
                    return author;
                else{
                    if(names.length==2) return author;
                }
            }
        }
        return null;
    }
    /**
     * Функция возращения автора из списка заданного списка авторов по идентификатору.
     * @param id идентификатор автора
     * @param authors список авторов
     * @return автора
     */
    Author findToId(int id, ObservableList<Author> authors){
        for(Author author : authors){
            if (author.getId() == id){
                return author;
            }
        }
        return null;
    }
    /**
     * Функция проверки наличия произведеня с заданным идентификатором из списка {@link InsertController#booksListViews}.
     * @param id идентификатор произведения
     * @param name название произведения
     * @return индекс в списке или -1
     */
    int containsNameBook(int id, String name){
        int i = -1;
        for (int j = 0; j< booksListViews.size(); j++){
            if (booksListViews.get(j).getName().equals(name) && booksListViews.get(j).getId() == id)
            {
                i=j;
            }
        }
        return i;
    }
    /**
     * Функция возращения произведения с заданным названием и списком авторов.
     * @param authors список авторов произведения
     * @param name название произведения
     * @return произведение или null
     */
    @Nullable
    Book findToNameAuthorsBook(String name, String authors){
        for(Book book : this.booksListViews){
            if (book.getName().equals(name) && book.getAuthors().equals(authors)){
                return book;
            }
        }
        return null;
    }
    /**
     * Функция получения короткого имени автора
     * @param firstName фамилия автора
     * @param lastName имя автора
     * @param middleName отчество автора
     * @return короткое имя автора
     */
    private StringBuilder ShortName(String firstName, String lastName, String middleName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(firstName);
        stringBuilder.append(" ");
        stringBuilder.append(lastName.charAt(0));
        stringBuilder.append(".");
        if (!middleName.equals("") && !middleName.equals(" ")){
            stringBuilder.append(middleName.charAt(0));
            stringBuilder.append(".");
        }
        return stringBuilder;
    }
    /**
     * Функция очищения компонентов панели {@link InsertController#pCreateBooks}
     */
    void clearBookPanel(){
        tfDiscription.clear();
        tfISBN.clear();
        tfNameBook.clear();
        tfPrice.clear();
        tfYearPubl.clear();
        ivImage.setImage(null);
        genrePanel.setVisible(false);
        authorPanel.setVisible(false);
        lvAuthorsBook.getItems().clear();
        genrePanelController.tfClear();
        authorPanelController.tfClear();
    }
    /**
     * Функция отображения панели компановки для редактирования выбранного автора {@link InsertController#selectAuthor}.
     */
    public void onEditAuthor(ActionEvent actionEvent) {
        if (selectAuthor != null){
            showDialog("insertAuthor.fxml", "Редактирование данных автора " + selectAuthor, "EditA");
            selectData();
        }
    }
    /**
     * Функция отображения значения при нажатии {@link InsertController#sCount} в {@link InsertController#lValueSlider}.
     */
    public void onClickedValue(MouseEvent mouseEvent) {
        lValueSlider.setText(String.valueOf((int) Math.round(sCount.getValue())));
    }
}
