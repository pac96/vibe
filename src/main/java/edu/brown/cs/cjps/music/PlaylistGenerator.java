package edu.brown.cs.cjps.music;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.models.User;

public class PlaylistGenerator {

  public PlaylistGenerator() {
  }

  // Returns a list of track URIs
  public VibePlaylist makePlaylist(Settings s, int length, Api api,
      User curentUser, String accessToken) {

    // Error check
    if (s == null || length < 0 || api == null || curentUser == null
        || accessToken == null) {
      return null;
    }

    List<String> genres = s.getGenres();
    float mood = s.getMood();
    int hotness = s.getHotness();
    float energy = s.getEnergy();
    int numTracks = length / 3;
    if (numTracks > 100) {
      numTracks = 100;
    }
    if (numTracks < 1) {
      numTracks = 1;
    }

    // Convert the genre list into a string
    String genreString = "";
    for (int i = 0; i < genres.size() - 2; i++) {
      genreString = genreString + genres.get(i) + ",";
    }
    // Add last one
    genreString = genreString + genres.get(genres.size() - 1);

    // Connection to recommendations
    String recString = "https://api.spotify.com/v1/recommendations?target_energy="
        + String.valueOf(energy)
        + "&target_popularity="
        + String.valueOf(hotness)
        + "&target_valence="
        + mood
        + "&limit="
        + numTracks + "&seed_genres=" + genreString + "&market=US";

    URLConnection connection = null;
    try {
      connection = new URL(recString).openConnection();
    } catch (MalformedURLException e1) {
      System.out.println("ERROR: Malformed URL");
      return null;
    } catch (IOException e1) {
      System.out.println("ERROR: IO Exception");
      return null;
    }
    connection.setRequestProperty("Host", "api.spotify.com");
    connection.setRequestProperty("Accept", "application/json");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Authorization", "Bearer " + accessToken);
    connection.setRequestProperty("User-Agent", "Spotify API Console v0.1");
    InputStream response = null;
    try {
      response = connection.getInputStream();
    } catch (IOException e) {
      System.out.println("ERROR: IO Exception");
      return null;
    }
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

    // Before returning, shuffle the track order to give variety to the
    // playlists
    Collections.shuffle(trackList);
    VibePlaylist vp = new VibePlaylist(trackList, s);
    return vp;
  }

}
