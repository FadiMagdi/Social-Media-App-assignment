package org.socialmediaapp.social_media_app.dao;

import org.socialmediaapp.social_media_app.domain.friendRequest;

import java.sql.*;

public class FriendSystemDao {

    private Connection DBConnection;


    public FriendSystemDao(Connection DBConnection) {
        this.DBConnection = DBConnection;
    }

    public boolean createFriendRequest(friendRequest friendReq){
boolean success = false;
        String sql = "INSERT INTO friendRequest (sender_id,receiver_id,request_date) values (?,?,?)";

        try (
                PreparedStatement stmt = this.DBConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        )
        {
            stmt.setInt(1,friendReq.getSourceUser().userID());
            stmt.setInt(2,friendReq.getReceiverID());
            stmt.setDate(3, (Date) friendReq.getSendDate());


            int rows = stmt.executeUpdate();

            if(rows>0) {
                success = true;
                System.out.println("Friend Request added successfully");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
            return success;
        }

return success;
    }

    public boolean acceptFriendRequest(friendRequest friendReq){

        // adding to friends table
        boolean success = false;
        String sql = "INSERT INTO friends (user1_id,user2_id,friendship_date) values (?,?,?)";

        try (
                PreparedStatement stmt = this.DBConnection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

        )
        {
            stmt.setInt(1,friendReq.getSourceUser().userID());
            stmt.setInt(2,friendReq.getReceiverID());
            stmt.setDate(3, (Date) new java.util.Date());


            int rows = stmt.executeUpdate();

            if(rows>0) {
                success = true;
                System.out.println("friendship added successfully");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
            return success;
        }

        // delete the friend request

        String delsql = "Delete  from  table friend_request where id = ?";

        try(
                PreparedStatement stmt = this.DBConnection.prepareStatement(delsql);
        ){
            stmt.setInt(1,friendReq.getRequestID());
            int rows = stmt.executeUpdate();
            if(rows >0){

                System.out.println("student deleted successfully");
            }
        } catch (SQLException e) {
            System.out.println("Could not delete friend request");
        }

return success;
    }


    public boolean ignoreFriendRequest(friendRequest friendReq){
        boolean success = false;

        String delsql = "Delete  from  table friend_request where id = ?";

        try(
                PreparedStatement stmt = this.DBConnection.prepareStatement(delsql);
        ){
            stmt.setInt(1,friendReq.getRequestID());
            int rows = stmt.executeUpdate();
            if(rows >0){
success = true;
                System.out.println("student deleted successfully");
            }
        } catch (SQLException e) {
            System.out.println("Could not delete friend request");
        }


        return success;
    }



}
