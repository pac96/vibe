package edu.brown.cs.cjps.calendar;

import java.util.UUID;

/**A class to represent a calendar event.*/
public class CalendarEvent {
   
  private String name;
  private EventTime start;
  private EventTime end;
  private UUID id;
  
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
  
  public UUID getId() {
    return id;
  }
  
  public void setStart(int newHour,int newMinute, boolean amOrPm) {
    start.update(newHour, newMinute, amOrPm);
    
    if (start.compareTo(end) > 0) {
      System.out.println("The start time cannot occur after the end time or vice-versa");
      throw new IllegalArgumentException();
    }
  }
  
  public void setEnd(int newHour,int newMinute, boolean amOrPm) {
    end.update(newHour, newMinute, amOrPm);
    
    if (end.compareTo(start) < 0) {
      System.out.println("The start time cannot occur after the end time");
      throw new IllegalArgumentException();
    }
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
         && this.getName().equals(other.getName());   
   }
 }
 
 
 @Override
 public String toString() {
   StringBuilder sb = new StringBuilder();
   sb.append(getName() + "\n");
   sb.append("Start : " );
   sb.append(getStart().toString() + "\n");
   sb.append("End : " + getEnd().toString());
   return sb.toString();
 }

  
  
  
  
}
