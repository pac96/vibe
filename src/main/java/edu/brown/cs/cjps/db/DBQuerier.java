package edu.brown.cs.cjps.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.brown.cs.cjps.calendar.CalendarEvent;
import edu.brown.cs.cjps.calendar.EventTime;

public class DBQuerier {
  //The connection to the DB.
  private Connection conn;

  public DBQuerier(String db) throws ClassNotFoundException, SQLException {
    //(1): Load the driver class
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + db;

    //(2): Set up a connection to the db.
    Connection conn = DriverManager.getConnection(urlToDB);

    //(3): Store the connection in the global field
    this.conn = conn;
  }

  public boolean userIsInDatabase(String userid) throws SQLException {
    //(1): Write query as a string.
    String query = "SELECT * FROM USER where userid = ?";
    PreparedStatement ps = conn.prepareStatement(query);
    ResultSet res = ps.executeQuery();

    return res.first();
  }


  public void insertCalendarEvent(CalendarEvent event, String userId) throws SQLException {

    //(1): Write query as a string.
    String query = "INSERT INTO USEREVENTS VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ;";

    //(2): Create a preparedstatement.
    PreparedStatement ps = conn.prepareStatement(query);

    //(3): Set the values for the prepared statement.
    ps.setString(1, userId);
    ps.setString(2, event.getName());
    ps.setString(3, event.getId().toString());
    ps.setString(4, event.getPlayListId());
    ps.setInt(5, event.getStart().getHour());
    ps.setInt(6, event.getEnd().getHour());
    ps.setInt(7, event.getStart().getMinute());
    ps.setInt(8, event.getEnd().getMinute());
     if (event.getStart().IsAm()) {
       ps.setInt(9, 1);
     } else {
       ps.setInt(9,0);
     }

     if(event.getEnd().IsAm()) {
       ps.setInt(10,1);
     } else {
       ps.setInt(10, 0);
     }

     //(4): Execute the query, and close everything.
     ps.addBatch();
     ps.executeBatch();
     ps.close();
  }

  public void editCalendarEvent(CalendarEvent event) throws SQLException {
    String eventid = event.getId().toString();

    //(1): Write a query as String
    String query = "UPDATE USEREVENTS "
        + "SET eventname = ? ,"
        + "SET starthour = ? , "
        + "SET startminute = ? , "
        + "SET startAM = ? , "
        + "SET endhour = ? , "
        + "SET endminute = ?"
        + "SET endAM = ?"
        + "WHERE USEREVENTS.eventid = ? ";

    //(2): Create a prepared statement.
    PreparedStatement ps = conn.prepareStatement(query);

    //(3): Fill in the values for the query

    //(3): Execute the query.
    ps.executeUpdate();
  }


  public void deleteCalendarEventFromEventsTable(String eventid, String userid) throws SQLException {

    //(1): Write query as String
    String query = "DELETE from USEREVENTS "
        + " WHERE eventid = ?  AND userid = ? ;";

    //(2): Create a preparedstatement.
    PreparedStatement ps = conn.prepareStatement(query);

    //(3): Set the arguments to be used in the query.
    ps.setString(1, eventid);

    ps.setString(2, userid);

    //(4): Execute the query.
    ps.execute();

    //5. Close the connection
    ps.close();
  }

  public void insertUser(String userID, String name)throws SQLException {
    String query = "INSERT INTO users VALUES (?,?) ; ";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, userID);
    ps.setString(2, name);

    ps.addBatch();
    ps.executeBatch();
    ps.close();
  }

  public List<CalendarEvent> getEventsGivenUserId(String userID) throws SQLException {
    //(1): Write the query as a String
    List<CalendarEvent> toReturn = new ArrayList<>();
    String query = "SELECT * "
        + " FROM USEREVENTS "
        + " WHERE userid = ? ; ";

    //(2): Create a preparedstatment.
    PreparedStatement ps = conn.prepareStatement(query);

    //(3): Set the arguments to use in the query.
    ps.setString(1, userID);

    //(4): Execute the query
    ResultSet res = ps.executeQuery();

    //(5): Add the results to the list
    while (res.next()) {

      //Read from res and create the startTime object.
      EventTime startTime = new EventTime();
      startTime.setHour(res.getInt("starthour"));
      startTime.setMinute(res.getInt("startminute"));
      startTime.setAM((res.getInt("startAMorPM") == 1));

     //Read from res and create the endTime object.
      EventTime endTime = new EventTime();
      endTime.setHour(res.getInt("endhour"));
      endTime.setMinute(res.getInt("endminute"));
      endTime.setAM((res.getInt("endAMorPM") == 1));

      //Create an event and set the eventid and playlistid
      CalendarEvent event = new CalendarEvent(res.getString("eventname"), startTime, endTime);
      event.setPlayListId(res.getString("playlistid"));
      event.setId(UUID.fromString(res.getString("eventid")));

      toReturn.add(event);
    }


    ps.close();
    res.close();
    return toReturn;
  }

}
