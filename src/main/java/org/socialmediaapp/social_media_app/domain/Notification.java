package org.socialmediaapp.social_media_app.domain;

import java.util.Date;

public class Notification {
    private User sourceUser;
    private String topic;
    private Date notificationDate;
    private String notificationText;
    private Integer notificationID;
    public Notification(User sourceUser, String topic, Date notificationDate, String notificationText) {
        this.sourceUser = sourceUser;
        this.topic = topic;
        this.notificationDate = notificationDate;
        this.notificationText = notificationText;
    }

    public Notification(User sourceUser, String topic, Date notificationDate, String notificationText, Integer notificationID) {
        this.sourceUser = sourceUser;
        this.topic = topic;
        this.notificationDate = notificationDate;
        this.notificationText = notificationText;
        this.notificationID = notificationID;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public User getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(User sourceUser) {
        this.sourceUser = sourceUser;
    }
}
