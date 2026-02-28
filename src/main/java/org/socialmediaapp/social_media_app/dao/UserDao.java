package org.socialmediaapp.social_media_app.dao;

import org.socialmediaapp.social_media_app.domain.Profile;
import org.socialmediaapp.social_media_app.domain.User;
import org.socialmediaapp.social_media_app.domain.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for user and profile operations.
 */
public class UserDao {

    private final Connection conn;

    public UserDao(Connection conn) {
        this.conn = conn;
    }

    // ── Read Operations ──

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM app_user WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("age")
                );
                user.setProfile(getProfileByUserId(user.getId()));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by email: " + e.getMessage());
        }
        return null;
    }

    public UserDTO getUserDTOById(int userId) {
        String sql = "SELECT id, name FROM app_user WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Profile profile = getProfileByUserId(userId);
                return new UserDTO(rs.getInt("id"), rs.getString("name"), profile);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user DTO: " + e.getMessage());
        }
        return null;
    }

    public Profile getProfileByUserId(int userId) {
        String sql = "SELECT * FROM profile WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Profile(rs.getInt("id"), rs.getString("bio"), rs.getString("image_path"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting profile: " + e.getMessage());
        }
        return null;
    }

    public List<UserDTO> searchUsers(String query) {
        List<UserDTO> results = new ArrayList<>();
        String sql = "SELECT id, name FROM app_user WHERE name LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Profile profile = getProfileByUserId(rs.getInt("id"));
                results.add(new UserDTO(rs.getInt("id"), rs.getString("name"), profile));
            }
        } catch (SQLException e) {
            System.err.println("Error searching users: " + e.getMessage());
        }
        return results;
    }

    // ── Write Operations ──

    public boolean createUser(String email, String password, String name, int age) {
        String sql = "INSERT INTO app_user (email, password, name, age) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setInt(4, age);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    int userId = keys.getInt(1);
                    createProfile(userId, "", "");
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
        return false;
    }

    public boolean createProfile(int userId, String bio, String imagePath) {
        String sql = "INSERT INTO profile (user_id, bio, image_path) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, bio);
            stmt.setString(3, imagePath);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating profile: " + e.getMessage());
        }
        return false;
    }

    public boolean updateProfile(int userId, String name, String bio, String imagePath) {
        // Update user name
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE app_user SET name = ? WHERE id = ?")) {
            stmt.setString(1, name);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating name: " + e.getMessage());
            return false;
        }

        // Update or insert profile
        try (PreparedStatement check = conn.prepareStatement("SELECT COUNT(*) FROM profile WHERE user_id = ?")) {
            check.setInt(1, userId);
            ResultSet rs = check.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Update existing profile
                try (PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE profile SET bio = ?, image_path = ? WHERE user_id = ?")) {
                    stmt.setString(1, bio);
                    stmt.setString(2, imagePath);
                    stmt.setInt(3, userId);
                    stmt.executeUpdate();
                }
            } else {
                createProfile(userId, bio, imagePath);
            }
        } catch (SQLException e) {
            System.err.println("Error updating profile: " + e.getMessage());
            return false;
        }
        return true;
    }
}
