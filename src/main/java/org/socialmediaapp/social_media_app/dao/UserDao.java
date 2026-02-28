package org.socialmediaapp.social_media_app.dao;

import org.socialmediaapp.social_media_app.domain.Notification;
import org.socialmediaapp.social_media_app.domain.Profile;
import org.socialmediaapp.social_media_app.domain.User;
import org.socialmediaapp.social_media_app.domain.userDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private Connection DBConnection;


    public UserDao(Connection DBConnection) {
        this.DBConnection = DBConnection;
    }

    // retrieving userDTOs

    public userDTO getUserDTOByID(Integer userID){
userDTO targetUser = null;
        //getting user profile
        Profile userProfile = null;
        String sql = "select * from profile where user_id = "+ String.valueOf(userID);

        try(
                Statement stmt = this.DBConnection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ){

                Profile checkProfile = new Profile(rs.getString("bio"), rs.getString("image_path") ,rs.getInt("id")) ;

                userProfile = checkProfile;


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;

        }





//getting the user himself
        String usersql = "select id, name from app_user where id = "+ String.valueOf(userID);

        try(
                Statement stmt = this.DBConnection.createStatement();
                ResultSet rs = stmt.executeQuery(usersql);
        ){

               targetUser = new userDTO(rs.getInt("id"),rs.getString("name"),userProfile);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
return targetUser;
    }


 public boolean createUser(String email, String password, String userName, int age,String bio,String image_path){

        // create the user
     boolean success = false;
     Integer userID = -1;
     String sql = "INSERT INTO app_user (age,name,email,password) values (?,?,?,?)";

     try (
             PreparedStatement stmt = this.DBConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

     )
     {
         stmt.setInt(1,age);
         stmt.setString(2,userName);
         stmt.setString(3, email);
         stmt.setString(4,password);


         int rows = stmt.executeUpdate();

         if(rows>0) {
             success = true;
             System.out.println("user added successfully");
         }

         ResultSet inrs = stmt.getGeneratedKeys();

         if(inrs.next()){
             userID = inrs.getInt(1);
         }


     } catch (SQLException e) {
         System.out.println(e.getMessage());
         success = false;
         return success;
     }

     boolean addingProfile = false;

     if(userID != -1){
         addingProfile = createProfile(userID,bio,image_path);
     }else{
         System.out.println("Could not make user profile");
         return false;
     }

     return success && addingProfile ;



 }


 // should be update profile
 public boolean createProfile(Integer userID , String bio , String image_Path){
     // creating the profile
boolean success = false;

     String pfsql = "INSERT INTO profile (user_id,bio,image_path) values (?,?,?)";

     try (
             PreparedStatement pfstmt = this.DBConnection.prepareStatement(pfsql, Statement.RETURN_GENERATED_KEYS);

     )
     {
         pfstmt.setInt(1,userID);
         pfstmt.setString(2,bio);
         pfstmt.setString(3, image_Path );


         int rows = pfstmt.executeUpdate();

         if(rows>0) {
             success = true;
             System.out.println("Profile added successfully");
         }
     } catch (SQLException e) {
         System.out.println(e.getMessage());
         success = false;
         return success;
     }


     return success;


 }

 public User getUserByEmail(String Email){
     User targetUser = null;
     //getting the user himself
     String usersql = "select id, name from app_user where email = "+ Email;

     try(
             Statement stmt = this.DBConnection.createStatement();
             ResultSet rs = stmt.executeQuery(usersql);
     ){

         targetUser = new User(rs.getInt("id"),rs.getString("email"),rs.getString("password"),rs.getString("name"),rs.getInt("age"));


     } catch (SQLException e) {
         throw new RuntimeException(e);
     }




     //getting user profile
     Profile userProfile = null;
     String sql = "select * from profile where user_id = "+ String.valueOf(targetUser.getUserID());

     try(
             Statement stmt = this.DBConnection.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
     ){

         Profile checkProfile = new Profile(rs.getString("bio"), rs.getString("image_path") ,rs.getInt("id")) ;

         targetUser.setUserProfile(checkProfile);


     } catch (SQLException e) {
         System.out.println(e.getMessage());
         return null;

     }


     return targetUser;
 }

    public List<userDTO> getUserFriends(Integer userID) {
        String sql = "select apu.id,apu.name\n" +
                "from app_user apu\n" +
                "join friends fds on fds.user2_id = apu.id\n" +
                "where fds.user1_id = " + String.valueOf(userID);
        List<userDTO> friendsList = new ArrayList<userDTO>();
        try (
                Statement stmt = this.DBConnection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ) {
            while (rs.next()) {
                userDTO friend = this.getUserDTOByID(rs.getInt("id"));
                friendsList.add(friend);


            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return friendsList;
    }







}
