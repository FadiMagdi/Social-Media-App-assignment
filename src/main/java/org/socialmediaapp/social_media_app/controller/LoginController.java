package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.socialmediaapp.social_media_app.domain.User;
import org.socialmediaapp.social_media_app.service.UserService;
import org.socialmediaapp.social_media_app.util.SceneManager;
import org.socialmediaapp.social_media_app.util.SessionManager;

/**
 * Controller for login-view.fxml.
 * Uses UserService for authentication.
 */
public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserService userService = new UserService();

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        User user = userService.login(email, password);
        if (user != null) {
            SessionManager.getInstance().setCurrentUser(user);
            SceneManager.getInstance().showMain();
        } else {
            showError("Invalid email or password.");
        }
    }

    @FXML
    private void handleGoToRegister() {
        SceneManager.getInstance().showRegister();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}
