package edu.brown.cs.cjps.calendar;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**A class to represent a user's day.*/

public class Day implements Collection<CalendarEvent> {
  
  //A map from event times to the events that occur at that time
  HashMap<EventTime , Set<CalendarEvent>> timesToEvents = new HashMap<>();
  
  public Day() {}

  @Override
  public boolean add(CalendarEvent arg0) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean addAll(Collection<? extends CalendarEvent> arg0) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void clear() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean contains(Object arg0) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean containsAll(Collection<?> arg0) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isEmpty() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Iterator<CalendarEvent> iterator() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean remove(Object arg0) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean removeAll(Collection<?> arg0) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean retainAll(Collection<?> arg0) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int size() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Object[] toArray() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> T[] toArray(T[] arg0) {
    // TODO Auto-generated method stub
    return null;
  }
  
}
