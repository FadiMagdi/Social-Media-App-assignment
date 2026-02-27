package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.socialmediaapp.social_media_app.dao.NotificationDao;
import org.socialmediaapp.social_media_app.database.DatabaseConnection;
import org.socialmediaapp.social_media_app.domain.Notification;
import org.socialmediaapp.social_media_app.util.SessionManager;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

public class NotificationsController {

    @FXML private VBox notificationsContainer;
    @FXML private Label noNotificationsLabel;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy 'at' HH:mm");

    @FXML
    public void initialize() {
        loadNotifications();
    }

    private void loadNotifications() {
        notificationsContainer.getChildren().clear();

        try {
            Connection conn = DatabaseConnection.getDBConnection();
            NotificationDao notifDao = new NotificationDao(conn);

            Integer currentUserId = SessionManager.getInstance().getCurrentUserID();
            List<Notification> notifications = notifDao.getUserNotifications(currentUserId);

            if (notifications == null || notifications.isEmpty()) {
                notificationsContainer.getChildren().add(noNotificationsLabel);
            } else {
                noNotificationsLabel.setVisible(false);
                for (Notification notif : notifications) {
                    notificationsContainer.getChildren().add(createNotificationItem(notif));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            notificationsContainer.getChildren().add(new Label("Failed to load notifications."));
        }
    }

    private HBox createNotificationItem(Notification notif) {
        HBox item = new HBox(12);
        item.getStyleClass().add("notification-item");
        item.setAlignment(Pos.CENTER_LEFT);

        // Icon based on topic
        VBox iconBox = new VBox();
        iconBox.setAlignment(Pos.CENTER);
        iconBox.setMinSize(40, 40);
        iconBox.setMaxSize(40, 40);

        String iconText;
        String iconColor;
        String topic = notif.getTopic() != null ? notif.getTopic().toLowerCase() : "";

        if (topic.contains("like")) {
            iconText = "♥";
            iconColor = "#FA3E3E";
        } else if (topic.contains("comment")) {
            iconText = "💬";
            iconColor = "#1877F2";
        } else if (topic.contains("friend")) {
            iconText = "👥";
            iconColor = "#42B72A";
        } else {
            iconText = "🔔";
            iconColor = "#FFA500";
        }

        iconBox.setStyle("-fx-background-color: " + iconColor + "22; -fx-background-radius: 20;");
        Label iconLabel = new Label(iconText);
        iconLabel.setStyle("-fx-font-size: 18px;");
        iconBox.getChildren().add(iconLabel);

        // Text and date
        VBox textBox = new VBox(3);
        Label textLabel = new Label(notif.getNotificationText() != null ?
                notif.getNotificationText() : "New notification");
        textLabel.getStyleClass().add("notification-text");
        textLabel.setWrapText(true);

        Label dateLabel = new Label(notif.getNotificationDate() != null ?
                dateFormat.format(notif.getNotificationDate()) : "");
        dateLabel.getStyleClass().add("notification-date");

        textBox.getChildren().addAll(textLabel, dateLabel);

        item.getChildren().addAll(iconBox, textBox);
        return item;
    }
}
