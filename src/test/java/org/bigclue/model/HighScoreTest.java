package org.bigclue.model;

import junit.framework.TestCase;
import org.bigclue.model.HighScores;

/**
 * Class for...
 * Author: Randy Wilson
 * Date: 9/14/13
 */
public class HighScoreTest extends TestCase {
  public void testHighScore() {
    HighScores.HighScore hs = new HighScores.HighScore("Linette,999");
    assertEquals("Linette", hs.getName());
    assertEquals(999, hs.getScore());

    HighScores highScores = new HighScores(new String[]{"Linette,999", "Randy,888", "Linette,666"});
    highScores.insertScore("Linette", 1000);
    highScores.insertScore("Randy", 777);
    highScores.insertScore("Randy2", 777);
    highScores.insertScore("Linette", 555);

    int i = 0;
    for (String s : new String[]{"Linette,1000", "Linette,999", "Randy,888", "Randy,777", "Randy2,777", "Linette,666", "Linette,555"}) {
      HighScores.HighScore highScore = new HighScores.HighScore(s);
      assertEquals(highScore.getName(), highScores.getScores().get(i).getName());
      assertEquals(highScore.getScore(), highScores.getScores().get(i).getScore());
      i++;
    }

    // Add 14 more scores.  All should get added.
    for (i = 0; i < 13; i++) {
      assertTrue(highScores.insertScore("Sienna", 412 - i));
    }

    // This one is too low, so shouldn't get added.
    assertFalse(highScores.insertScore("Bubba", 300));

    // This one is high, so should.
    assertTrue(highScores.insertScore("Bubba", 500));
    assertEquals(500, highScores.getScores().get(7).getScore());
    assertEquals(401, highScores.getScores().get(19).getScore());
    assertEquals(20, highScores.getScores().size());
  }
}