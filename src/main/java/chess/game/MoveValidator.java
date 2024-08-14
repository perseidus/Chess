package chess.game;

public class MoveValidator {

  public static Piece[][] setUpBoard() {
    Piece[][] pieces = new Piece[8][8];

    for (int i = 0; i < 8; i++) {
      pieces[1][i] = new Piece("black", PieceType.PAWN);
      pieces[6][i] = new Piece("white", PieceType.PAWN);
    }

    pieces[0][0] = new Piece("black", PieceType.ROOK);
    pieces[0][7] = new Piece("black", PieceType.ROOK);
    pieces[0][1] = new Piece("black", PieceType.KNIGHT);
    pieces[0][6] = new Piece("black", PieceType.KNIGHT);
    pieces[0][2] = new Piece("black", PieceType.BISHOP);
    pieces[0][5] = new Piece("black", PieceType.BISHOP);
    pieces[0][3] = new Piece("black", PieceType.QUEEN);
    pieces[0][4] = new Piece("black", PieceType.KING);

    pieces[7][0] = new Piece("white", PieceType.ROOK);
    pieces[7][7] = new Piece("white", PieceType.ROOK);
    pieces[7][1] = new Piece("white", PieceType.KNIGHT);
    pieces[7][6] = new Piece("white", PieceType.KNIGHT);
    pieces[7][2] = new Piece("white", PieceType.BISHOP);
    pieces[7][5] = new Piece("white", PieceType.BISHOP);
    pieces[7][3] = new Piece("white", PieceType.QUEEN);
    pieces[7][4] = new Piece("white", PieceType.KING);

    return pieces;
  }

}
