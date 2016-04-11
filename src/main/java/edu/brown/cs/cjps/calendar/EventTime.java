package edu.brown.cs.cjps.calendar;

/**A class to represent an eventTime. It takes an integer
 * of the time, and a boolean representing AM or PM.*/
public class EventTime implements Comparable<EventTime> {

    //Integer representing the hour
    private int hour;
    
    //Boolean Representing AM (true) or PM (false)
    private boolean isAM;
    
  public EventTime(int hour , boolean amOrPm) {
    this.hour = hour;
    this.isAM = amOrPm;
  }
  
  public void update(int hour, boolean amOrPm) {
    if (hour < 0 || hour > 12) {
      throw new IllegalArgumentException();
    }
    
    this.hour = hour;
    this.isAM = amOrPm;
  }

  @Override
  public int compareTo(EventTime other) {
    if (this.isAM && !(other.isAM)) {
      return -1;
    } else if (this.isAM && other.isAM) {
      return Integer.compare(this.getHour(), other.getHour());
    } else if (!(this.isAM) && other.isAM) {
      return 1;
    } else {
      return Integer.compare(this.getHour(), other.getHour());
    }
  }
  
  /**
 * @return the hour
 */
public int getHour() {
  return hour;
}

@Override
public boolean equals(Object o) {
  if (!(o  instanceof EventTime)) {
    return false;
  } else {
    EventTime other = (EventTime) o;
    return (this.isAM == other.isAM)
        && (this.getHour() == other.getHour());
  }
}


}
