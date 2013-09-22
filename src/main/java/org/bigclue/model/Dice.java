package org.bigclue.model;

import java.util.Random;

/**
 * Class representing a set of 5 6-sided dice.
 * Author: Randy Wilson
 * Date: 8/7/13
 */
public class Dice {
  public static final int numDice = 5;

  private final int[] dice;
  private boolean[] selected = new boolean[numDice];
  private Random rnd = new Random();

  public Dice(int... dice) {
    this.dice = dice == null || dice.length != 5 ? new int[numDice] : dice;
  }

  public int[] rollAll() {
    selectAll();
    return rollSelected();
  }

  /**
   * Roll all of the selected dice, and return the index of the first selected one,
   *   or -1 if there are none selected.
   * @return index of first selected die.
   */
  public int shake() {
    int firstSelected = -1;
    for (int i = 0; i < numDice; i++) {
      if (selected[i]) {
        dice[i] = rnd.nextInt(6) + 1;
        if (firstSelected < 0) {
          firstSelected = i;
        }
      }
    }
    return firstSelected;
  }

  /**
   * Roll all the selected dice, and then deselect the first selected die.
   */
  public int rollFirst() {
    int firstSelected = shake();
    if (firstSelected >= 0) {
      toggle(firstSelected);
    }
    return firstSelected;
  }

  public int[] rollSelected() {
    shake();
    clear();
    return getCurrentValues();
  }

  public int[] getCurrentValues() {
    return dice;
  }

  public boolean[] getSelected() {
    return selected;
  }

  public void setSelected(int index, boolean isSelected) {
    selected[index] = isSelected;
  }

  public void clear() {
    for (int i = 0; i < numDice; i++) {
      setSelected(i, false);
    }
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < numDice; i++) {
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(dice[i]).append(selected[i] ? "*" : "");
    }
    return sb.toString();
  }

  public void toggle(int dieIndex) {
    selected[dieIndex] = !selected[dieIndex];
  }

  public int getFirstSelected() {
    for (int i = 0; i < numDice; i++) {
      if (selected[i]) {
        return i;
      }
    }
    return -1;
  }

  public void selectAll() {
    for (int i = 0; i < numDice; i++) {
      setSelected(i, true);
    }
  }
}
