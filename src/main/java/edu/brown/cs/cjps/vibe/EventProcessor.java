package edu.brown.cs.cjps.vibe;

import java.sql.SQLException;
import java.util.List;

import edu.brown.cs.cjps.calendar.CalendarEvent;
import edu.brown.cs.cjps.calendar.EventTime;
/**
 * Event processor constructor
 */
import edu.brown.cs.cjps.db.DBQuerier;

public class EventProcessor {

  // DBQuerier to query a database for events and users
  private DBQuerier dbquerier;

  /**
   * Event processor constructor
   */
  public EventProcessor(String db) {
    try {
      this.dbquerier = new DBQuerier(db);
    } catch (ClassNotFoundException e) {
      System.out.println("Class not found");
      e.printStackTrace();
    } catch (SQLException e) {
      System.out.println("Invalid database");
      e.printStackTrace();
    }
  }

  /**
   * Creates a CalendarEvent object for an event that the user wants to add
   *
   * @param startTime
   *          - the starting time of the event
   * @param amOrPm
   *          - start boolean (true if AM, false if PM)
   * @param endTime
   *          - the end time of the event
   * @param endAMorPM
   *          - end boolean (true if AM, false if PM)
   * @param name
   *          - the name of the event
   * @return the CalendarEvent object of the event you just added , null if a
   *         NumberException is caught
   * @throws SQLException
   */
  public CalendarEvent addEvent(String startTime, Boolean amOrPm,
      String endTime, Boolean endAMorPM, String name, String userid)
      throws SQLException {

    // Parse the start time to hour and minute.
    String[] startTimeSplit = startTime.split(":");
    Integer startHour = null;
    Integer startMinute = null;
    try {
      startHour = Integer.parseInt(startTimeSplit[0]);
      startMinute = Integer.parseInt(startTimeSplit[1]);
    } catch (NumberFormatException e) {
      System.out.println("Invalid format for time.");
      System.out.println("Be sure the format is hh:mm , and both are numbers");
      return null;
    }

    // Create an EventTime object for the start
    EventTime start = new EventTime(startHour, startMinute, amOrPm);

    // Parse the end time to hour and minute.
    String[] endTimeSplit = endTime.split(":");

    Integer endHour = null;
    Integer endMinute = null;

    try {
      endHour = Integer.parseInt(endTimeSplit[0]);
      endMinute = Integer.parseInt(endTimeSplit[1]);
    } catch (NumberFormatException e) {
      System.out.println("Invalid format for time.");
      System.out.println("Be sure the format is hh:mm");
      return null;
    }

    // Create an EventTime object for the end
    EventTime end = new EventTime(endHour, endMinute, endAMorPM);

    // Parse the name from the front end.
    // System.out.println("Event name: " + name);
    // System.out.println("Event start time: " + start.toString());
    // System.out.println("Event end time: " + end.toString());

    // Create a calendar event
    CalendarEvent newEvent = new CalendarEvent(name, start, end);

    // TODO: pass in the users id
    dbquerier.insertCalendarEvent(newEvent, userid);
    return newEvent;
  }

  /**
   * Edits and returns a CalendarEvent object for an event that the user wants
   * to edit
   *
   * @param startTime
   *          - the new starting time of the event
   * @param amOrPm
   *          - the new start boolean (true if AM, false if PM)
   * @param endTime
   *          - the new end time of the event
   * @param endAMorPM
   *          - the new end boolean (true if AM, false if PM)
   * @param name
   *          - the new name of the event
   * @return the CalendarEvent object of the event you just edited, if the
   *         formatting was incorrect, or an error was encountered, the original
   *         calendar event is returned unmodified.
   */
  public CalendarEvent editEvent(String startTime, Boolean amOrPm,
      String endTime, Boolean endAMorPM, String name, CalendarEvent event) {

    try {
      // Parse the start time
      String[] startSplit = startTime.split(":");
      Integer startHour = Integer.parseInt(startSplit[0]);
      Integer startMinute = Integer.parseInt(startSplit[1]);
      event.getStart().update(startHour, startMinute, amOrPm);

      // Parse the end time
      String[] endSplit = endTime.split(":");
      Integer endHour = Integer.parseInt(endSplit[0]);
      Integer endMinute = Integer.parseInt(endSplit[1]);
      event.getEnd().update(endHour, endMinute, endAMorPM);

      event.setName(name);

      System.out.println("Updated event: " + event);

      dbquerier.editCalendarEvent(event);

    } catch (SQLException e) {
      e.printStackTrace();

    } catch (NumberFormatException e) {
      System.out.println("Error in format of times");
      e.printStackTrace();

    }

    return event;

  }

  /**
   * Deletes an event from the database with the specified eventID
   *
   * @param eventID
   *          - the id of the event you want to delete
   * @throws SQLException
   */
  public void deleteEvent(String eventID, String userID) throws SQLException {
    // TODO: delete an event with a certain eventID
    dbquerier.deleteCalendarEventFromEventsTable(eventID, userID);
  }

  public CalendarEvent getEventFromEventID(String eventId) throws SQLException {
    return dbquerier.getEventFromEventID(eventId);
  }

  public List<CalendarEvent> getEventsFromUserID(String userID)
      throws SQLException {
    return dbquerier.getEventsGivenUserId(userID);
  }
}
