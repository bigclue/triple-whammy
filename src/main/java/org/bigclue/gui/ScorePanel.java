package org.bigclue.gui;

import org.bigclue.model.Category;
import org.bigclue.model.Game;
import org.bigclue.model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JPanel that holds the category names and score cards.
 *
 * Author: Randy Wilson
 * Date: 8/28/13
 */
public class ScorePanel extends JPanel {

  protected static final Color headerColor = new Color(230, 230, 255);
  private static final Color sumColor = new Color(180, 180, 250);
  private static final Color numColor = new Color(210, 210, 250);
  private static final Color sumColor2 = new Color(160, 160, 230);
  private static final Color numColor2 = new Color(190, 190, 230);
  private static final Color totalColor = new Color(220, 220, 255);
  private static final Color totalColor2 = new Color(200, 200, 235);
  private static final Color backgroundColor = new Color(100, 0, 100);
  private static final Color myTurn = new Color(20, 0, 20);
  private static final Color myTurnForeground = new Color(200, 0, 200);
  private static final Color textBackground = Color.black;

  protected static final int borderSize = 3;
  private JLabel[] playerLabels;
  private JLabel[] totalLabels;
  // Map of player to category to array of labels, one per column.
  private Map<Player, Map<Category, JLabel[]>> labelMap = new LinkedHashMap<Player, Map<Category, JLabel[]>>();
  private Game game;

  public void setup(GuiController wham, Game game) {
    removeAll();
    this.game = game;
    // Build score grid.  First column is labels; then 3 columns for each player, with top and bottom row spanning all 3.
    setLayout(new GridBagLayout());
     // Add column names to left.
    GridBagConstraints c = new GridBagConstraints();
    c.weightx = c.weighty = 1.0;
    c.fill = GridBagConstraints.BOTH;
    c.insets = new Insets(1, 1, 1, 1);
    c.anchor = GridBagConstraints.LINE_START;
    c.gridx = 0;
    c.gridy = 0;
    add(makeLabel("", headerColor), c);
    setBackground(backgroundColor);
    c.gridy++;
    for (Category category : Category.allCategories) {
      JLabel label = makeLabel(category.getTitle(), category.isComputed() ? sumColor : numColor);
      add(label, c);
      c.gridy++;
    }
    JLabel label = makeLabel("Grand total", totalColor);
    label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, (int)(label.getFont().getSize() * 1.5)));
    add(label, c);
    c.gridx++;

    // Add each player column
    int playerIndex = 0;
    playerLabels = new JLabel[game.getPlayers().size()];
    totalLabels = new JLabel[game.getPlayers().size()];
    for (Player player : game.getPlayers()) {
      Map<Category, JLabel[]> categoryMap = new LinkedHashMap<Category, JLabel[]>();
      labelMap.put(player, categoryMap);
      c.gridx = playerIndex * 3 + 1;
      c.gridy = 0;
      c.gridwidth = 3;
      c.anchor = GridBagConstraints.CENTER;
      playerLabels[playerIndex] = label = makeLabel(player.getName(), headerColor);
      label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, (int)(label.getFont().getSize() * 1.5)));
      label.setHorizontalAlignment(JLabel.CENTER);
      add(label, c);

      c.gridwidth = 1;
      for (int columnIndex = 0; columnIndex < 3; columnIndex++) {
        c.gridx = playerIndex * 3 + 1 + columnIndex;
        c.gridy = 1;
        for (Category category : Category.allCategories) {
          ScoreLabel scoreLabel = new ScoreLabel(wham, game, player, columnIndex, category, category.isComputed() ? sumColor : numColor);
          // Make sure numbers are right-aligned
          scoreLabel.setHorizontalAlignment(JLabel.RIGHT);
          setLabelSize(scoreLabel, "99999");
          add(scoreLabel, c);
          JLabel[] columnLabels = columnIndex == 0 ? new JLabel[3] : categoryMap.get(category);
          if (columnIndex == 0) {
            categoryMap.put(category, columnLabels);
          }
          columnLabels[columnIndex] = scoreLabel;
          c.gridy++;
        }
      }
      // Grand total spans all 3 columns.
      c.gridx = playerIndex * 3 + 1;
      c.gridwidth = 3;
      ScoreLabel scoreLabel = new ScoreLabel(wham, game, player, 0, null, totalColor);
      scoreLabel.setFont(new Font(label.getFont().getFontName(), Font.BOLD, (int)(label.getFont().getSize() * 1.5)));
      scoreLabel.setHorizontalAlignment(JLabel.CENTER);
      add(scoreLabel, c);
      totalLabels[playerIndex] = scoreLabel;
      c.gridwidth = 1;
      playerIndex++;
    }
  }

  protected static void setLabelSize(JLabel label, String longestString) {
    // Set the width to the maximum possible width of the column so it won't resize as scores are added.
    FontMetrics fm = label.getFontMetrics(label.getFont());
    int w = fm.stringWidth(longestString);
    int h = fm.getHeight();
    Dimension size = new Dimension(w, h);
    label.setMinimumSize(size);
    label.setPreferredSize(size);
  }

  public void paint(Graphics g) {
    super.paint(g);
    if (game != null) {
      for (int i = 0; i < game.getPlayers().size(); i++) {
        boolean isMyTurn = game.isMyTurn(game.getPlayers().get(i));
        playerLabels[i].setBackground(isMyTurn ? myTurn : headerColor);
        playerLabels[i].setForeground(isMyTurn ? myTurnForeground : textBackground);
        //playerLabels[i].repaint();
        totalLabels[i].setBackground(isMyTurn ? totalColor2 : totalColor);
        //totalLabels[i].repaint();
        Map<Category, JLabel[]> categoryMap = labelMap.get(game.getPlayers().get(i));
        for (Category category : Category.allCategories) {
          JLabel[] columnLabels = categoryMap.get(category);
          for (JLabel columnLabel : columnLabels) {
            columnLabel.setBackground(isMyTurn ? (category.isComputed() ? sumColor2 : numColor2) : (category.isComputed() ? sumColor : numColor));
            //columnLabel.repaint();
          }
        }
      }
    }
  }

  protected static JLabel makeLabel(String text, Color backgroundColor) {
    JLabel label = new JLabel(text);
    label.setOpaque(true);
    label.setBackground(backgroundColor);
    label.setBorder(BorderFactory.createEmptyBorder(borderSize, borderSize, borderSize, borderSize));
    return label;
  }
}
