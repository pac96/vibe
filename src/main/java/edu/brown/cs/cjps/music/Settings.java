package edu.brown.cs.cjps.music;

import java.util.ArrayList;
import java.util.List;

public class Settings {

  private List<String> genres;
  private String mood;
  private float hotness;
  private float energy;

  // Empty constructor
  public Settings() {

    genres = new ArrayList<String>();
    mood = null;
    hotness = 0;
    energy = 0;

  }

  // All in one constructor
  public Settings(List<String> initG, String m, float h, float e) {
    genres = initG;
    mood = m;
    hotness = h;
    energy = e;
  }

  public void addGenre(String genre) {
    // TODO: There should be a master list/hashset of genres somewhere to check
    // these against
    genres.add(genre);
  }

  public void setMood(String newMood) {
    // TODO: Same with mood
    mood = newMood;
  }

  public void setHotness(float hot) {
    hotness = hot;
  }

  public void setEnergy(float en) {
    energy = en;
  }

  public List<String> getGenres() {
    return genres;
  }

  public String getMood() {
    return mood;
  }

  public float getHotness() {
    return hotness;
  }

  public float getEnergy() {
    return energy;
  }
}
