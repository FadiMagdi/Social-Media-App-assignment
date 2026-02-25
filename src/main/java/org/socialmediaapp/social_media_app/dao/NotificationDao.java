package org.socialmediaapp.social_media_app.dao;

import org.socialmediaapp.social_media_app.domain.Notification;
import org.socialmediaapp.social_media_app.domain.User;

import java.sql.Connection;
import java.util.List;

public class NotificationDao {

    private Connection DBConnection;

    public NotificationDao(Connection DBConnection) {
        this.DBConnection = DBConnection;
    }

    public void addNotification(Notification not , List<User> targetAudience){

    }
}
