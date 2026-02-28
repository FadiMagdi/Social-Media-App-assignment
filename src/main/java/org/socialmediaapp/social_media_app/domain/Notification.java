package org.socialmediaapp.social_media_app.domain;

import java.util.Date;

/**
 * A notification about post activity (like, comment, etc).
 */
public class Notification {

    private int id;
    private int senderId;
    private int postId;
    private String type;
    private String text;
    private Date date;

    public Notification() {}

    public Notification(int senderId, int postId, String type, String text, Date date) {
        this.senderId = senderId;
        this.postId = postId;
        this.type = type;
        this.text = text;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
