package org.socialmediaapp.social_media_app.service;

import org.socialmediaapp.social_media_app.dao.NotificationDao;
import org.socialmediaapp.social_media_app.database.DatabaseConnection;
import org.socialmediaapp.social_media_app.domain.Notification;

import java.sql.Connection;
import java.util.List;

public class NotificationService {

    private NotificationDao notificationDao;

    public NotificationService() {
        Connection conn = DatabaseConnection.getDBConnection();
        this.notificationDao = new NotificationDao(conn);
    }

    public void createNotification(int senderId, int receiverId, int postId, String type, String text) {
        this.notificationDao.createNotification(senderId, receiverId, postId, type, text);
    }

    public int getPostOwnerId(int postId) {
        return this.notificationDao.getPostOwnerId(postId);
    }

    public List<Notification> getNotifications(int userId) {
        return this.notificationDao.getNotifications(userId);
    }
}