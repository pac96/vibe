package edu.brown.cs.cjps.music;

import java.util.ArrayList;
import java.util.List;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.AddTrackToPlaylistRequest;
import com.wrapper.spotify.methods.PlaylistCreationRequest;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.User;

public class SpotifyConverter {

  // private Api api;
  // private User user;
  // private List<String> trackList;
  // private String playlistID;

  public SpotifyConverter() {
    // this.api = api;
    // user = currentUser;
    // trackList = tracks;
    // this.makeSpotifyPlaylist(api, user, trackList);
  }

  public String makeSpotifyPlaylist(Api api, User user, List<String> trackList) {
    System.out.println("in make spotify");
    // final PlaylistCreationRequest request = api
    // .createPlaylist(user.getId(), "title").publicAccess(true).build();
    final PlaylistCreationRequest request = api
        .createPlaylist("pacdaboss", "title").publicAccess(true).build();

    System.out.println("request: " + request.toString());
    String playlistID = null;
    String playlistURI = "";
    try {
      final Playlist playlist = request.get();
      playlistID = playlist.getId();
      playlistURI = playlist.getUri();
      System.out.println(playlistID);
      System.out.println("uri: " + playlist.getUri());

      System.out.println("You just created this playlist!");
      System.out.println("Its title is " + playlist.getName() + " and link is "
          + playlistURI);
    } catch (Exception e) {
      System.out.println("P1: Something went wrong!" + e.getMessage());
      e.printStackTrace();
    }

    // Index starts at 0
    final int insertIndex = 0;
    for (int i = 0; i < trackList.size(); i++) {
      List<String> testList = new ArrayList<>();
      // testList.add(trackList.get(i));
      // Builder track = api.getTrack("spotify:track:4BYGxv4rxSNcTgT3DsFB9o");
      // System.out.println(track);
      testList.add("spotify:track:6zHrqd3mvAtIMoqDpw0dW");
      // "spotify:track:5YtKNQRPr4WE4EW1q2KJ8y"

      final AddTrackToPlaylistRequest addRequest = api
          .addTracksToPlaylist(user.getId(), playlistID, testList)
          .position(insertIndex).build();
      // System.out.println(trackList);

      try {
        addRequest.get();
        System.out.println("SUCCESS");
      } catch (Exception e) {
        System.out.println("P2: Something went wrong!" + e.getMessage());
      }
    }
    return playlistURI;
  }

  // public String getSpotifyPlaylistID() {
  // return playlistID;
  // }

}
