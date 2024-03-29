package edu.brown.cs.cjps.music;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.echonest.api.v4.EchoNestAPI;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.models.User;

public class PlaylistGenerator {

  private static final String API_KEY = "OOT8LZ0VYRFYT5YYK";
  EchoNestAPI _echoNest;

  public PlaylistGenerator() {
    _echoNest = new EchoNestAPI(API_KEY);

  }

  // Returns a list of track URIs
  public VibePlaylist makePlaylist(Settings s, Api api, User curentUser,
      String accessToken) {
    System.out.println("Makin a playlist");
    List<String> genres = s.getGenres();
    float mood = s.getMood();
    float hotness = s.getHotness();
    float energy = s.getEnergy();
    int numTracks = 20;

    // Convert the genre list into a string
    String genreString = "";
    for (int i = 0; i < genres.size() - 2; i++) {
      genreString = genreString + genres.get(i) + ",";
    }
    // Add last one
    genreString = genreString + genres.get(genres.size() - 1);
    System.out.println(genreString);

    // FOR TESTS
    int inthotness = 30;
    // Connection to recommendations
    String recString = "https://api.spotify.com/v1/recommendations?target_energy="
        + String.valueOf(energy)
        + "&target_popularity="
        + String.valueOf(inthotness)
        + "&target_valence="
        + mood
        + "&limit=20&seed_genres=" + genreString + "&market=US";

    System.out.println("where are we sticking");
    URLConnection connection = null;
    try {
      connection = new URL(recString).openConnection();
    } catch (MalformedURLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    System.out.println("running out of options");
    connection.setRequestProperty("Host", "api.spotify.com");
    connection.setRequestProperty("Accept", "application/json");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Authorization", "Bearer " + accessToken);
    connection.setRequestProperty("User-Agent", "Spotify API Console v0.1");
    System.out.println("for places we might be stuck");
    InputStream response = null;
    try {
      response = connection.getInputStream();
    } catch (IOException e) {
      System.out.println("FOUND YOU");
      e.printStackTrace();
    }
    System.out.println(response);
    java.util.Scanner scanner = new java.util.Scanner(response)
        .useDelimiter("\\A");
    String stringVersion = scanner.hasNext() ? scanner.next() : "";
    System.out.println(stringVersion);
    JsonParser jparser = new JsonParser();
    JsonElement je = jparser.parse(stringVersion);
    JsonObject jo = je.getAsJsonObject();
    JsonArray jarray = jo.getAsJsonArray("tracks");
    List<String> trackList = new ArrayList<>();
    for (int i = 0; i < jarray.size(); i++) {
      JsonObject firstTrack = jarray.get(i).getAsJsonObject();
      JsonElement uri = firstTrack.get("uri");
      String trackString = uri.getAsString();
      trackList.add(trackString);
    }

    VibePlaylist vp = new VibePlaylist(trackList, s);
    System.out.println("End of playlist making");
    return vp;
  }

}
