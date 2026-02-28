package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import org.socialmediaapp.social_media_app.util.SceneManager;
import org.socialmediaapp.social_media_app.util.SessionManager;

/**
 * Controller for main-view.fxml (the main shell with sidebar + content area).
 */
public class MainController {

    @FXML private BorderPane contentArea;
    @FXML private TextField searchField;
    @FXML private Label userNameLabel;
    @FXML private Button feedBtn;
    @FXML private Button myProfileBtn;
    @FXML private Button friendsBtn;
    @FXML private Button notificationsBtn;
    @FXML private Button searchBtn;

    @FXML
    public void initialize() {
        SceneManager.getInstance().setMainContentPane(contentArea);
        String name = SessionManager.getInstance().getCurrentUser().getName();
        userNameLabel.setText("Hi, " + name);

        // Load News Feed by default
        handleFeed();
    }

    @FXML
    private void handleFeed() {
        clearActive();
        feedBtn.getStyleClass().add("sidebar-btn-active");
        SceneManager.getInstance().loadContent("news-feed-view.fxml");
    }

    @FXML
    private void handleMyProfile() {
        clearActive();
        myProfileBtn.getStyleClass().add("sidebar-btn-active");
        ProfileController ctrl = SceneManager.getInstance()
                .loadContentAndGetController("profile-view.fxml");
        if (ctrl != null) {
            ctrl.loadProfile(SessionManager.getInstance().getCurrentUserId());
        }
    }

    @FXML
    private void handleFriends() {
        clearActive();
        friendsBtn.getStyleClass().add("sidebar-btn-active");
        SceneManager.getInstance().loadContent("friends-view.fxml");
    }

    @FXML
    private void handleNotifications() {
        clearActive();
        notificationsBtn.getStyleClass().add("sidebar-btn-active");
        SceneManager.getInstance().loadContent("notifications-view.fxml");
    }

    @FXML
    private void handleSearchPage() {
        clearActive();
        searchBtn.getStyleClass().add("sidebar-btn-active");
        SceneManager.getInstance().loadContent("search-view.fxml");
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            clearActive();
            searchBtn.getStyleClass().add("sidebar-btn-active");
            SearchController ctrl = SceneManager.getInstance()
                    .loadContentAndGetController("search-view.fxml");
            if (ctrl != null) {
                ctrl.performSearch(query);
            }
        }
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().logout();
        SceneManager.getInstance().showLogin();
    }

    private void clearActive() {
        feedBtn.getStyleClass().remove("sidebar-btn-active");
        myProfileBtn.getStyleClass().remove("sidebar-btn-active");
        friendsBtn.getStyleClass().remove("sidebar-btn-active");
        notificationsBtn.getStyleClass().remove("sidebar-btn-active");
        searchBtn.getStyleClass().remove("sidebar-btn-active");
    }
}
