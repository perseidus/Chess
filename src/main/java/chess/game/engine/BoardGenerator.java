package chess.game.engine;

import chess.game.logic.Move;
import chess.game.logic.Piece;
import chess.game.logic.PieceType;
import chess.game.logic.SpecialMoveType;
import java.util.HashMap;
import java.util.List;

public class BoardGenerator {

  public static Piece[][] setUpBoard() {
    Piece[][] pieces = new Piece[8][8];

    for (int i = 0; i < 8; i++) {
      pieces[1][i] = new Piece(false, PieceType.PAWN);
      pieces[6][i] = new Piece(true, PieceType.PAWN);
    }

    pieces[0][0] = new Piece(false, PieceType.ROOK);
    pieces[0][7] = new Piece(false, PieceType.ROOK);
    pieces[0][1] = new Piece(false, PieceType.KNIGHT);
    pieces[0][6] = new Piece(false, PieceType.KNIGHT);
    pieces[0][2] = new Piece(false, PieceType.BISHOP);
    pieces[0][5] = new Piece(false, PieceType.BISHOP);
    pieces[0][3] = new Piece(false, PieceType.QUEEN);
    pieces[0][4] = new Piece(false, PieceType.KING);

    pieces[7][0] = new Piece(true, PieceType.ROOK);
    pieces[7][7] = new Piece(true, PieceType.ROOK);
    pieces[7][1] = new Piece(true, PieceType.KNIGHT);
    pieces[7][6] = new Piece(true, PieceType.KNIGHT);
    pieces[7][2] = new Piece(true, PieceType.BISHOP);
    pieces[7][5] = new Piece(true, PieceType.BISHOP);
    pieces[7][3] = new Piece(true, PieceType.QUEEN);
    pieces[7][4] = new Piece(true, PieceType.KING);

    return pieces;
  }

  public static Piece[][] getBoardAfterMove(Piece[][] pieces, Move move) {

    pieces[move.getTo()[0]][move.getTo()[1]] = pieces[move.getFrom()[0]][move.getFrom()[1]];
    pieces[move.getFrom()[0]][move.getFrom()[1]] = null;

    if (move.getMoveType() == SpecialMoveType.EN_PASSANT) {     //remove enemy pawn
      pieces[move.getTo()[0] + 1][move.getTo()[1]] = null;

    } else if (move.getMoveType() == SpecialMoveType.CASTLE) {  //move rook from (right/left)
      if (move.getFrom()[1] < move.getTo()[1]) {
        pieces[move.getTo()[0]][move.getTo()[1] - 1] = pieces[move.getTo()[0]][7];
        pieces[move.getTo()[0]][7] = null;
      } else {
        pieces[move.getTo()[0]][move.getTo()[1] + 1] = pieces[move.getTo()[0]][0];
        pieces[move.getTo()[0]][0] = null;
      }
    }

    return pieces;
  }

  public static boolean[][] movesToBitboard(HashMap<String, List<Move>> moves) {
    boolean[][] pieceAttacksSquare = new boolean[8][8];
    for (List<Move> movesForPiece : moves.values()) {
      for (Move move : movesForPiece) {
        pieceAttacksSquare[move.getTo()[0]][move.getTo()[1]] = true;
      }
    }
    return pieceAttacksSquare;
  }

  public static boolean[][] movesToBitboard(List<Move> moves) {
    boolean[][] pieceAttacksSquare = new boolean[8][8];
    for (Move move : moves) {
      pieceAttacksSquare[move.getTo()[0]][move.getTo()[1]] = true;
    }
    return pieceAttacksSquare;
  }

  public static boolean[][] getEnemyPosBitboard(Piece[][] pieces, String colorToTurn) {
    boolean[][] enemyPiece = new boolean[8][8];
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (pieces[i][j] != null && !pieces[i][j].getColor().equals(colorToTurn)) {
          enemyPiece[i][j] = true;
        }
      }
    }
    return enemyPiece;
  }

  public static Piece[][] flipBoard(Piece[][] pieces) {
    Piece[] tmp;
    for (int i = 0, j = 7; i < 4; i++, j--) {
      tmp = pieces[i];
      pieces[i] = pieces[j];
      pieces[j] = tmp;
    }
    Piece temp;
    for (int i = 0; i < 8; i++) {
      for (int j = 0, k = 7; j < 4; j++, k--) {
        temp = pieces[i][j];
        pieces[i][j] = pieces[i][k];
        pieces[i][k] = temp;
      }
    }
    return pieces;
  }
}
