package chess.game;

public class Piece {

  private boolean whitePiece;
  private PieceType type;

  public boolean isWhitePiece() {
    return whitePiece;
  }

  public void setWhitePiece(boolean whitePiece) {
    this.whitePiece = whitePiece;
  }

  public PieceType getType() {
    return type;
  }

  public void setType(PieceType type) {
    this.type = type;
  }
}
