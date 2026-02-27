package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.socialmediaapp.social_media_app.HelloApplication;
import org.socialmediaapp.social_media_app.dao.FriendSystemDao;
import org.socialmediaapp.social_media_app.dao.PostManagementDao;
import org.socialmediaapp.social_media_app.dao.UserDao;
import org.socialmediaapp.social_media_app.database.DatabaseConnection;
import org.socialmediaapp.social_media_app.domain.*;
import org.socialmediaapp.social_media_app.util.SceneManager;
import org.socialmediaapp.social_media_app.util.SessionManager;

import java.io.File;
import java.sql.Connection;
import java.util.List;

public class ProfileController {

    @FXML private ImageView profileImageView;
    @FXML private VBox avatarFallback;
    @FXML private Label profileInitial;
    @FXML private Label profileNameLabel;
    @FXML private Label profileBioLabel;
    @FXML private Label profileEmailLabel;
    @FXML private Label profileAgeLabel;
    @FXML private Label friendsCountLabel;
    @FXML private Button editProfileBtn;
    @FXML private Button addFriendBtn;
    @FXML private HBox actionButtonsBox;
    @FXML private VBox userPostsContainer;
    @FXML private Label noPostsLabel;

    private Integer profileUserID;

    public void loadProfile(Integer userID) {
        this.profileUserID = userID;

        try {
            Connection conn = DatabaseConnection.getDBConnection();
            UserDao userDao = new UserDao(conn);
            FriendSystemDao friendDao = new FriendSystemDao(conn, userDao);
            PostManagementDao postDao = new PostManagementDao(conn, userDao);

            userDTO userInfo = userDao.getUserDTOByID(userID);
            if (userInfo == null) return;

            // Set name
            profileNameLabel.setText(userInfo.userName());
            profileInitial.setText(userInfo.userName().substring(0, 1).toUpperCase());

            // Set profile info
            Profile profile = userInfo.userProfile();
            if (profile != null) {
                if (profile.bio != null && !profile.bio.isEmpty()) {
                    profileBioLabel.setText(profile.bio);
                }
                if (profile.imagePath != null && !profile.imagePath.isEmpty()) {
                    try {
                        File imgFile = new File(profile.imagePath);
                        if (imgFile.exists()) {
                            profileImageView.setImage(new Image(imgFile.toURI().toString()));
                            avatarFallback.setVisible(false);
                        }
                    } catch (Exception ignored) {}
                }
            }

            // Friends count
            List<userDTO> friends = friendDao.getUserFriends(userID);
            friendsCountLabel.setText(String.valueOf(friends != null ? friends.size() : 0));

            // Show correct buttons
            Integer currentUserID = SessionManager.getInstance().getCurrentUserID();
            if (userID.equals(currentUserID)) {
                editProfileBtn.setVisible(true);
                editProfileBtn.setManaged(true);
            } else {
                addFriendBtn.setVisible(true);
                addFriendBtn.setManaged(true);
            }

            // Set email/age (only if own profile)
            if (userID.equals(currentUserID)) {
                User currentUser = SessionManager.getInstance().getCurrentUser();
                profileEmailLabel.setText(currentUser.getEmail());
                profileAgeLabel.setText("Age: " + currentUser.getAge());
            } else {
                profileEmailLabel.setText("");
                profileAgeLabel.setText("");
            }

            // Load user posts
            loadUserPosts(postDao, userID);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUserPosts(PostManagementDao postDao, Integer userID) {
        userPostsContainer.getChildren().clear();
        try {
            List<Post> posts = postDao.getPostsByUserID(userID);
            if (posts == null || posts.isEmpty()) {
                userPostsContainer.getChildren().add(noPostsLabel);
            } else {
                noPostsLabel.setVisible(false);
                for (Post post : posts) {
                    FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("post-card.fxml"));
                    Parent postCard = loader.load();
                    PostCardController controller = loader.getController();
                    controller.setPost(post);
                    userPostsContainer.getChildren().add(postCard);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("edit-profile-dialog.fxml"));
            Parent dialogContent = loader.load();
            EditProfileController controller = loader.getController();
            controller.setProfileController(this);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Profile");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(SceneManager.getInstance().getPrimaryStage());
            Scene scene = new Scene(dialogContent, 420, 400);
            scene.getStylesheets().add(
                    HelloApplication.class.getResource("styles.css").toExternalForm()
            );
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddFriend() {
        try {
            Connection conn = DatabaseConnection.getDBConnection();
            UserDao userDao = new UserDao(conn);
            FriendSystemDao friendDao = new FriendSystemDao(conn, userDao);

            Integer currentUserId = SessionManager.getInstance().getCurrentUserID();
            userDTO currentUserDTO = userDao.getUserDTOByID(currentUserId);

            friendRequest request = new friendRequest(
                    currentUserDTO,
                    new java.sql.Date(System.currentTimeMillis()),
                    profileUserID
            );

            boolean success = friendDao.createFriendRequest(request);
            if (success) {
                addFriendBtn.setText("Request Sent");
                addFriendBtn.setDisable(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Refresh profile after edit.
     */
    public void refreshProfile() {
        if (profileUserID != null) {
            loadProfile(profileUserID);
        }
    }
}
