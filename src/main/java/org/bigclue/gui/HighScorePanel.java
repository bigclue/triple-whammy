package org.bigclue.gui;

import org.bigclue.model.HighScores;

import javax.swing.*;
import java.awt.*;

/**
 * Class for...
 * Author: Randy Wilson
 * Date: 9/16/13
 */
public class HighScorePanel extends JPanel {
  HighScores highScores;

  public HighScorePanel(HighScores highScores) {
    this.highScores = highScores;
    rebuild();
  }

  public void rebuild() {
    removeAll();
    setLayout(new GridBagLayout());
    setBackground(GuiController.yellowish);
    GridBagConstraints c = new GridBagConstraints();
    //c.weightx = c.weighty = 1.0;
    c.insets = new Insets(1, 1, 1, 1);
    c.anchor = GridBagConstraints.LINE_START;
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 3;
    Color color = GuiController.yellowish;
    JLabel label = ScorePanel.makeLabel("High Scores", color);
    label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, (int)(label.getFont().getSize() * 1.5)));
    add(label, c);
    c.gridwidth = 1;
    c.gridy++;
    int rank = 1;
    for (HighScores.HighScore highScore : highScores.getScores()) {
      c.gridx = 0;
      add(ScorePanel.makeLabel(Integer.toString(rank), color), c);
      c.gridx++;
      add(ScorePanel.makeLabel(highScore.getName(), color), c);
      c.gridx++;
      add(ScorePanel.makeLabel(Integer.toString(highScore.getScore()), color), c);
      c.gridy++;
      rank++;
    }
    repaint();
  }
}
