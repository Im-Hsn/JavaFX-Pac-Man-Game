module com.example.oop2project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.oop2project to javafx.fxml;
    exports com.example.oop2project;
}