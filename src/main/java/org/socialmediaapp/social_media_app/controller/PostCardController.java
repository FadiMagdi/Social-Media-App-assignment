package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.socialmediaapp.social_media_app.domain.Comment;
import org.socialmediaapp.social_media_app.domain.Like;
import org.socialmediaapp.social_media_app.domain.Post;
import org.socialmediaapp.social_media_app.domain.UserDTO;
import org.socialmediaapp.social_media_app.service.FriendService;
import org.socialmediaapp.social_media_app.service.NotificationService;
import org.socialmediaapp.social_media_app.service.PostService;
import org.socialmediaapp.social_media_app.util.SessionManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Controller for post-card.fxml.
 * Displays a single post with like/comment functionality.
 */
public class PostCardController {

    @FXML
    private Label avatarInitial;
    @FXML
    private ImageView avatarImageView;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label privacyLabel;
    @FXML
    private Label postTextLabel;
    @FXML
    private Label likesCountLabel;
    @FXML
    private Label commentsCountLabel;
    @FXML
    private Button likeBtn;
    @FXML
    private VBox commentsSection;
    @FXML
    private VBox commentsContainer;
    @FXML
    private TextField commentInput;

    private Post post;
    private boolean commentsVisible = false;
    private final PostService postService = new PostService();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

    private final NotificationService notificationService = new NotificationService();
    private final FriendService friendsService = new FriendService();

    public void setPost(Post post) {
        this.post = post;
        displayPost();
    }

    private void displayPost() {
        if (post == null)
            return;

        // Author info
        usernameLabel.setText(post.getAuthor().name());
        avatarInitial.setText(post.getAuthor().name().substring(0, 1).toUpperCase());

        // Author profile image
        if (post.getAuthor().profile() != null && post.getAuthor().profile().getImagePath() != null
                && !post.getAuthor().profile().getImagePath().isEmpty()) {
            loadAvatarImage(post.getAuthor().profile().getImagePath());
        }

        // Date and privacy
        if (post.getDate() != null) {
            dateLabel.setText(dateFormat.format(post.getDate()));
        }
        if (post.getPrivacy() != null) {
            privacyLabel.setText("· " + post.getPrivacy());
        }

        // Post text
        if (post.getText() != null && !post.getText().isEmpty()) {
            postTextLabel.setText(post.getText());
            postTextLabel.setVisible(true);
            postTextLabel.setManaged(true);
        } else {
            postTextLabel.setVisible(false);
            postTextLabel.setManaged(false);
        }

        // Counts
        likesCountLabel.setText(post.getLikes().size() + " Likes");
        commentsCountLabel.setText(post.getComments().size() + " Comments");
    }

    @FXML
    private void handleLike() {
        int userId = SessionManager.getInstance().getCurrentUserId();
        String userName = SessionManager.getInstance().getCurrentUser().getName();
        boolean success = postService.likePost(post.getId(), userId);
        if (success) {
            List<UserDTO> userFriends = this.friendsService.getFriends(post.getAuthor().id());
            String postMakerName = post.getAuthor().name();
            String Notification_Text = userName + " liked " + postMakerName + "'s post";
            for (int i = 0; i < userFriends.size(); i++) {
                this.notificationService.createNotification(userId, userFriends.get(i).id(), post.getAuthor().id(),
                        "Like", Notification_Text);
            }

            post.getLikes().add(new Like(new UserDTO(userId, "", null)));
            likesCountLabel.setText(post.getLikes().size() + " Likes");
            likeBtn.setText("Liked");
            likeBtn.setDisable(true);
        }
    }

    @FXML
    private void handleToggleComments() {
        commentsVisible = !commentsVisible;
        commentsSection.setVisible(commentsVisible);
        commentsSection.setManaged(commentsVisible);
        if (commentsVisible) {
            loadComments();
        }
    }

    @FXML
    private void handleAddComment() {
        String text = commentInput.getText().trim();
        if (text.isEmpty())
            return;

        int userId = SessionManager.getInstance().getCurrentUserId();
        boolean success = postService.addComment(post.getId(), userId, text);
        if (success) {
            commentInput.clear();
            String userName = SessionManager.getInstance().getCurrentUser().getName();

            List<UserDTO> userFriends = this.friendsService.getFriends(post.getAuthor().id());
            String postMakerName = post.getAuthor().name();
            String Notification_Text = userName + " commented on " + postMakerName + "'s post";
            for (int i = 0; i < userFriends.size(); i++) {
                this.notificationService.createNotification(userId, userFriends.get(i).id(), post.getAuthor().id(),
                        "Comment", Notification_Text);
            }

            Comment newComment = new Comment(new UserDTO(userId, userName, null), text, new java.util.Date());
            post.getComments().add(newComment);
            commentsCountLabel.setText(post.getComments().size() + " Comments");
            loadComments();
        }
    }

    private void loadComments() {
        commentsContainer.getChildren().clear();
        for (Comment c : post.getComments()) {
            Label label = new Label(c.getAuthor().name() + ": " + c.getText());
            label.setWrapText(true);
            label.getStyleClass().add("comment-text");
            label.setStyle("-fx-padding: 8; -fx-background-color: #F0F2F5; -fx-background-radius: 12;");
            commentsContainer.getChildren().add(label);
        }
    }

    private void loadAvatarImage(String path) {
        try {
            Image image;
            if (path.startsWith("file:") || path.startsWith("http")) {
                image = new Image(path, 40, 40, true, true);
            } else {
                image = new Image(new File(path).toURI().toString(), 40, 40, true, true);
            }
            if (!image.isError()) {
                avatarImageView.setImage(image);
                avatarInitial.setText("");
            }
        } catch (Exception e) {
            System.err.println("Error loading avatar: " + e.getMessage());
        }
    }
}
