package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.socialmediaapp.social_media_app.dao.NotificationDao;
import org.socialmediaapp.social_media_app.database.DatabaseConnection;
import org.socialmediaapp.social_media_app.domain.Notification;
import org.socialmediaapp.social_media_app.util.SessionManager;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Controller for notifications-view.fxml.
 * Displays user notifications.
 */
public class NotificationsController {

    @FXML private VBox notificationsContainer;
    @FXML private Label noNotificationsLabel;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

    @FXML
    public void initialize() {
        loadNotifications();
    }

    private void loadNotifications() {
        notificationsContainer.getChildren().clear();
        try {
            NotificationDao notifDao = new NotificationDao(DatabaseConnection.getDBConnection());
            int userId = SessionManager.getInstance().getCurrentUserId();
            List<Notification> notifications = notifDao.getNotifications(userId);

            if (notifications.isEmpty()) {
                notificationsContainer.getChildren().add(noNotificationsLabel);
            } else {
                noNotificationsLabel.setVisible(false);
                for (Notification notif : notifications) {
                    String text = notif.getText() != null ? notif.getText() : "New notification";
                    String date = notif.getDate() != null ? " - " + dateFormat.format(notif.getDate()) : "";

                    Label label = new Label(text + date);
                    label.setWrapText(true);
                    label.setStyle("-fx-padding: 12; -fx-background-color: white; -fx-background-radius: 8;");
                    notificationsContainer.getChildren().add(label);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Label errorLabel = new Label("No notifications yet.");
            errorLabel.setStyle("-fx-text-fill: #65676B; -fx-padding: 20;");
            notificationsContainer.getChildren().add(errorLabel);
        }
    }
}
