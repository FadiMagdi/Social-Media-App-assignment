package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.socialmediaapp.social_media_app.dao.UserDao;
import org.socialmediaapp.social_media_app.database.DatabaseConnection;
import org.socialmediaapp.social_media_app.util.SceneManager;

import java.io.File;
import java.sql.Connection;

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField ageField;
    @FXML private TextArea bioField;
    @FXML private Label imagePathLabel;
    @FXML private Label errorLabel;

    private String selectedImagePath = "";

    @FXML
    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String ageText = ageField.getText().trim();
        String bio = bioField.getText() != null ? bioField.getText().trim() : "";

        // Validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || ageText.isEmpty()) {
            showError("Please fill in all required fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showError("Please enter a valid email address.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age < 13 || age > 120) {
                showError("Age must be between 13 and 120.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid age.");
            return;
        }

        try {
            Connection conn = DatabaseConnection.getDBConnection();
            UserDao userDao = new UserDao(conn);

            boolean success = userDao.createUser(email, password, name, age, bio, selectedImagePath);

            if (success) {
                // Go back to login
                SceneManager.getInstance().showLogin();
            } else {
                showError("Registration failed. Email may already be in use.");
            }

        } catch (Exception e) {
            showError("Registration failed. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBrowseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File file = fileChooser.showOpenDialog(SceneManager.getInstance().getPrimaryStage());
        if (file != null) {
            selectedImagePath = file.getAbsolutePath();
            imagePathLabel.setText(file.getName());
        }
    }

    @FXML
    private void handleGoToLogin() {
        SceneManager.getInstance().showLogin();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}
