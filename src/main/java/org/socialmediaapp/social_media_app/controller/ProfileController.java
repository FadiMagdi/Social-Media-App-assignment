package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.socialmediaapp.social_media_app.HelloApplication;
import org.socialmediaapp.social_media_app.domain.Post;
import org.socialmediaapp.social_media_app.domain.UserDTO;
import org.socialmediaapp.social_media_app.service.FriendService;
import org.socialmediaapp.social_media_app.service.PostService;
import org.socialmediaapp.social_media_app.service.UserService;
import org.socialmediaapp.social_media_app.util.SceneManager;
import org.socialmediaapp.social_media_app.util.SessionManager;

import java.io.File;
import java.util.List;

/**
 * Controller for profile-view.fxml.
 * Uses UserService, PostService, FriendService.
 */
public class ProfileController {

    @FXML private Label profileInitial;
    @FXML private ImageView profileImageView;
    @FXML private Label profileNameLabel;
    @FXML private Label profileBioLabel;
    @FXML private Label profileEmailLabel;
    @FXML private Label profileAgeLabel;
    @FXML private Label friendsCountLabel;
    @FXML private Button editProfileBtn;
    @FXML private Button addFriendBtn;
    @FXML private VBox userPostsContainer;
    @FXML private Label noPostsLabel;

    private int profileUserId;
    private final UserService userService = new UserService();
    private final PostService postService = new PostService();
    private final FriendService friendService = new FriendService();

    /** Load and display profile for the given user ID. */
    public void loadProfile(int userId) {
        this.profileUserId = userId;
        UserDTO userInfo = userService.getUserProfile(userId);
        if (userInfo == null) return;

        // Name and initial
        profileNameLabel.setText(userInfo.name());

        // Profile image
        if (userInfo.profile() != null && userInfo.profile().getImagePath() != null
                && !userInfo.profile().getImagePath().isEmpty()) {
            loadProfileImage(userInfo.profile().getImagePath());
        } else {
            profileInitial.setText(userInfo.name().substring(0, 1).toUpperCase());
            profileImageView.setImage(null);
        }

        // Bio
        if (userInfo.profile() != null && userInfo.profile().getBio() != null
                && !userInfo.profile().getBio().isEmpty()) {
            profileBioLabel.setText(userInfo.profile().getBio());
        } else {
            profileBioLabel.setText("No bio yet");
        }

        // Friends count
        friendsCountLabel.setText(String.valueOf(userService.getFriendsCount(userId)));

        // Show appropriate buttons
        int currentUserId = SessionManager.getInstance().getCurrentUserId();
        if (userId == currentUserId) {
            editProfileBtn.setVisible(true);
            editProfileBtn.setManaged(true);
            profileEmailLabel.setText(SessionManager.getInstance().getCurrentUser().getEmail());
            profileAgeLabel.setText("Age: " + SessionManager.getInstance().getCurrentUser().getAge());
        } else {
            addFriendBtn.setVisible(true);
            addFriendBtn.setManaged(true);
            profileEmailLabel.setText("");
            profileAgeLabel.setText("");

            if (friendService.areFriends(currentUserId, userId)) {
                addFriendBtn.setText("Friends ✓");
                addFriendBtn.setDisable(true);
                addFriendBtn.setStyle("-fx-background-color: #E4E6EB; -fx-text-fill: #1C1E21;");
            } else if (friendService.hasPendingRequest(currentUserId, userId)) {
                addFriendBtn.setText("Request Sent");
                addFriendBtn.setDisable(true);
            }
        }

        // Load user posts
        loadUserPosts(userId);
    }

    private void loadUserPosts(int userId) {
        userPostsContainer.getChildren().clear();
        List<Post> posts = postService.getUserPosts(userId);

        if (posts.isEmpty()) {
            userPostsContainer.getChildren().add(noPostsLabel);
        } else {
            noPostsLabel.setVisible(false);
            for (Post post : posts) {
                try {
                    FXMLLoader loader = new FXMLLoader(
                        HelloApplication.class.getResource("post-card.fxml"));
                    Parent card = loader.load();
                    PostCardController ctrl = loader.getController();
                    ctrl.setPost(post);
                    userPostsContainer.getChildren().add(card);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("edit-profile-dialog.fxml"));
            Parent dialog = loader.load();
            EditProfileController ctrl = loader.getController();
            ctrl.setProfileController(this);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Profile");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(SceneManager.getInstance().getPrimaryStage());
            Scene scene = new Scene(dialog, 420, 550);
            scene.getStylesheets().add(
                HelloApplication.class.getResource("styles.css").toExternalForm());
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddFriend() {
        int currentUserId = SessionManager.getInstance().getCurrentUserId();
        boolean success = friendService.sendFriendRequest(currentUserId, profileUserId);
        if (success) {
            addFriendBtn.setText("Request Sent");
            addFriendBtn.setDisable(true);
        }
    }

    /** Refresh profile after edit. */
    public void refreshProfile() {
        loadProfile(profileUserId);
    }

    private void loadProfileImage(String path) {
        try {
            Image image;
            if (path.startsWith("file:") || path.startsWith("http")) {
                image = new Image(path, 100, 100, true, true);
            } else {
                image = new Image(new File(path).toURI().toString(), 100, 100, true, true);
            }
            if (!image.isError()) {
                profileImageView.setImage(image);
                profileInitial.setText("");
            }
        } catch (Exception e) {
            System.err.println("Error loading profile image: " + e.getMessage());
        }
    }
}
