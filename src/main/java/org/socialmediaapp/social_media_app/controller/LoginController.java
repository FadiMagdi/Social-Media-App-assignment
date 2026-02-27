package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.socialmediaapp.social_media_app.dao.UserDao;
import org.socialmediaapp.social_media_app.database.DatabaseConnection;
import org.socialmediaapp.social_media_app.domain.User;
import org.socialmediaapp.social_media_app.util.SceneManager;
import org.socialmediaapp.social_media_app.util.SessionManager;

import java.sql.Connection;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Validation
        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        try {
            Connection conn = DatabaseConnection.getDBConnection();
            UserDao userDao = new UserDao(conn);
            User user = userDao.getUserByEmail("'" + email + "'");

            if (user == null) {
                showError("No account found with this email.");
                return;
            }

            if (!user.getPassword().equals(password)) {
                showError("Incorrect password.");
                return;
            }

            // Login success
            SessionManager.getInstance().setCurrentUser(user);
            SceneManager.getInstance().showMain();

        } catch (Exception e) {
            showError("Login failed. Please try again.");
            e.printStackTrace();
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
