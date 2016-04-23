package edu.brown.cs.cjps.music;

import java.util.List;

import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Song;

public class PlaylistHQ {

  private PlaylistDefaults def;
  private PlaylistGenerator pg;

  public PlaylistHQ() {

    def = new PlaylistDefaults();
    pg = new PlaylistGenerator();
  }

  public void generateFromTag(String tag) {
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

    List<Song> songs = pg.makePlaylist(defaults);
    // System.out.println(songs);
    for (Song s : songs) {
      System.out.println(s.getTitle());
      System.out.println(" " + s.getArtistName());
      try {
        // System.out.println(" " + s.getSongHotttnesss());
        System.out.println(" " + s.getArtistHotttnesss());

      } catch (EchoNestException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }
  }

  public void generateGeneric() {
    Settings s = def.getRestfulDefaults();
    List<Song> songs = pg.makePlaylist(s);
  }

  public void generateCustom(List<String> params) {
    // TODO: Convert the params to a setting and pass to make playlist
  }

  private String convertForSpotify() {
    return null;
  }

}
