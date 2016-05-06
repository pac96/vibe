//package edu.brown.cs.cjps.vibetests;
//
//import static org.junit.Assert.*;
//
//import org.junit.Test;
//
//import edu.brown.cs.cjps.calendar.EventTime;
//
//public class EventTimeTest {
//  
//  @Test
//  public void constructorTest() {
//    //Tests generic constructor generation
//    
//    //creates an EventTime
//    EventTime testTime = new EventTime(1, 12, false);
//    
//    //Check the values are the same as the original input.
//    assertTrue(testTime.getHour() == 1);
//    assertTrue(testTime.getMinute() == 12);
//    assertTrue(testTime.IsAm() == false);
//  }
//  
//  @Test
//  public  void invalidConstructorTest() {
//    //test for constructing an event time with invalid times.
//    try {
//    // test where hour < 0
//      EventTime test = new EventTime(-10, 10 , true);
//    } catch (IllegalArgumentException e) {
//      //the constructor should fall into this case because of invalid construction
//     assertTrue(true);
//    }
//    //test for hour > 12
//    try {
//      //test where hour > 12
//      EventTime test2 = new EventTime(13, 11, true);     
//    } catch (IllegalArgumentException e) {
//      assertTrue(true);
//    }
//  }
//  
//  @Test
//  public void validUpdateTime() {
//    //Tests updating the time of a calendar event with a valid time.
//    //An eventtime is created with a valid time, and then is updated.
//    EventTime test = new EventTime(12, 15 , true);
//    test.update(8, 15, false);
//    assertTrue(test.getHour() == 8);
//    assertTrue(test.getMinute() == 15);
//    assertTrue(test.IsAm() == false);
//  }
//  
//  @Test
//  public void invalidUpdateTime() {
//    EventTime test = new EventTime(12,30 , false);
//    //update the event to a time that is not valid.
//    try {
//      test.update(12,90,false);
//      } catch (IllegalArgumentException e) {
//        System.out.println("Incorrect time");
//        assertTrue(true);
//    }  
//  }
//  
//  @Test
//  public void compareEqualEventtimes() {
//    EventTime testA = new EventTime(12, 30 , false);
//    EventTime testB = new EventTime(12, 30 , false);   
//    assertTrue(testA.equals(testB));
//  }
//  
//  @Test
//  public void compareALessThanB() {
//    EventTime testA = new EventTime(12, 30 , true);
//    EventTime testB = new EventTime(12, 45, true);
//    EventTime testC = new EventTime(12, 45, false);
//    
//    assertTrue(testA.compareTo(testB) < 0);
//    assertTrue(testA.compareTo(testC) < 0);
//  }
//  
//  
//  //YOU NEED TO REVISIT THIS AND WRITE A CORRECT TEST CASE
//  @Test 
//  public void compareAGreaterThanB() {
//    EventTime testA = new EventTime(12, 30 , true);
//    EventTime testB = new EventTime(12, 45, true);
//    EventTime testC = new EventTime(12, 45, false);
//    
//    assertTrue(testA.compareTo(testB) < 0);
//    assertTrue(testA.compareTo(testC) < 0);
//    
//  }
// }
