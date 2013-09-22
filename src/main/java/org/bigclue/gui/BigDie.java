package org.bigclue.gui;

import org.bigclue.model.Dice;

import javax.swing.*;
import java.awt.*;

/**
 * Class for...
 * Author: Randy Wilson
 * Date: 9/14/13
 */
public class BigDie {

  public BigDie() {
    createAndShowGUI();
  }
  protected static final Color yellowish = new Color(227, 197, 35);

  private JFrame mainFrame; // overall frame with dice on left, game panel on right

  /**
   * Create the GUI and show it.  For thread safety,
   * this method should be invoked from the
   * event-dispatching thread.
   */
  private void createAndShowGUI() {
    //Create and set up the window.
    mainFrame = new JFrame("Triple Whammy");
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setBackground(yellowish);

    Dice dice = new Dice();
    dice.getCurrentValues()[0] = 5;
    JPanel diePanel = new DiePanel(dice, 0);
    diePanel.setPreferredSize(new Dimension(512, 512));
    mainFrame.getContentPane().add(diePanel);

    //Display the window.
    mainFrame.pack();
    mainFrame.setVisible(true);
  }
  public static void main(String[] args) {
    //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {
          public void run() {
              new BigDie();
          }
      });
  }
}
