package com.example.librarywork;
import javafx.scene.control.Alert;
        import javafx.scene.control.ButtonType;
        import javafx.scene.image.Image;

        import javax.imageio.ImageIO;
        import java.awt.image.BufferedImage;
        import java.io.File;
        import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Класс работы с изображениями.
 * <p>
 * Данный класс позволяет выгрузить фотографию из подконревого каталога или сохранить фотографию туда.
 * В дальнейшем все методы могут быть изменены.
 * @author Автор Сербова Алена
 * @version 1.3
 */
public class ImageBook {
    /**
     * Метод находит обложку издания в корневом каталоге по имени
     * @param nameImg имя обложки
     * @return название обложки
     */
    public Image LoadImage(String nameImg){
        String path = new File("").getAbsolutePath();
        javafx.scene.image.Image img = new javafx.scene.image.Image(String.valueOf(Paths.get("./images/" + nameImg).toUri()));
        return img;
    }
    /**
     * Метод возвращает сохранена обложки в корневой каталог или нет
     * @param img фотография товара
     * @return состояние сохранения
     */
    public Boolean SaveImage(File img){
        try {
            Path path = Files.createDirectories(Path.of(String.valueOf(Paths.get("./images/" + img.getName()))));
            File newFile = new File(path.toString());
            BufferedImage input = ImageIO.read(img);
            ImageIO.write(input, "PNG", newFile);
            return true;
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка сохранения картинки "+e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return false;
        }
    }

}