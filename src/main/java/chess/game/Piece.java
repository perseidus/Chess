package chess.game;

public class Piece {

  private final String color;
  private boolean firstMove;
  private PieceType type;

  public Piece(String color, PieceType type) {
    this.color = color;
    this.type = type;
    firstMove = true;
  }

  public String getColor() {
    return color;
  }

  public PieceType getType() {
    return type;
  }

  public void setType(PieceType type) {
    this.type = type;
  }

  public void setFirstMove(boolean firstMove) {
    this.firstMove = firstMove;
  }
}
