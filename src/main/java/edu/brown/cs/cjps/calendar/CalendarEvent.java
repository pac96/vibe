package edu.brown.cs.cjps.calendar;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.ImmutableMap;

/** A class to represent a calendar event. */
public class CalendarEvent {

  private String name;

  private EventTime start;
  private EventTime end;
  private UUID id;

  // We set the playlist id to this default. Spotify playlist IDs are
  private String playListId = "";

  public CalendarEvent(String name, EventTime start, EventTime end) {
    this.name = name;
    this.start = start;
    this.end = end;
    this.id = UUID.randomUUID();
  }

  public EventTime getStart() {
    return start;
  }

  public EventTime getEnd() {
    return end;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  public String getPlayListURI() {
    return playListId;
  }

  public void setPlayListURI(String playListID) {
    this.playListId = playListID;
  }

  public void setStart(int newHour, int newMinute, boolean amOrPm) throws IllegalArgumentException {
    start.update(newHour, newMinute, amOrPm);

    if (start.compareTo(end) > 0) {
      System.out
          .println("The start time cannot occur after the end time or vice-versa");
      throw new IllegalArgumentException();
    }
  }

  public void setEnd(int newHour, int newMinute, boolean amOrPm) throws IllegalArgumentException {
    end.update(newHour, newMinute, amOrPm);

    if (end.compareTo(start) < 0) {
      System.out.println("The start time cannot occur after the end time");
      throw new IllegalArgumentException();
    }
  }

  // returns duration of event in minutes
  public int getDuration() {
    int startTime = this.getTimeInMins(start);
    int endTime = this.getTimeInMins(end);
    int duration = endTime - startTime;
    return duration;
  }

  // returns the time in military minutes from midnight
  public int getTimeInMins(EventTime time) {
    int startH = time.getHour();
    int startM = time.getMinute();
    boolean isAm = time.IsAm();
    // If PM and not 12pm, add 12 hours
    if ((!isAm && (startH != 12))) {
      startH = startH + 12; // accounting for 24 hour time
    }
    // If 12AM, convert to 0
    if (isAm && startH == 12) {
      startH = 0;
    }
    int startinMins = startH * 60 + startM;
    return startinMins;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CalendarEvent)) {
      return false;
    } else {
      CalendarEvent other = (CalendarEvent) o;
      return this.getStart().equals(other.getStart())
          && this.getEnd().equals(other.getEnd())
          && this.getId().equals(other.getId())
          && this.getName().equals(other.getName())
          && this.getPlayListURI().equals(other.getPlayListURI());
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getName() + "\n");
    sb.append("Start : ");
    sb.append(getStart().toString() + "\n");
    sb.append("End : " + getEnd().toString());
    return sb.toString();
  }

  /**
   * JSON representation of a CalendarEvent.
   *
   * */
  public Map<String, Object> toJson() {
    return ImmutableMap.of("name", name, "start", start.toJson(), "end",
        end.toJson(), "id", id.toString());
  }

}
