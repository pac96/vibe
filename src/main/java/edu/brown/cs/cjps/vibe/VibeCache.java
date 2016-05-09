package edu.brown.cs.cjps.vibe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import edu.brown.cs.cjps.calendar.CalendarEvent;
import edu.brown.cs.cjps.music.VibePlaylist;
import edu.brown.cs.cjps.vibe.MusicEventTag.Tag;

/**
 * Cache all the things
 *
 * @author smayfiel
 *
 */
public class VibeCache {

  private static HashMap<UUID, VibePlaylist> playlistCache;
  // private static ListMultimap<Tag, String> tags;
  private static HashMap<String, Tag> tags;
  private static HashMap<UUID, CalendarEvent> eventCache;

  /**
   * Constuctor just initializes the HashMap cache.
   */
  public VibeCache() {
    playlistCache = new HashMap<UUID, VibePlaylist>();
    eventCache = new HashMap<UUID, CalendarEvent>();

    // Music tags
    // tags = ArrayListMultimap.create();
    tags = new HashMap<String, Tag>();
    List<String> workwords = Arrays.asList("study", "review", "meeting",
        "read", "study", "revise", "essay", "assignemnt", "class", "test",
        "exam", "quiz", "prepare", "library");
    List<String> partywords = Arrays.asList("party", "turn up", "lit", "jam",
        "rager", " birthday", "celebration", "dance", "pregame");
    List<String> eatwords = Arrays.asList("eat", "snack", "dinner", "friends",
        "lunch", "breakfast");
    List<String> restwords = Arrays.asList("sleep", "relax", "rest", "calm",
        "meditate", "nap", "chill");
    List<String> exercisewords = Arrays.asList("exercise", "run", "job",
        "work out", "weight", "lift", "practice", "sport", "swim", "train",
        "marathon", "football", "tennis", "soccer", "baseball", "basketball",
        "shower");

    for (String s : workwords) {
      tags.put(s, Tag.WORKSTUDY);
    }
    for (String s : partywords) {
      tags.put(s, Tag.PARTY);
    }
    for (String s : eatwords) {
      tags.put(s, Tag.EATSOCIAL);
    }
    for (String s : restwords) {
      tags.put(s, Tag.RESTFUL);
    }
    for (String s : exercisewords) {
      tags.put(s, Tag.EXERCISE);
    }
  }

  /**
   * Returns the Playlist HashMap to the caller. This method and the HashMap are
   * static to ensure that only one exists for the duration of the program.
   *
   * @return the cache HashMap
   */
  public static HashMap<UUID, VibePlaylist> getPlaylistCache() {
    return playlistCache;
  }

  public static HashMap<String, Tag> getTagMap() {
    return tags;
  }

  public static HashMap<UUID, CalendarEvent> getEventCache() {
    return eventCache;
  }

  // List of genres:
  // alternative", "blues", "classical", "country", "dancehall", "electronic",
  // "folk", "funk", "gospel", "hip-hop", "indie", "jazz", "k-pop", "pop",
  // "r-n-b", "reggae", "reggaeton", "rock", "soul", "spanish"

}