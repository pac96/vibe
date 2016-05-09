package edu.brown.cs.cjps.musictests;

import org.junit.Test;

import edu.brown.cs.cjps.music.PlaylistDefaults;
import edu.brown.cs.cjps.music.Settings;

public class PlaylistDefaultsTest {

  @Test
  public void testWorkStudy() {
    PlaylistDefaults pd = new PlaylistDefaults();
    Settings s = pd.getWorkStudyDefaults();
    assert (s.getGenres().contains("classical"));
    assert (s.getGenres().contains("jazz"));
    assert (s.getHotness() == 40);
    assert (s.getEnergy() == 0.4f);
    assert (s.getMood() == 0.6f);
  }

  @Test
  public void testParty() {
    PlaylistDefaults pd = new PlaylistDefaults();
    Settings s = pd.getWorkStudyDefaults();
    assert (s.getGenres().contains("pop"));
    assert (s.getGenres().contains("hip-hop"));
    assert (s.getHotness() == 100);
    assert (s.getEnergy() == 1.0f);
    assert (s.getMood() == 0.8f);
  }

}
