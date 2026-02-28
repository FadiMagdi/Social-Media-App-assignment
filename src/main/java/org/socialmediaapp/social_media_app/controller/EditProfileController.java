package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.socialmediaapp.social_media_app.domain.Profile;
import org.socialmediaapp.social_media_app.domain.User;
import org.socialmediaapp.social_media_app.service.UserService;
import org.socialmediaapp.social_media_app.util.SessionManager;

import java.io.File;

/**
 * Controller for edit-profile-dialog.fxml.
 * Uses UserService to update profile.
 */
public class EditProfileController {

    @FXML private TextField nameField;
    @FXML private TextArea bioField;
    @FXML private Label messageLabel;
    @FXML private ImageView profileImageView;
    @FXML private Label imageInitial;

    private ProfileController profileController;
    private final UserService userService = new UserService();
    private String selectedImagePath = "";

    public void setProfileController(ProfileController profileController) {
        this.profileController = profileController;
        populateFields();
    }

    private void populateFields() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user != null) {
            nameField.setText(user.getName());
            Profile profile = user.getProfile();
            if (profile != null && profile.getBio() != null) {
                bioField.setText(profile.getBio());
            }
            // Load current profile image
            if (profile != null && profile.getImagePath() != null && !profile.getImagePath().isEmpty()) {
                selectedImagePath = profile.getImagePath();
                loadImage(selectedImagePath);
            } else {
                imageInitial.setText(user.getName().substring(0, 1).toUpperCase());
            }
        }
    }

    @FXML
    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        Stage stage = (Stage) nameField.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            selectedImagePath = file.toURI().toString();
            loadImage(selectedImagePath);
        }
    }

    private void loadImage(String path) {
        try {
            Image image;
            if (path.startsWith("file:") || path.startsWith("http")) {
                image = new Image(path, 100, 100, true, true);
            } else {
                image = new Image(new File(path).toURI().toString(), 100, 100, true, true);
            }
            if (!image.isError()) {
                profileImageView.setImage(image);
                imageInitial.setText("");
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        String name = nameField.getText().trim();
        String bio = bioField.getText() != null ? bioField.getText().trim() : "";

        if (name.isEmpty()) {
            messageLabel.setText("Name cannot be empty.");
            messageLabel.setVisible(true);
            messageLabel.setManaged(true);
            return;
        }

        int userId = SessionManager.getInstance().getCurrentUserId();

        boolean success = userService.updateProfile(userId, name, bio, selectedImagePath);
        if (success) {
            // Update local session data
            User user = SessionManager.getInstance().getCurrentUser();
            user.setName(name);
            if (user.getProfile() != null) {
                user.getProfile().setBio(bio);
                user.getProfile().setImagePath(selectedImagePath);
            }
            if (profileController != null) {
                profileController.refreshProfile();
            }
            closeDialog();
        } else {
            messageLabel.setText("Failed to update profile.");
            messageLabel.setVisible(true);
            messageLabel.setManaged(true);
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
}
