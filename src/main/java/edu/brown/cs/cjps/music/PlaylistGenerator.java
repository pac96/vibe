package edu.brown.cs.cjps.music;

import java.util.List;

import com.echonest.api.v4.Artist;
import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Song;
import com.echonest.api.v4.SongParams;

public class PlaylistGenerator {

  private static final String API_KEY = "OOT8LZ0VYRFYT5YYK";
  EchoNestAPI _echoNest;

  public PlaylistGenerator() {
    _echoNest = new EchoNestAPI(API_KEY);

    this.testConnection();
    // this.makePlaylist();
    SongParams p = new SongParams();
    p.setArtist("Michael Jackson");

    List<Song> songs = null;
    try {
      songs = _echoNest.searchSongs(p);
    } catch (EchoNestException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    try {

      this.dumpSong(songs.get(0));
    } catch (EchoNestException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

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

    // // Oldest Top artist test
    // List<Artist> artists100 = null;
    // try {
    // artists100 = _echoNest.topHotArtists(1);
    // } catch (EchoNestException e1) {
    // // TODO Auto-generated catch block
    // e1.printStackTrace();
    // }
    //
    // Collections.sort(artists100, new Comparator<Artist>() {
    // @Override
    // public int compare(Artist t1, Artist t2) {
    // try {
    // Long y1 = t1.getYearsActive().getRange()[0];
    // Long y2 = t2.getYearsActive().getRange()[0];
    // int i1 = y1 != null ? y1.intValue() : 0;
    // int i2 = y2 != null ? y2.intValue() : 0;
    // return i1 - i2;
    // } catch (EchoNestException e) {
    // return 0;
    // }
    // }
    // });
    //
    // for (Artist artist : artists100) {
    // YearsActive ya;
    // try {
    // ya = artist.getYearsActive();
    // Long earliest = ya.getRange()[0];
    // System.out.println(earliest + " " + artist.getName());
    // } catch (EchoNestException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //
    // }

  }

  // public List<Song> makePlaylist() {
  // BasicPlaylistParams params = new BasicPlaylistParams();
  // params.addArtist("Weezer");
  // params.setType(BasicPlaylistParams.PlaylistType.ARTIST_RADIO);
  // params.setResults(10);
  // Playlist playlist = null;
  // try {
  // playlist = _echoNest.createBasicPlaylist(params);
  // } catch (EchoNestException e) {
  // System.out.println("ERROR: could not create playlist");
  // e.printStackTrace();
  // }
  //
  // for (Song song : playlist.getSongs()) {
  // System.out.println(song.toString());
  // }
  // return playlist.getSongs();
  // }

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
}
