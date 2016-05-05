package edu.brown.cs.cjps.music;

import java.util.HashMap;
import java.util.List;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.models.User;

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

  public VibePlaylist generateFromTag(String eventID, Api api, User curentUser,
      String accessToken) {
    Tag tag = this.findTag(eventID);
    // Tag tag = Tag.RESTFUL;
    System.out.println("tag is " + tag);
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

    VibePlaylist p;

    p = pg.makePlaylist(defaults, api, curentUser, accessToken);
    VibeCache.getPlaylistCache().put(eventID, p);
    System.out.println("returning from HQ");
    return p;
  }

  public void generateCustom(List<String> params) {
    // TODO: Convert the params to a setting and pass to make playlist
  }

  /**
   * Returns the spotify uri of a specific playlist for an event
   * 
   * @param eventID
   * @param spotifyAPI
   * @param spotifyUser
   * @return
   */
  public String convertForSpotify(VibePlaylist p, String eventID,
      Api spotifyAPI, User spotifyUser) {
    VibeCache.getPlaylistCache().get(eventID);
    List<String> trackIDs = p.getTracks();
    String uri = sc.makeSpotifyPlaylist(eventID, spotifyAPI, spotifyUser,
        trackIDs);
    return uri;
  }

  private Tag findTag(String eventID) {
    HashMap<String, Tag> tagmap = VibeCache.getTagMap();
    String[] idArray = eventID.split(" ");
    for (int i = 0; i < idArray.length; i++) {
      for (String s : tagmap.keySet()) {
        if (idArray[i].contains(s)) {
          return tagmap.get(s);
        }
      }
    }
    return null;
  }
}
