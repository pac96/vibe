package edu.brown.cs.cjps.vibe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import edu.brown.cs.cjps.music.VibePlaylist;
import edu.brown.cs.cjps.vibe.MusicEventTag.Tag;

/**
 * Cache all the things
 *
 * @author smayfiel
 *
 */
public class VibeCache {

  private static HashMap<String, VibePlaylist> playlistCache;
  // private static ListMultimap<Tag, String> tags;
  private static ListMultimap<Tag, String> tags;

  /**
   * Constuctor just initializes the HashMap cache.
   */
  public VibeCache() {
    playlistCache = new HashMap<String, VibePlaylist>();

    // Music tags
    tags = ArrayListMultimap.create();
    List<String> workwords = Arrays.asList("study", "review", "meeting",
        "read", "study", "revise", "essay", "assignemnt", "class", "test",
        "exam", "quiz", "prepare");
    List<String> partywords = Arrays.asList("party", "turn up", "lit", "jam",
        "rager", " birhtday", "celebration", "dance");
    List<String> eatwords = Arrays.asList("snack", "dinner", "friends",
        "lunch", "breakfast");
    List<String> restwords = Arrays.asList("sleep", "relax", "rest", "calm",
        "meditate", "nap", "chill");
    List<String> exercisewords = Arrays.asList("exercise", "run", "job",
        "work out", "weight", "lift", "practice", "sport", "swim", "train",
        "marathon", "football", "tennis", "soccer", "baseball", "basketball");
    tags.putAll(Tag.WORKSTUDY, workwords);
    tags.putAll(Tag.PARTY, partywords);
    tags.putAll(Tag.EATSOCIAL, eatwords);
    tags.putAll(Tag.RESTFUL, restwords);
    tags.putAll(Tag.EXERCISE, exercisewords);

  }

  /**
   * Returns the Playlist HashMap to the caller. This method and the HashMap are
   * static to ensure that only one exists for the duration of the program.
   *
   * @return the cache HashMap
   */
  public static HashMap<String, VibePlaylist> getPlaylistCache() {
    return playlistCache;
  }

  public static ListMultimap<Tag, String> getTagMap() {
    return tags;
  }

  // List of genres:
  // alternative", "blues", "classical", "country", "dancehall", "electronic",
  // "folk", "funk", "gospel", "hip-hop", "indie", "jazz", "k-pop", "pop",
  // "r-n-b", "reggae", "reggaeton", "rock", "soul", "spanish"

}