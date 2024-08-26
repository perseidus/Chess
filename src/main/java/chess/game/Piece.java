package chess.game;

public class Piece {

  private final boolean white;
  private boolean firstMove;
  private PieceType type;

  public Piece(boolean white, PieceType type) {
    this.white = white;
    this.type = type;
    firstMove = true;
  }

  public boolean isWhite() {
    return white;
  }

  public String getColor() {
    if (white) {
      return "white";
    }
    return "black";
  }

  public PieceType getType() {
    return type;
  }

  public void setType(PieceType type) {
    this.type = type;
  }

  public boolean isFirstMove() {
    return firstMove;
  }

  public void setFirstMove(boolean firstMove) {
    this.firstMove = firstMove;
  }
}
