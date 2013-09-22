package org.bigclue.gui;

import org.bigclue.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main GUI class for Triple Whammy
 * Author: Randy Wilson
 * Date: 8/12/13
 */
public class GuiController {

  public GuiController() {
    createAndShowGUI();
  }
  protected static final Color yellowish = new Color(227, 197, 35);

  private JFrame mainFrame; // overall frame with dice on left, game panel on right
  private ScorePanel scorePanel; // where scores are.  Rebuilt each new game.
  private Game game;
  private PlayerDialog playerDialog;
  private JButton rollButton;
  private JLabel statusLabel;
  private JLabel rollLabel;
  private JPanel dicePanel;
  private Dice dice;
  private HighScores highScores;
  private HighScorePanel highScorePanel;
  private boolean highScoresVisible = true;
  private JMenuItem highScoreItem;

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
    // Dice panel has 5 dice.
    dicePanel = new JPanel();
    dicePanel.setBackground(yellowish);
    dicePanel.setPreferredSize(new Dimension(100, 500));
    dicePanel.setLayout(new BoxLayout(dicePanel, BoxLayout.Y_AXIS));
    dicePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    dice = new Dice();
    dice.rollAll();
    for (int i = 0; i < 5; i++) {
      final DiePanel diePanel = new DiePanel(dice, i);
      dicePanel.add(diePanel);
      diePanel.addMouseListener(new MouseListener() {
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        public void mousePressed(MouseEvent mouseEvent) {
        }

        public void mouseReleased(MouseEvent mouseEvent) {
          doClickDie(diePanel);
        }

        public void mouseEntered(MouseEvent mouseEvent) {
        }

        public void mouseExited(MouseEvent mouseEvent) {
        }
      });
    }
    mainFrame.setFocusable(true);
    mainFrame.addKeyListener(new KeyListener() {
      public void keyTyped(KeyEvent keyEvent) {
        if (game != null && game.canSelect()) {
          if (keyEvent.getKeyChar() >= '1' && keyEvent.getKeyChar() <= '5') {
            game.getDice().toggle(keyEvent.getKeyChar() - '1');
            dicePanel.repaint();
            updateStatus();
          }
        }
        if (game != null && game.canRoll() && (keyEvent.getKeyChar() == 'r' || keyEvent.getKeyChar() == KeyEvent.VK_ENTER)) {
          doRoll();
        }
      }
      public void keyPressed(KeyEvent keyEvent) {
      }
      public void keyReleased(KeyEvent keyEvent) {
      }
    });

    // Left panel has dice, roll button, status text.
    JPanel leftPanel = new JPanel();
    leftPanel.setBackground(yellowish);
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.add(dicePanel);

