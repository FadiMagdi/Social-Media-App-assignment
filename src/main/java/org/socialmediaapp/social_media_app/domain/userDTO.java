package org.socialmediaapp.social_media_app.domain;

import java.util.List;

public class userDTO {

    private Integer userID;
    private String userName;
    private Profile userProfile;
    private List<Post> userPosts; // to be added in service layer

    public userDTO(Integer userID, String userName, Profile userProfile) {
        this.userID = userID;
        this.userName = userName;
        this.userProfile = userProfile;
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

    public Profile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(Profile userProfile) {
        this.userProfile = userProfile;
    }

    public List<Post> getUserPosts() {
        return userPosts;
    }

    public void setUserPosts(List<Post> userPosts) {
        this.userPosts = userPosts;
    }
}
