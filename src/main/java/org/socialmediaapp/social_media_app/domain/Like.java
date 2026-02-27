package org.socialmediaapp.social_media_app.domain;

public class Like {

    public String userName;
    private Integer userID;

    public Like(String userName, Integer userID) {
        this.userName = userName;
        this.userID = userID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
