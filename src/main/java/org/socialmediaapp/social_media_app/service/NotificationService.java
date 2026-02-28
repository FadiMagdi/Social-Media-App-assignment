package org.socialmediaapp.social_media_app.service;

import org.socialmediaapp.social_media_app.dao.NotificationDao;
import org.socialmediaapp.social_media_app.domain.Notification;
import org.socialmediaapp.social_media_app.domain.User;

import java.util.Date;
import java.util.List;

public class NotificationService {

    private NotificationDao notificationDao;


    public NotificationService(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }


    public List<Notification> getUserNotifications(Integer UserID){
        return this.notificationDao.getUserNotifications(UserID);
    }

    public boolean addNotification(Integer SourceUserID , String NotificationText , String NotificationTopic,Integer PostID , List<User> targetAudience){

        Notification newNotification  = new Notification(SourceUserID,NotificationTopic,new Date() , NotificationText, PostID);

        return this.notificationDao.addNotification(newNotification,targetAudience);



    }
}
