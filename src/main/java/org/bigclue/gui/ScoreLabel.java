package org.bigclue.gui;

import org.bigclue.model.Category;
import org.bigclue.model.Game;
import org.bigclue.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Class for displaying scores in each cell.
 * Author: Randy Wilson
 * Date: 8/17/13
 */
public class ScoreLabel extends JLabel {
  final GuiController wham;
  final Game game;
  final Player player;
  final int columnIndex;   // (ignored for Grand Total)
  final Category category; // (or null for Grand Total)
  final static int borderSize = ScorePanel.borderSize;
  private boolean inCell;

  public ScoreLabel(final GuiController wham, final Game game, final Player player, final int  columnIndex, final Category category, Color color) {
    super("<init>");
    this.wham = wham;
    this.game = game;
    this.player = player;
    this.columnIndex = columnIndex;
    this.category = category;
    this.setOpaque(true);
    this.setBackground(color);
    this.setBorder(BorderFactory.createEmptyBorder(borderSize, borderSize, borderSize, borderSize));
    this.setPreferredSize(new Dimension(80, 30));
    final ScoreLabel thus = this;
    addMouseListener(new MouseListener() {
      public void mouseClicked(MouseEvent mouseEvent) {
        if (game.canScore() && !category.isComputed() && player.getScoreColumn(columnIndex).getScore(category) == null && game.isMyTurn(player)) {
          wham.setScore(thus);
        }
      }

      public void mousePressed(MouseEvent mouseEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
      }

      public void mouseReleased(MouseEvent mouseEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
      }

      public void mouseEntered(MouseEvent mouseEvent) {
        if (game.canScore() && category != null && !category.isComputed() && game.isMyTurn(player)) {
          Integer score = player.getScoreColumn(columnIndex).getScore(category);
          if (score == null) {
            inCell = true;
            thus.updateUI();
          }
        }
      }

      public void mouseExited(MouseEvent mouseEvent) {
        if (inCell) {
          inCell = false;
          Integer score = player.getScoreColumn(columnIndex).getScore(category);
          if (score == null) {
            thus.updateUI();
          }
        }
      }
    });
  }

  public String getText() {
    if (player == null) {
      return "<no player>";
    }
    try {
      Integer score;
      if (category == null) {
        score = player.getGrandTotal();
      }
      else {
        score = player.getScoreColumn(columnIndex).getScore(category);
      }
      if (score == null) {
        if (inCell && game.canScore()) {
          return Integer.toString(category.getScore(game.getDice()));
        }
        else {
          return "_";
        }
      }
      else {
        return score.toString();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      return "<err>";
    }
  }

  public int getColumnIndex() {
    return columnIndex;
  }

  public Category getCategory() {
    return category;
  }
}
