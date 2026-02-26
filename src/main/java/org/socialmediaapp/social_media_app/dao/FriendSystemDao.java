package org.socialmediaapp.social_media_app.dao;

import org.socialmediaapp.social_media_app.domain.friendRequest;
import org.socialmediaapp.social_media_app.domain.userDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendSystemDao {

    private Connection DBConnection;
private  UserDao userDao;


// side not unfriend option not implemented



    public FriendSystemDao(Connection DBConnection , UserDao userDao) {
        this.DBConnection = DBConnection;
        this.userDao=userDao;
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

        // adding to friends table in two
        boolean success = false;
        String oneWaySql = "INSERT INTO friends (user1_id,user2_id,friendship_date) values (?,?,?)";
        String reverseSql = "INSERT INTO friends (user2_id,user1_id,friendship_date) values (?,?,?)";

        try (
                PreparedStatement oneWayStmt = this.DBConnection.prepareStatement(oneWaySql,Statement.RETURN_GENERATED_KEYS);
                PreparedStatement reverseStmt = this.DBConnection.prepareStatement(oneWaySql,Statement.RETURN_GENERATED_KEYS);

        )
        {
            oneWayStmt.setInt(1,friendReq.getSourceUser().userID());
            oneWayStmt.setInt(2,friendReq.getReceiverID());
            oneWayStmt.setDate(3, (Date) new java.util.Date());

            reverseStmt.setInt(2,friendReq.getSourceUser().userID());
            reverseStmt.setInt(1,friendReq.getReceiverID());
            reverseStmt.setDate(3, (Date) new java.util.Date());


            int oneWayRows = oneWayStmt.executeUpdate();
            int reverseRows = reverseStmt.executeUpdate();

            if((oneWayRows>0) && (reverseRows >0) ) {
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


 public List<friendRequest> getUserFriendRequests(Integer userID){
     String sql = "select fr.id , fr.sender_id,fr.receiver_id,fr.request_date\n" +
             "from friend_request fr\n" +
             "where fr.receiver_id = "+ String.valueOf(userID) ;
     List<friendRequest> friendRequestsList = new ArrayList<friendRequest>();
     try(
             Statement stmt = this.DBConnection.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
     ){
         while(rs.next()){
             userDTO senderUser = this.userDao.getUserDTOByID(rs.getInt("sender_id"));

             friendRequest fReq = new friendRequest(senderUser,(java.util.Date) rs.getDate("request_date"),rs.getInt("id"),rs.getInt("receiver_id"));

             friendRequestsList.add(fReq);
         }

     } catch (SQLException e) {
         throw new RuntimeException(e);
     }
return friendRequestsList;
 }

     public List<userDTO> getUserFriends(Integer userID){
         String sql = "select apu.id,apu.name\n" +
                 "from app_user apu\n" +
                 "join friends fds on fds.user2_id = apu.id\n" +
                 "where fds.user1_id = "+ String.valueOf(userID);
         List<userDTO> friendsList = new ArrayList<userDTO>();
         try(
                 Statement stmt = this.DBConnection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql);
         ){
             while(rs.next()){
                 userDTO friend = this.userDao.getUserDTOByID(rs.getInt("id"));
                 friendsList.add(friend);
             }

         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
return friendsList;
     }


}
