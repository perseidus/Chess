package chess.game;

import chess.state.GameState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MoveGenerator {

  private static int[] kingPos;
  private static int[] tempKingPos;
  private static boolean kingInCheck;
  private static String colorToTurn;

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
    colorToTurn = GameState.getInstance().getColorToTurn();
    HashMap<String, List<Move>> allMoves = new HashMap<>();

    boolean[][] enemyAttackedSquares = getEnemyAttackedSquares(pieces, true);
    kingPos = new int[]{tempKingPos[0], tempKingPos[1]};
    if (enemyAttackedSquares[kingPos[0]][kingPos[1]]) {
      kingInCheck = true;
    } else {
      kingInCheck = false;
    }

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Piece piece = pieces[i][j];
        if (piece != null && piece.getColor().equals(colorToTurn)) {
          List<Move> moveForPiece = getMoveForPiece(pieces, i, j, false);
          allMoves.put("" + i + j, moveForPiece);
        }
      }
    }

    return allMoves;
  }

  private static boolean[][] getEnemyAttackedSquares(Piece[][] pieces, boolean setTempKingPos) {
    HashMap<String, List<Move>> moves = new HashMap<>();
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Piece piece = pieces[i][j];
        if (piece != null) {
          if (!piece.getColor().equals(colorToTurn)) {
            List<Move> movesForPiece = getMoveForPiece(pieces, i, j, true);
            moves.put("" + i + j, movesForPiece);
          } else if (setTempKingPos && piece.getType().equals(PieceType.KING)) {
            tempKingPos = new int[]{i, j};
          }
        }
      }
    }
    return movesToBitboard(moves);
  }

  private static List<Move> getMoveForPiece(Piece[][] pieces, int i, int j, boolean enemyPiece) {
    List<Move> moves = new ArrayList<>();
    switch (pieces[i][j].getType()) {
      case PAWN:
        moves = getPawnMoves(pieces, i, j);
        break;
      case ROOK:
        moves = getRookMoves(pieces, i, j);
        break;
      case KNIGHT:
        moves = getKnightMoves(pieces, i, j);
        break;
      case BISHOP:
        moves = getBishopMoves(pieces, i, j);
        break;
      case QUEEN:
        moves = getQueenMoves(pieces, i, j);
        break;
      case KING:
        moves = getKingMoves(pieces, i, j);
        break;
    }
    if (!enemyPiece) {
      moves = removeIllegalMoves(pieces, moves);
    }
    return moves;
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

  //removes move if it leaves own king in check
  private static List<Move> removeIllegalMoves(Piece[][] pieces, List<Move> moves) {
    Piece[][] newPieces = new Piece[8][8];
    boolean[][] attackedSquares;

    for (Move move : moves) {
      for (int i = 0; i < pieces.length; i++) {
        newPieces[i] = Arrays.copyOf(pieces[i], pieces[i].length);
      }
      newPieces[move.getTo()[0]][move.getTo()[1]] = newPieces[move.getFrom()[0]][move.getFrom()[1]];
      newPieces[move.getFrom()[0]][move.getFrom()[1]] = null;
      attackedSquares = getEnemyAttackedSquares(newPieces, true);

      if (attackedSquares[tempKingPos[0]][tempKingPos[1]]) {
        moves.remove(move);
      }
    }

    return moves;
  }

  //TODO
  private static List<Move> getPawnMoves(Piece[][] pieces, int i, int j) {
    List<Move> moves = new ArrayList<>();
    int[] from = {i, j};
    int moveDir = pieces[i][j].getColor().equals(colorToTurn) ? 1 : -1;

    return moves;
  }

  private static List<Move> getRookMoves(Piece[][] pieces, int i, int j) {
    return null;
  }

  private static List<Move> getKnightMoves(Piece[][] pieces, int i, int j) {
    return null;
  }

  private static List<Move> getBishopMoves(Piece[][] pieces, int i, int j) {
    return null;
  }

  private static List<Move> getQueenMoves(Piece[][] pieces, int i, int j) {
    return null;
  }

  private static List<Move> getKingMoves(Piece[][] pieces, int i, int j) {
    return null;
  }
}
