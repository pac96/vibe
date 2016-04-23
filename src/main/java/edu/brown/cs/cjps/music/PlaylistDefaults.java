package edu.brown.cs.cjps.music;

import java.util.Arrays;
import java.util.List;

public class PlaylistDefaults {

  public PlaylistDefaults() {

  }

  // TODO wasting work regenerating these, could make them all at once
  public Settings getWorkStudyDefaults() {
    List<String> g = Arrays.asList("Classical", "Jazz");
    String m = "Relaxing";
    float hotness = 0.4f;
    float energy = 0.3f;
    return new Settings(g, m, hotness, energy);
  }

  public Settings getEatSocialDefaults() {
    List<String> g = Arrays.asList("Pop", "Rock", "Folk");
    String m = "Happy";
    float hotness = 0.6f;
    float energy = 0.4f;
    return new Settings(g, m, hotness, energy);
  }

  public Settings getExerciseDefaults() {
    List<String> g = Arrays.asList("Rap", "Hip hop", "Rock");
    float hotness = 0.7f;
    float energy = 1.0f;
    String m = "Excited";
    return new Settings(g, m, hotness, energy);
  }

  public Settings getPartyDefaults() {
    List<String> g = Arrays.asList("Pop", "Hip hop");
    float hotness = 1.0f;
    float energy = 1.0f;
    String m = "Excited";
    return new Settings(g, m, hotness, energy);
  }

  public Settings getRestfulDefaults() {
    List<String> g = Arrays.asList("Classical");
    float hotness = 0.3f;
    float energy = 0.2f;
    String m = "Relaxing";
    return new Settings(g, m, hotness, energy);
  }

}
