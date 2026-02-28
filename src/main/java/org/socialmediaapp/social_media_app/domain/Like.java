package org.socialmediaapp.social_media_app.domain;

/**
 * A like on a post.
 */
public class Like {

    private UserDTO user;

    public Like() {}

    public Like(UserDTO user) {
        this.user = user;
    }

    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }
}
