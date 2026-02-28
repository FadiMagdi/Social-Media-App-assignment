package org.socialmediaapp.social_media_app.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.socialmediaapp.social_media_app.HelloApplication;
import org.socialmediaapp.social_media_app.domain.Post;
import org.socialmediaapp.social_media_app.service.PostService;
import org.socialmediaapp.social_media_app.util.SessionManager;

import java.util.List;

/**
 * Controller for news-feed-view.fxml.
 * Uses PostService for creating and loading posts.
 */
public class NewsFeedController {

    @FXML private TextArea postTextArea;
    @FXML private ComboBox<String> privacyCombo;
    @FXML private VBox postsContainer;
    @FXML private Label emptyFeedLabel;

    private final PostService postService = new PostService();

    @FXML
    public void initialize() {
        privacyCombo.setItems(FXCollections.observableArrayList("Public", "Friends Only", "Private"));
        privacyCombo.setValue("Public");
        loadFeed();
    }

    @FXML
    private void handleCreatePost() {
        String text = postTextArea.getText() != null ? postTextArea.getText().trim() : "";
        if (text.isEmpty()) return;

        int userId = SessionManager.getInstance().getCurrentUserId();
        boolean success = postService.createPost(userId, text, "", privacyCombo.getValue());
        if (success) {
            postTextArea.clear();
            loadFeed();
        }
    }

    private void loadFeed() {
        postsContainer.getChildren().clear();
        int userId = SessionManager.getInstance().getCurrentUserId();
        List<Post> posts = postService.getFeedPosts(userId);

        if (posts.isEmpty()) {
            emptyFeedLabel.setVisible(true);
            postsContainer.getChildren().add(emptyFeedLabel);
        } else {
            emptyFeedLabel.setVisible(false);
            for (Post post : posts) {
                try {
                    FXMLLoader loader = new FXMLLoader(
                        HelloApplication.class.getResource("post-card.fxml"));
                    Parent card = loader.load();
                    PostCardController ctrl = loader.getController();
                    ctrl.setPost(post);
                    postsContainer.getChildren().add(card);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
