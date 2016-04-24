package edu.brown.cs.cjps.music;

import java.util.List;

import com.echonest.api.v4.Playlist;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.models.User;

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

  public void generateFromTag(String eventID, String tag) {
    Settings defaults;
    switch (tag) {
    case "WorkStudy":
      defaults = def.getWorkStudyDefaults();
      break;
    case "EatSocial":
      defaults = def.getEatSocialDefaults();
      break;
    case "Exercise":
      defaults = def.getExerciseDefaults();
      break;
    case "Party":
      defaults = def.getPartyDefaults();
      break;
    // Restful is default
    default:
      defaults = def.getRestfulDefaults();
    }

    Playlist p = pg.makePlaylist(defaults);
    // // System.out.println(songs);
    // for (Song s : songs) {
    // System.out.println(s.getTitle());
    // System.out.println(" " + s.getArtistName());
    // try {
    // // System.out.println(" " + s.getSongHotttnesss());
    // System.out.println(" " + s.getArtistHotttnesss());
    //
    // } catch (EchoNestException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //
    // }
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

}
