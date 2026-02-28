package org.socialmediaapp.social_media_app;

import javafx.application.Application;
import javafx.stage.Stage;
import org.socialmediaapp.social_media_app.util.SceneManager;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        SceneManager.getInstance().setPrimaryStage(stage);
        SceneManager.getInstance().showLogin();
    }

    public static void main(String[] args) {
        launch();
    }
}
