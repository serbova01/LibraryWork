package com.example.librarywork;

import com.example.librarywork.classesCompenents.Author;
import com.example.librarywork.classesCompenents.Edition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

/**
 * Класс контроллера для работы с каталожными карточками.
 * <p>
 * Данный класс позволяет сохранять выбранную каталожную карточку в текстовый файл.
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class CatalogCardsController {
    /** Элемент управления списком изданий */
    public ListView lvEditions;
    /** Текстовое поле для данных каталожной карточки */
    public TextArea taCatalogCard;
    /** Адрес базы данных */
    final String DB_URL = "jdbc:mysql://localhost:3306/library?useSSL=false";
    /** Логин для подключения к БД */
    final String LOGIN = "root";
    /** Пароль для подключения к БД */
    final String PASSWORD = "root_root";
    /** Statement для выполнения SQL запросов */
    private Statement statement;
    /** Connection для подключения к БД */
    private Connection connection;
    /** Окно приложения */
    private Stage dialogStage;
    /** Поле списка изданий */
    private ObservableList<Edition> listEditions;
    /** Поле названия новой каталожной карточки */
    private String title;
    /**
     * Функция изменения окна приложения {@link CatalogCardsController#dialogStage}
     * @param addStage окно прриложения
     */
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }
    /**
     * Функция инициализации класса и заполнение списка изданий  {@link CatalogCardsController#lvEditions}
     */
    @FXML
    void initialize(){
        if(connectDB()){
            try {
                ResultSet resultSet = statement.executeQuery("SELECT * from Editions " +
                        "join Books on books.id = editions.BookId " +
                        "join PublishingHouses on publishingHouses.Id = editions.PublHouseId");
                listEditions = FXCollections.observableArrayList();
                ObservableList<String> items = FXCollections.observableArrayList();
                while (resultSet.next()){
                    Edition edition = new Edition(resultSet.getInt("editions.Id"),
                            resultSet.getString("Discription"), resultSet.getInt("YearPublication"),
                            resultSet.getInt("CountPage"), resultSet.getString("Image"),
                            resultSet.getString("ISBN"), resultSet.getInt("books.Id"),
                            resultSet.getString("books.Name"), resultSet.getString("NamePH"),
                            resultSet.getInt("Price"));
                    listEditions.add(edition);
                }
                listEditions.forEach(item -> items.add(item.toString()));
                lvEditions.setItems(items);
                lvEditions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String>
                                                observableValue, String editionItemLV, String t1) {
                        if (t1 != null) {
                            Edition edition = findOfId(t1.split(" - ")[0]);
                            showCatalogCard(edition);
                        }
                    }
                });
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }
    /**
     * Функция поиска издания в {@link CatalogCardsController#listEditions} по идентификатору
     * @param id идентификатор
     * @return издание или null
     */
    private Edition findOfId(String id) {
        for (Edition edition : listEditions){
            if (Integer.valueOf(id) == edition.getId()){
                return edition;
            }
        }
        return null;
    }

    /**
     * Функция получения короткого имени автора из списка авторов произведения, по которому составляется каталожная карточка
     * @param author автор
     * @return короткое имя автора
     */
    String shortNameAuthor(Author author){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(author.getFirstName());
        stringBuilder.append(" ");
        stringBuilder.append(author.getLastName().charAt(0));
        stringBuilder.append(".");
        if (!author.getMiddleName().equals("") && !author.getMiddleName().equals(" ")){
            stringBuilder.append(author.getMiddleName().charAt(0));
            stringBuilder.append(".");
        }
        return stringBuilder.toString();
    }
    /**
     * Функция заполняющая текстовой поле {@link CatalogCardsController#taCatalogCard} по выбранному изданию произведения
     * @param edition выбранное из списка издание
     */
    private void showCatalogCard(Edition edition) {
        StringBuilder textCatalogCard = new StringBuilder();
        ObservableList<Author> listAuthors = FXCollections.observableArrayList();
        try {
            ResultSet authorsId = statement.executeQuery("Select * from Books_Authors " +
                    "join Authors on authors.Id = books_authors.AuthorId " +
                    "where BookId = " + edition.getBookId());
            while (authorsId.next()){
                Author author = new Author(authorsId.getInt("authorId"), authorsId.getString("FirstName"),
                        authorsId.getString("LastName"), authorsId.getString("MiddleName"));
                listAuthors.add(author);
            }
            textCatalogCard.append("\t\t" + listAuthors.get(0).getFirstName() + ", " + listAuthors.get(0).getLastName() + " " +
                    listAuthors.get(0).getMiddleName() + "\n");
            ResultSet record = statement.executeQuery("Select * from Editions " +
                    "join Books on books.id = editions.bookId " +
                    "join Genres on genres.Id = books.genreId " +
                    "join PublishingHouses on publishingHouses.Id = editions.publHouseId " +
                    "where editions.Id = " + edition.getId());
            record.next();
            StringBuilder authorsNames = new StringBuilder();
            for(int i=0; i<listAuthors.size(); i++){
                authorsNames.append(shortNameAuthor(listAuthors.get(i)));
                if (i+1!= listAuthors.size()) authorsNames.append(", ");
            }
            textCatalogCard.append(record.getString("Department") + "\t\t" +
                    edition.getBookName() +"/" + authorsNames);
            textCatalogCard.append(" - " + record.getString("Place") + " : " +
                    edition.getPublHouseName() + ", ");
            textCatalogCard.append(edition.getYearPubl() + ". - " + record.getInt("CountPage") + "c.\n");
            textCatalogCard.append("\t\t\tISBN " + record.getString("ISBN") + ".\n");
            textCatalogCard.append("\n" + record.getString("Discription"));
            taCatalogCard.setText(textCatalogCard.toString());
            title = record.getString("ISBN") + " - " + edition.getBookName();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Функция осуществления подключения к БД и инициализация поля {@link CatalogCardsController#statement}
     * @return состояние подключения
     */
    public boolean connectDB(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loading succesfull.");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Driver loading failed!");
            return false;

        }
        try{
            connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
            //устанавливаем соединение
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Connection error!");
            return false;
        }
        return true;
    }
    /**
     * Функция сохранения в текстовы файл с названием {@link CatalogCardsController#title} данных каталожной карточки из
     * {@link CatalogCardsController#taCatalogCard}
     */
    public void onCatalogCardsToFile(ActionEvent actionEvent) {
        if (taCatalogCard.getText() != null && taCatalogCard.getText() != ""){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Image File");
            fileChooser.setInitialFileName(title);
            File file = fileChooser.showSaveDialog(dialogStage);
            if (file != null) {
                try(FileWriter writer = new FileWriter(file.getParent() + "/"+ title + ".txt", false)){
                    String text = taCatalogCard.getText();
                    writer.write(text);
                    writer.flush();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Файл создан.", ButtonType.OK);
                    alert.showAndWait();
                }
                catch(IOException ex){
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}
