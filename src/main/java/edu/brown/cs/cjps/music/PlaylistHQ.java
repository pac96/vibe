package edu.brown.cs.cjps.music;

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
    // if (tag == null) {
    // tag = Tag.RESTFUL;
    // }
    //
    // Settings defaults;
    // switch (tag) {
    // case WORKSTUDY:
    // defaults = def.getWorkStudyDefaults();
    // break;
    // case EATSOCIAL:
    // defaults = def.getEatSocialDefaults();
    // break;
    // case EXERCISE:
    // defaults = def.getExerciseDefaults();
    // break;
    // case PARTY:
    // defaults = def.getPartyDefaults();
    // break;
    // // Restful is default
    // default:
    // defaults = def.getRestfulDefaults();
    // }
    Settings defaults = this.getDefaults(tag);

    VibePlaylist p;

    p = pg.makePlaylist(defaults, event.getDuration(), api, curentUser,
        accessToken);
    VibeCache.getPlaylistCache().put(event.getId(), p);
    return p;
  }

  public VibePlaylist generateCustom(Settings settings, CalendarEvent event,
      Api api, User curentUser, String accessToken) {
    // Add in the Tag settings to the specific settings that the user chose,
    // and fix anything left null
    Tag tag = settings.getTag();
    // If user selected a tag - merge settings
    if (tag != null) {
      Settings tagsettings = this.getDefaults(tag);
      List<String> userGenres = settings.getGenres();
      if (userGenres != null) {
        userGenres.addAll(tagsettings.getGenres());
        settings.replaceGenres(userGenres);
      } else {
        settings.replaceGenres(tagsettings.getGenres());
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
      userGenres.addAll(tagSettings.getGenres());
      finalSettings.replaceGenres(userGenres);
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
}
