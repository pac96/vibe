package edu.brown.cs.cjps.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

//Creates a database to store users
//Schema  USER , userID
public class UserDBCreator {
  //The connection to this database
  private Connection conn;
  
  
  /** 
   * @param db
   * @throws ClassNotFoundException
   * @throws SQLException
   * */
  public UserDBCreator(String db) throws ClassNotFoundException, SQLException {
    //(1): Load the driver class
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + db;
    
    //(2): Set up a connection to the db.
    Connection conn = DriverManager.getConnection(urlToDB);
    this.conn = conn;
    
    //These delete the tables if they exist.
    Statement stat = conn.createStatement();
    stat.execute("DROP TABLE IF EXISTS users");
    stat.execute("DROP TABLE IF EXISTS playlists");
    stat.execute("DROP TABLE IF EXISTS playlistSongs");
    stat.execute("DROP TABLE IF EXISTS songs");
    stat.close();
    
    
    //(3): Fill in the schema to create a table
    PreparedStatement prep;
    
    //Fill in the schema for the table called user
    String schema = "Create TABLE users ( "
        + "name TEXT"
        + "id TEXT);";
        
    //Fill in the schema to create a table called playlist
    
    
    
    
  }

}
