package org.bigclue.model;

import junit.framework.TestCase;
import org.bigclue.model.Category;
import org.bigclue.model.Dice;

/**
 * Class for...
 * Author: Randy Wilson
 * Date: 8/10/13
 */
public class CategoryTest extends TestCase {
  public void testCategories() {
    tryCategory(0, Category.upperCategories.get(0), new Dice(2, 2, 2, 2, 2));
    tryCategory(1, Category.upperCategories.get(0), new Dice(2, 1, 2, 2, 2));
    tryCategory(5, Category.upperCategories.get(0), new Dice(1, 1, 1, 1, 1));
    tryCategory(30, Category.upperCategories.get(5), new Dice(6, 6, 6, 6, 6));
    tryCategory(12, Category.upperCategories.get(5), new Dice(5, 6, 6, 5, 5));
    tryCategory(0, Category.threeOfAKind, new Dice(1, 2, 3, 4, 4));
    tryCategory(12, Category.threeOfAKind, new Dice(1, 2, 3, 3, 3));
    tryCategory(15, Category.threeOfAKind, new Dice(3, 3, 3, 3, 3));
    tryCategory(15, Category.fourOfAKind, new Dice(3, 3, 3, 3, 3));
    tryCategory(14, Category.fourOfAKind, new Dice(3, 3, 3, 3, 2));
    tryCategory(0, Category.fourOfAKind, new Dice(3, 3, 3, 2, 2));
    tryCategory(25, Category.fullHouse, new Dice(3, 3, 3, 2, 2));
    tryCategory(0, Category.fullHouse, new Dice(3, 3, 1, 2, 2));
    tryCategory(25, Category.fullHouse, new Dice(3, 3, 3, 3, 3)); // 5 of a kind = 3 + 2 of a kind
    tryCategory(30, Category.smallStraight, new Dice(1, 2, 3, 4, 4));
    tryCategory(30, Category.smallStraight, new Dice(4, 1, 2, 3, 4));
    tryCategory(30, Category.smallStraight, new Dice(3, 1, 2, 4, 1)); // order shouldn't matter
    tryCategory(30, Category.smallStraight, new Dice(3, 1, 2, 4, 3)); // two in the middle should be ok
    tryCategory(0, Category.smallStraight, new Dice(1, 2, 3, 5, 6));
    tryCategory(40, Category.largeStraight, new Dice(2, 5, 4, 3, 6));
    tryCategory(40, Category.largeStraight, new Dice(2, 5, 4, 3, 1));
    tryCategory(0, Category.largeStraight, new Dice(4, 1, 2, 3, 4));
    tryCategory(0, Category.fiveOfAKind, new Dice(1, 1, 1, 1, 2));
    tryCategory(50, Category.fiveOfAKind, new Dice(6, 6, 6, 6, 6));
    tryCategory(30, Category.chance, new Dice(6, 6, 6, 6, 6));
    tryCategory(15, Category.chance, new Dice(3, 3, 3, 3, 3));
  }

  private void tryCategory(int expectedScore, Category category, Dice dice) {
    int score = category.getScore(dice);
    assertEquals(expectedScore, score);
  }
}
