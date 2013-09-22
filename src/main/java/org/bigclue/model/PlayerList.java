package org.bigclue.model;

import java.util.prefs.Preferences;

/**
 * Class representing a list of players, and whether they are selected to play in a game.
 * Author: Randy Wilson
 * Date: 8/17/13
 */
public class PlayerList {
  public static final int maxPlayers = 5;
  private String[] names = new String[maxPlayers];
  private boolean[] selected = new boolean[maxPlayers];

  private String[] defaultNames = new String[]{"Randy", "Linette", "Jared", "Sienna", "Krunk"};
  Preferences preferences;

  public PlayerList() {
    preferences = Preferences.systemNodeForPackage(PlayerList.class);
    for (int i = 0; i < maxPlayers; i++) {
      names[i] = preferences.get("Name" + (i + 1), defaultNames[i]);
      selected[i] = preferences.getBoolean("Selected" + (i + 1), i < 2);
    }
  }

  public String[] getNames() {
    return names;
  }

  public void setName(int i, String name) {
    names[i] = name;
    preferences.put("Name" + (i + 1), name);
  }

  public boolean[] getSelected() {
    return selected;
  }

  public void setSelected(int i, boolean isSelected) {
    this.selected[i] = isSelected;
    preferences.putBoolean("Selected" + (i + 1), isSelected);
  }
}
