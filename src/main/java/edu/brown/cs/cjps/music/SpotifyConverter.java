package edu.brown.cs.cjps.music;

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

  public String makeSpotifyPlaylist(String eventID, Api api, User user,
      List<String> trackList) {

    // MAKING THE PLAYLIST

    final PlaylistCreationRequest request = api
        .createPlaylist(user.getId(), eventID).publicAccess(true).build();
    String playlistID = null;
    String playlistURI = "";
    Playlist playlist = null;
    try {
      playlist = request.get();
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

    // FILLING THE PLAYLIST

    // Index starts at 0
    final int insertIndex = 0;

    final AddTrackToPlaylistRequest addRequest = api
        .addTracksToPlaylist(user.getId(), playlistID, trackList)
        .position(insertIndex).build();

    try {
      addRequest.get();
      System.out.println("SUCCESS");
    } catch (Exception e) {
      System.out.println("P2: Something went wrong!" + e.getMessage());
      e.printStackTrace();
    }
    // }
    System.out.println("Tracklist: " + trackList);
    return playlistURI;
  }

}
