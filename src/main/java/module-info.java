module org.socialmediaapp.social_media_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens org.socialmediaapp.social_media_app to javafx.fxml;
    opens org.socialmediaapp.social_media_app.controller to javafx.fxml;

    exports org.socialmediaapp.social_media_app;
}
