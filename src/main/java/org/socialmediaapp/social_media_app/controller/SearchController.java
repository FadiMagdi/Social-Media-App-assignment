package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import org.socialmediaapp.social_media_app.HelloApplication;
import org.socialmediaapp.social_media_app.domain.Post;
import org.socialmediaapp.social_media_app.domain.UserDTO;
import org.socialmediaapp.social_media_app.service.PostService;
import org.socialmediaapp.social_media_app.service.UserService;
import org.socialmediaapp.social_media_app.util.SceneManager;

import java.io.File;
import java.util.List;

/**
 * Controller for search-view.fxml.
 * Uses UserService and PostService for searching.
 */
public class SearchController {

    @FXML private TextField searchField;
    @FXML private VBox usersResultsContainer;
    @FXML private VBox postsResultsContainer;
    @FXML private Label usersHeader;
    @FXML private Label postsHeader;
    @FXML private Label noResultsLabel;

    private final UserService userService = new UserService();
    private final PostService postService = new PostService();

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) performSearch(query);
    }

    /** Perform search (also called from MainController). */
    public void performSearch(String query) {
        searchField.setText(query);
        usersResultsContainer.getChildren().clear();
        postsResultsContainer.getChildren().clear();
        noResultsLabel.setVisible(false);

        boolean found = false;

        // Search users
        List<UserDTO> users = userService.searchUsers(query);
        if (!users.isEmpty()) {
            found = true;
            usersHeader.setVisible(true);
            usersHeader.setManaged(true);
            for (UserDTO user : users) {
                usersResultsContainer.getChildren().add(createUserItem(user));
            }
        } else {
            usersHeader.setVisible(false);
            usersHeader.setManaged(false);
        }

        // Search posts
        List<Post> posts = postService.searchPosts(query);
        if (!posts.isEmpty()) {
            found = true;
            postsHeader.setVisible(true);
            postsHeader.setManaged(true);
            for (Post post : posts) {
                try {
                    FXMLLoader loader = new FXMLLoader(
                        HelloApplication.class.getResource("post-card.fxml"));
                    Parent card = loader.load();
                    PostCardController ctrl = loader.getController();
                    ctrl.setPost(post);
                    postsResultsContainer.getChildren().add(card);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            postsHeader.setVisible(false);
            postsHeader.setManaged(false);
        }

        if (!found) {
            noResultsLabel.setText("No results found for \"" + query + "\"");
            noResultsLabel.setVisible(true);
        }
    }

    private HBox createUserItem(UserDTO user) {
        HBox item = new HBox(12);
        item.getStyleClass().add("friend-card");
        item.setAlignment(Pos.CENTER_LEFT);

        item.getChildren().add(createAvatar(user));

        Label nameLabel = new Label(user.name());
        nameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1C1E21;");
        nameLabel.setMinWidth(Region.USE_PREF_SIZE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button viewBtn = new Button("View Profile");
        viewBtn.getStyleClass().add("btn-outline");
        viewBtn.setOnAction(e -> {
            ProfileController ctrl = SceneManager.getInstance()
                    .loadContentAndGetController("profile-view.fxml");
            if (ctrl != null) ctrl.loadProfile(user.id());
        });

        item.getChildren().addAll(nameLabel, spacer, viewBtn);
        return item;
    }

    private StackPane createAvatar(UserDTO user) {
        StackPane avatar = new StackPane();
        avatar.setMinSize(40, 40);
        avatar.setMaxSize(40, 40);

        Circle bg = new Circle(20);
        bg.setStyle("-fx-fill: #1877F2;");
        avatar.getChildren().add(bg);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);
        Circle clip = new Circle(20, 20, 20);
        imageView.setClip(clip);
        avatar.getChildren().add(imageView);

        Label initial = new Label(user.name().substring(0, 1).toUpperCase());
        initial.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        avatar.getChildren().add(initial);

        if (user.profile() != null && user.profile().getImagePath() != null
                && !user.profile().getImagePath().isEmpty()) {
            try {
                String path = user.profile().getImagePath();
                Image image;
                if (path.startsWith("file:") || path.startsWith("http")) {
                    image = new Image(path, 40, 40, true, true);
                } else {
                    image = new Image(new File(path).toURI().toString(), 40, 40, true, true);
                }
                if (!image.isError()) {
                    imageView.setImage(image);
                    initial.setText("");
                }
            } catch (Exception e) {
                // keep initial letter
            }
        }
        return avatar;
    }
}
