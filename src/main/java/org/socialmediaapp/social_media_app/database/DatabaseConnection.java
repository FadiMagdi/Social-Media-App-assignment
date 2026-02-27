package org.socialmediaapp.social_media_app.database;
import java.sql.*;
public class DatabaseConnection {
    // implementing database connection using singleton design pattern
    private static String constring = "jdbc:mysql://localhost:3306/social_media_app_db";
    private static String username = "root";
    private static String password = "root";


    private static Connection connection = null;





    public static Connection getDBConnection(){

        if( connection == null){
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection= DriverManager.getConnection(constring , username, password);

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return connection;
    }


    public static void CloseConnection(){
        try{
            if(connection != null){
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Database Connection failed to Terminate");
        }
    }

}
