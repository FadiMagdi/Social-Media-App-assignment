package org.socialmediaapp.social_media_app.dao;

import org.socialmediaapp.social_media_app.domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostManagementDao {

private Connection DBConnection;



    public PostManagementDao(Connection DBConnection) {
        this.DBConnection = DBConnection;

    }

    //Likes

    public boolean addLikeToPost(Like newLike,Integer postID) {
        boolean success = false;

        String sql = "INSERT INTO likes (post_id,user_id) values (?,?)";

        try (
                PreparedStatement stmt = this.DBConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ) {
            stmt.setInt(1, postID);
            stmt.setInt(2, newLike.getUserID());

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
        String sql = "select lk.user_id , apu.name\n" +
                "from likes lk\n" +
                "join app_user apu\n" +
                "on apu.id = lk.user_id\n" +
                "where lk.post_id = "+ String.valueOf(postID);
        List<Like> LikesList = new ArrayList<Like>();
        try(
                Statement stmt = this.DBConnection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ){
            while(rs.next()){


                LikesList.add(new Like(rs.getString("name"), rs.getInt("user_id")));
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
            stmt.setInt(2, addedComment.getUserID());
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
        String sql = "select cm.user_id , cm.comment_text , cm.comment_date , cm.id , cm.post_id , apu.name\n" +
                "from comments cm\n" +
                "join app_user apu\n" +
                "on apu.id = cm.user_id\n" +
                "where cm.post_id =  "+ String.valueOf(postID);
        List<Comment> CommentList = new ArrayList<Comment>();
        try(
                Statement stmt = this.DBConnection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ){
            while(rs.next()){


                CommentList.add(new Comment(rs.getInt("user_id"),rs.getString("name"),rs.getString("comment_text"),(java.util.Date)rs.getDate("comment_date"),rs.getInt("id")));

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return CommentList;
    }


    // Posts
    public boolean addPost(Post newPost) {
        boolean success = false;
        // adding the notification to post_notification table
        String sql = "INSERT INTO posts (user_id,post_text,post_image_path,post_date,privacy) values (?,?,?,?,?)";

        try (
                PreparedStatement stmt = this.DBConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ) {
            stmt.setInt(1, newPost.getPostMakerID());
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

        String sql = "select po.id,po.user_id,po.post_date,po.post_image_path,po.post_text,po.privacy,apu.name\n" +
                "from posts po \n" +
                "join app_user apu\n" +
                "on po.user_id = apu.id\n" +
                "where po.id = "+ String.valueOf(userID);
        List<Post> PostsList = new ArrayList<Post>();
        try(
                Statement stmt = this.DBConnection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ){
            while(rs.next()){

                List<Like> postLikes = this.getPostLikes(rs.getInt("post_id"));
                List<Comment> postComments = this.getPostComments(rs.getInt("post_id"));

               Post exploredPost  = new Post(rs.getInt("user_id"),rs.getString("name"),(java.util.Date) rs.getDate("post_date"),rs.getString("post_image_path")
                       ,rs.getInt("post_id"),postLikes,postComments,rs.getString("post_text"),rs.getString("privacy"));
            PostsList.add(exploredPost);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return PostsList;


    }


    // News Feed

    public List<Post> getPostsForNewsFeed(Integer userID){

        String sql = "select p.id , p.user_id , p.post_text,p.post_image_path,p.privacy,apu.name\n" +
                "from posts p\n" +
                "join post_notifications pn on p.id = pn.post_id\n" +
                "join app_user_post_notifications apupn on pn.id = apupn.notification_id\n" +
                "join app_user apu on apu.id = p.user_id\n" +
                "where apupn.receiver_id ="+ String.valueOf(userID);
        List<Post> PostsList = new ArrayList<Post>();
        try(
                Statement stmt = this.DBConnection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ){
            while(rs.next()){

                List<Like> postLikes = this.getPostLikes(rs.getInt("post_id"));
                List<Comment> postComments = this.getPostComments(rs.getInt("post_id"));

                Post exploredPost  = new Post(rs.getInt("user_id"),rs.getString("name"),(java.util.Date) rs.getDate("post_date"),rs.getString("post_image_path")
                        ,rs.getInt("post_id"),postLikes,postComments,rs.getString("post_text"),rs.getString("privacy"));
                PostsList.add(exploredPost);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return PostsList;


    }


    // getting posts based on privacy
    // to test in mysql
    public List<Post> getPostsByUserIDAndPrivacy (Integer userID,String privacy){

        String sql = "select po.id,po.user_id,po.post_date,po.post_image_path,po.post_text,po.privacy,apu.name\n" +
                "from posts po \n" +
                "join app_user apu\n" +
                "on po.user_id = apu.id\n" +
                "where po.id = "+ String.valueOf(userID) + "and po.privacy = " + privacy   ;
        List<Post> PostsList = new ArrayList<Post>();
        try(
                Statement stmt = this.DBConnection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ){
            while(rs.next()){

                List<Like> postLikes = this.getPostLikes(rs.getInt("post_id"));
                List<Comment> postComments = this.getPostComments(rs.getInt("post_id"));

                Post exploredPost  = new Post(rs.getInt("user_id"),rs.getString("name"),(java.util.Date) rs.getDate("post_date"),rs.getString("post_image_path")
                        ,rs.getInt("post_id"),postLikes,postComments,rs.getString("post_text"),rs.getString("privacy"));
                PostsList.add(exploredPost);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return PostsList;


    }



}
