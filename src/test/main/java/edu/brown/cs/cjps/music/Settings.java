package edu.brown.cs.cjps.music;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.cjps.vibe.MusicEventTag.Tag;

public class Settings {

  private Tag tag;
  private List<String> genres;
  private float mood;
  private float hotness;
  private float energy;

  // Empty constructor
  public Settings() {

    tag = Tag.RESTFUL;
    genres = new ArrayList<String>();
    mood = 0;
    hotness = 0;
    energy = 0;

  }

  // All in one constructor
  public Settings(Tag t, List<String> initG, float m, float h, float e) {
    tag = t;
    genres = initG;
    mood = m;
    hotness = h;
    energy = e;
  }

  public void setTag(Tag t) {
    tag = t;
  }

  public void addGenre(String genre) {
    genres.add(genre);
  }

  public void setMood(float newMood) {
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

  public float getMood() {
    return mood;
  }

  public float getHotness() {
    return hotness;
  }

  public float getEnergy() {
    return energy;
  }
}
