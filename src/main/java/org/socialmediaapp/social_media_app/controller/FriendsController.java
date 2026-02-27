package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.socialmediaapp.social_media_app.dao.FriendSystemDao;
import org.socialmediaapp.social_media_app.dao.UserDao;
import org.socialmediaapp.social_media_app.database.DatabaseConnection;
import org.socialmediaapp.social_media_app.domain.friendRequest;
import org.socialmediaapp.social_media_app.domain.userDTO;
import org.socialmediaapp.social_media_app.util.SceneManager;
import org.socialmediaapp.social_media_app.util.SessionManager;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

public class FriendsController {

    @FXML private TabPane friendsTabPane;
    @FXML private VBox friendsListContainer;
    @FXML private VBox requestsListContainer;
    @FXML private Label noFriendsLabel;
    @FXML private Label noRequestsLabel;
    @FXML private Tab requestsTab;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

    @FXML
    public void initialize() {
        loadFriends();
        loadFriendRequests();
    }

    private void loadFriends() {
        friendsListContainer.getChildren().clear();

        try {
            Connection conn = DatabaseConnection.getDBConnection();
            UserDao userDao = new UserDao(conn);
            FriendSystemDao friendDao = new FriendSystemDao(conn, userDao);

            Integer currentUserId = SessionManager.getInstance().getCurrentUserID();
            List<userDTO> friends = friendDao.getUserFriends(currentUserId);

            if (friends == null || friends.isEmpty()) {
                friendsListContainer.getChildren().add(noFriendsLabel);
            } else {
                noFriendsLabel.setVisible(false);
                for (userDTO friend : friends) {
                    friendsListContainer.getChildren().add(createFriendCard(friend));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            friendsListContainer.getChildren().add(new Label("Failed to load friends."));
        }
    }

    private void loadFriendRequests() {
        requestsListContainer.getChildren().clear();

        try {
            Connection conn = DatabaseConnection.getDBConnection();
            UserDao userDao = new UserDao(conn);
            FriendSystemDao friendDao = new FriendSystemDao(conn, userDao);

            Integer currentUserId = SessionManager.getInstance().getCurrentUserID();
            List<friendRequest> requests = friendDao.getUserFriendRequests(currentUserId);

            if (requests == null || requests.isEmpty()) {
                requestsListContainer.getChildren().add(noRequestsLabel);
                requestsTab.setText("Friend Requests");
            } else {
                noRequestsLabel.setVisible(false);
                requestsTab.setText("Friend Requests (" + requests.size() + ")");
                for (friendRequest request : requests) {
                    requestsListContainer.getChildren().add(createRequestCard(request));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            requestsListContainer.getChildren().add(new Label("Failed to load requests."));
        }
    }

    private HBox createFriendCard(userDTO friend) {
        HBox card = new HBox(12);
        card.getStyleClass().add("friend-card");
        card.setAlignment(Pos.CENTER_LEFT);

        // Avatar
        VBox avatar = new VBox();
        avatar.setAlignment(Pos.CENTER);
        avatar.setMinSize(40, 40);
        avatar.setMaxSize(40, 40);
        avatar.setStyle("-fx-background-color: #1877F2; -fx-background-radius: 20;");
        Label initial = new Label(friend.userName().substring(0, 1).toUpperCase());
        initial.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        avatar.getChildren().add(initial);

        // Name
        Label nameLabel = new Label(friend.userName());
        nameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // View Profile Button
        Button viewBtn = new Button("View Profile");
        viewBtn.getStyleClass().add("btn-outline");
        viewBtn.setOnAction(e -> {
            ProfileController controller = SceneManager.getInstance()
                    .loadContentAndGetController("profile-view.fxml");
            if (controller != null) {
                controller.loadProfile(friend.userID());
            }
        });

        card.getChildren().addAll(avatar, nameLabel, spacer, viewBtn);
        return card;
    }

    private HBox createRequestCard(friendRequest request) {
        HBox card = new HBox(12);
        card.getStyleClass().add("friend-card");
        card.setAlignment(Pos.CENTER_LEFT);

        // Avatar
        VBox avatar = new VBox();
        avatar.setAlignment(Pos.CENTER);
        avatar.setMinSize(40, 40);
        avatar.setMaxSize(40, 40);
        avatar.setStyle("-fx-background-color: #42B72A; -fx-background-radius: 20;");
        Label initial = new Label(request.getSourceUser().userName().substring(0, 1).toUpperCase());
        initial.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        avatar.getChildren().add(initial);

        // Info
        VBox info = new VBox(2);
        Label nameLabel = new Label(request.getSourceUser().userName());
        nameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        Label dateLabel = new Label("Sent: " + (request.getSendDate() != null ?
                dateFormat.format(request.getSendDate()) : ""));
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #65676B;");
        info.getChildren().addAll(nameLabel, dateLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Accept Button
        Button acceptBtn = new Button("Accept");
        acceptBtn.getStyleClass().add("btn-secondary");
        acceptBtn.setOnAction(e -> handleAcceptRequest(request, card));

        // Ignore Button
        Button ignoreBtn = new Button("Ignore");
        ignoreBtn.getStyleClass().add("btn-danger");
        ignoreBtn.setOnAction(e -> handleIgnoreRequest(request, card));

        card.getChildren().addAll(avatar, info, spacer, acceptBtn, ignoreBtn);
        return card;
    }

    private void handleAcceptRequest(friendRequest request, HBox card) {
        try {
            Connection conn = DatabaseConnection.getDBConnection();
            UserDao userDao = new UserDao(conn);
            FriendSystemDao friendDao = new FriendSystemDao(conn, userDao);

            boolean success = friendDao.acceptFriendRequest(request);
            if (success) {
                requestsListContainer.getChildren().remove(card);
                loadFriends(); // Refresh friends list
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleIgnoreRequest(friendRequest request, HBox card) {
        try {
            Connection conn = DatabaseConnection.getDBConnection();
            UserDao userDao = new UserDao(conn);
            FriendSystemDao friendDao = new FriendSystemDao(conn, userDao);

            boolean success = friendDao.ignoreFriendRequest(request);
            if (success) {
                requestsListContainer.getChildren().remove(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
