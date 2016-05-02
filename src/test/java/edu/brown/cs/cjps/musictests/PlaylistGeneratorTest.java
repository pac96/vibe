package edu.brown.cs.cjps.musictests;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.brown.cs.cjps.music.Settings;
import edu.brown.cs.cjps.vibe.MusicEventTag.Tag;

public class PlaylistGeneratorTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void basicSettingsTest() {
    Settings set = new Settings(Tag.RESTFUL, Arrays.asList("classical"), 0.5f,
        50f, 0.5f);

  }

}
