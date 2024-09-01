package chess.game.state;

public enum MatchResult {
  WHITE_WINS,
  BLACK_WINS,
  DRAW,
  ONGOING;

  private String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
