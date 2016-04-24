package edu.brown.cs.cjps.music;

import com.echonest.api.v4.Playlist;

public class VibePlaylist {

  private Playlist echoP;

  public VibePlaylist(Playlist p) {
    echoP = p;
  }

  public Playlist getEchoPlaylist() {
    return echoP;
  }
}
