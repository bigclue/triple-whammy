package org.bigclue.model;

import java.util.*;

/**
 * Class representing one column on a score card.
 * Author: Randy Wilson
 * Date: 8/7/13
 */
public class ScoreColumn {
  private final int scoreMultiplier;

  // Map of all non-computed categories to the score that has been stored for it.
  private Map<Category, Integer> categoryScoreMap = new LinkedHashMap<Category, Integer>();

  // Number of times a fiveOfAKind was placed into this column (with a non-zero score)
  // AFTER a 5-of-a-kind was already placed in the
  protected int fiverBonuses = 0;


  public ScoreColumn(int scoreMultiplier) {
    this.scoreMultiplier = scoreMultiplier;
    for (Category category : Category.allCategories) {
      if (!category.isComputed()) {
        categoryScoreMap.put(category, null);
      }
    }
  }

  /**
   * Get the stored score for the given category.
   * @param category - Category to get score for.
   * @return score, if any, or null if not yet filled in.
   */
  public Integer getScore(Category category) {
    return category.isComputed() ? category.getScore(this) :  categoryScoreMap.get(category);
  }

  /**
   * Set the score for the given category with the given roll of the dice.
   * If the dice are all the same number, and the score in the given category is non-zero,
   * and there is already a fiveOfAKind in this column, then a fiveOfAKind bonus is added.
   * @param category - category to set the score in
   * @param dice - current roll of the dice.
   */
  public void setScore(Category category, Dice dice) {
    if (categoryScoreMap.get(category) != null) {
      throw new IllegalArgumentException("Error: Already had a score for " + category.getTitle());
    }
    if (category.isComputed()) {
      throw new IllegalArgumentException("Error: Can't put a score into a computed field.");
    }
    Integer fiverScore = categoryScoreMap.get(Category.fiveOfAKind);
    int score = category.getScore(dice);
    if (score > 0 && fiverScore != null && fiverScore > 0 && Category.fiveOfAKind.getScore(dice) > 0) {
      // Already had a fiveOfAKind; and this is another fiveOfAKind being put somewhere else; and it gets points
      // so rack up a fiveOfAKind bonus
      fiverBonuses++;
    }
    categoryScoreMap.put(category, score);
  }

  /**
   * Tell whether all of the values in this score column are filled in.
   * @return true if all the values are filled in (i.e., non-null); false otherwise.
   */
  public boolean isComplete() {
    for (Category category : categoryScoreMap.keySet()) {
      if (categoryScoreMap.get(category) == null) {
        return false;
      }
    }
    return true;
  }

  public int getScoreMultiplier() {
    return scoreMultiplier;
  }
}
