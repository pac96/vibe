package edu.brown.cs.cjps.music;

import java.util.List;

import com.echonest.api.v4.Playlist;
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

  public void generateFromTag(String eventID) {
    Tag tag = this.findTag(eventID);
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

    Playlist p = pg.makePlaylist(defaults);

    VibePlaylist newVPlaylist = new VibePlaylist(p);
    VibeCache.getPlaylistCache().put(eventID, newVPlaylist);

  }

  // public void generateGeneric() {
  // Settings s = def.getRestfulDefaults();
  // List<Song> songs = pg.makePlaylist(s);
  // }

  public void generateCustom(List<String> params) {
    // TODO: Convert the params to a setting and pass to make playlist
  }

  public String convertForSpotify(String eventID, Api spotifyAPI,
      User spotifyUser) {
    VibeCache.getPlaylistCache().get(eventID);
    List<String> trackIDs = null;
    // TODO get trackids somehow
    String id = sc.makeSpotifyPlaylist(spotifyAPI, spotifyUser, trackIDs);
    return id;
  }

  private Tag findTag(String eventID) {
    String[] idArray = eventID.split(" ");
    for (int i = 0; i < idArray.length; i++) {
      if (VibeCache.getTagMap().containsValue(idArray[i])) {
        for (Tag t : VibeCache.getTagMap().keys()) {
          if (VibeCache.getTagMap().containsEntry(t, idArray[i])) {
            return t;
          }
        }
      }
    }
    return null;
  }
}
