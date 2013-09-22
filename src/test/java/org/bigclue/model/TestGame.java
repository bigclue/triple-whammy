package org.bigclue.model;

import junit.framework.TestCase;
import org.bigclue.agent.GreedyAgent;
import org.bigclue.agent.RandomAgent;
import org.bigclue.model.Category;
import org.bigclue.model.Dice;
import org.bigclue.model.Game;
import org.bigclue.model.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Class for...
 * Author: Randy Wilson
 * Date: 8/9/13
 */
public class TestGame extends TestCase {

  public void testGame() {
    List<Player> players = Arrays.asList(new Player("Rand"), new Player("Guido"));
    Game game = new Game(players, new Dice());
    RandomAgent randish = new RandomAgent();
    GreedyAgent guido = new GreedyAgent();

    while (!game.isGameOver()) {
      // Do random turn
      System.out.println("=====================");
      System.out.println("Rand:");
      Category scoredCategory;
      do {
        System.out.print("  Dice: " + game.getDice().toString() + " => ");
        game.rollDice();
        scoredCategory = randish.decide(game);
        System.out.println(game.getDice());
      } while (scoredCategory == null);
      System.out.println(" ==> " + scoredCategory.getTitle());

      System.out.println("Guido:");
      do {
        System.out.print("  Dice: " + game.getDice().toString() + " => ");
        game.rollDice();
        scoredCategory = guido.decide(game);
        System.out.println(game.getDice());
      } while(scoredCategory == null);
      System.out.println(" ==> " + scoredCategory.getTitle());
      System.out.println(game.toString());
    }
  }
}