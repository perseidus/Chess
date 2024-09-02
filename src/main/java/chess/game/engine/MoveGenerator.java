package chess.game.engine;

import chess.game.logic.Move;
import chess.game.logic.Piece;
import chess.game.logic.PieceType;
import chess.game.logic.SpecialMoveType;
import chess.game.state.GameState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MoveGenerator {

  private static int[] tempKingPos;
  private static boolean[][] enemyAttackedSquares;
  private static String colorToTurn;
  private static Move lastEnemyMove;

  public static HashMap<String, List<Move>> getPossibleMoves(Piece[][] pieces) {
    GameState gameState = GameState.getInstance();
    colorToTurn = gameState.getColorToTurn();
    lastEnemyMove = gameState.getLastEnemyMove();
    HashMap<String, List<Move>> allMoves = new HashMap<>();

    enemyAttackedSquares = getEnemyAttackedSquares(pieces, true);

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

  public static boolean[][] getEnemyAttackedSquares(Piece[][] pieces, boolean setTempKingPos) {
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
    return BoardGenerator.movesToBitboard(moves);
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

  //removes move if it leaves own king in check
  private static List<Move> removeIllegalMoves(Piece[][] pieces, List<Move> moves) {
    Piece[][] newPieces = new Piece[8][8];
    boolean[][] attackedSquares;

    Iterator<Move> iterator = moves.iterator();

    while (iterator.hasNext()) {
      Move move = iterator.next();
      for (int i = 0; i < pieces.length; i++) {
        newPieces[i] = Arrays.copyOf(pieces[i], pieces[i].length);
      }

      newPieces = BoardGenerator.getBoardAfterMove(newPieces, move, false);
      attackedSquares = getEnemyAttackedSquares(newPieces, true);

      if (attackedSquares[tempKingPos[0]][tempKingPos[1]]) {
        iterator.remove();
      }
    }

    return moves;
  }

  private static List<Move> getPawnMoves(Piece[][] pieces, int i, int j) {
    List<Move> moves = new ArrayList<>();
    int[] from = {i, j};
    int moveDirWhite = GameState.getInstance().getMoveDirWhite();
    int moveDir = pieces[i][j].getColor().equals("white") ? moveDirWhite : moveDirWhite * -1;

    //double move, en passant (not for enemy pieces), normal move (+ promotion), capturing move (+ promotion)
    if (pieces[i][j].isFirstMove()
        && pieces[i - moveDir][j] == null && pieces[i - 2 * moveDir][j] == null) {
      moves.add(new Move(from, new int[]{i - 2 * moveDir, j}, SpecialMoveType.DOUBLE_PAWN));
    }
    if (lastEnemyMove != null && lastEnemyMove.getMoveType() == SpecialMoveType.DOUBLE_PAWN
        && pieces[i][j].getColor().equals(colorToTurn)
        && 7 - lastEnemyMove.getTo()[0] == i && 7 - lastEnemyMove.getTo()[1] == j - 1) {
      moves.add(new Move(from, new int[]{i - moveDir, j - 1}, SpecialMoveType.EN_PASSANT));
    }
    if (lastEnemyMove != null && lastEnemyMove.getMoveType() == SpecialMoveType.DOUBLE_PAWN
        && pieces[i][j].getColor().equals(colorToTurn)
        && 7 - lastEnemyMove.getTo()[0] == i && 7 - lastEnemyMove.getTo()[1] == j + 1) {
      moves.add(new Move(from, new int[]{i - moveDir, j + 1}, SpecialMoveType.EN_PASSANT));
    }
    if (pieces[i - moveDir][j] == null) {
      if (i - moveDir == 7 || i - moveDir == 0) {
        moves.add(new Move(from, new int[]{i - moveDir, j}, SpecialMoveType.PROMOTION));
      } else {
        moves.add(new Move(from, new int[]{i - moveDir, j}));
      }
    }
    if (j - 1 >= 0 && pieces[i - moveDir][j - 1] != null
        && !pieces[i - moveDir][j - 1].getColor().equals(pieces[i][j].getColor())) {
      if (i - moveDir == 7 || i - moveDir == 0) {
        moves.add(new Move(from, new int[]{i - moveDir, j - 1}, SpecialMoveType.PROMOTION));
      } else {
        moves.add(new Move(from, new int[]{i - moveDir, j - 1}));
      }
    }
    if (j + 1 <= 7 && pieces[i - moveDir][j + 1] != null
        && !pieces[i - moveDir][j + 1].getColor().equals(pieces[i][j].getColor())) {
      if (i - moveDir == 7 || i - moveDir == 0) {
        moves.add(new Move(from, new int[]{i - moveDir, j + 1}, SpecialMoveType.PROMOTION));
      } else {
        moves.add(new Move(from, new int[]{i - moveDir, j + 1}));
      }
    }

    return moves;
  }

  private static List<Move> getOrthogonalMoves(Piece[][] pieces, int i, int j) {
    List<Move> moves = new ArrayList<>();
    int[] from = {i, j};
    int count = 1;

    while ((i - count) >= 0) {              //UP
      if (pieces[i - count][j] == null) {   //empty square
        moves.add(new Move(from, new int[]{i - count, j}));
      } else if (!pieces[i - count][j].getColor()
          .equals(pieces[i][j].getColor())) {  //enemy pieces square
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
      } else if (!pieces[i][j + count].getColor().equals(pieces[i][j].getColor())) {
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
      } else if (!pieces[i + count][j].getColor().equals(pieces[i][j].getColor())) {
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
      } else if (!pieces[i][j - count].getColor().equals(pieces[i][j].getColor())) {
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
        !pieces[i - 2][j - 1].getColor().equals(pieces[i][j].getColor()))) {
      moves.add(new Move(from, new int[]{i - 2, j - 1}));
    }
    if ((((i - 1) >= 0) && ((j - 2) >= 0)) && ((pieces[i - 1][j - 2] == null) ||
        !pieces[i - 1][j - 2].getColor().equals(pieces[i][j].getColor()))) {
      moves.add(new Move(from, new int[]{i - 1, j - 2}));
    }
    if ((((i + 1) <= 7) && ((j - 2) >= 0)) && ((pieces[i + 1][j - 2] == null) ||
        !pieces[i + 1][j - 2].getColor().equals(pieces[i][j].getColor()))) {
      moves.add(new Move(from, new int[]{i + 1, j - 2}));
    }
    if ((((i + 2) <= 7) && ((j - 1) >= 0)) && ((pieces[i + 2][j - 1] == null) ||
        !pieces[i + 2][j - 1].getColor().equals(pieces[i][j].getColor()))) {
      moves.add(new Move(from, new int[]{i + 2, j - 1}));
    }
    if ((((i + 2) <= 7) && ((j + 1) <= 7)) && ((pieces[i + 2][j + 1] == null) ||
        !pieces[i + 2][j + 1].getColor().equals(pieces[i][j].getColor()))) {
      moves.add(new Move(from, new int[]{i + 2, j + 1}));
    }
    if ((((i + 1) <= 7) && ((j + 2) <= 7)) && ((pieces[i + 1][j + 2] == null) ||
        !pieces[i + 1][j + 2].getColor().equals(pieces[i][j].getColor()))) {
      moves.add(new Move(from, new int[]{i + 1, j + 2}));
    }
    if ((((i - 1) >= 0) && ((j + 2) <= 7)) && ((pieces[i - 1][j + 2] == null) ||
        !pieces[i - 1][j + 2].getColor().equals(pieces[i][j].getColor()))) {
      moves.add(new Move(from, new int[]{i - 1, j + 2}));
    }
    if ((((i - 2) >= 0) && ((j + 1) <= 7)) && ((pieces[i - 2][j + 1] == null) ||
        !pieces[i - 2][j + 1].getColor().equals(pieces[i][j].getColor()))) {
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
      } else if (!pieces[i - count][j - count].getColor().equals(pieces[i][j].getColor())) {
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
      } else if (!pieces[i - count][j + count].getColor().equals(pieces[i][j].getColor())) {
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
      } else if (!pieces[i + count][j + count].getColor().equals(pieces[i][j].getColor())) {
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
      } else if (!pieces[i + count][j - count].getColor().equals(pieces[i][j].getColor())) {
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
        !pieces[i - 1][j].getColor().equals(pieces[i][j].getColor()))) {
      moves.add(new Move(from, new int[]{i - 1, j}));
    }
    if ((((i - 1) >= 0) && ((j - 1) >= 0)) && ((pieces[i - 1][j - 1] == null) ||
        !pieces[i - 1][j - 1].getColor().equals(pieces[i][j].getColor()))) {
      moves.add(new Move(from, new int[]{i - 1, j - 1}));
    }
    if (((j - 1) <= 7) && ((pieces[i][j - 1] == null) ||
        !pieces[i][j - 1].getColor().equals(pieces[i][j].getColor()))) {
      moves.add(new Move(from, new int[]{i, j - 1}));
    }
    if ((((i + 1) <= 7) && ((j - 1) >= 0)) && ((pieces[i + 1][j - 1] == null) ||
        !pieces[i + 1][j - 1].getColor().equals(pieces[i][j].getColor()))) {
      moves.add(new Move(from, new int[]{i + 1, j - 1}));
    }
    if (((i + 1) <= 7) && ((pieces[i + 1][j] == null) ||
        !pieces[i + 1][j].getColor().equals(pieces[i][j].getColor()))) {
      moves.add(new Move(from, new int[]{i + 1, j}));
    }
    if ((((i + 1) <= 7) && ((j + 1) <= 7)) && ((pieces[i + 1][j + 1] == null) ||
        !pieces[i + 1][j + 1].getColor().equals(pieces[i][j].getColor()))) {
      moves.add(new Move(from, new int[]{i + 1, j + 1}));
    }
    if (((j + 1) <= 7) && ((pieces[i][j + 1] == null) ||
        !pieces[i][j + 1].getColor().equals(pieces[i][j].getColor()))) {
      moves.add(new Move(from, new int[]{i, j + 1}));
    }
    if ((((i - 1) >= 0) && ((j + 1) <= 7)) && ((pieces[i - 1][j + 1] == null) ||
        !pieces[i - 1][j + 1].getColor().equals(pieces[i][j].getColor()))) {
      moves.add(new Move(from, new int[]{i - 1, j + 1}));
    }

    //castling moves, right + left (not for enemy pieces)
    if (!pieces[i][j].getColor().equals(colorToTurn)) {
      return moves;
    }
    if (pieces[i][j].isFirstMove() && !enemyAttackedSquares[i][j]) {
      if (pieces[7][7] != null && pieces[7][7].isFirstMove()
          && pieces[i][j + 1] == null && pieces[i][j + 2] == null && pieces[7][6] == null
          && !enemyAttackedSquares[i][j + 1] && !enemyAttackedSquares[i][j + 2]) {
        moves.add(new Move(from, new int[]{i, j + 2}, SpecialMoveType.CASTLE));
      }
      if (pieces[7][0] != null && pieces[7][0].isFirstMove()
          && pieces[i][j - 1] == null && pieces[i][j - 2] == null && pieces[7][1] == null
          && !enemyAttackedSquares[i][j - 1] && !enemyAttackedSquares[i][j - 2]) {
        moves.add(new Move(from, new int[]{i, j - 2}, SpecialMoveType.CASTLE));
      }
    }

    return moves;
  }

  public static void switchColors() {
    if (colorToTurn.equals("white")) {
      colorToTurn = "black";
    } else {
      colorToTurn = "white";
    }
  }
}
