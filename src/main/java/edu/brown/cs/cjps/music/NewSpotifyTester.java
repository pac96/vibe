package edu.brown.cs.cjps.music;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.models.User;

public class NewSpotifyTester {

  private Api api;
  private User user;
  private String accessToken;

  public NewSpotifyTester(Api api, User curentUser, String accessToken) {
    this.api = api;
    this.user = curentUser;
    this.accessToken = accessToken;

    System.out.println(accessToken);

    // try {
    // //this.recommendations();
    // } catch (MalformedURLException e) {
    // // TODO Auto-generated catch block
    // System.out.println("MALFORMED URL");
    // e.printStackTrace();
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // System.out.println("IO EXCEPTION");
    // e.printStackTrace();
    // }
  }

  public String testTrack() throws MalformedURLException, IOException {
    String request = "https://api.spotify.com/v1/tracks/2zYzyRzz6pRmhPzyfMEC8s";
    URLConnection connection = new URL(request).openConnection();
    connection.setRequestProperty("Host", "api.spotify.com");
    connection.setRequestProperty("Accept", "application/json");
    connection.setRequestProperty("Content-Type", "application/json");
    // connection.setRequestProperty("Accept-Encoding",
    // "gzip, deflate, compress");
    connection.setRequestProperty("Authorization", "Bearer " + accessToken);
    connection.setRequestProperty("User-Agent", "Spotify API Console v0.1");
    InputStream response = connection.getInputStream();

    java.util.Scanner s = new java.util.Scanner(response).useDelimiter("\\A");
    String stringVersion = s.hasNext() ? s.next() : "";
    // System.out.println(response);

    System.out.println(stringVersion);
    JsonParser jparser = new JsonParser();
    JsonElement je = jparser.parse(stringVersion);
    JsonObject jo = je.getAsJsonObject();
    System.out.println("where are we getting stuck");
    JsonElement uri = jo.get("uri");
    String trackString = uri.getAsString();
    System.out.println("what is confusing here");
    System.out.println(trackString);
    return trackString;
  }

  public String recommendations() throws MalformedURLException, IOException {
    // String recString = "https://api.spotify.com/v1/recommendations";
    String recString = "https://api.spotify.com/v1/recommendations?min_popularity=50&min_energy=0.4&seed_tracks=0c6xIDDpzE81m2q797ordA&market=US&seed_artists=4NHQUGzhtTLFvgF5SZesLK";

    URLConnection connection = new URL(recString).openConnection();
    connection.setRequestProperty("Host", "api.spotify.com");
    connection.setRequestProperty("Accept", "application/json");
    connection.setRequestProperty("Content-Type", "application/json");
    // connection.setRequestProperty("Accept-Encoding",
    // "gzip, deflate, compress");
    connection.setRequestProperty("Authorization", "Bearer " + accessToken);
    connection.setRequestProperty("User-Agent", "Spotify API Console v0.1");
    InputStream response = connection.getInputStream();
    System.out.println(response);

    // java.util.Scanner s = new
    // java.util.Scanner(response).useDelimiter("\\A");
    // String stringVersion = s.hasNext() ? s.next() : "";

    final int bufferSize = 1024;
    final char[] buffer = new char[bufferSize];
    final StringBuilder out = new StringBuilder();
    Reader in = new InputStreamReader(response, "UTF-8");
    for (;;) {
      int rsz = in.read(buffer, 0, buffer.length);
      if (rsz < 0)
        break;
      out.append(buffer, 0, rsz);
    }
    String stringVersion = out.toString();

    System.out.println(stringVersion);
    JsonParser jparser = new JsonParser();
    JsonElement je = jparser.parse(stringVersion);
    System.out
        .println("------------------------------------------------------------------------------------");
    JsonObject jo = je.getAsJsonObject();
    JsonArray jarray = jo.getAsJsonArray("tracks");
    System.out.println(jarray);
    System.out.println("done");
    JsonObject tracID = jarray.get(jarray.size() - 1).getAsJsonObject();
    String s = tracID.get("uri").toString();
    System.out.println(s);
    return s;
  }
}
