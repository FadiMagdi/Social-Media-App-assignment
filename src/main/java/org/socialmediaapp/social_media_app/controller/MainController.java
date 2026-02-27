package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import org.socialmediaapp.social_media_app.domain.User;
import org.socialmediaapp.social_media_app.util.SceneManager;
import org.socialmediaapp.social_media_app.util.SessionManager;

public class MainController {

    @FXML private BorderPane mainBorderPane;
    @FXML private BorderPane contentArea;
    @FXML private TextField searchField;
    @FXML private Label userNameLabel;
    @FXML private Button feedBtn;
    @FXML private Button myProfileBtn;
    @FXML private Button friendsBtn;
    @FXML private Button notificationsBtn;
    @FXML private Button searchBtn;
    @FXML private Button notifBtn;
    @FXML private Button profileBtn;

    @FXML
    public void initialize() {
        // Set the content pane in SceneManager for inner navigation
        SceneManager.getInstance().setMainContentPane(contentArea);

        // Set welcome message
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            userNameLabel.setText("Hi, " + currentUser.getUserName());
        }

        // Load News Feed by default
        handleFeed();
    }

    @FXML
    private void handleFeed() {
        clearActiveStates();
        feedBtn.getStyleClass().add("sidebar-btn-active");
        SceneManager.getInstance().loadContent("news-feed-view.fxml");
    }

    @FXML
    private void handleMyProfile() {
        clearActiveStates();
        myProfileBtn.getStyleClass().add("sidebar-btn-active");
        ProfileController controller = SceneManager.getInstance()
                .loadContentAndGetController("profile-view.fxml");
        if (controller != null) {
            controller.loadProfile(SessionManager.getInstance().getCurrentUserID());
        }
    }

    @FXML
    private void handleFriends() {
        clearActiveStates();
        friendsBtn.getStyleClass().add("sidebar-btn-active");
        SceneManager.getInstance().loadContent("friends-view.fxml");
    }

    @FXML
    private void handleNotifications() {
        clearActiveStates();
        notificationsBtn.getStyleClass().add("sidebar-btn-active");
        SceneManager.getInstance().loadContent("notifications-view.fxml");
    }

    @FXML
    private void handleSearchPage() {
        clearActiveStates();
        searchBtn.getStyleClass().add("sidebar-btn-active");
        SceneManager.getInstance().loadContent("search-view.fxml");
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            clearActiveStates();
            searchBtn.getStyleClass().add("sidebar-btn-active");
            SearchController controller = SceneManager.getInstance()
                    .loadContentAndGetController("search-view.fxml");
            if (controller != null) {
                controller.performSearch(query);
            }
        }
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().logout();
        SceneManager.getInstance().showLogin();
    }

    private void clearActiveStates() {
        feedBtn.getStyleClass().remove("sidebar-btn-active");
        myProfileBtn.getStyleClass().remove("sidebar-btn-active");
        friendsBtn.getStyleClass().remove("sidebar-btn-active");
        notificationsBtn.getStyleClass().remove("sidebar-btn-active");
        searchBtn.getStyleClass().remove("sidebar-btn-active");
    }
}
