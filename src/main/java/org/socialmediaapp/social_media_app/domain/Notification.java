package org.socialmediaapp.social_media_app.domain;

import java.util.Date;

public class Notification {
    private Integer sourceUserID;
    private String topic;
    private Date notificationDate;
    private String notificationText;
    private Integer notificationID;
    private Integer postID;

    public Notification(Integer sourceUserID, String topic, Date notificationDate, String notificationText, Integer postID) {
        this.sourceUserID = sourceUserID;
        this.topic = topic;
        this.notificationDate = notificationDate;
        this.notificationText = notificationText;
        this.postID = postID;
    }

    public Notification(Integer sourceUserID, String topic, Date notificationDate, String notificationText, Integer notificationID, Integer postID) {
        this.sourceUserID = sourceUserID;
        this.topic = topic;
        this.notificationDate = notificationDate;
        this.notificationText = notificationText;
        this.notificationID = notificationID;
        this.postID = postID;
    }

    public Integer getPostID() {
        return postID;
    }

    public void setPostID(Integer postID) {
        this.postID = postID;
    }

    public Integer getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(Integer notificationID) {
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

    public Integer getSourceUserID() {
        return sourceUserID;
    }

    public void setSourceUserID(Integer sourceUserID) {
        this.sourceUserID = sourceUserID;
    }
}
