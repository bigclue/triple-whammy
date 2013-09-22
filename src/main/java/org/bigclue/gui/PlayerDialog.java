package org.bigclue.gui;

import org.bigclue.model.PlayerList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class for...
 * Author: Randy Wilson
 * Date: 8/17/13
 */
public class PlayerDialog extends JDialog {
  private final PlayerList playerList = new PlayerList();
  private final JCheckBox[] checkBoxes = new JCheckBox[PlayerList.maxPlayers];
  private final JTextField[] textFields = new JTextField[PlayerList.maxPlayers];
  // Flag for whether 'cancel' was hit last time.
  private boolean wasCanceled;

  public PlayerDialog(Frame parentFrame) {
    super(parentFrame, true);
    setLayout(new GridBagLayout());

    GridBagConstraints c = new GridBagConstraints();
    c.anchor = GridBagConstraints.LINE_START;
    for (int i = 0; i < PlayerList.maxPlayers; i++) {
      c.gridx = 0;
      c.gridy = i;
      checkBoxes[i] = new JCheckBox();
      checkBoxes[i].setSelected(playerList.getSelected()[i]);
      add(checkBoxes[i], c);

      c.gridx = 1;
      textFields[i] = new JTextField(20);
      textFields[i].setText(playerList.getNames()[i]);
      add(textFields[i], c);
    }
    // Add "ok" and "cancel" button
    c.gridx = 0;
    c.gridy = PlayerList.maxPlayers;
    c.gridwidth = 2;
    JPanel buttonPanel = new JPanel();
    JButton okButton = new JButton("OK");
    getRootPane().setDefaultButton(okButton);
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        for (int i = 0; i < PlayerList.maxPlayers; i++) {
          if (!textFields[i].getText().equals(playerList.getNames()[i])) {
            playerList.setName(i, textFields[i].getText());
          }
          if (checkBoxes[i].isSelected() != playerList.getSelected()[i]) {
            playerList.setSelected(i, checkBoxes[i].isSelected());
          }
        }
        // Close dialog box.
        setVisible(false);
        wasCanceled = false;
      }
    });
    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        setVisible(false);
        wasCanceled = true;
      }
    });
    buttonPanel.add(okButton);
    buttonPanel.add(cancelButton);
    add(buttonPanel, c);
  }

  public PlayerList getPlayerList() {
    return playerList;
  }

  public boolean wasCanceled() {
    return wasCanceled;
  }
}
