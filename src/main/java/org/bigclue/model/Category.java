package org.bigclue.model;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Class for...
 * Author: Randy Wilson
 * Date: 8/7/13
 */
public abstract class Category {
  private String title;
  private boolean isComputed;

  protected Category(String title, boolean computed) {
    this.title = title;
    isComputed = computed;
  }

  /**
   * Get the title for this category, to be displayed in the UI.
   * @return title of the category
   */
  public String getTitle() {
    return title;
  }

  /**
   * Tell whether this category is computed
   * @return true if this category is computed; false if users can enter scores into it.
   */
  public boolean isComputed() {
    return isComputed;
  }

  /**
   * Tell what the score would be in this category for the given roll of the dice.
   * Should be overridden by all non-computed categories.
   * Will not be used by computed categories.
   * @param dice - Current roll of dice.
   * @return score yielded by the given roll of the dice for this category
   */
  public int getScore(Dice dice) {
    throw new NotImplementedException();
  }

  /**
   * Get the score for the given column of the given player.
   * (Overridden by all computed fields).
   * @param player - player to get stored or computed score for.
   * @param scoreColumn - index of which column to get score fore.
   * @return score for the given column of the given player, or null if not stored yet.
   */
  public Integer getScore(Player player, int scoreColumn) {
    return getScore(player.getScoreColumn(scoreColumn));
  }

  public Integer getScore(ScoreColumn scoreColumn) {
    if (isComputed) {
      throw new NotImplementedException();
    }
    return scoreColumn.getScore(this);
  }

  public static final List<Category> upperCategories = Arrays.asList(new Category[]{
          new SingleNumber(1, "Ones"),
          new SingleNumber(2, "Twos"),
          new SingleNumber(3, "Threes"),
          new SingleNumber(4, "Fours"),
          new SingleNumber(5, "Fives"),
          new SingleNumber(6, "Sixes")});

  public static final Category threeOfAKind = new Summer("3 of a kind", 3);
  public static final Category fourOfAKind = new Summer("4 of a kind", 4);
  public static final Category fullHouse = new FullHouse();
  public static final Category smallStraight = new Straight("Small straight", 4, 30);
  public static final Category largeStraight = new Straight("Large straight", 5, 40);
  public static final Category fiveOfAKind = new FiveOfAKind();
  public static final Category chance = new Summer("Chance", 0);

  public static final List<Category> lowerCategories = Arrays.asList(
          threeOfAKind, fourOfAKind, fullHouse, smallStraight, largeStraight, fiveOfAKind, chance);

  /**
   * Category that has a single number.
   */
  public static class SingleNumber extends Category {
    int number;

    public SingleNumber(int number, String name) {
      super(name, false);
      this.number = number;
    }

    public int getScore(Dice dice) {
      int sum = 0;
      for (int value : dice.getCurrentValues()) {
        if (value == number) {
          sum += number;
        }
      }
      return sum;
    }
  }

  /**
   * Category that sums all of the dice, as long as some number of them are the same value (even 0).
   */
  public static class Summer extends Category {
    private int minSame;

    protected Summer(String title, int minSame) {
      super(title, false);
      this.minSame = minSame;
    }

    @Override
    public int getScore(Dice dice) {
      boolean qualifies = minSame == 0; // auto-qualify if minSame is 0
      for (int i = 0; i < Dice.numDice && !qualifies; i++) {
        int numSame = 1;
        for (int j = i + 1; j < Dice.numDice; j++) {
          if (dice.getCurrentValues()[j] == dice.getCurrentValues()[i]) {
            numSame++;
            if (numSame >= minSame) {
              qualifies = true;
              break;
            }
          }
        }
      }
      int sum = 0;
      if (qualifies) {
        for (int value : dice.getCurrentValues()) {
          sum += value;
        }
      }
      return sum;
    }
  }

  public static class FullHouse extends Category {
    public FullHouse() {
      super("Full house", false);
    }

    @Override
    public int getScore(Dice dice) {
      int value1 = dice.getCurrentValues()[0];
      int count1 = 1;
      int value2 = -1;
      int count2 = 0;
      for (int i = 1; i < Dice.numDice; i++) {
        int value = dice.getCurrentValues()[i];
        if (value == value1) {
          count1++;
        }
        else if (value == value2 || value2 == -1) {
          count2++;
          value2 = value;
        }
        else {
          break; // problem.
        }
      }
      if (count1 == Dice.numDice || (count1 == 2 && count2 == 3) || (count1 == 3 && count2 == 2)) {
        return 25;
      }
      return 0; // didn't qualify
    }
  }

