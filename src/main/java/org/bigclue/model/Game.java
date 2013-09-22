package org.bigclue.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for tracking the entire game's players and scores.
 * Author: Randy Wilson
 * Date: 8/7/13
 */
public class Game {
  List<Player> players;
  Dice dice;

  // Index in players of whose turn it is.
  int whoseTurn = 0;
  // Which roll we're on:
  // 0 => haven't rolled yet.  Click "roll" to do the first roll. (eventually, hit undo to unscore previous...)
  // 1 => rolled once, can choose which ones to re-roll, or score it.
  // 2 => rolled twice, can choose which ones to re-roll, or score it.
  // 3 => rolled 3 times, need to choose where to score it.
  int whichRoll = 0;
  boolean gameOver = false;

  public Game(PlayerList playerList, Dice dice) {
    this(getPlayers(playerList), dice);
  }

  public Game(List<Player> players, Dice dice) {
    this.players = players;
    this.dice = dice;
  }

  private static List<Player> getPlayers(PlayerList playerList) {
    List<Player> players = new ArrayList<Player>();
    for (int i = 0; i < playerList.getNames().length; i++) {
      if (playerList.getSelected()[i]) {
        Player player = new Player(playerList.getNames()[i]);
        players.add(player);
      }
    }
    return players;
  }

  public boolean canScore() {
    return whichRoll > 0;
  }

  public boolean canSelect() {
    return whichRoll > 0 && whichRoll < 3;
  }

  public boolean canRoll() {
    return !gameOver && (whichRoll == 0 || (whichRoll < 3 && dice.getFirstSelected() >= 0));
  }

  /**
   * Roll the dice for the current player.
   */
  public void rollDice() {
    if (whichRoll >= 3) {
      throw new IllegalStateException("Need to score before rolling.");
    }
    if (whichRoll == 0) {
      dice.rollAll();
    }
    else {
      dice.rollSelected();
    }
    whichRoll++;
  }

  public void rolledDice() {
    if (whichRoll >= 3) {
      throw new IllegalStateException("Need to score before rolling.");
    }
    whichRoll++;
  }

  /**
   * Record the score using the current state of the dice
   *   into the given category in the given column of the current player.
   * Begins the next player's turn, and sets 'isFinished' if all done.
   * @param column - column of current player in which to record the score.
   * @param category - category to record the score in.
   * @return true if the game is now over, false otherwise
   */
  public boolean setScore(int column, Category category) {
    players.get(whoseTurn).getScoreColumn(column).setScore(category, dice);
    whoseTurn++;
    if (whoseTurn >= players.size()) {
      whoseTurn = 0;
      // See if the game is over
      if (players.get(0).isComplete()) {
        gameOver = true;
      }
    }
    whichRoll = 0;
    return gameOver;
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public Player getCurrentPlayer() {
    return players.get(whoseTurn);
  }

  public Dice getDice() {
    return dice;
  }

  public int getWhichRoll() {
    return whichRoll;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    int c1 = 18;
    int sc = 6;
    // Print header
    sb.append(String.format("%" + c1 + "s |", "Category"));
    for (Player player : players) {
      sb.append(String.format(" %-" + (sc * 3 + 4) + "s  |", player.getName()));
    }
    sb.append("\n");
    for (Category category : Category.allCategories) {
      sb.append(String.format("%" + c1 + "s |", category.getTitle() + (category.isComputed() ? "=" : ":")));
      for (Player player : players) {
        for (int i = 0; i < 3; i++) {
          Integer score = category.getScore(player, i);
          sb.append(String.format(" %" + sc + "s ", score == null ? "" : score.toString()));
        }
        sb.append(" |");
      }
      sb.append("\n");
    }
    sb.append(String.format("%" + c1 + "s |", "Grand Total"));
    for (Player player : players) {
      sb.append(String.format(" %" + (sc * 3 + 4) + "s  |", player.getGrandTotal()));
    }
    sb.append("\n");
    return sb.toString();
  }

  public boolean isMyTurn(Player player) {
    return !gameOver && players.get(whoseTurn) == player;
  }
}
