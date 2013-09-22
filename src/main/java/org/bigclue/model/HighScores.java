package org.bigclue.model;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Class for tracking high scores
 * Author: Randy Wilson
 * Date: 9/14/13
 */
public class HighScores {
  private static final int maxScores = 20;

  List<HighScore> scores = new ArrayList<HighScore>();
  Preferences preferences;

  public HighScores() {
    preferences = Preferences.systemNodeForPackage(HighScores.class);
    for (int i = 0; i < maxScores; i++) {
      String highScoreLine = preferences.get("High score " + (i + 1), null);
      if (highScoreLine != null) {
        scores.add(new HighScore(highScoreLine));
      }
      else {
        break; // hit the last high score
      }
    }
  }

  public HighScores(String[] lines) {
    for (int i = 0; i < maxScores; i++) {
      String highScoreLine = i < lines.length ? lines[i] : null;
      if (highScoreLine != null) {
        scores.add(new HighScore(highScoreLine));
      }
      else {
        break; // hit the last high score
      }
    }
  }

  public List<HighScore> getScores() {
    return scores;
  }

  public boolean addScore(String name, int score) {
    if (insertScore(name, score)) {
      for (int i = 0; i < scores.size(); i++) {
        preferences.put("High score " + (i + 1), scores.get(i).toString());
      }
      return true;
    }
    return false;
  }

  /**
   * Add a new score to the list, if it is higher than any of the top 20 scores.
   * @param name - name of player who got this score
   * @param score - score
   * @return true if this score turned out to be in the top 20.
   */
  public boolean insertScore(String name, int score) {
    // See if (a) the list isn't full yet, in which case EVERY score is a new one;
    // or (b) the list is indeed full, but the new score is higher than the last one in the list.
    if (scores.size() < maxScores || score > scores.get(scores.size() - 1).getScore()) {
      if (scores.size() == maxScores) {
        // The list is full, so remove the last one, since we know it is lower than the new high score.
        scores.remove(scores.size() - 1);
      }
      // At this point, there is at least one free spot in the list, so find the first element in the
      // list less than the new score, and insert this element there.
      HighScore highScore = new HighScore(name, score);
      scores.add(highScore); // add the new score to the bottom--it will work its way up if it isn't last.
      for (int i = scores.size() - 1; i > 0 && score > scores.get(i - 1).getScore();  i--) {
        scores.set(i, scores.get(i - 1));
        scores.set(i - 1, highScore);
      }
      return true;
    }
    return false;
  }

  public static class HighScore {
    String name;
    int score;

    public HighScore(String line) {
      int pos = line.lastIndexOf(",");
      name = line.substring(0, pos);
      score = Integer.parseInt(line.substring(pos + 1));
    }

    public HighScore(String name, int score) {
      this.name = name;
      this.score = score;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getScore() {
      return score;
    }

    public void setScore(int score) {
      this.score = score;
    }

    public String toString() {
      return name + "," + score;
    }
  }
}
