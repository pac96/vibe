package edu.brown.cs.cjps.music;

import java.util.List;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.AddTrackToPlaylistRequest;
import com.wrapper.spotify.methods.PlaylistCreationRequest;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.User;

public class SpotifyConverter {

  private Api api;
  private User user;
  private List<String> trackList;
  private String playlistID;

  public SpotifyConverter(Api api, User currentUser, List<String> tracks) {
    this.api = api;
    user = currentUser;
    trackList = tracks;
    this.makeSpotifyPlaylist();
  }

  public String makeSpotifyPlaylist() {
    final PlaylistCreationRequest request = api
        .createPlaylist(user.getId(), "title")
        .publicAccess(true)
        .build();
    
    System.out.println("request: " + request.toString());
    playlistID = null;
    try {
      final Playlist playlist = request.get();
      playlistID = playlist.getId();

      System.out.println("You just created this playlist!");
      System.out.println("Its title is " + playlist.getName());
    } catch (Exception e) {
      System.out.println("P1: Something went wrong!" + e.getMessage());
      e.printStackTrace();
    }

    // Index starts at 0
    final int insertIndex = 0;

    final AddTrackToPlaylistRequest addRequest = api
        .addTracksToPlaylist(user.getId(), playlistID, trackList)
        .position(insertIndex).build();

    try {
      addRequest.get();
    } catch (Exception e) {
      System.out.println("P2: Something went wrong!" + e.getMessage());
    }
    return null;
  }

  public String getSpotifyPlaylistID() {
    return playlistID;
  }

}
