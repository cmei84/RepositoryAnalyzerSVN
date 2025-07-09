/**
 * 
 */
package test;

import java.sql.*;

/**
 * @author Christian
 * 
 */
public class GetMantisData {

    public static void main(String[] args) {

        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql:///mantis","root", "");

            Statement statement = conn.createStatement();
            ResultSet rs; 
            
            rs=statement.executeQuery("SELECT table_name FROM INFORMATION_SCHEMA.TABLES");
            
            while (rs.next()) {
                System.out.println(rs.getString(1));                            
            }
            
            statement.close();
            conn.close();

            // connect to the 'sample' database

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
