package org.socialmediaapp.social_media_app.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.socialmediaapp.social_media_app.HelloApplication;
import org.socialmediaapp.social_media_app.domain.Post;
import org.socialmediaapp.social_media_app.domain.UserDTO;
import org.socialmediaapp.social_media_app.service.FriendService;
import org.socialmediaapp.social_media_app.service.NotificationService;
import org.socialmediaapp.social_media_app.service.PostService;
import org.socialmediaapp.social_media_app.util.SessionManager;

import java.io.File;
import java.util.List;

/**
 * Controller for news-feed-view.fxml.
 * Uses PostService for creating and loading posts.
 */
public class NewsFeedController {

    @FXML
    private TextArea postTextArea;
    @FXML
    private ComboBox<String> privacyCombo;
    @FXML
    private VBox postsContainer;
    @FXML
    private Label emptyFeedLabel;
    @FXML
    private HBox imagePreviewBox;
    @FXML
    private ImageView imagePreview;

    private File selectedImageFile;
    private final PostService postService = new PostService();
    private final NotificationService notificationService = new NotificationService();
    private final FriendService friendsService = new FriendService();

    @FXML
    public void initialize() {
        privacyCombo.setItems(FXCollections.observableArrayList("Public", "Friends Only", "Private"));
        privacyCombo.setValue("Public");
        loadFeed();
    }

    @FXML
    private void handleAddImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        File file = fileChooser.showOpenDialog(postTextArea.getScene().getWindow());
        if (file != null) {
            selectedImageFile = file;
            Image img = new Image(file.toURI().toString(), 300, 150, true, true);
            imagePreview.setImage(img);
            imagePreviewBox.setVisible(true);
            imagePreviewBox.setManaged(true);
        }
    }

    @FXML
    private void handleRemoveImage() {
        selectedImageFile = null;
        imagePreview.setImage(null);
        imagePreviewBox.setVisible(false);
        imagePreviewBox.setManaged(false);
    }

    @FXML
    private void handleCreatePost() {
        String text = postTextArea.getText() != null ? postTextArea.getText().trim() : "";
        String imagePath = selectedImageFile != null ? selectedImageFile.getAbsolutePath() : "";

        // Must have at least text or image
        if (text.isEmpty() && imagePath.isEmpty())
            return;

        int userId = SessionManager.getInstance().getCurrentUserId();

        int postID = postService.createPost(userId, text, imagePath, privacyCombo.getValue());
        if (postID != -1) {
            // create notification
            if (!privacyCombo.getValue().equals("Private")) {
                List<UserDTO> userFriends = this.friendsService.getFriends(userId);
                String postMakerName = SessionManager.getInstance().getCurrentUser().getName();
                String Notification_Text = postMakerName + " posted an update";
                for (int i = 0; i < userFriends.size(); i++) {
                    this.notificationService.createNotification(userId, userFriends.get(i).id(), postID, "Post",
                            Notification_Text);
                }
            }

            postTextArea.clear();
            handleRemoveImage();
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
