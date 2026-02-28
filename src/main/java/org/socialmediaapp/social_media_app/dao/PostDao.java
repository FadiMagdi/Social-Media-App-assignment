package org.socialmediaapp.social_media_app.dao;

import org.socialmediaapp.social_media_app.domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for post, like, and comment operations.
 */
public class PostDao {

    private final Connection conn;
    private final UserDao userDao;

    public PostDao(Connection conn, UserDao userDao) {
        this.conn = conn;
        this.userDao = userDao;
    }

    // ── Post CRUD ──

    public int createPost(Post post) {
        String sql = "INSERT INTO posts (user_id, post_text, post_image_path, post_date, privacy) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, post.getAuthor().id());
            stmt.setString(2, post.getText());
            stmt.setString(3, post.getImagePath());
            stmt.setDate(4, new java.sql.Date(post.getDate().getTime()));
            stmt.setString(5, post.getPrivacy());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating post: " + e.getMessage());
        }
        return -1;
    }

    public List<Post> getPostsByUserId(int userId) {
        String sql = "SELECT * FROM posts WHERE user_id = ? ORDER BY post_date DESC";
        return queryPosts(sql, userId);
    }

    public List<Post> getFeedPosts(int userId) {
        // Show: own posts + public posts from anyone + friends' posts (public or friends-only)
        String sql = """
            SELECT DISTINCT p.* FROM posts p
            LEFT JOIN friends f ON f.user2_id = p.user_id AND f.user1_id = ?
            WHERE p.user_id = ?
               OR p.privacy = 'Public'
               OR (f.user1_id IS NOT NULL AND p.privacy = 'Friends Only')
            ORDER BY p.post_date DESC
            """;
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                posts.add(buildPost(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting feed: " + e.getMessage());
        }
        return posts;
    }

    public List<Post> searchPosts(String query) {
        String sql = "SELECT * FROM posts WHERE post_text LIKE ? AND privacy = 'Public' ORDER BY post_date DESC";
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                posts.add(buildPost(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching posts: " + e.getMessage());
        }
        return posts;
    }

    // ── Likes ──

    public boolean addLike(int postId, int userId) {
        String sql = "INSERT INTO likes (post_id, user_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding like: " + e.getMessage());
        }
        return false;
    }

    public List<Like> getPostLikes(int postId) {
        List<Like> likes = new ArrayList<>();
        String sql = "SELECT user_id FROM likes WHERE post_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserDTO user = userDao.getUserDTOById(rs.getInt("user_id"));
                likes.add(new Like(user));
            }
        } catch (SQLException e) {
            System.err.println("Error getting likes: " + e.getMessage());
        }
        return likes;
    }

    // ── Comments ──

    public boolean addComment(int postId, int userId, String text) {
        String sql = "INSERT INTO comments (post_id, user_id, comment_text, comment_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            stmt.setString(3, text);
            stmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding comment: " + e.getMessage());
        }
        return false;
    }

    public List<Comment> getPostComments(int postId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM comments WHERE post_id = ? ORDER BY comment_date";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserDTO author = userDao.getUserDTOById(rs.getInt("user_id"));
                Comment comment = new Comment(author, rs.getString("comment_text"), rs.getDate("comment_date"));
                comment.setId(rs.getInt("id"));
                comments.add(comment);
            }
        } catch (SQLException e) {
            System.err.println("Error getting comments: " + e.getMessage());
        }
        return comments;
    }

    // ── Helpers ──

    private List<Post> queryPosts(String sql, int userId) {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                posts.add(buildPost(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error querying posts: " + e.getMessage());
        }
        return posts;
    }

    private Post buildPost(ResultSet rs) throws SQLException {
        int postId = rs.getInt("id");
        UserDTO author = userDao.getUserDTOById(rs.getInt("user_id"));

        Post post = new Post(
            author,
            rs.getString("post_text"),
            rs.getString("post_image_path"),
            rs.getDate("post_date"),
            rs.getString("privacy")
        );
        post.setId(postId);
        post.setLikes(getPostLikes(postId));
        post.setComments(getPostComments(postId));
        return post;
    }
}
