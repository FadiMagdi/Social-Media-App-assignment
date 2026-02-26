package org.socialmediaapp.social_media_app.dao;

import org.socialmediaapp.social_media_app.domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostManagementDao {

private Connection DBConnection;
private UserDao userDao;


    public PostManagementDao(Connection DBConnection , UserDao userDao) {
        this.DBConnection = DBConnection;
        this.userDao = userDao;
    }

    //Likes

    public boolean addLikeToPost(Like newLike,Integer postID) {
        boolean success = false;

        String sql = "INSERT INTO likes (post_id,user_id) values (?,?)";

        try (
                PreparedStatement stmt = this.DBConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ) {
            stmt.setInt(1, postID);
            stmt.setInt(2, newLike.userMadeLike.userID());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                success = true;
                System.out.println("Like added successfully");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
            return success;
        }

        return success;
    }

    public List<Like> getPostLikes(Integer postID){
        String sql = "select * from likes\n" +
                "where post_id = "+ String.valueOf(postID);
        List<Like> LikesList = new ArrayList<Like>();
        try(
                Statement stmt = this.DBConnection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ){
            while(rs.next()){
                userDTO userMadeLike = this.userDao.getUserDTOByID(rs.getInt("user_id"));

                LikesList.add(new Like(userMadeLike));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
return LikesList;
    }

    // comments

    public boolean addCommentToPost(Comment addedComment, Integer postID) {
        boolean success = false;
        // adding the notification to post_notification table
        String sql = "INSERT INTO comments (post_id,user_id,comment_text,comment_date) values (?,?,?,?)";

        try (
                PreparedStatement stmt = this.DBConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ) {
            stmt.setInt(1, postID);
            stmt.setInt(2, addedComment.userMadeComment.userID());
            stmt.setString(3, addedComment.commentText);
            stmt.setDate(4, (Date) addedComment.commentDate);


            int rows = stmt.executeUpdate();

            if (rows > 0) {
                success = true;
                System.out.println("Comment added to post successfully");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
            return success;
        }
        return success;
    }


    public List<Comment> getPostComments(Integer postID){
        String sql = "select * from Comments\n" +
                "where post_id = "+ String.valueOf(postID);
        List<Comment> CommentList = new ArrayList<Comment>();
        try(
                Statement stmt = this.DBConnection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ){
            while(rs.next()){
                userDTO userMadeComment = this.userDao.getUserDTOByID(rs.getInt("user_id"));

                CommentList.add(new Comment(userMadeComment,rs.getString("comment_text"),(java.util.Date)rs.getDate("comment_date"),rs.getInt("id")));

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return CommentList;
    }


    // Posts
    public boolean addPosts(Post newPost) {
        boolean success = false;
        // adding the notification to post_notification table
        String sql = "INSERT INTO posts (user_id,post_text,post_image_path,post_date,privacy) values (?,?,?,?,?)";

        try (
                PreparedStatement stmt = this.DBConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ) {
            stmt.setInt(1, newPost.postMaker.userID());
            stmt.setString(2, newPost.postText);
            stmt.setString(3, newPost.getImage_path());
            stmt.setString(5, newPost.getPrivacy());
            stmt.setDate(4, (Date) newPost.getPostDate());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                success = true;
                System.out.println("Posts added successfully");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
            return success;
        }
        return success;
    }


    // getting posts of a user
    public List<Post> getPostsByUserID (Integer userID){

        String sql = "\n" +
                "select * from posts\n" +
                "where user_id =  "+ String.valueOf(userID);
        List<Post> PostsList = new ArrayList<Post>();
        try(
                Statement stmt = this.DBConnection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ){
            while(rs.next()){
                userDTO postMaker = this.userDao.getUserDTOByID(rs.getInt("user_post"));
                List<Like> postLikes = this.getPostLikes(rs.getInt("post_id"));
                List<Comment> postComments = this.getPostComments(rs.getInt("post_id"));

               Post exploredPost  = new Post(postMaker,(java.util.Date) rs.getDate("post_date"),rs.getString("post_image_path")
                       ,rs.getInt("post_id"),postLikes,postComments,rs.getString("post_text"),rs.getString("privacy"));
            PostsList.add(exploredPost);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return PostsList;


    }


    // News Feed

    public List<Post> getPostsForUseFeed(Integer userID){

        String sql = "select p.id , p.user_id , p.post_text,p.post_image_path,p.privacy\n" +
                "from posts p\n" +
                "join post_notifications pn on p.id = pn.post_id\n" +
                "join app_user_post_notifications apupn on pn.id = apupn.notification_id\n" +
                "where apupn.receiver_id ="+ String.valueOf(userID);
        List<Post> PostsList = new ArrayList<Post>();
        try(
                Statement stmt = this.DBConnection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ){
            while(rs.next()){
                userDTO postMaker = this.userDao.getUserDTOByID(rs.getInt("user_post"));
                List<Like> postLikes = this.getPostLikes(rs.getInt("post_id"));
                List<Comment> postComments = this.getPostComments(rs.getInt("post_id"));

                Post exploredPost  = new Post(postMaker,(java.util.Date) rs.getDate("post_date"),rs.getString("post_image_path")
                        ,rs.getInt("post_id"),postLikes,postComments,rs.getString("post_text"),rs.getString("privacy"));
                PostsList.add(exploredPost);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return PostsList;


    }

}
