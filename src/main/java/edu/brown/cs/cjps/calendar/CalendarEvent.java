package edu.brown.cs.cjps.calendar;

/**A class to represent a calendar event.*/
public class CalendarEvent {
   
  private String name;
  private EventTime start;
  private EventTime end;
  
  public CalendarEvent(String name, EventTime start, EventTime end) {
    this.name = name;
    this.start = start;
    this.end = end;
  }
  
  public EventTime getStart() {
    return start;
  }
  
  public EventTime getEnd() {
    return end;
  }
  
  public void setStart(int newHour,int newMinute, boolean amOrPm) {
    start.update(newHour, newMinute, amOrPm);
    
    if (start.compareTo(end) < 0) {
      System.out.println("The start time cannot occur after the end time");
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
  

  
  
  
  
}
