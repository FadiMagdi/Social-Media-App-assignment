package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.socialmediaapp.social_media_app.service.UserService;
import org.socialmediaapp.social_media_app.util.SceneManager;

/**
 * Controller for register-view.fxml.
 * Uses UserService for registration.
 */
public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField ageField;
    @FXML private Label errorLabel;

    private final UserService userService = new UserService();

    @FXML
    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String ageText = ageField.getText().trim();

        // Validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || ageText.isEmpty()) {
            showError("Please fill in all required fields.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }
        if (!email.contains("@")) {
            showError("Please enter a valid email.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            showError("Please enter a valid age.");
            return;
        }

        boolean success = userService.register(name, email, password, age);
        if (success) {
            SceneManager.getInstance().showLogin();
        } else {
            showError("Registration failed. Email may already be in use.");
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
