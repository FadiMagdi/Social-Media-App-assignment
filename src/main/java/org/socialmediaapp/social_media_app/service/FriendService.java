package org.socialmediaapp.social_media_app.service;

import org.socialmediaapp.social_media_app.dao.FriendDao;
import org.socialmediaapp.social_media_app.dao.NotificationDao;
import org.socialmediaapp.social_media_app.dao.UserDao;
import org.socialmediaapp.social_media_app.database.DatabaseConnection;
import org.socialmediaapp.social_media_app.domain.FriendRequest;
import org.socialmediaapp.social_media_app.domain.UserDTO;

import java.sql.Connection;
import java.util.List;

/**
 * Service layer for friend-related business logic.
 * Handles: sending/accepting/ignoring requests, friends list.
 */
public class FriendService {

    private final FriendDao friendDao;
    private final UserDao userDao;
    private final NotificationDao notificationDao;

    public FriendService() {
        Connection conn = DatabaseConnection.getDBConnection();
        this.userDao = new UserDao(conn);
        this.friendDao = new FriendDao(conn, userDao);
        this.notificationDao = new NotificationDao(conn);
    }

    /** Send a friend request from sender to receiver. */
    public boolean sendFriendRequest(int senderId, int receiverId) {
        boolean success = friendDao.sendFriendRequest(senderId, receiverId);
        if (success) {
            UserDTO sender = userDao.getUserDTOById(senderId);
            if (sender != null) {
                notificationDao.createNotification(senderId, receiverId, 0, "friend",
                        sender.name() + " sent you a friend request");
            }
        }
        return success;
    }

    /** Accept a friend request (adds friendship in both directions). */
    public boolean acceptFriendRequest(FriendRequest request) {
        boolean success = friendDao.acceptFriendRequest(request);
        if (success) {
            UserDTO accepter = userDao.getUserDTOById(request.getReceiverId());
            if (accepter != null) {
                notificationDao.createNotification(request.getReceiverId(), request.getSender().id(), 0, "friend",
                        accepter.name() + " accepted your friend request");
            }
        }
        return success;
    }

    /** Ignore/delete a friend request. */
    public boolean ignoreFriendRequest(int requestId) {
        return friendDao.deleteRequest(requestId);
    }

    /** Get all friends of a user. */
    public List<UserDTO> getFriends(int userId) {
        return friendDao.getFriends(userId);
    }

    /** Get pending friend requests for a user. */
    public List<FriendRequest> getFriendRequests(int userId) {
        return friendDao.getFriendRequests(userId);
    }
}
