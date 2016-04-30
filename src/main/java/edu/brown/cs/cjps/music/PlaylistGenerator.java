package edu.brown.cs.cjps.music;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
    params.addIDSpace("spotify");
    params.setType(PlaylistParams.PlaylistType.ARTIST);
    params.addArtist("Jimi Hendrix");
    params.includeTracks();
    params.setLimit(true);
    params.setAdventurousness(0);

    Playlist playlist = en.createStaticPlaylist(params);

    List<String> tracks = new ArrayList<>();

    String stringURL = "http://developer.echonest.com/api/v4/song/search?api_key=OOT8LZ0VYRFYT5YYK&format=json&results=1&artist=radiohead&title=karma%20police&bucket=id:spotify&bucket=tracks&limit=true";
    URL actualURL = null;
    try {
      actualURL = new URL(stringURL);
    } catch (MalformedURLException e1) {
      System.out.println("ERROR: Bad URL");
    }
    InputStream response = null;
    try {
      response = actualURL.openStream();
      // update to time of this call

    } catch (IOException e) {
      // Return and try again in one second - shouldn't interrupt anyone else
      e.printStackTrace();
    }

    java.util.Scanner s = new java.util.Scanner(response).useDelimiter("\\A");
    String stringVersion = s.hasNext() ? s.next() : "";
    // If there are no updates, [] is returned. In this case, no need to
    // continue
    System.out.println(stringVersion);

    Pattern pattern = Pattern.compile("\"spotify:track:(.*?)\"");

    Matcher matcher = pattern.matcher(stringVersion);

    if (matcher.find()) {
      String found = matcher.group(0);
      // System.out.println("regex found " + found);
      found = found.substring(1, found.length() - 2);
      System.out.println("AND NOW " + found);
      tracks.add(found);
    }
    try {
      response.close();
    } catch (IOException e) {
      System.out.println("ERROR: error closing stream");
    }
    s.close();

    System.out.println(tracks);
    return tracks;
  }

}
