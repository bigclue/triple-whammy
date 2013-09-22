package org.bigclue.model;

import java.util.Arrays;
import java.util.List;

/**
 * Class representing a single player and their three columns.
 * Author: Randy Wilson
 * Date: 8/7/13
 */
public class Player {
  String name;
  List<ScoreColumn> scoreColumns = Arrays.asList(new ScoreColumn(1), new ScoreColumn(2), new ScoreColumn(3));

  public Player(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getGrandTotal() {
    int sum = 0;
    for (ScoreColumn scoreColumn : scoreColumns) {
      sum += scoreColumn.getScore(Category.tripleTotal);
    }
    return sum;
  }

  public ScoreColumn getScoreColumn(int index) {
    return scoreColumns.get(index);
  }

  /**
   * Tell whether all 3 columns are completely filled in for this player.
   * @return true if all 3 columns are completely filled in, false otherwise.
   */
  public boolean isComplete() {
    for (ScoreColumn scoreColumn : scoreColumns) {
      if (!scoreColumn.isComplete()) {
        return false;
      }
    }
    return true;
  }
}
