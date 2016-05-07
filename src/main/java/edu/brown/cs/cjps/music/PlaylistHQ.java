package edu.brown.cs.cjps.music;

import java.util.ArrayList;
import java.util.Arrays;
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

  public PlaylistHQ() {

    def = new PlaylistDefaults();
    pg = new PlaylistGenerator();
    sc = new SpotifyConverter();
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

  public VibePlaylist generateCustom(List<String> stringSettings,
      CalendarEvent event, Api api, User curentUser, String accessToken) {
    Settings settings = this.makeSettings(stringSettings);
    // Add in the Tag settings to the specific settings that the user chose,
    // and fix anything left null
    Tag tag = settings.getTag();
    // If user selected a tag - merge settings
    if (tag != null) {
      System.out.println("Tag not null");
      settings = this.mergeSettings(settings, this.getDefaults(tag));
    } else { // Need to check to make sure there are no nulls
      if (settings.getGenres() == null) {
        settings.replaceGenres(Arrays.asList("classical"));
      }
      if (settings.getEnergy() < 0) {
        settings.setEnergy(0.7f);
      }
      if (settings.getHotness() < 0) {
        settings.setHotness(70);
      }
      if (settings.getMood() < 0) {
        settings.setMood(0.7f);
      }
    }
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

  public Settings getDefaults(Tag tag) {
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

  public Settings mergeSettings(Settings userSettings, Settings tagSettings) {
    // Genres
    Settings finalSettings = new Settings();
    List<String> userGenres = userSettings.getGenres();
    if (userGenres != null) {
      List<String> finalGenres = new ArrayList<>();
      finalGenres.addAll(userGenres);
      // userGenres.addAll(tagSettings.getGenres());
      finalGenres.addAll(tagSettings.getGenres());
      finalSettings.replaceGenres(finalGenres);
    } else {
      finalSettings.replaceGenres(tagSettings.getGenres());
    }
    // Energy
    float userE = userSettings.getEnergy();
    if (userE > 0f) {
      float averageE = (userE + tagSettings.getEnergy()) / 2;
      finalSettings.setEnergy(averageE);
    } else {
      finalSettings.setEnergy(tagSettings.getEnergy());
    }
    // Mood
    float userM = userSettings.getMood();
    if (userM > 0f) {
      float averageM = (userM + tagSettings.getMood()) / 2;
      finalSettings.setMood(averageM);
    } else {
      finalSettings.setEnergy(tagSettings.getEnergy());
    }
    // Hotness
    int userH = userSettings.getHotness();
    if (userH > 0) {
      int averageH = (userH + tagSettings.getHotness()) / 2;
      finalSettings.setHotness(averageH);
    } else {
      finalSettings.setHotness(tagSettings.getHotness());
    }
    return finalSettings;
  }

  public Settings makeSettings(List<String> str) {
    String tagStr = str.get(0);
    String genreStr = str.get(1);
    String energyStr = str.get(2);
    String hotnessStr = str.get(3);
    String moodStr = str.get(4);
    Tag tag = this.tagFromTagString(tagStr);
    List<String> genreList = this.parseGenreString(genreStr);

    // Energy, hotness, and mood
    float energy = -1f;
    int hotness = -1;
    float mood = -1f;
    if (!energyStr.equals("none")) {
      energy = Float.parseFloat(energyStr);
    }
    if (!hotnessStr.equals("none")) {
      hotness = Integer.parseInt(hotnessStr);
    }
    if (!moodStr.equals("none")) {
      mood = Float.parseFloat(moodStr);
    }
    return new Settings(tag, genreList, energy, hotness, mood);
  }

  private Tag tagFromTagString(String tagStr) {
    if (tagStr.equals("none")) {
      return null;
    }
    Tag tag = null;
    switch (tagStr) {
    case "party":
      tag = Tag.PARTY;
    case "workstudy":
      tag = Tag.WORKSTUDY;
    case "exercise":
      tag = Tag.EXERCISE;
    case "eatsocial":
      tag = Tag.EATSOCIAL;
    case "restful":
      tag = Tag.RESTFUL;
    }
    return tag;
  }

  private List<String> parseGenreString(String genreStr) {
    if (genreStr.equals("none")) {
      return null;
    }
    String[] splitList = genreStr.split(",");
    List<String> retList = new ArrayList<>();
    for (int i = 0; i < splitList.length; i++) {
      retList.add(splitList[i]);
    }
    return retList;
  }
}
