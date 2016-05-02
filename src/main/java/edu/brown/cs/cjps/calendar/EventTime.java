package edu.brown.cs.cjps.calendar;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**A class to represent an eventTime. It takes an integer
 * of the time, and a boolean representing AM or PM.*/
public class EventTime implements Comparable<EventTime> {

    //Integer representing the hour of the event.
    private int hour;
    
    /**
     * @return the isAM
     */
    public boolean isAM() {
      return isAM;
    }

    /**
     * @param hour the hour to set
     */
    public void setHour(int hour) {
      this.hour = hour;
    }

    /**
     * @param minute the minute to set
     */
    public void setMinute(int minute) {
      this.minute = minute;
    }

    /**
     * @param isAM the isAM to set
     */
    public void setAM(boolean isAM) {
      this.isAM = isAM;
    }

    //Integer representing the minute the event occurs.
    private int minute;
    
    //Boolean Representing AM (true) or PM (false) for the event time.
    private boolean isAM;
    
    //Empty Constructor that can be set later
    public EventTime(){}
  
  //Constructor for an event time.
  public EventTime(int hour , int minute, boolean amOrPm) throws IllegalArgumentException {
    if ( hour < 0 || hour > 12 || minute < 0 || minute > 59) {
      throw new IllegalArgumentException();
    }
    
    this.hour = hour;
    this.minute = minute;
    this.isAM = amOrPm;
  }
  
  public void update(int _hour, int _minute, boolean _amOrPm) throws IllegalArgumentException {
    if (hour < 0 || hour > 12 || minute < 0 || minute > 60) {
      throw new IllegalArgumentException();
    }
    
    this.hour = _hour;
    this.isAM = _amOrPm;
    this.minute = _minute;
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
 * @return the hour.
 */
public int getHour() {
  return hour;
}

/** 
 *@return the minute.
 * */
public int getMinute() {
  return minute;
}

/**  
 * @return if the time is AM or PM.
 * */
public boolean IsAm() {
  return isAM;
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

@Override
public String toString() {
  StringBuilder sb = new StringBuilder();
  sb.append(hour + ":" + minute);
  if (isAM) {
    sb.append(":AM");
  } else {
    sb.append(":PM");
  }
  
  return sb.toString();
}

public Map<String, Object> toJson() {
  return ImmutableMap.of("hour" , hour , 
      "minute", minute
      ,"isAM",isAM);
}

}