  public static class Straight extends Category {
    int numInRow;
    int score;
    public Straight(String title, int numInRow, int score) {
      super(title, false);
      this.numInRow = numInRow;
      this.score = score;
    }

    @Override
    public int getScore(Dice dice) {
      boolean qualifies = false;
      Set<Integer> values = new TreeSet<Integer>();
      for (int value : dice.getCurrentValues()) {
        values.add(value);
      }
      List<Integer> list = new ArrayList<Integer>(values);
      for (int start = 0; start <= Dice.numDice - numInRow && start <= list.size() - numInRow; start++) {
        int value = list.get(start);
        qualifies = true;
        for (int i = 0; i < numInRow; i++) {
          if (start + i >= list.size() || list.get(start + i) != value + i) {
            // Not a straight starting at 'start'.
            qualifies = false;
            break;
          }
        }
        if (qualifies) {
          break;
        }
      }
      return qualifies ? score : 0;
    }
  }

  public static class FiveOfAKind extends Category {
    public FiveOfAKind() {
      super("5 of a Kind!", false);
    }

    public int getScore(Dice dice) {
      return allSame(dice) ? 50 : 0;
    }
  }

  public static boolean allSame(Dice dice) {
    int value = dice.getCurrentValues()[0];
    for (int i = 1; i < Dice.numDice; i++) {
      if (dice.getCurrentValues()[i] != value) {
        return false;
      }
    }
    return true;
  }

  public static final Category singleTotal = new Category("Upper Subtotal", true) {
    @Override
    public Integer getScore(ScoreColumn scoreColumn) {
      return sumScores(scoreColumn, Category.upperCategories);
    }
  };

  public static final Category upperBonus = new Category("Bonus", true) {
    @Override
    public Integer getScore(ScoreColumn scoreColumn) {
      return singleTotal.getScore(scoreColumn) >= 63 ? 35 : 0;
    }
  };

  public static final Category upperTotal = new Category("Upper Total", true) {
    @Override
    public Integer getScore(ScoreColumn scoreColumn) {
      return singleTotal.getScore(scoreColumn)  + upperBonus.getScore(scoreColumn);
    }
  };

  public static final Category lowerSubtotal = new Category("Lower subtotal", true) {
    @Override
    public Integer getScore(ScoreColumn scoreColumn) {
      return sumScores(scoreColumn, Category.lowerCategories);
    }
  };

  public static final Category fiverBonus = new Category("Fiver Bonus", true) {
    @Override
    public Integer getScore(ScoreColumn scoreColumn) {
      return scoreColumn.fiverBonuses * 100;
    }
  };

  public static final Category combinedTotal = new Category("Combined Total", true) {
    @Override
    public Integer getScore(ScoreColumn scoreColumn) {
      return upperTotal.getScore(scoreColumn) + lowerSubtotal.getScore(scoreColumn) + fiverBonus.getScore(scoreColumn);
    }
  };

  public static final Category tripleTotal = new Category("Triple Total", true) {
    @Override
    public Integer getScore(ScoreColumn scoreColumn) {
      return combinedTotal.getScore(scoreColumn) * scoreColumn.getScoreMultiplier();
    }
  };


  public static List<Category> allCategories = new ArrayList<Category>();
  static {
    allCategories.addAll(upperCategories);
    allCategories.add(singleTotal);
    allCategories.add(upperBonus);
    allCategories.add(upperTotal);
    allCategories.addAll(lowerCategories);
    allCategories.add(lowerSubtotal);
    allCategories.add(fiverBonus);
    allCategories.add(combinedTotal);
    allCategories.add(tripleTotal);
  }

  /**
   * Sum the scores in this column from all the given categories.
   * @param categories - List of categories to sum the scores for
   * @return sum of scores in this column for the given categories.
   */
  private static int sumScores(ScoreColumn scoreColumn, List <Category> categories) {
    int sum = 0;
    for (Category category : categories) {
      Integer score = scoreColumn.getScore(category);
      if (score != null) {
        sum += score;
      }
    }
    return sum;
  }

  public String toString() {
    return title;
  }
}
