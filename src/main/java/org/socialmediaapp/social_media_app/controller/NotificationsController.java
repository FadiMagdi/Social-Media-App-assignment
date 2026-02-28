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
                noNotificationsLabel.setVisible(true);
                notificationsContainer.getChildren().add(noNotificationsLabel);
            } else {
                for (Notification notif : notifications) {
                    String text = (notif.getText() != null && !notif.getText().isBlank())
                            ? notif.getText()
                            : "New " + (notif.getType() != null ? notif.getType() : "") + " notification";
                    String date = notif.getDate() != null ? " - " + dateFormat.format(notif.getDate()) : "";

                    Label label = new Label(text + date);
                    label.setWrapText(true);
                    label.setMaxWidth(Double.MAX_VALUE);
                    label.setStyle("-fx-padding: 12; -fx-background-color: white; -fx-background-radius: 8; -fx-font-size: 14px; -fx-text-fill: #1C1E21;");
                    notificationsContainer.getChildren().add(label);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            notificationsContainer.getChildren().clear();
            Label errorLabel = new Label("Could not load notifications.");
            errorLabel.setStyle("-fx-text-fill: #65676B; -fx-padding: 20; -fx-font-size: 14px;");
            notificationsContainer.getChildren().add(errorLabel);
        }
    }
}
