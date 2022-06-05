package com.example.librarywork;

import com.example.librarywork.classesCompenents.Edition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Класс для запуска программы со свойством <p>primaryStage<p/>.
 * <p>
 * Данный класс позволяет запустить программу.
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class HelloApplication extends Application {
    /** Окно программы */
    private static Stage primaryStage;
    /**
     * Функция получения значение поля {@link HelloApplication#primaryStage}
     * @return окно программы
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    /**
     * Функция налчала работы программы
     * @param primaryStage окно программы
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        Parent root =
                FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Library");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }
    /**
     * Функция запуска программы
     */
    public static void main(String[] args) {
        launch();
    }
}