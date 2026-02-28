package org.socialmediaapp.social_media_app.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.socialmediaapp.social_media_app.HelloApplication;

import java.io.IOException;

/**
 * Manages scene navigation throughout the application (Singleton).
 * Handles full scene switches (login/register) and content loading into the main shell.
 */
public class SceneManager {

    private static SceneManager instance;
    private Stage primaryStage;
    private BorderPane mainContentPane;

    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setMainContentPane(BorderPane pane) {
        this.mainContentPane = pane;
    }

    /** Switch the entire scene (for login, register, main transitions). */
    public void switchScene(String fxmlFile, String title, double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(
                HelloApplication.class.getResource("styles.css").toExternalForm()
            );
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load scene: " + fxmlFile);
            e.printStackTrace();
        }
    }

    /** Load a view into the main shell's content area (center of BorderPane). */
    public void loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Parent content = loader.load();
            if (mainContentPane != null) {
                mainContentPane.setCenter(content);
            }
        } catch (IOException e) {
            System.err.println("Failed to load content: " + fxmlFile);
            e.printStackTrace();
        }
    }

    /** Load a view into main content area and return its controller. */
    public <T> T loadContentAndGetController(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Parent content = loader.load();
            if (mainContentPane != null) {
                mainContentPane.setCenter(content);
            }
            return loader.getController();
        } catch (IOException e) {
            System.err.println("Failed to load content: " + fxmlFile);
            e.printStackTrace();
            return null;
        }
    }

    // ── Navigation shortcuts ──

    public void showLogin() {
        switchScene("login-view.fxml", "Social Media App - Login", 500, 600);
    }

    public void showRegister() {
        switchScene("register-view.fxml", "Social Media App - Register", 500, 650);
    }

    public void showMain() {
        switchScene("main-view.fxml", "Social Media App", 1100, 750);
    }
}
