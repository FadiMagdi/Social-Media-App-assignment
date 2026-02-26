package org.socialmediaapp.social_media_app.domain;

public class Profile {

    public String bio;
    public String imagePath;
    private Integer profileID;

    public Profile(String bio, String imagePath) {
        this.bio = bio;
        this.imagePath = imagePath;
    }

    public Profile(String bio, String imagePath, Integer profileID) {
        this.bio = bio;
        this.imagePath = imagePath;
        this.profileID = profileID;
    }

    public Integer getProfileID() {
        return profileID;
    }

    public void setProfileID(Integer profileID) {
        this.profileID = profileID;
    }
}
