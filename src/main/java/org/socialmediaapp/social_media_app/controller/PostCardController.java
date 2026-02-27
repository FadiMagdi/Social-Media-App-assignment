package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.socialmediaapp.social_media_app.dao.PostManagementDao;
import org.socialmediaapp.social_media_app.dao.UserDao;
import org.socialmediaapp.social_media_app.database.DatabaseConnection;
import org.socialmediaapp.social_media_app.domain.*;
import org.socialmediaapp.social_media_app.util.SessionManager;

import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

public class PostCardController {

    @FXML private Label avatarInitial;
    @FXML private Label usernameLabel;
    @FXML private Label dateLabel;
    @FXML private Label privacyLabel;
    @FXML private Label postTextLabel;
    @FXML private ImageView postImageView;
    @FXML private Label likesCountLabel;
    @FXML private Label commentsCountLabel;
    @FXML private Button likeBtn;
    @FXML private Button commentBtn;
    @FXML private VBox commentsSection;
    @FXML private VBox commentsContainer;
    @FXML private TextField commentInput;

    private Post post;
    private boolean commentsVisible = false;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy 'at' HH:mm");

    public void setPost(Post post) {
        this.post = post;
        populatePostData();
    }

    private void populatePostData() {
        if (post == null) return;

        // Username and avatar
        if (post.getPostMaker() != null) {
            String name = post.getPostMaker().userName();
            usernameLabel.setText(name);
            avatarInitial.setText(name.substring(0, 1).toUpperCase());
        }

        // Date
        if (post.getPostDate() != null) {
            dateLabel.setText(dateFormat.format(post.getPostDate()));
        }

        // Privacy
        if (post.getPrivacy() != null) {
            privacyLabel.setText("· " + post.getPrivacy());
        }

        // Post text
        if (post.getPostText() != null && !post.getPostText().isEmpty()) {
            postTextLabel.setText(post.getPostText());
            postTextLabel.setVisible(true);
            postTextLabel.setManaged(true);
        } else {
            postTextLabel.setVisible(false);
            postTextLabel.setManaged(false);
        }

        // Post image
        if (post.getImage_path() != null && !post.getImage_path().isEmpty()) {
            try {
                File imgFile = new File(post.getImage_path());
                if (imgFile.exists()) {
                    Image image = new Image(imgFile.toURI().toString(), 560, 0, true, true);
                    postImageView.setImage(image);
                    postImageView.setVisible(true);
                    postImageView.setManaged(true);
                }
            } catch (Exception e) {
                postImageView.setVisible(false);
                postImageView.setManaged(false);
            }
        }

        // Likes count
        if (post.getPostLikes() != null) {
            likesCountLabel.setText(post.getPostLikes().size() + " Likes");
        }

        // Comments count
        if (post.getPostComments() != null) {
            commentsCountLabel.setText(post.getPostComments().size() + " Comments");
        }
    }

    @FXML
    private void handleLike() {
        try {
            Connection conn = DatabaseConnection.getDBConnection();
            UserDao userDao = new UserDao(conn);
            PostManagementDao postDao = new PostManagementDao(conn, userDao);

            Integer currentUserId = SessionManager.getInstance().getCurrentUserID();
            userDTO currentUserDTO = userDao.getUserDTOByID(currentUserId);

            Like newLike = new Like(currentUserDTO);
            boolean success = postDao.addLikeToPost(newLike, post.getPostID());

            if (success) {
                post.getPostLikes().add(newLike);
                likesCountLabel.setText(post.getPostLikes().size() + " Likes");
                likeBtn.getStyleClass().add("post-action-btn-liked");
                likeBtn.setText("Liked");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        if (text.isEmpty()) return;

        try {
            Connection conn = DatabaseConnection.getDBConnection();
            UserDao userDao = new UserDao(conn);
            PostManagementDao postDao = new PostManagementDao(conn, userDao);

            Integer currentUserId = SessionManager.getInstance().getCurrentUserID();
            userDTO currentUserDTO = userDao.getUserDTOByID(currentUserId);

            Comment newComment = new Comment(
                    currentUserDTO,
                    text,
                    new java.sql.Date(System.currentTimeMillis())
            );

            boolean success = postDao.addCommentToPost(newComment, post.getPostID());

            if (success) {
                post.getPostComments().add(newComment);
                commentsCountLabel.setText(post.getPostComments().size() + " Comments");
                commentInput.clear();
                addCommentToUI(newComment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadComments() {
        commentsContainer.getChildren().clear();
        if (post.getPostComments() != null) {
            for (Comment comment : post.getPostComments()) {
                addCommentToUI(comment);
            }
        }
    }

    private void addCommentToUI(Comment comment) {
        VBox commentBox = new VBox(2);
        commentBox.getStyleClass().add("comment-box");

        HBox header = new HBox(8);
        Label nameLabel = new Label(comment.getUserMadeComment().userName());
        nameLabel.getStyleClass().add("comment-username");

        Label dateLabel = new Label("");
        if (comment.getCommentDate() != null) {
            dateLabel.setText(dateFormat.format(comment.getCommentDate()));
        }
        dateLabel.getStyleClass().add("comment-date");
        header.getChildren().addAll(nameLabel, dateLabel);

        Label textLabel = new Label(comment.getCommentText());
        textLabel.getStyleClass().add("comment-text");
        textLabel.setWrapText(true);

        commentBox.getChildren().addAll(header, textLabel);
        commentsContainer.getChildren().add(commentBox);
    }
}
