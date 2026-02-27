package org.socialmediaapp.social_media_app.util;

import org.socialmediaapp.social_media_app.domain.User;

/**
 * Manages the current logged-in user session.
 * Singleton pattern to keep track of who is logged in.
 */
public class SessionManager {

    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void logout() {
        this.currentUser = null;
    }

    public Integer getCurrentUserID() {
        if (currentUser != null) {
            return currentUser.getUserID();
        }
        return null;
    }
}
