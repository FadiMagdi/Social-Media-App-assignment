package org.socialmediaapp.social_media_app.dao;

import org.socialmediaapp.social_media_app.domain.FriendRequest;
import org.socialmediaapp.social_media_app.domain.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for friend system (requests, friends list).
 */
public class FriendDao {

    private final Connection conn;
    private final UserDao userDao;

    public FriendDao(Connection conn, UserDao userDao) {
        this.conn = conn;
        this.userDao = userDao;
    }

    // ── Friend Requests ──

    public boolean sendFriendRequest(int senderId, int receiverId) {
        String sql = "INSERT INTO friend_request (sender_id, receiver_id, request_date) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error sending friend request: " + e.getMessage());
        }
        return false;
    }

    public boolean acceptFriendRequest(FriendRequest request) {
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        String sql = "INSERT INTO friends (user1_id, user2_id, friendship_date) VALUES (?, ?, ?)";

        try {
            // Add friendship in both directions
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, request.getSender().id());
                stmt.setInt(2, request.getReceiverId());
                stmt.setDate(3, today);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, request.getReceiverId());
                stmt.setInt(2, request.getSender().id());
                stmt.setDate(3, today);
                stmt.executeUpdate();
            }

            // Delete the friend request
            return deleteRequest(request.getId());

        } catch (SQLException e) {
            System.err.println("Error accepting friend request: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteRequest(int requestId) {
        String sql = "DELETE FROM friend_request WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting friend request: " + e.getMessage());
        }
        return false;
    }

    public List<FriendRequest> getFriendRequests(int userId) {
        List<FriendRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM friend_request WHERE receiver_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserDTO sender = userDao.getUserDTOById(rs.getInt("sender_id"));
                FriendRequest request = new FriendRequest(sender, rs.getInt("receiver_id"), rs.getDate("request_date"));
                request.setId(rs.getInt("id"));
                requests.add(request);
            }
        } catch (SQLException e) {
            System.err.println("Error getting friend requests: " + e.getMessage());
        }
        return requests;
    }

    // ── Friends List ──

    public boolean areFriends(int userId1, int userId2) {
        String sql = "SELECT 1 FROM friends WHERE user1_id = ? AND user2_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId1);
            stmt.setInt(2, userId2);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking friendship: " + e.getMessage());
        }
        return false;
    }

    public boolean hasPendingRequest(int senderId, int receiverId) {
        String sql = "SELECT 1 FROM friend_request WHERE sender_id = ? AND receiver_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking pending request: " + e.getMessage());
        }
        return false;
    }

    public List<UserDTO> getFriends(int userId) {
        List<UserDTO> friends = new ArrayList<>();
        String sql = "SELECT user2_id FROM friends WHERE user1_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserDTO friend = userDao.getUserDTOById(rs.getInt("user2_id"));
                if (friend != null) {
                    friends.add(friend);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting friends: " + e.getMessage());
        }
        return friends;
    }
}
