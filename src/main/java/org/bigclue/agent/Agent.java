package org.bigclue.agent;

import org.bigclue.model.Category;
import org.bigclue.model.Game;

/**
 * Interface for an Agent that can decide how to play.
 * Author: Randy Wilson
 * Date: 8/9/13
 */
public interface Agent {

  /**
   * Select which dice to roll, or score the current dice in one
   *   of the player's ScoreColumns.
   * @param game - game in which to make the decision on the current roll for the current player.
   * @return category that was used to set the score, or null if need to keep rolling.
   */
  public Category decide(Game game);
}
