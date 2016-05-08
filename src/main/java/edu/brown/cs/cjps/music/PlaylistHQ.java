package edu.brown.cs.cjps.music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.models.User;

import edu.brown.cs.cjps.calendar.CalendarEvent;
import edu.brown.cs.cjps.vibe.MusicEventTag.Tag;
import edu.brown.cs.cjps.vibe.VibeCache;

public class PlaylistHQ {

  private PlaylistDefaults def;
  private PlaylistGenerator pg;
  private SpotifyConverter sc;
  private PlaylistGetter getter;

  public PlaylistHQ() {

    def = new PlaylistDefaults();
    pg = new PlaylistGenerator();
    sc = new SpotifyConverter();
    getter = new PlaylistGetter();
  }

  // NOTE: this method caches the playlist
  public VibePlaylist generateFromTag(CalendarEvent event, Api api,
      User curentUser, String accessToken) {
    String eventName = event.getName();
    Tag tag = this.findTag(eventName);
    // Tag tag = Tag.RESTFUL;
    System.out.println("tag is " + tag);

    Settings defaults = this.getDefaults(tag);

    VibePlaylist p;

    p = pg.makePlaylist(defaults, event.getDuration(), api, curentUser,
        accessToken);
    VibeCache.getPlaylistCache().put(event.getId(), p);
    return p;
  }

  public VibePlaylist generateCustom(List<String> genres,
      List<String> stringSettings, CalendarEvent event, Api api,
      User curentUser, String accessToken) {
    Settings settings = this.makeSettings(genres, stringSettings);

    System.out.println("after formatting");
    System.out.println("genres " + settings.getGenres());
    System.out.println("energy " + settings.getEnergy());
    System.out.println("hotness " + settings.getHotness());
    System.out.println("mood " + settings.getMood());
    System.out.println("tag " + settings.getTag());

    // Add in the Tag settings to the specific settings that the user chose,
    // and fix anything left null
    Tag tag = settings.getTag();
    // If user selected a tag - merge settings
    settings = this.mergeSettings(settings, this.getDefaults(tag));

    VibePlaylist p = pg.makePlaylist(settings, event.getDuration(), api,
        curentUser, accessToken);
    VibeCache.getPlaylistCache().put(event.getId(), p);
    return p;
  }

  /**
   * Returns the spotify uri of a specific playlist for an event
   * 
   * @param eventID
   * @param spotifyAPI
   * @param spotifyUser
   * @return
   */
  public String convertForSpotify(VibePlaylist p, String eventName,
      Api spotifyAPI, User spotifyUser) {
    List<String> trackIDs = p.getTracks();
    String uri = sc.makeSpotifyPlaylist(eventName, spotifyAPI, spotifyUser,
        trackIDs);
    return uri;
  }

  public List<String> getAllPlaylists(Api spotifyAPI, User user) {
    return getter.getAllUserPlaylists(spotifyAPI, user);
  }

  private Tag findTag(String eventname) {
    eventname = eventname.toLowerCase();
    HashMap<String, Tag> tagmap = VibeCache.getTagMap();
    String[] idArray = eventname.split(" ");
    for (int i = 0; i < idArray.length; i++) {
      for (String s : tagmap.keySet()) {
        if (idArray[i].contains(s)) {
          return tagmap.get(s);
        }
      }
    }
    return null;
  }

  private Settings getDefaults(Tag tag) {
    if (tag == null) {
      tag = Tag.RESTFUL;
    }

    Settings defaults;
    switch (tag) {
    case WORKSTUDY:
      defaults = def.getWorkStudyDefaults();
      break;
    case EATSOCIAL:
      defaults = def.getEatSocialDefaults();
      break;
    case EXERCISE:
      defaults = def.getExerciseDefaults();
      break;
    case PARTY:
      defaults = def.getPartyDefaults();
      break;
    // Restful is default
    default:
      defaults = def.getRestfulDefaults();
    }
    return defaults;
  }

  private Settings mergeSettings(Settings userSettings, Settings tagSettings) {
    // Genres
    Settings finalSettings = new Settings();
    List<String> userGenres = userSettings.getGenres();
    if (userGenres.size() != 0) {
      List<String> finalGenres = new ArrayList<>();
      finalGenres.addAll(userGenres);
      // userGenres.addAll(tagSettings.getGenres());
      finalGenres.addAll(tagSettings.getGenres());
      finalSettings.replaceGenres(finalGenres);
    } else {
      System.out.println("size was 0");
      finalSettings.replaceGenres(tagSettings.getGenres());
    }
    // Energy
    float userE = userSettings.getEnergy();
    float averageE = (userE + tagSettings.getEnergy()) / 2;
    finalSettings.setEnergy(averageE);
    // Mood
    float userM = userSettings.getMood();
    float averageM = (userM + tagSettings.getMood()) / 2;
    finalSettings.setMood(averageM);

    // Hotness
    int userH = userSettings.getHotness();
    int averageH = (userH + tagSettings.getHotness()) / 2;
    finalSettings.setHotness(averageH);

    return finalSettings;
  }

  public Settings makeSettings(List<String> genres, List<String> str) {
    String tagStr = str.get(0);
    String energyStr = str.get(1);
    String hotnessStr = str.get(2);
    String moodStr = str.get(3);
    Tag tag = this.tagFromTagString(tagStr);

    // Lowercase the genres
    for (int i = 0; i < genres.size(); i++) {
      genres.set(i, genres.get(i).toLowerCase());
    }

    // Energy, hotness, and mood
    float energy = (float) (Float.parseFloat(energyStr) / 10.0f); // 0-1 scale
    int hotness = (int) (Integer.parseInt(hotnessStr) * 10); // 0-100 scale
    float mood = Float.parseFloat(moodStr); // mood is already 0-1
    return new Settings(tag, genres, mood, hotness, energy);
  }

  private Tag tagFromTagString(String tagStr) {
    Tag tag = null;
    switch (tagStr) {
    case "Party":
      tag = Tag.PARTY;
      break;
    case "Work/Study":
      tag = Tag.WORKSTUDY;
      break;
    case "Exercise":
      tag = Tag.EXERCISE;
      break;
    case "Eat/Social":
      tag = Tag.EATSOCIAL;
      break;
    default:
      tag = Tag.RESTFUL;
      break;
    }
    return tag;
  }
}
