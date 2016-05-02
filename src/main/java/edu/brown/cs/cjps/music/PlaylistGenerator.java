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

    // try {
    // //this.playlistTest();
    // } catch (EchoNestException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }

  }

  // Returns a list of track URIs
  public VibePlaylist makePlaylist(Settings s, Api api, User curentUser,
      String accessToken) throws MalformedURLException, IOException {
    List<String> genres = s.getGenres();
    String mood = s.getMood();
    float hotness = s.getHotness();
    float energy = s.getEnergy();
    int numTracks = 20;

    // Hotness ranges
    float min_hot = hotness - 30;
    if (min_hot < 0) {
      min_hot = 0;
    }
    float max_hot = hotness + 30;
    if (max_hot > 100) {
      max_hot = 100;
    }

    // Energy ranges
    float min_e = energy - 30;
    if (min_e < 0) {
      min_e = 0;
    }
    float max_e = energy + 30;
    if (max_e > 100) {
      max_e = 100;
    }

    // Convert the genre list into a string
    String genreString = "";
    for (int i = 0; i < genres.size() - 2; i++) {
      genreString = genreString + genres.get(i) + ",";
    }
    // Add last one
    genreString = genreString + genres.get(genres.size() - 1);
    System.out.println(genreString);

    // Connection to recommendations
    String recString = "https://api.spotify.com/v1/recommendations?min_energy="
        + String.valueOf(min_e) + "&max_popularity=" + String.valueOf(max_hot)
        + "&limit=20&seed_genres=" + genreString + "min_popularity="
        + String.valueOf(min_hot) + "&market=US&max_energy="
        + String.valueOf(max_e);

    URLConnection connection = new URL(recString).openConnection();
    connection.setRequestProperty("Host", "api.spotify.com");
    connection.setRequestProperty("Accept", "application/json");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Authorization", "Bearer " + accessToken);
    connection.setRequestProperty("User-Agent", "Spotify API Console v0.1");
    InputStream response = connection.getInputStream();

    java.util.Scanner scanner = new java.util.Scanner(response)
        .useDelimiter("\\A");
    String stringVersion = scanner.hasNext() ? scanner.next() : "";

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

    return vp;
  }

}
