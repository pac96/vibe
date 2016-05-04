package edu.brown.cs.cjps.music;

import java.util.List;

public class VibePlaylist {

  private List<String> trackList;
  private Settings settings;

  public VibePlaylist(List<String> tracks, Settings s) {
    trackList = tracks;
    settings = s;

  }

  public List<String> getTracks() {
    return trackList;
  }
}
