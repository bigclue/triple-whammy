package org.bigclue;

import org.bigclue.gui.GuiController;

import javax.swing.*;

/**
 * Class for...
 * Author: Randy Wilson
 * Date: 9/14/13
 */
public class TripleWhammy {

  public static void main(String[] args) {
    System.setProperty("apple.laf.useScreenMenuBar", "true");
    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Triple Whammy");
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
          public void run() {
              new GuiController();
          }
      });
  }
}
