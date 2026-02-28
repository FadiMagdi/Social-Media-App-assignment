package org.socialmediaapp.social_media_app.service;

import org.socialmediaapp.social_media_app.dao.FriendDao;
import org.socialmediaapp.social_media_app.dao.UserDao;
import org.socialmediaapp.social_media_app.database.DatabaseConnection;
import org.socialmediaapp.social_media_app.domain.User;
import org.socialmediaapp.social_media_app.domain.UserDTO;

import java.sql.Connection;
import java.util.List;

/**
 * Service layer for user-related business logic.
 * Handles: login, registration, profile management, search.
 */
public class UserService {

    private final UserDao userDao;
    private final FriendDao friendDao;

    public UserService() {
        Connection conn = DatabaseConnection.getDBConnection();
        this.userDao = new UserDao(conn);
        this.friendDao = new FriendDao(conn, userDao);
    }

    /** Authenticate user by email and password. Returns User if valid, null otherwise. */
    public User login(String email, String password) {
        User user = userDao.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    /** Register a new user. Returns true if successful. */
    public boolean register(String name, String email, String password, int age) {
        return userDao.createUser(email, password, name, age);
    }

    /** Get user profile info as a DTO. */
    public UserDTO getUserProfile(int userId) {
        return userDao.getUserDTOById(userId);
    }

    /** Update user's name and profile info. */
    public boolean updateProfile(int userId, String name, String bio, String imagePath) {
        return userDao.updateProfile(userId, name, bio, imagePath);
    }

    /** Get number of friends for a user. */
    public int getFriendsCount(int userId) {
        return friendDao.getFriends(userId).size();
    }

    /** Search users by name. */
    public List<UserDTO> searchUsers(String query) {
        return userDao.searchUsers(query);
    }
}
