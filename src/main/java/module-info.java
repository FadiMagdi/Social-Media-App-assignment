module org.socialmediaapp.social_media_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens org.socialmediaapp.social_media_app to javafx.fxml;
    exports org.socialmediaapp.social_media_app;
}