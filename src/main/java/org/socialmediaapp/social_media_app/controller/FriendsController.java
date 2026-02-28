package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import org.socialmediaapp.social_media_app.domain.FriendRequest;
import org.socialmediaapp.social_media_app.domain.UserDTO;
import org.socialmediaapp.social_media_app.service.FriendService;
import org.socialmediaapp.social_media_app.util.SceneManager;
import org.socialmediaapp.social_media_app.util.SessionManager;

import java.io.File;
import java.util.List;

/**
 * Controller for friends-view.fxml.
 * Uses FriendService for friends list and requests.
 */
public class FriendsController {

    @FXML private VBox friendsListContainer;
    @FXML private VBox requestsListContainer;
    @FXML private Label noFriendsLabel;
    @FXML private Label noRequestsLabel;
    @FXML private Tab requestsTab;

    private final FriendService friendService = new FriendService();

    @FXML
    public void initialize() {
        loadFriends();
        loadRequests();
    }

    private void loadFriends() {
        friendsListContainer.getChildren().clear();
        int userId = SessionManager.getInstance().getCurrentUserId();
        List<UserDTO> friends = friendService.getFriends(userId);

        if (friends.isEmpty()) {
            friendsListContainer.getChildren().add(noFriendsLabel);
        } else {
            noFriendsLabel.setVisible(false);
            for (UserDTO friend : friends) {
                friendsListContainer.getChildren().add(createFriendItem(friend));
            }
        }
    }

    private void loadRequests() {
        requestsListContainer.getChildren().clear();
        int userId = SessionManager.getInstance().getCurrentUserId();
        List<FriendRequest> requests = friendService.getFriendRequests(userId);

        if (requests.isEmpty()) {
            requestsListContainer.getChildren().add(noRequestsLabel);
            requestsTab.setText("Friend Requests");
        } else {
            noRequestsLabel.setVisible(false);
            requestsTab.setText("Friend Requests (" + requests.size() + ")");
            for (FriendRequest req : requests) {
                requestsListContainer.getChildren().add(createRequestItem(req));
            }
        }
    }

    private HBox createFriendItem(UserDTO friend) {
        HBox item = new HBox(12);
        item.getStyleClass().add("friend-card");
        item.setAlignment(Pos.CENTER_LEFT);

        item.getChildren().add(createAvatar(friend));

        Label nameLabel = new Label(friend.name());
        nameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1C1E21;");
        nameLabel.setMinWidth(Region.USE_PREF_SIZE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button viewBtn = new Button("View Profile");
        viewBtn.getStyleClass().add("btn-outline");
        viewBtn.setOnAction(e -> {
            ProfileController ctrl = SceneManager.getInstance()
                    .loadContentAndGetController("profile-view.fxml");
            if (ctrl != null) ctrl.loadProfile(friend.id());
        });

        item.getChildren().addAll(nameLabel, spacer, viewBtn);
        return item;
    }

    private HBox createRequestItem(FriendRequest req) {
        HBox item = new HBox(12);
        item.getStyleClass().add("friend-card");
        item.setAlignment(Pos.CENTER_LEFT);

        item.getChildren().add(createAvatar(req.getSender()));

        Label nameLabel = new Label(req.getSender().name());
        nameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1C1E21;");
        nameLabel.setMinWidth(Region.USE_PREF_SIZE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button acceptBtn = new Button("Accept");
        acceptBtn.getStyleClass().add("btn-secondary");
        acceptBtn.setOnAction(e -> {
            if (friendService.acceptFriendRequest(req)) {
                loadFriends();
                loadRequests();
            }
        });

        Button ignoreBtn = new Button("Ignore");
        ignoreBtn.getStyleClass().add("btn-danger");
        ignoreBtn.setOnAction(e -> {
            if (friendService.ignoreFriendRequest(req.getId())) {
                loadRequests();
            }
        });

        item.getChildren().addAll(nameLabel, spacer, acceptBtn, ignoreBtn);
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
