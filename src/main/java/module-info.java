module com.example.librarywork {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.jetbrains.annotations;
    requires java.desktop;


    opens com.example.librarywork to javafx.fxml;
    exports com.example.librarywork;
    exports com.example.librarywork.classesCompenents;
    opens com.example.librarywork.classesCompenents to javafx.fxml;
}