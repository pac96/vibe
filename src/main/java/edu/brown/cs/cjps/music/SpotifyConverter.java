package edu.brown.cs.cjps.music;

import java.util.List;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.AddTrackToPlaylistRequest;
import com.wrapper.spotify.methods.PlaylistCreationRequest;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.User;

public class SpotifyConverter {

  public SpotifyConverter() {
  }

  public String makeSpotifyPlaylist(String eventName, Api api, User user,
      List<String> trackList) {

    // MAKING THE PLAYLIST

    final PlaylistCreationRequest request = api
        .createPlaylist(user.getId(), eventName).publicAccess(true).build();
    String playlistID = null;
    String playlistURI = "";
    Playlist playlist = null;
    try {
      playlist = request.get();
      playlistID = playlist.getId();
      playlistURI = playlist.getUri();

      System.out.println("Playlist created with title " + playlist.getName()
          + " and link " + playlistURI);
    } catch (Exception e) {
      System.out.println("P1: Something went wrong!" + e.getMessage());
      return null;
    }

    // FILLING THE PLAYLIST

    // Index starts at 0
    final int insertIndex = 0;

    final AddTrackToPlaylistRequest addRequest = api
        .addTracksToPlaylist(user.getId(), playlistID, trackList)
        .position(insertIndex).build();

    try {
      addRequest.get();
    } catch (Exception e) {
      System.out.println("P2: Something went wrong!" + e.getMessage());
      return null;
    }
    // }
    System.out.println("Tracklist: " + trackList);
    return playlistURI;
  }

}
