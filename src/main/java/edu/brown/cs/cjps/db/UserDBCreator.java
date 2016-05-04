package edu.brown.cs.cjps.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.cjps.calendar.CalendarEvent;

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
    stat.execute("DROP TABLE IF EXISTS USERS");
    stat.execute("DROP TABLE IF EXISTS USEREVENTS");
    stat.close();
    
    
    //(3): Fill in the schema to create a table
    PreparedStatement prep;
    
    //Fill in the schema for the table called user
    String schema = "Create TABLE USERS ( "
        + " userid TEXT , "
        + " name TEXT "
        + " ); ";
    buildTable(schema);
        
    //Fill in the schema to create a table called playlist
    schema = "Create TABLE USEREVENTS ( "
        + " userid TEXT ,"
        + " eventname TEXT , "
        + " eventid TEXT , "
        + " playlistid TEXT , "
        + " starthour int , "
        + " endhour int , "
        + " startminute int , "
        + " endminute int , "
        + " starAMorPM int, "
        + " endAMorPM int ); ";
    buildTable(schema);  
  }
  
  /**
   * Creates a new table according to the schema
   * @param schema
   * @throws SQLException
   */
  private void buildTable(String schema) throws SQLException {
    //TODO(1): Use a PreparedStatement to execute the command in
    //the argument schema. Since it will build a table, we do
    //not care about the results.
     PreparedStatement prep = conn.prepareStatement(schema);
     prep.executeUpdate();

    //TODO(2): Close the PreparedStatement
     prep.close();
  }
  
  

}
