package org.socialmediaapp.social_media_app.service;

import org.socialmediaapp.social_media_app.dao.NotificationDao;
import org.socialmediaapp.social_media_app.dao.PostDao;
import org.socialmediaapp.social_media_app.dao.UserDao;
import org.socialmediaapp.social_media_app.database.DatabaseConnection;
import org.socialmediaapp.social_media_app.domain.Post;
import org.socialmediaapp.social_media_app.domain.UserDTO;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * Service layer for post-related business logic.
 * Handles: creating posts, news feed, likes, comments, search.
 */
public class PostService {

    private final PostDao postDao;
    private final UserDao userDao;
    private final NotificationDao notificationDao;

    public PostService() {
        Connection conn = DatabaseConnection.getDBConnection();
        this.userDao = new UserDao(conn);
        this.postDao = new PostDao(conn, userDao);
        this.notificationDao = new NotificationDao(conn);
    }

    /** Create a new post for the given user. */
    public boolean createPost(int userId, String text, String imagePath, String privacy) {
        UserDTO author = userDao.getUserDTOById(userId);
        if (author == null) return false;

        Post post = new Post(author, text, imagePath, new Date(), privacy);
        return postDao.createPost(post);
    }

    /** Get all posts visible to the user (own + friends + public). */
    public List<Post> getFeedPosts(int userId) {
        return postDao.getFeedPosts(userId);
    }

    /** Get posts created by a specific user. */
    public List<Post> getUserPosts(int userId) {
        return postDao.getPostsByUserId(userId);
    }

    /** Like a post. */
    public boolean likePost(int postId, int userId) {
        boolean success = postDao.addLike(postId, userId);
        if (success) {
            int ownerId = notificationDao.getPostOwnerId(postId);
            UserDTO sender = userDao.getUserDTOById(userId);
            if (sender != null) {
                notificationDao.createNotification(userId, ownerId, postId, "like",
                        sender.name() + " liked your post");
            }
        }
        return success;
    }

    /** Add a comment to a post. */
    public boolean addComment(int postId, int userId, String text) {
        boolean success = postDao.addComment(postId, userId, text);
        if (success) {
            int ownerId = notificationDao.getPostOwnerId(postId);
            UserDTO sender = userDao.getUserDTOById(userId);
            if (sender != null) {
                notificationDao.createNotification(userId, ownerId, postId, "comment",
                        sender.name() + " commented on your post");
            }
        }
        return success;
    }

    /** Search posts by text content. */
    public List<Post> searchPosts(String query) {
        return postDao.searchPosts(query);
    }
}
