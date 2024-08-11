package chess.state;

public class MatchConfiguration {

  private static MatchConfiguration instance;

  private boolean pvp;
  private boolean whitePlayer;
  private int time;
  private int increment;

  private MatchConfiguration() {
    pvp = true;
    whitePlayer = true;
    time = Parameters.DEFAULT_TIME;
    increment = Parameters.DEFAULT_INCREMENT;
  }

  public static MatchConfiguration getInstance() {
    if (instance == null) {
      instance = new MatchConfiguration();
    }
    return instance;
  }

  public boolean isPvp() {
    return pvp;
  }

  public void setPvp(boolean pvp) {
    this.pvp = pvp;
  }

  public boolean isWhitePlayer() {
    return whitePlayer;
  }

  public void setWhitePlayer(boolean whitePlayer) {
    this.whitePlayer = whitePlayer;
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
