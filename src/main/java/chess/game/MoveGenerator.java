package chess.game;

import chess.state.GameState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MoveGenerator {

  private static int[] kingPos;
  private static int[] tempKingPos;
  private static boolean[][] enemyAttackedSquares;
  private static String colorToTurn;

  public static HashMap<String, List<Move>> getPossibleMoves(Piece[][] pieces) {
    colorToTurn = GameState.getInstance().getColorToTurn();
    HashMap<String, List<Move>> allMoves = new HashMap<>();

    enemyAttackedSquares = getEnemyAttackedSquares(pieces, true);
    kingPos = new int[]{tempKingPos[0], tempKingPos[1]};

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
        moves = getOrthogonalMoves(pieces, i, j);
        break;
      case KNIGHT:
        moves = getKnightMoves(pieces, i, j);
        break;
      case BISHOP:
        moves = getDiagonalMoves(pieces, i, j);
        break;
      case QUEEN:
        moves = getDiagonalMoves(pieces, i, j);
        for (Move m : getOrthogonalMoves(pieces, i, j)) {
          moves.add(m);
        }
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

  //TODO check checks for special move types
  //removes move if it leaves own king in check
  private static List<Move> removeIllegalMoves(Piece[][] pieces, List<Move> moves) {
    Piece[][] newPieces = new Piece[8][8];
    boolean[][] attackedSquares;

    for (Move move : moves) {
      for (int i = 0; i < pieces.length; i++) {
        newPieces[i] = Arrays.copyOf(pieces[i], pieces[i].length);
      }
      if (move.isSpecialMove()){
        newPieces = BoardGenerator.getBoardAfterSpecialMove(newPieces, move);
      } else {
        newPieces[move.getTo()[0]][move.getTo()[1]] = newPieces[move.getFrom()[0]][move.getFrom()[1]];
        newPieces[move.getFrom()[0]][move.getFrom()[1]] = null;
      }
      attackedSquares = getEnemyAttackedSquares(newPieces, true);

      if (attackedSquares[tempKingPos[0]][tempKingPos[1]]) {
        moves.remove(move);
      }
    }

    return moves;
  }

  //TODO pawn moves
  private static List<Move> getPawnMoves(Piece[][] pieces, int i, int j) {
    List<Move> moves = new ArrayList<>();
    int[] from = {i, j};
    int moveDir = pieces[i][j].getColor().equals(colorToTurn) ? 1 : -1;

    return moves;
  }

  private static List<Move> getOrthogonalMoves(Piece[][] pieces, int i, int j) {
    List<Move> moves = new ArrayList<>();
    int[] from = {i, j};
    int count = 1;

    while ((i - count) >= 0) {              //UP
      if (pieces[i - count][j] == null) {   //empty square
        moves.add(new Move(from, new int[]{i - count, j}));
      } else if (!pieces[i - count][j].getColor().equals(colorToTurn)) {  //enemy pieces square
        moves.add(new Move(from, new int[]{i - count, j}));
        break;
      } else {    //own pieces square
        break;
      }
      count++;
    }

    count = 1;
    while ((j + count) <= 7) {              //RIGHT
      if (pieces[i][j + count] == null) {
        moves.add(new Move(from, new int[]{i, j + count}));
      } else if (!pieces[i][j + count].getColor().equals(colorToTurn)) {
        moves.add(new Move(from, new int[]{i, j + count}));
        break;
      } else {
        break;
      }
      count++;
    }

    count = 1;
    while ((i + count) <= 7) {              //DOWN
      if (pieces[i + count][j] == null) {
        moves.add(new Move(from, new int[]{i + count, j}));
      } else if (!pieces[i + count][j].getColor().equals(colorToTurn)) {
        moves.add(new Move(from, new int[]{i + count, j}));
        break;
      } else {
        break;
      }
      count++;
    }

    count = 1;
    while ((j - count) >= 0) {              //LEFT
      if (pieces[i][j - count] == null) {
        moves.add(new Move(from, new int[]{i, j - count}));
      } else if (!pieces[i][j - count].getColor().equals(colorToTurn)) {
        moves.add(new Move(from, new int[]{i, j - count}));
        break;
      } else {
        break;
      }
      count++;
    }

    return moves;
  }

  private static List<Move> getKnightMoves(Piece[][] pieces, int i, int j) {
    List<Move> moves = new ArrayList<>();
    int[] from = {i, j};

    //counter-clockwise starting at upper left move
    if ((((i - 2) >= 0) && ((j - 1) >= 0)) && ((pieces[i - 2][j - 1] == null) ||
        !pieces[i - 2][j - 1].getColor().equals(colorToTurn))) {
      moves.add(new Move(from, new int[]{i - 2, j - 1}));
    }
    if ((((i - 1) >= 0) && ((j - 2) >= 0)) && ((pieces[i - 1][j - 2] == null) ||
        !pieces[i - 1][j - 2].getColor().equals(colorToTurn))) {
      moves.add(new Move(from, new int[]{i - 1, j - 2}));
    }
    if ((((i + 1) <= 7) && ((j - 2) >= 0)) && ((pieces[i + 1][j - 2] == null) ||
        !pieces[i + 1][j - 2].getColor().equals(colorToTurn))) {
      moves.add(new Move(from, new int[]{i + 1, j - 2}));
    }
    if ((((i + 2) <= 7) && ((j - 1) >= 0)) && ((pieces[i + 2][j - 1] == null) ||
        !pieces[i + 2][j - 1].getColor().equals(colorToTurn))) {
      moves.add(new Move(from, new int[]{i + 2, j - 1}));
    }
    if ((((i + 2) <= 7) && ((j + 1) <= 7)) && ((pieces[i + 2][j + 1] == null) ||
        !pieces[i + 2][j + 1].getColor().equals(colorToTurn))) {
      moves.add(new Move(from, new int[]{i + 2, j + 1}));
    }
    if ((((i + 1) <= 7) && ((j + 2) <= 7)) && ((pieces[i + 1][j + 2] == null) ||
        !pieces[i + 1][j + 2].getColor().equals(colorToTurn))) {
      moves.add(new Move(from, new int[]{i + 1, j + 2}));
    }
    if ((((i - 1) >= 0) && ((j + 2) <= 7)) && ((pieces[i - 1][j + 2] == null) ||
        !pieces[i - 1][j + 2].getColor().equals(colorToTurn))) {
      moves.add(new Move(from, new int[]{i - 1, j + 2}));
    }
    if ((((i - 2) >= 0) && ((j + 1) <= 7)) && ((pieces[i - 2][j + 1] == null) ||
        !pieces[i - 2][j + 1].getColor().equals(colorToTurn))) {
      moves.add(new Move(from, new int[]{i - 2, j + 1}));
    }

    return moves;
  }

  private static List<Move> getDiagonalMoves(Piece[][] pieces, int i, int j) {
    List<Move> moves = new ArrayList<>();
    int[] from = {i, j};
    int count = 1;

    while (((i - count) >= 0) && ((j - count) >= 0)) {          //UP-LEFT
      if (pieces[i - count][j - count] == null) {
        moves.add(new Move(from, new int[]{i - count, j - count}));
      } else if (!pieces[i - count][j - count].getColor().equals(colorToTurn)) {
        moves.add(new Move(from, new int[]{i - count, j - count}));
        break;
      } else {
        break;
      }
      count++;
    }

    count = 1;
    while (((i - count) >= 0) && ((j + count) <= 7)) {                      //UP-RIGHT
      if (pieces[i - count][j + count] == null) {
        moves.add(new Move(from, new int[]{i - count, j + count}));
      } else if (!pieces[i - count][j + count].getColor().equals(colorToTurn)) {
        moves.add(new Move(from, new int[]{i - count, j + count}));
        break;
      } else {
        break;
      }
      count++;
    }

    count = 1;
    while (((i + count) <= 7) && ((j + count) <= 7)) {                      //DOWN-RIGHT
      if (pieces[i + count][j + count] == null) {
        moves.add(new Move(from, new int[]{i + count, j + count}));
      } else if (!pieces[i + count][j + count].getColor().equals(colorToTurn)) {
        moves.add(new Move(from, new int[]{i + count, j + count}));
        break;
      } else {
        break;
      }
      count++;
    }

    count = 1;
    while (((i + count) <= 7) && ((j - count) >= 0)) {                      //DOWN-LEFT
      if (pieces[i + count][j - count] == null) {
        moves.add(new Move(from, new int[]{i + count, j - count}));
      } else if (!pieces[i + count][j - count].getColor().equals(colorToTurn)) {
        moves.add(new Move(from, new int[]{i + count, j - count}));
        break;
      } else {
        break;
      }
      count++;
    }

    return moves;
  }

  private static List<Move> getKingMoves(Piece[][] pieces, int i, int j) {
    List<Move> moves = new ArrayList<>();
    int[] from = {i, j};

    //counter-clockwise starting at top square
    if (((i - 1) >= 0) && ((pieces[i - 1][j] == null) ||
        !pieces[i - 1][j].getColor().equals(colorToTurn))) {
      moves.add(new Move(from, new int[]{i - 1, j}));
    }
    if ((((i - 1) >= 0) && ((j - 1) >= 0)) && ((pieces[i - 1][j - 1] == null) ||
        !pieces[i - 1][j - 1].getColor().equals(colorToTurn))) {
      moves.add(new Move(from, new int[]{i - 1, j - 1}));
    }
    if (((j - 1) >= 7) && ((pieces[i][j - 1] == null) ||
        !pieces[i][j - 1].getColor().equals(colorToTurn))) {
      moves.add(new Move(from, new int[]{i, j - 1}));
    }
    if ((((i + 1) <= 7) && ((j - 1) >= 0)) && ((pieces[i + 1][j - 1] == null) ||
        !pieces[i + 1][j - 1].getColor().equals(colorToTurn))) {
      moves.add(new Move(from, new int[]{i + 1, j - 1}));
    }
    if (((i + 1) <= 7) && ((pieces[i + 1][j] == null) ||
        !pieces[i + 1][j].getColor().equals(colorToTurn))) {
      moves.add(new Move(from, new int[]{i + 1, j}));
    }
    if ((((i + 1) <= 7) && ((j + 1) <= 7)) && ((pieces[i + 1][j + 1] == null) ||
        !pieces[i + 1][j + 1].getColor().equals(colorToTurn))) {
      moves.add(new Move(from, new int[]{i + 1, j + 1}));
    }
    if (((j + 1) <= 7) && ((pieces[i][j + 1] == null) ||
        !pieces[i][j + 1].getColor().equals(colorToTurn))) {
      moves.add(new Move(from, new int[]{i, j + 1}));
    }
    if ((((i - 1) >= 0) && ((j + 1) <= 7)) && ((pieces[i - 1][j + 1] == null) ||
        !pieces[i - 1][j + 1].getColor().equals(colorToTurn))) {
      moves.add(new Move(from, new int[]{i - 1, j + 1}));
    }

    //castling moves, right + left
    if (pieces[i][j].isFirstMove() && !enemyAttackedSquares[i][j]) {
      if (pieces[7][7].isFirstMove()
          && pieces[i][j + 1] == null && pieces[i][j + 2] == null && pieces[7][6] == null
          && !enemyAttackedSquares[i][j + 1] && !enemyAttackedSquares[i][j + 2]) {
        moves.add(new Move(from, new int[]{i, j + 2}, SpecialMoveType.CASTLE));
      }
      if (pieces[7][0].isFirstMove()
          && pieces[i][j - 1] == null && pieces[i][j - 2] == null && pieces[7][1] == null
          && !enemyAttackedSquares[i][j - 1] && !enemyAttackedSquares[i][j - 2]) {
        moves.add(new Move(from, new int[]{i, j - 2}, SpecialMoveType.CASTLE));
      }
    }

    return moves;
  }
}
