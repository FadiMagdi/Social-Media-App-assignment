package org.socialmediaapp.social_media_app.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.socialmediaapp.social_media_app.HelloApplication;

import java.io.IOException;

/**
 * Manages scene navigation throughout the application.
 * Handles switching between full scenes (login/register) and
 * loading content into the main shell's center area.
 */
public class SceneManager {

    private static SceneManager instance;
    private Stage primaryStage;
    private BorderPane mainContentPane; // The center pane of main-view

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

    public void setMainContentPane(BorderPane mainContentPane) {
        this.mainContentPane = mainContentPane;
    }

    public BorderPane getMainContentPane() {
        return mainContentPane;
    }

    /**
     * Switch the entire scene (used for login, register, main-view transitions).
     */
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
            e.printStackTrace();
            System.err.println("Failed to load scene: " + fxmlFile);
        }
    }

    /**
     * Switch to full scene with default size.
     */
    public void switchScene(String fxmlFile, String title) {
        switchScene(fxmlFile, title, 1000, 700);
    }

    /**
     * Load a view into the main shell's content area (center of BorderPane).
     */
    public void loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Parent content = loader.load();
            if (mainContentPane != null) {
                mainContentPane.setCenter(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load content: " + fxmlFile);
        }
    }

    /**
     * Load a view into the main shell's content area and return the controller.
     */
    public <T> T loadContentAndGetController(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Parent content = loader.load();
            if (mainContentPane != null) {
                mainContentPane.setCenter(content);
            }
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load content: " + fxmlFile);
            return null;
        }
    }

    /**
     * Navigate to Login screen.
     */
    public void showLogin() {
        switchScene("login-view.fxml", "Social Media App - Login", 500, 600);
    }

    /**
     * Navigate to Register screen.
     */
    public void showRegister() {
        switchScene("register-view.fxml", "Social Media App - Register", 500, 700);
    }

    /**
     * Navigate to Main shell (after login).
     */
    public void showMain() {
        switchScene("main-view.fxml", "Social Media App", 1100, 750);
    }
}
