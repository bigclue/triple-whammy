package org.bigclue.agent;

import org.bigclue.model.*;

/**
 * Class that plays the game using a simple "greedy" algorithm:
 * - Stop rolling if you get a large straight or fiveOfAKind (and there is a place for them)
 * - Pick the highest number that there is the most of.
 * Author: Randy Wilson
 * Date: 8/10/13
 */
public class GreedyAgent implements Agent {

  /**
   * Decide what to do.
   * @param game - game to do
   * @return true if should continue rolling; false if score was set
   */
  public Category decide(Game game) {
    if (game.getWhichRoll() == 3 || qualifies(game, Category.fiveOfAKind) || qualifies(game, Category.largeStraight)) {
      // Score the dice.
      int chosenColumn = -1;
      Category chosenCategory = null;
      int chosenScore = 0;
      for (int column = 0; column < 3; column++) {
        ScoreColumn scoreColumn = game.getCurrentPlayer().getScoreColumn(column);
        for (Category category : Category.allCategories) {
          if (!category.isComputed() && scoreColumn.getScore(category) == null) {
            // not selected yet, so consider it
            if (chosenCategory == null || category.getScore(game.getDice()) * scoreColumn.getScoreMultiplier() >= chosenScore) {
              chosenColumn = column;
              chosenCategory = category;
              chosenScore = category.getScore(game.getDice()) * scoreColumn.getScoreMultiplier();
            }
          }
        }
      }
      game.setScore(chosenColumn, chosenCategory);
      return chosenCategory;
    }
    else {
      int bestNumber = getBestNumber(game.getDice());
      for (int die = 0; die < Dice.numDice; die++) {
        game.getDice().setSelected(die, game.getDice().getCurrentValues()[die] != bestNumber);
      }
      return null;
    }
  }

  /**
   * Find the highest number that has the most of the same number.
   * Ties go to the higher number.
   * @param dice - current dice
   * @return which value (1..6) has the most dice with that value.
   */
  private static int getBestNumber(Dice dice) {
    int bestNumber = 6;
    int bestCount = 0;
    for (int i = 6; i >= 1; i--) {
      int count = 0;
      for (int j = 0; j < Dice.numDice; j++) {
        if (dice.getCurrentValues()[j] == i) {
          count++;
        }
      }
      if (count > bestCount) {
        bestNumber = i;
        bestCount = count;
      }
    }
    return bestNumber;
  }

  private static boolean qualifies(Game game, Category category) {
    if (category.getScore(game.getDice()) > 0) {
      // Qualifies as a large straight or fiveOfAKind, so see if there is one available...
      Player player = game.getCurrentPlayer();
      for (int i = 2; i >= 0; i--) {
        ScoreColumn scoreColumn = player.getScoreColumn(i);
        if (scoreColumn.getScore(category) == null) {
          return true;
        }
      }
    }
    return false;
  }
}
