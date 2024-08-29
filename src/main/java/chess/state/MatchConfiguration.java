package chess.state;

public class MatchConfiguration {

  private static MatchConfiguration instance;

  private boolean pvpMode;
  private boolean playerWhiteAtStart;
  private int time;
  private int increment;

  private MatchConfiguration() {
    pvpMode = false;
    playerWhiteAtStart = true;
    time = Parameters.DEFAULT_TIME;
    increment = Parameters.DEFAULT_INCREMENT;
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

  public void setPlayerWhiteAtStart(boolean playerWhiteAtStart) {
    this.playerWhiteAtStart = playerWhiteAtStart;
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
}
