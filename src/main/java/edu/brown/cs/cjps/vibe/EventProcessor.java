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
	 * Adds an event to the calendar and returns that event object
	 * @param qm - the map that contains values from the front-end
	 * @return the event object of the event you created and added
	 */
	public CalendarEvent addEvent(QueryParamsMap qm) {
		// Parse the start time to hour and minute.
	      String startTime = qm.value("start");
	      String[] startTimeSplit = startTime.split(":");
	      Integer startHour = Integer.parseInt(startTimeSplit[0]);
	      Integer startMinute = Integer.parseInt(startTimeSplit[1]);
	      Boolean amOrPm = Boolean.parseBoolean(qm.value("startAMPM"));
	      EventTime start = new EventTime(startHour, startMinute, amOrPm);

	      // Parse the end time to hour and minute.
	      String endTime = qm.value("end");
	      String[] endTimeSplit = endTime.split(":");
	      Integer endHour = Integer.parseInt(endTimeSplit[0]);
	      Integer endMinute = Integer.parseInt(endTimeSplit[1]);
	      Boolean endAMorPM = Boolean.parseBoolean(qm.value("endAMPM"));
	      EventTime end = new EventTime(endHour, endMinute, endAMorPM);

	      // Parse the name from the front end.
	      String name = qm.value("name");
	      System.out.println("Event name: " + name);
	      System.out.println("Event start time: " + start.toString());
	      System.out.println("Event end time: " + end.toString());

	      // Create a calendar event
	      CalendarEvent newEvent = new CalendarEvent(name, start, end);
	      
	      return newEvent;
	}
}
