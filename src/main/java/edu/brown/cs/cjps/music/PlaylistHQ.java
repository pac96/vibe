package edu.brown.cs.cjps.music;

import java.util.List;

import com.google.common.collect.Multimap;
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
    // Tag tag = this.findTag(eventID); --- BROKEN SOMEHOW
    Tag tag = Tag.RESTFUL;
    // System.out.println("tag is " + tag);
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
      defaults = def.getExerciseDefaults();
    }

    VibePlaylist p;

    p = pg.makePlaylist(defaults, api, curentUser, accessToken);
    // VibeCache.getPlaylistCache().put(eventID, p);
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
    // VibeCache.getPlaylistCache().get(eventID);
    List<String> trackIDs = p.getTracks();
    String uri = sc.makeSpotifyPlaylist(spotifyAPI, spotifyUser, trackIDs);
    return uri;
  }

  // TODO: THIS IS BROKEN FOR SOME UNKNOWN REASON
  private Tag findTag(String eventID) {
    Multimap<Tag, String> wtf = VibeCache.getTagMap();
    System.out.println(wtf);
    System.out.println("why is this happening");
    System.out.println(wtf.containsValue("test"));
    System.out.println("In find tag??");
    System.out.println("event ID" + eventID);
    String[] idArray = eventID.split(" ");
    System.out.println(idArray.length);
    for (int i = 0; i < idArray.length; i++) {
      System.out.println("In for loop");
      System.out.println("This piece is " + idArray[i]);
      if (VibeCache.getTagMap().containsValue(idArray[i])) {
        System.out.println("in the if");
        for (Tag t : VibeCache.getTagMap().keys()) {
          if (VibeCache.getTagMap().containsEntry(t, idArray[i])) {
            return t;
          }
        }
      }
      System.out.println("made it through the if");
    }
    System.out.println("returning null");
    return null;
  }
}
