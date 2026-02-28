package org.socialmediaapp.social_media_app.domain;

import java.util.Date;

public class FriendRequest {

    private int id;
    private UserDTO sender;
    private int receiverId;
    private Date date;

    public FriendRequest(UserDTO sender, int receiverId, Date date) {
        this.sender = sender;
        this.receiverId = receiverId;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public UserDTO getSender() { return sender; }
    public void setSender(UserDTO sender) { this.sender = sender; }

    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
