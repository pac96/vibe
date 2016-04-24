package edu.brown.cs.cjps.music;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.echonest.api.v4.Artist;
import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Playlist;
import com.echonest.api.v4.PlaylistParams;
import com.echonest.api.v4.PlaylistParams.PlaylistType;
import com.echonest.api.v4.Song;
import com.echonest.api.v4.Track;

public class PlaylistGenerator {

  private static final String API_KEY = "OOT8LZ0VYRFYT5YYK";
  EchoNestAPI _echoNest;

  public PlaylistGenerator() {
    _echoNest = new EchoNestAPI(API_KEY);

    // try {
    // //this.playlistTest();
    // } catch (EchoNestException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }

  }

  public Playlist makePlaylist(Settings s) {
    PlaylistParams params = new PlaylistParams();
    params.addIDSpace("spotify-WW");
    params.setType(PlaylistType.ARTIST_RADIO);

    // this.getArtistsByGenre(s.getGenres());

    // TODO: Gengre radio isn't real, so need to add artists who are the genre,
    // perhaps?
    // MOOD
    params.addMood(s.getMood());
    // Energy
    float minE = s.getEnergy() - 0.2f;
    float maxE = s.getEnergy() + 0.2f;
    if (minE < 0) {
      minE = 0;
    }
    if (maxE > 1) {
      maxE = 1f;
    }
    params.setMinEnergy(minE);
    params.setMaxEnergy(maxE);

    // HOTNESS
    float minH = s.getHotness() - 0.2f;
    float maxH = s.getHotness() + 0.2f;
    if (minH < 0) {
      minH = 0;
    }
    if (maxH > 1) {
      maxH = 1f;
    }
    // TODO: Nothing makes min hotness happy

    params.includeArtistHotttnesss();
    // params.setArtistMinHotttnesss(0.0f);
    params.setArtistMaxHotttnesss(maxH);
    params.addArtist("The Beatles");
    params.addArtist("The Rolling Stones");
    params.addArtist("Pink Floyd");
    params.addArtist("Queen");
    params.addArtist("Led Zepplin");

    params.includeTracks(); // IDK what this does but it was in the demo
    params.setLimit(true); // see above
    params.setResults(10);

    Playlist playlist = null;
    try {
      playlist = _echoNest.createStaticPlaylist(params);
    } catch (EchoNestException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return playlist;
  }

  // public List<Artist> getArtistsByGenre(List<String> genres) {
  // ArtistParams params = new ArtistParams();
  // // params.addGenre();
  // List<Artist> l = null;
  // try {
  // l = _echoNest.suggestArtists("rock");
  // } catch (EchoNestException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  // System.out.println(l);
  // return l;
  // }

  public void testConnection() {

    List<Artist> artists = null;
    try {
      artists = _echoNest.searchArtists("Weezer");
    } catch (EchoNestException e) {
      System.out.println("ERROR: Couldn't connect to echonest");
      e.printStackTrace();
    }

    if (artists.size() > 0) {
      Artist weezer = artists.get(0);
      try {
        System.out.println("Similar artists for " + weezer.getName());
        for (Artist simArtist : weezer.getSimilar(10)) {
          System.out.println("   " + simArtist.getName());
        }
      } catch (EchoNestException e) {
        System.out.println("ERROR: couldn't get artists");
        e.printStackTrace();
      }

    }

  }

  public void dumpSong(Song song) throws EchoNestException {
    System.out.printf("%s\n", song.getTitle());
    System.out.printf("   artist: %s\n", song.getArtistName());
    // System.out.printf("   dur   : %.3f\n", song.getDuration());
    System.out.printf("   BPM   : %.3f\n", song.getTempo());
    System.out.printf("   Mode  : %d\n", song.getMode());
    System.out.printf("   S hot : %.3f\n", song.getSongHotttnesss());
    System.out.printf("   A hot : %.3f\n", song.getArtistHotttnesss());
    System.out.printf("   A fam : %.3f\n", song.getArtistFamiliarity());
    System.out.printf("   A loc : %s\n", song.getArtistLocation());
  }

  public List<String> playlistTest() throws EchoNestException {
    System.out.println("playlisttest");
    EchoNestAPI en = new EchoNestAPI(API_KEY);
    PlaylistParams params = new PlaylistParams();
    params.addIDSpace("spotify-WW");
    params.setType(PlaylistParams.PlaylistType.ARTIST_RADIO);
    params.addArtist("Michael Jackson");
    params.includeTracks();
    params.setLimit(true);

    Playlist playlist = en.createStaticPlaylist(params);
    Song testSong = playlist.getSongs().get(0);
    System.out.println("ID is " + testSong.getID());
    Track t = testSong.getTrack("spotify");
    System.out.println("trak1 is " + t);
    System.out.println(testSong.toString());
    System.out.println("and now...");
    // REGEX
    String test = "hello'this'and I hope'this' it works";
    // Pattern pattern = Pattern.compile(":\"(.*?)\"");
    Pattern pattern = Pattern
        .compile("tracks\":\\[\\{\"foreign_id\":\"(.*?)\"");
    System.out.println("which line");
    Matcher matcher = pattern.matcher(testSong.toString());
    System.out.println("is failing me");
    if (matcher.find()) {
      String found = matcher.group(0);
      System.out.println("regex found " + found);
      found = found.substring(24, found.length() - 2);
      System.out.println("AND NOW " + found);
    }
    System.out.println("I do not know");
    // System.out.println(testSong.getAnalysisURL());
    // System.out.println(testSong.getTrackNew("spotify-WW"));
    // System.out.println(testSong.getTrackOld("spotify-WW"));
    // Track t = testSong.getTrack("spotify-WW");
    // System.out.println("track is " + t);
    // System.out.println("Audio url: " + t.getAudioUrl());
    // System.out.println(t.getForeignID());
    // System.out.println(t.getSongID());
    // System.out.println(t.getID());
    // System.out.println(t.getAnalysisURL());
    // System.out.println(testSong);
    // System.out.println(testSong.getForeignID());

    List<String> tracks = new ArrayList<>();
    for (Song song : playlist.getSongs()) {
      // System.out.println(song);
      Track track = song.getTrack("spotify-WW");
      System.out.println("right before the question line");
      // tracks.add(track.getID());
    }
    System.out.println("The track list in PlaylistGenerator");
    System.out.println(tracks);
    return tracks;
  }
}
