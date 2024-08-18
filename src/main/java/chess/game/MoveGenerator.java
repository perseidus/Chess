package chess.game;

import chess.state.GameState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoveGenerator {

  private static Piece[][] pieces;
  private static String colorToTurn;

  private static boolean enemyAttacksSquare[][];

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

  public static HashMap<String, List<Move>> getPossibleMoves(Piece[][] pieces) {
    MoveGenerator.pieces = pieces;
    colorToTurn = GameState.getInstance().getColorToTurn();
    HashMap<String, List<Move>> moves = new HashMap<>();

    setEnemyAttackedSquares();

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Piece piece = pieces[i][j];
        if (piece != null && piece.getColor().equals(colorToTurn)) {
          getMoveForPiece(piece, true);
          List<Move> moveForPiece = getMoveForPiece(piece, false);
          moves.put("" + i + j, moveForPiece);
        }
      }
    }

    return moves;
  }

  private static void setEnemyAttackedSquares() {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Piece piece = pieces[i][j];
        if (piece != null && !piece.getColor().equals(colorToTurn)) {
          getMoveForPiece(piece, true);
        }
      }
    }
  }

  private static List<Move> getMoveForPiece(Piece piece, boolean enemyPiece) {
    List<Move> moves = new ArrayList<>();
    switch (piece.getType()) {
      case PAWN:
        moves = getPawnMoves(piece);
        break;
      case ROOK:
        moves = getRookMoves(piece);
        break;
      case KNIGHT:
        moves = getKnightMoves(piece);
        break;
      case BISHOP:
        moves = getBishopMoves(piece);
        break;
      case QUEEN:
        moves = getQueenMoves(piece);
        break;
      case KING:
        moves = getKingMoves(piece);
        break;
    }
    if (enemyPiece) {
      boolean[][] pieceAttacksSquare = movesToBitboard(moves);
      for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
          if (pieceAttacksSquare[i][j]) {
            enemyAttacksSquare[i][j] = true;
          }
        }
      }
    }
    return moves;
  }

  public static boolean[][] movesToBitboard(List<Move> moves) {
    boolean[][] pieceAttacksSquare = new boolean[7][7];
    for (Move move : moves) {
      pieceAttacksSquare[move.getTo()[0]][move.getTo()[1]] = true;
    }
    return pieceAttacksSquare;
  }

  //TODO
  private static List<Move> getPawnMoves(Piece piece) {
    return null;
  }

  private static List<Move> getRookMoves(Piece piece) {
    return null;
  }

  private static List<Move> getKnightMoves(Piece piece) {
    return null;
  }

  private static List<Move> getBishopMoves(Piece piece) {
    return null;
  }

  private static List<Move> getQueenMoves(Piece piece) {
    return null;
  }

  private static List<Move> getKingMoves(Piece piece) {
    return null;
  }
}
