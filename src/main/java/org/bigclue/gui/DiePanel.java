package org.bigclue.gui;

import org.bigclue.model.Dice;

import javax.swing.*;
import java.awt.*;

/**
 * Class for drawing a die in a JPanel.
 * Author: Randy Wilson
 * Date: 8/12/13
 */
public class DiePanel extends JPanel {
  private static final int normalHighlight = 255;
  private static final int normalFace = 235;
  private static final int normalShadow = 180;
  private static final int selectedFace = 150;
  private static final int selectedShadow = 100;
  private static final int dot = 40;
  private static final int dotReflect = 255;
  private static final Color background = GuiController.yellowish;

  private int dieIndex; // 0..4 of which of the dice this is.
  private final Dice dice; // dice to use.

  public DiePanel(final Dice dice, final int dieIndex) {
    this.dice = dice;
    this.dieIndex = dieIndex;
    //super.setPreferredSize(new Dimension(120, 120));
  }

  public int getDieIndex() {
    return dieIndex;
  }

  @Override
  // Don't call this directly.  Call repaint() from my app if the number changes.
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D graphics = (Graphics2D) g;
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    Rectangle rect = this.getBounds();
    int w = (int)rect.getWidth();
    int h = (int)rect.getHeight();
    // Make sure it's square
    if (w > h) {
      w = h;
    }
    if (h > w) {
      h = w;
    }

    // 5, 10, 5, 10, 5, 10, 5
    // 9 divisions in a die: x=()=()=()=x
    int div = w / 9;
    boolean isSelected = dice.getSelected()[dieIndex];
    // Draw background
    graphics.setColor(background);
    graphics.fillRect(0, 0, w, h);

    // Draw outer rounded rectangle in gray (or darker blue, if selected)
    int arc = div * 2;
    int inset = div/2;
//    for (int i = 0; i < inset; i++) {
//      int c = normalHighlight + ((normalFace - normalHighlight) * i) / inset;
//      graphics.setColor(new Color(c, c, c));
//      graphics.fillRoundRect(div + i, div + i, div * 7 - i, div * 7 - i, arc, arc);
//    }
    int face = isSelected ? selectedFace : normalFace;
    int shadow = isSelected ? selectedShadow : normalShadow;
    for (int i = 0; i < inset; i++) {
      int c = shadow + ((face - shadow) * i) / inset;
      graphics.setColor(new Color(c, c, c));
      graphics.fillRoundRect(div + i, div + i, div * 7 - i * 2, div * 7 - i * 2, arc, arc);
    }

    // Draw black dots
    graphics.setColor(Color.BLACK);
    int left = div;
    int middle = div * 3;
    int right = div * 5;
    int top = left;
    int bottom = div * 5;
    switch(dice.getCurrentValues()[dieIndex]) {
      case 1: 
        drawDot(middle, middle, div, graphics); 
        break;
      case 2: 
        drawDot(top,    left, div, graphics);
        drawDot(bottom, right, div, graphics);
        break;
      case 3: 
        drawDot(top,    left, div, graphics);
        drawDot(middle, middle, div, graphics);
        drawDot(bottom, right, div, graphics);
        break;
      case 4: 
        drawDot(top,    left, div, graphics);
        drawDot(top,    right, div, graphics);
        drawDot(bottom, left, div, graphics);
        drawDot(bottom, right, div, graphics);
        break;
      case 5: 
        drawDot(top,    left, div, graphics);
        drawDot(top,    right, div, graphics);
        drawDot(middle, middle, div, graphics);
        drawDot(bottom, left, div, graphics);
        drawDot(bottom, right, div, graphics);
        break;
      case 6: 
        drawDot(top,    left, div, graphics);
        drawDot(middle, left, div, graphics);
        drawDot(bottom, left, div, graphics);
        drawDot(top,    right, div, graphics);
        drawDot(middle, right, div, graphics);
        drawDot(bottom, right, div, graphics);
        break;
    }
  }
  
  private void drawDot(int y, int x, int size, Graphics graphics) {
    graphics.setColor(new Color(dot, dot, dot));
    int extra = size / 4;
    graphics.fillOval(size + x - extra, size + y - extra, size + 2 * extra, size + 2 * extra);
    // Make the highlight dot 1/3 the size of the dot.
    int reflectSize = size / 2;
    // Make the center of the highlight dot halfway towards the edge of the highlight.
    int shiftSize = reflectSize * 2 / 3;
    // Start a little darker on the edge of the highlight and lighter in the middle.
    int startColor = dotReflect / 2;
    // Change x and y to point at upper-left of highlight dot.
    x = size + x + size / 2;
    y = size + y + size / 2;
    extra /= 2;
    for (int i = 0; i < reflectSize; i++) {
      int c = startColor + i * (dotReflect - startColor) / reflectSize;
      graphics.setColor(new Color(c, c, c));
      int shift = (i * shiftSize) / reflectSize;
      graphics.fillOval(x + shift + extra, y + shift + extra, reflectSize - i, reflectSize - i);
    }
  }
}
