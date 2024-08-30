package chess.state;

public class MatchConfiguration {

  private static MatchConfiguration instance;

  private boolean pvpMode;
  private boolean playerWhiteAtStart;

  private int selectedColorButton;
  private int time;
  private int increment;
  private String computer;

  private MatchConfiguration() {
    pvpMode = false;
    playerWhiteAtStart = true;

    selectedColorButton = Parameters.DEFAULT_COLOR;
    time = Parameters.DEFAULT_TIME;
    increment = Parameters.DEFAULT_INCREMENT;
    computer = Parameters.DEFAULT_COMPUTER;
  }

  public static MatchConfiguration getInstance() {
    if (instance == null) {
      instance = new MatchConfiguration();
    }
    return instance;
  }

  public boolean isPvpMode() {
    return pvpMode;
  }

  public void setPvpMode(boolean pvpMode) {
    this.pvpMode = pvpMode;
  }

  public boolean isPlayerWhiteAtStart() {
    return playerWhiteAtStart;
  }

  public int getTime() {
    return time;
  }

  public void setTime(int time) {
    this.time = time;
  }

  public int getIncrement() {
    return increment;
  }

  public void setIncrement(int increment) {
    this.increment = increment;
  }

  public String getComputer() {
    return computer;
  }

  public void setComputer(String computer) {
    this.computer = computer;
  }

  public int getSelectedColorButton() {
    return selectedColorButton;
  }

  public void setSelectedColorButton(int selectedColorButton) {
    this.selectedColorButton = selectedColorButton;
    if (!pvpMode && selectedColorButton == 1) {
      playerWhiteAtStart = Math.random() < 0.5;
    } else if (!pvpMode && selectedColorButton == 2) {
      playerWhiteAtStart = false;
    } else {
      playerWhiteAtStart = true;
    }
  }
}
