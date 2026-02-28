package org.socialmediaapp.social_media_app.dao;

import org.socialmediaapp.social_media_app.domain.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for notification operations.
 */
public class NotificationDao {

    private final Connection conn;

    public NotificationDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Create a notification and link it to the receiver.
     * @param senderId  who triggered the action
     * @param receiverId who should see the notification
     * @param postId    related post (0 if not post-related)
     * @param type      like, comment, friend
     * @param text      notification message
     */
    public void createNotification(int senderId, int receiverId, int postId, String type, String text) {
        // Don't notify yourself
        if (senderId == receiverId) return;

        String insertNotif = "INSERT INTO post_notifications (sender_id, post_id, notification_type, notification_text, notification_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertNotif, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, senderId);
            if (postId > 0) {
                stmt.setInt(2, postId);
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            stmt.setString(3, type);
            stmt.setString(4, text);
            stmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int notifId = keys.getInt(1);
                // Link notification to receiver
                String linkSql = "INSERT INTO app_user_post_notifications (receiver_id, notification_id) VALUES (?, ?)";
                try (PreparedStatement linkStmt = conn.prepareStatement(linkSql)) {
                    linkStmt.setInt(1, receiverId);
                    linkStmt.setInt(2, notifId);
                    linkStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating notification: " + e.getMessage());
        }
    }

    /** Get post owner user_id. */
    public int getPostOwnerId(int postId) {
        String sql = "SELECT user_id FROM posts WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("user_id");
        } catch (SQLException e) {
            System.err.println("Error getting post owner: " + e.getMessage());
        }
        return -1;
    }

    public List<Notification> getNotifications(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = """
            SELECT pn.* FROM post_notifications pn
            JOIN app_user_post_notifications aupn ON pn.id = aupn.notification_id
            WHERE aupn.receiver_id = ?
            ORDER BY pn.notification_date DESC
            """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Notification notif = new Notification(
                    rs.getInt("sender_id"),
                    rs.getInt("post_id"),
                    rs.getString("notification_type"),
                    rs.getString("notification_text"),
                    rs.getDate("notification_date")
                );
                notif.setId(rs.getInt("id"));
                notifications.add(notif);
            }
        } catch (SQLException e) {
            System.err.println("Error getting notifications: " + e.getMessage());
        }
        return notifications;
    }
}
