package edu.brown.cs.cjps.vibe;

import edu.brown.cs.cjps.calendar.CalendarEvent;
import edu.brown.cs.cjps.calendar.EventTime;
import spark.QueryParamsMap;

public class EventProcessor {
	
	/**
	 * Event processor constructor
	 */
	public EventProcessor() {}
	
	/**
	 * Creates a CalendarEvent object for an event that the user 
	 * wants to add
	 * 
	 * @param startTime - the starting time of the event
	 * @param amOrPm - start boolean (true if AM, false if PM)
	 * @param endTime - the end time of the event
	 * @param endAMorPM - end boolean (true if AM, false if PM)
	 * @param name - the name of the event
	 * @return the CalendarEvent object of the event you just added
	 */
	public CalendarEvent addEvent(String startTime, Boolean amOrPm, 
			String endTime, Boolean endAMorPM, String name) {
		// Parse the start time to hour and minute.
	      String[] startTimeSplit = startTime.split(":");
	      Integer startHour = Integer.parseInt(startTimeSplit[0]);
	      Integer startMinute = Integer.parseInt(startTimeSplit[1]);
	      EventTime start = new EventTime(startHour, startMinute, amOrPm);

	      // Parse the end time to hour and minute.
	      String[] endTimeSplit = endTime.split(":");
	      Integer endHour = Integer.parseInt(endTimeSplit[0]);
	      Integer endMinute = Integer.parseInt(endTimeSplit[1]);
	      EventTime end = new EventTime(endHour, endMinute, endAMorPM);

	      // Parse the name from the front end.
	      System.out.println("Event name: " + name);
	      System.out.println("Event start time: " + start.toString());
	      System.out.println("Event end time: " + end.toString());

	      // Create a calendar event
	      CalendarEvent newEvent = new CalendarEvent(name, start, end);
	      
	      // TODO: add this event to the database
	      
	      return newEvent;
	}

	/**
	 * Edits and returns a CalendarEvent object for an event that the user 
	 * wants to edit
	 * 
	 * @param startTime - the new starting time of the event
	 * @param amOrPm - the new start boolean (true if AM, false if PM)
	 * @param endTime - the new end time of the event
	 * @param endAMorPM - the new end boolean (true if AM, false if PM)
	 * @param name - the new name of the event
	 * @return the CalendarEvent object of the event you just edited
	 */
	public CalendarEvent editEvent(String startTime, Boolean amOrPm, 
			String endTime, Boolean endAMorPM, String name) {
		      
	      // TODO: edit the old event in the database
	      
	      return null;
	}
	
	/**
	 * Deletes an event from the database with the specified eventID
	 * 
	 * @param eventID - the id of the event you want to delete
	 */
	public void deleteEvent(String eventID) {
		// TODO: delete an event with a certain eventID
	}
}