    rollButton = new JButton("Roll");
    rollButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    rollButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        doRoll();
      }
    });
    rollButton.setDefaultCapable(true);
    leftPanel.add(rollButton);

    statusLabel = new JLabel("Sign in");
    statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    ScorePanel.setLabelSize(statusLabel, "Select or score");
    statusLabel.setPreferredSize(new Dimension(statusLabel.getPreferredSize().width, statusLabel.getPreferredSize().height * 2));
    leftPanel.add(statusLabel);

    rollLabel = new JLabel("");
    rollLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    ScorePanel.setLabelSize(rollLabel, "Roll 3");
    rollLabel.setPreferredSize(new Dimension(rollLabel.getPreferredSize().width, rollLabel.getPreferredSize().height * 2));
    leftPanel.add(rollLabel);

    mainFrame.getContentPane().add(leftPanel, BorderLayout.LINE_START);
    scorePanel = new ScorePanel();
    scorePanel.setBorder(BorderFactory.createLineBorder(Color.black));
    scorePanel.setPreferredSize(new Dimension(600, 400));
    mainFrame.getContentPane().add(scorePanel, BorderLayout.CENTER);

    highScores = new HighScores();
    highScorePanel = new HighScorePanel(highScores);
    highScorePanel.setPreferredSize(new Dimension(200, 400));
    mainFrame.getContentPane().add(highScorePanel, BorderLayout.EAST);
    highScorePanel.setVisible(false);

    // Do menu
    JMenuBar menubar = new JMenuBar();
    JMenu gameMenu = new JMenu("Game");
    JMenuItem newGameItem = new JMenuItem("New game");
    newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.META_MASK));
    newGameItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        newGame();
      }
    });
    gameMenu.add(newGameItem);

    highScoreItem = new JMenuItem("Show high scores");
    highScoreItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.META_MASK));
    highScoreItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        if (highScoresVisible) {
          hideHighScore();
        }
        else {
          showHighScore();
        }
      }
    });
    gameMenu.add(highScoreItem);
    menubar.add(gameMenu);
    mainFrame.setJMenuBar(menubar);
    menubar.setVisible(true);

    playerDialog = new PlayerDialog(mainFrame);
    playerDialog.pack();

    //Display the window.
    mainFrame.pack();
    mainFrame.setVisible(true);

    newGame();
  }


  private void newGame() {
    playerDialog.setLocationRelativeTo(mainFrame);
    playerDialog.setVisible(true);
    if (!playerDialog.wasCanceled()) {
      PlayerList playerList = playerDialog.getPlayerList();
      game = new Game(playerList, dice);
      dice.selectAll();
      scorePanel.setup(this, game);
      mainFrame.pack();
      mainFrame.repaint();
      updateStatus();
    }
  }

  private static final int numShake = 5;
  private static final int shakeWait = 40;

  private boolean isRolling = false;
  private int whichShake;

  private ActionListener rollAnimator = new ActionListener() {
    public void actionPerformed(ActionEvent actionEvent) {
      updateRoll();
    }
  };

  private final Timer timer = new Timer(shakeWait, rollAnimator);

  private void updateRoll() {
    if (isRolling) {
      if (whichShake < numShake) {
        game.getDice().shake();
        whichShake++;
      }
      else {
        game.getDice().rollFirst();
        whichShake = 0;
      }
      dicePanel.repaint();
      if (game.getDice().getFirstSelected() < 0) {
        // Finished rolling, so stop stuff
        isRolling = false;
        timer.stop();
        game.rolledDice();
        updateStatus();
      }
    }
  }

  private void doRoll() {
    if (game != null && game.canRoll()) {
      if (game.getWhichRoll() == 0) {
        game.getDice().selectAll();
      }
      rollButton.setEnabled(false);
      isRolling = true;
      statusLabel.setText("Rolling...");
      whichShake = 0;
      timer.start();
    }
  }

  /**
   * Update the status label and the enabling of the roll button.
   */
  private void updateStatus() {
    if (game == null) {
      rollButton.setEnabled(false);
      statusLabel.setText("Sign in");
      rollLabel.setText("");
      return;
    }
    rollButton.setEnabled(game.canRoll());
    if (game.isGameOver()) {
      statusLabel.setText("Game over!");
    }
    else if (game.canRoll()) {
      statusLabel.setText(game.canScore() ? "Roll or score" : "Roll 'em!");

    }
    else if (game.canSelect()) {
      statusLabel.setText("Select or score");
    }
    else {
      statusLabel.setText("Score it!");
    }
    switch (game.getWhichRoll()) {
      case 0: rollLabel.setText(""); break;
      case 1: rollLabel.setText("First roll"); break;
      case 2: rollLabel.setText("Second roll"); break;
      case 3: rollLabel.setText("Last roll"); break;
      default: rollLabel.setText("");
    }
  }

  private void doClickDie(DiePanel diePanel) {
    if (!isRolling && game != null && game.canSelect()) {
      game.getDice().toggle(diePanel.getDieIndex());
      diePanel.repaint();
      updateStatus();
    }
  }

  protected void setScore(ScoreLabel scoreLabel) {
    if (isRolling) {
      return;
    }
    game.setScore(scoreLabel.getColumnIndex(), scoreLabel.getCategory());
    if (game.isGameOver()) {
      // Game just now got over, so set high scores, if any.
      for (Player player : game.getPlayers()) {
        highScores.addScore(player.getName(), player.getGrandTotal());
      }
      highScorePanel.rebuild();
      showHighScore();
    }
    else {
      game.getDice().selectAll();
      dicePanel.repaint();
    }
    scorePanel.repaint();
    updateStatus();
  }

  private void showHighScore() {
    if (!highScoresVisible) {
      highScoreItem.setText("Hide high scores");
      highScorePanel.setVisible(true);
      mainFrame.pack();
      mainFrame.invalidate();
      highScoresVisible = true;
    }
  }

  private void hideHighScore() {
    if (highScoresVisible) {
      highScoreItem.setText("Show high scores");
      highScorePanel.setVisible(false);
      mainFrame.pack();
      mainFrame.invalidate();
      highScoresVisible = false;
    }
  }
}
