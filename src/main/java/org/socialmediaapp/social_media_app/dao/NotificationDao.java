package org.socialmediaapp.social_media_app.dao;

import org.socialmediaapp.social_media_app.domain.Notification;
import org.socialmediaapp.social_media_app.domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDao {

    private Connection DBConnection;

    public NotificationDao(Connection DBConnection) {
        this.DBConnection = DBConnection;
    }


    public List<Notification> getUserNotifications(Integer UserID){
        String sql = "select pn.id,pn.post_id,pn.sender_id , pn.notification_type,pn.notification_text,pn.notification_date from post_notifications pn\n" +
                "join app_user_post_notifications aupn\n" +
                "where aupn.receiver_id = "+ String.valueOf(UserID);
        List<Notification> NotificationList = new ArrayList<Notification>();
        try(
                Statement stmt = this.DBConnection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ){
            while(rs.next()){
                Notification explored = new Notification(rs.getInt("sender_id"),rs.getString("notification_type"),rs.getDate("notification_date"),rs.getString("notfication_text"),rs.getInt("post_id"));

                NotificationList.add(explored);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return NotificationList;
    }

    public boolean addNotification(Notification not , List<User> targetAudience){
 boolean success=false;
  // adding the notification to post_notification table
        String sql = "INSERT INTO post_notifications (post_id,sender_id,notification_text,notification_type,notification_date) values (?,?,?,?,?)";

        try (
                PreparedStatement stmt = this.DBConnection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

        )
        {
            stmt.setInt(1,not.getNotificationID());
            stmt.setInt(2,not.getSourceUserID());
            stmt.setString(3,not.getNotificationText());
            stmt.setString(4,not.getTopic());
            stmt.setDate(5, (Date) not.getNotificationDate());

            int rows = stmt.executeUpdate();

            if(rows>0) {
                success = true;
                System.out.println("Notification added successfully");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
            return success;
        }

  // adding the post notification mapping

        String mapsql = "INSERT INTO app_user_post_notifications (notification_id,receiver_id) values (?,?)";

        try (
                PreparedStatement stmt = this.DBConnection.prepareStatement(mapsql,Statement.RETURN_GENERATED_KEYS);

        )
        {
            for(User targetuser : targetAudience) {
                stmt.setInt(1, not.getNotificationID());
                stmt.setInt(2, targetuser.getUserID());
stmt.addBatch();
            }

            stmt.executeBatch();
            success = true;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            success = false;

        }



        return success;
    }
}
