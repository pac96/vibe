package edu.brown.cs.cjps.calendar;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections.MultiMap;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**A class to represent a user's day.*/

public class Day implements Collection<CalendarEvent> {
  
  //A map from event times to the events that start at that time.
  Multimap<EventTime , CalendarEvent> timesToEvents = HashMultimap.create();
  
  //Empty constructor for a day objects.
  public Day(){};
  
  //Constructor for a Day that takes a Collection of calendar events.
  public Day(Collection<CalendarEvent> schedule) {
    addAll(schedule);
  }

  @Override
  public boolean add(CalendarEvent evnt) {
    EventTime start = evnt.getStart();
    if (!contains(evnt)) {
      return timesToEvents.put(start, evnt);
    } else {
      return false;
    }
  }

  @Override
  public boolean addAll(Collection<? extends CalendarEvent> events) {
    for (CalendarEvent c : events) {
      add(c);
    }
    return false;
  }

  @Override
  public void clear() {
    timesToEvents.clear();   
  }

  @Override
  public boolean contains(Object event) {
    if (!(event instanceof CalendarEvent)) {
      return false;
    } else {
      CalendarEvent cEvent = (CalendarEvent) event;
      return timesToEvents.get(cEvent.getStart()).contains(event);
    }
  }

  @Override
  public boolean containsAll(Collection<?> arg0) {
    for (Object c : arg0) {
      if (!contains(c)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean isEmpty() {
    return timesToEvents.isEmpty();
  }

  @Override
  public Iterator<CalendarEvent> iterator() {
    return null;
  }

  @Override
  public boolean remove(Object arg0) {
    if (!(arg0 instanceof CalendarEvent)) {
      return false;
    } else {
      CalendarEvent evnt = (CalendarEvent) arg0;
      return timesToEvents.remove(evnt.getStart(), evnt);
    }

  }

  @Override
  public boolean removeAll(Collection<?> arg0) {
    return false;
  }

  @Override
  public boolean retainAll(Collection<?> arg0) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int size() {    
    return timesToEvents.values().size();
  }

  @Override
  public Object[] toArray() {
    // TODO Return an array of Pairs 
    return null;
  }

  @Override
  public <T> T[] toArray(T[] arg0) {
    // TODO Auto-generated method stub
    return null;
  }
  
}
