package edu.brown.cs.cjps.music;

import java.util.ArrayList;
import java.util.List;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.UserPlaylistsRequest;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.SimplePlaylist;
import com.wrapper.spotify.models.User;

public class PlaylistGetter {

  public PlaylistGetter() {

  }

  public List<String[]> getAllUserPlaylists(Api api, User currentUser) {
    List<String[]> playlistList = new ArrayList<>();
    final UserPlaylistsRequest request = api.getPlaylistsForUser(
        currentUser.getId()).build();
    try {
      final Page<SimplePlaylist> playlistsPage = request.get();

      for (SimplePlaylist playlist : playlistsPage.getItems()) {
        String[] pObject = { playlist.getName(), playlist.getUri() };

        playlistList.add(pObject);
      }
    } catch (Exception e) {
      System.out.println("Something went wrong!" + e.getMessage());
      return null;
    }
    return playlistList;
  }

}
