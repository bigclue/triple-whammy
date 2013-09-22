package org.bigclue.agent;

import org.bigclue.model.Category;
import org.bigclue.model.Dice;
import org.bigclue.model.Game;
import org.bigclue.model.ScoreColumn;

import java.util.Random;

/**
 * Agent that randomly decides whether to score or keep rolling; randomly selects dice to re-roll;
 *   and then picks a random remaining slot to put the score into.
 * Author: Randy Wilson
 * Date: 8/9/13
 */
public class RandomAgent implements Agent {
  Random rnd = new Random();

  /**
   *
   * @return true if should roll; false if scored it
   */
  public Category decide(Game game) {
    if (game.getWhichRoll() == 3 || rnd.nextInt(4) == 0) {
      // Score the dice.
      int numAvailable = 0;
      int chosenColumn = -1;
      Category chosenCategory = null;
      for (int column = 0; column < 3; column++) {
        ScoreColumn scoreColumn = game.getCurrentPlayer().getScoreColumn(column);
        for (Category category : Category.allCategories) {
          if (scoreColumn.getScore(category) == null) {
            // not selected yet, so consider it
            numAvailable++;
            if (chosenCategory == null || rnd.nextInt(numAvailable) == 0) {
              chosenColumn = column;
              chosenCategory = category;
            }
          }
        }
      }
      game.setScore(chosenColumn, chosenCategory);
      return chosenCategory;
    }
    else {
      // Randomly select dice to re-roll
      for (int die = 0; die < Dice.numDice; die++) {
        game.getDice().setSelected(die, rnd.nextBoolean());
      }
      return null;
    }
  }
}
