package edu.brown.cs.cjps.musictests;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.brown.cs.cjps.music.Settings;
import edu.brown.cs.cjps.vibe.MusicEventTag.Tag;

public class SettingsTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void constructorTest() {
    Settings s = new Settings();
    assert (s != null);
    Settings s2 = new Settings(Tag.PARTY, Arrays.asList("classical"), 0.4f, 50,
        0.3f);
    assert (s2 != null);
  }

  @Test
  public void getsetTest() {
    Settings s2 = new Settings(Tag.PARTY, Arrays.asList("classical"), 0.4f, 50,
        0.3f);
    assert (s2.getTag() == Tag.PARTY);
    assert (s2.getGenres().contains("classical"));
    assert (s2.getEnergy() == 0.3f);
    assert (s2.getHotness() == 50);
    assert (s2.getMood() == 0.4f);
  }

}
