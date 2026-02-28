package org.socialmediaapp.social_media_app.domain;

/**
 * User profile with bio and image.
 */
public class Profile {

    private int id;
    private String bio;
    private String imagePath;

    public Profile() {}

    public Profile(String bio, String imagePath) {
        this.bio = bio;
        this.imagePath = imagePath;
    }

    public Profile(int id, String bio, String imagePath) {
        this.id = id;
        this.bio = bio;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
