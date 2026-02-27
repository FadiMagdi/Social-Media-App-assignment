package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.socialmediaapp.social_media_app.dao.UserDao;
import org.socialmediaapp.social_media_app.database.DatabaseConnection;
import org.socialmediaapp.social_media_app.domain.Profile;
import org.socialmediaapp.social_media_app.domain.User;
import org.socialmediaapp.social_media_app.util.SceneManager;
import org.socialmediaapp.social_media_app.util.SessionManager;

import java.io.File;
import java.sql.Connection;

public class EditProfileController {

    @FXML private TextField nameField;
    @FXML private TextArea bioField;
    @FXML private Label imagePathLabel;
    @FXML private Label messageLabel;

    private String selectedImagePath = "";
    private ProfileController profileController;

    public void setProfileController(ProfileController profileController) {
        this.profileController = profileController;
        populateFields();
    }

    private void populateFields() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            nameField.setText(currentUser.getUserName());
            Profile profile = currentUser.getUserProfile();
            if (profile != null) {
                bioField.setText(profile.bio != null ? profile.bio : "");
                if (profile.imagePath != null && !profile.imagePath.isEmpty()) {
                    selectedImagePath = profile.imagePath;
                    imagePathLabel.setText(new File(profile.imagePath).getName());
                }
            }
        }
    }

    @FXML
    private void handleSave() {
        String name = nameField.getText().trim();
        String bio = bioField.getText() != null ? bioField.getText().trim() : "";

        if (name.isEmpty()) {
            showMessage("Name cannot be empty.", true);
            return;
        }

        try {
            Connection conn = DatabaseConnection.getDBConnection();
            UserDao userDao = new UserDao(conn);

            Integer userId = SessionManager.getInstance().getCurrentUserID();

            // Update profile in database
            boolean success = userDao.createProfile(userId, bio, selectedImagePath);

            if (success) {
                // Update session
                User currentUser = SessionManager.getInstance().getCurrentUser();
                currentUser.setUserName(name);
                if (currentUser.getUserProfile() != null) {
                    currentUser.getUserProfile().bio = bio;
                    currentUser.getUserProfile().imagePath = selectedImagePath;
                }

                // Refresh profile view
                if (profileController != null) {
                    profileController.refreshProfile();
                }

                // Close dialog
                closeDialog();
            } else {
                showMessage("Failed to update profile.", true);
            }

        } catch (Exception e) {
            showMessage("Error updating profile.", true);
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
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
        messageLabel.setManaged(true);
        if (isError) {
            messageLabel.setStyle("-fx-text-fill: #FA3E3E;");
        } else {
            messageLabel.setStyle("-fx-text-fill: #42B72A;");
        }
    }
}
