package chess.game;

public enum PieceType {
  PAWN("pieces/pawn_"),
  KNIGHT("pieces/knight_"),
  BISHOP("/pieces/bishop_"),
  ROOK("/pieces/rook_"),
  QUEEN("/pieces/queen_"),
  KING("/pieces/king_");

  private String path;

  private PieceType(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }
}
