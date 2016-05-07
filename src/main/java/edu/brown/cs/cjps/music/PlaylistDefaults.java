package edu.brown.cs.cjps.music;

import java.util.Arrays;
import java.util.List;

import edu.brown.cs.cjps.vibe.MusicEventTag.Tag;

public class PlaylistDefaults {

  public PlaylistDefaults() {

  }

  // TODO wasting work regenerating these, could make them all at once
  public Settings getWorkStudyDefaults() {
    List<String> g = Arrays.asList("classical", "jazz");
    float m = 0.8f;
    int hotness = 40;
    float energy = 0.3f;
    return new Settings(Tag.WORKSTUDY, g, m, hotness, energy);
  }

  public Settings getEatSocialDefaults() {
    List<String> g = Arrays.asList("pop", "rock", "folk");
    float m = 0.6f;
    int hotness = 60;
    float energy = 0.4f;
    return new Settings(Tag.EATSOCIAL, g, m, hotness, energy);
  }

  public Settings getExerciseDefaults() {
    List<String> g = Arrays.asList("hip-hop", "rock");
    int hotness = 70;
    float energy = 1.0f;
    float m = 0.5f;
    return new Settings(Tag.EXERCISE, g, m, hotness, energy);
  }

  public Settings getPartyDefaults() {
    List<String> g = Arrays.asList("pop", "hip-hop");
    int hotness = 100;
    float energy = 1.0f;
    float m = 0.8f;
    return new Settings(Tag.PARTY, g, m, hotness, energy);
  }

  public Settings getRestfulDefaults() {
    List<String> g = Arrays.asList("classical");
    int hotness = 30;
    float energy = 0.2f;
    float m = 0.5f;
    return new Settings(Tag.RESTFUL, g, m, hotness, energy);
  }

}
