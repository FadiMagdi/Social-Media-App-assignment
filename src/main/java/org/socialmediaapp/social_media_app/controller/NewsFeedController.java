package org.socialmediaapp.social_media_app.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.socialmediaapp.social_media_app.HelloApplication;
import org.socialmediaapp.social_media_app.dao.PostManagementDao;
import org.socialmediaapp.social_media_app.dao.UserDao;
import org.socialmediaapp.social_media_app.database.DatabaseConnection;
import org.socialmediaapp.social_media_app.domain.Post;
import org.socialmediaapp.social_media_app.domain.userDTO;
import org.socialmediaapp.social_media_app.util.SceneManager;
import org.socialmediaapp.social_media_app.util.SessionManager;

import java.io.File;
import java.sql.Connection;
import java.util.List;

public class NewsFeedController {

    @FXML private TextArea postTextArea;
    @FXML private ComboBox<String> privacyCombo;
    @FXML private Label selectedImageLabel;
    @FXML private VBox postsContainer;
    @FXML private Label emptyFeedLabel;

    private String selectedImagePath = "";

    @FXML
    public void initialize() {
        // Setup privacy combo
        privacyCombo.setItems(FXCollections.observableArrayList("Public", "Friends Only", "Private"));
        privacyCombo.setValue("Public");

        // Load feed posts
        loadFeedPosts();
    }

    @FXML
    private void handleCreatePost() {
        String text = postTextArea.getText() != null ? postTextArea.getText().trim() : "";
        String privacy = privacyCombo.getValue();

        if (text.isEmpty() && selectedImagePath.isEmpty()) {
            return; // Nothing to post
        }

        try {
            Connection conn = DatabaseConnection.getDBConnection();
            UserDao userDao = new UserDao(conn);
            PostManagementDao postDao = new PostManagementDao(conn, userDao);

            Integer currentUserId = SessionManager.getInstance().getCurrentUserID();
            userDTO postMaker = userDao.getUserDTOByID(currentUserId);

            Post newPost = new Post(
                    postMaker,
                    new java.sql.Date(System.currentTimeMillis()),
                    selectedImagePath,
                    text,
                    privacy
            );

            boolean success = postDao.addPosts(newPost);
            if (success) {
                postTextArea.clear();
                selectedImagePath = "";
                selectedImageLabel.setText("");
                loadFeedPosts();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(SceneManager.getInstance().getPrimaryStage());
        if (file != null) {
            selectedImagePath = file.getAbsolutePath();
            selectedImageLabel.setText(file.getName());
        }
    }

    private void loadFeedPosts() {
        postsContainer.getChildren().clear();

        try {
            Connection conn = DatabaseConnection.getDBConnection();
            UserDao userDao = new UserDao(conn);
            PostManagementDao postDao = new PostManagementDao(conn, userDao);

            Integer currentUserId = SessionManager.getInstance().getCurrentUserID();
            List<Post> feedPosts = postDao.getPostsForUseFeed(currentUserId);

            if (feedPosts == null || feedPosts.isEmpty()) {
                postsContainer.getChildren().add(emptyFeedLabel);
                emptyFeedLabel.setVisible(true);
            } else {
                emptyFeedLabel.setVisible(false);
                for (Post post : feedPosts) {
                    addPostCard(post);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            postsContainer.getChildren().add(new Label("Failed to load feed."));
        }
    }

    private void addPostCard(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("post-card.fxml"));
            Parent postCard = loader.load();
            PostCardController controller = loader.getController();
            controller.setPost(post);
            postsContainer.getChildren().add(postCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
