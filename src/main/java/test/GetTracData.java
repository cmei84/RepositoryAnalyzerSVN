/**
 * 
 */
package test;

import java.sql.*;

/**
 * @author Christian
 * 
 */
public class GetTracData {

    public static void main(String[] args) {

        try {

            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager
                    .getConnection("jdbc:sqlite:C:/Users/Christian/Documents/Trac.Projekte/test/db/trac.db");

            Statement statement = conn.createStatement();
            ResultSet rs;
            
            // statement.execute("CREATE VIEW INFORMATION_SCHEMA_TABLES AS SELECT 'main' AS TABLE_CATALOG, 'sqlite' AS
            // TABLE_SCHEMA, tbl_name AS TABLE_NAME, CASE WHEN type = 'table' THEN 'BASE TABLE' WHEN type = 'view' THEN 'VIEW' END
            // AS TABLE_TYPE, sql AS TABLE_SOURCE FROM sqlite_master WHERE type IN ('table', 'view') AND tbl_name NOT LIKE
            // 'INFORMATION_SCHEMA_%' ORDER BY TABLE_TYPE, TABLE_NAME;");
            // statement.execute("drop view INFORMATION_SCHEMA_TABLES");
            
            rs=statement.executeQuery("SELECT * FROM ticket");
            
            while (rs.next()) {
                System.out.print(rs.getString(1)+" ");
                System.out.print(rs.getString(2)+" ");
                System.out.print(rs.getString(3)+" ");
                System.out.print(rs.getString(4)+" ");
                System.out.print(rs.getString(5)+" ");
                System.out.print(rs.getString(6)+" ");
                System.out.print(rs.getString(7)+" ");
                System.out.print(rs.getString(8)+" ");
                System.out.print(rs.getString(9)+" ");
                System.out.print(rs.getString(10)+" ");
                System.out.print(rs.getString(11)+" ");
                System.out.print(rs.getString(12)+" ");
                System.out.print(rs.getString(13)+" ");
                System.out.print(rs.getString(14)+" ");
                System.out.print(rs.getString(15)+" ");
                System.out.print(rs.getString(16)+" ");
                System.out.print(rs.getString(17)+"\n");                      
            }
            
            statement.close();
            conn.close();

            // connect to the 'sample' database

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
