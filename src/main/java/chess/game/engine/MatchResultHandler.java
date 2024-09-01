package chess.game.engine;

import chess.game.logic.Piece;
import chess.game.logic.PieceType;
import chess.game.state.GameState;

public class MatchResultHandler {

  public static void gameOverTimeOut() {

  }

  public static void calculateKingsPos(Piece[][] pieces) {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (pieces[i][j] != null && pieces[i][j].getType() == PieceType.KING) {
          if (pieces[i][j].getColor().equals("white")) {
            GameState.getInstance().setWhiteKingPos(new int[]{i, j});
          } else {
            GameState.getInstance().setBlackKingPos(new int[]{i, j});
          }
        }
      }
    }
  }

  public static void calculateKingsInCheck(Piece[][] pieces) {
    GameState gameState = GameState.getInstance();
    int[] whiteKing = gameState.getWhiteKingPos();
    int[] blackKing = gameState.getBlackKingPos();

    //enemy attacks own king
    MoveGenerator.switchColors();
    boolean[][] attacked1 = MoveGenerator.getEnemyAttackedSquares(pieces, false);
    MoveGenerator.switchColors();

    //enemy kings gets attacked
    boolean[][] attacked2 = BoardGenerator.movesToBitboard(MoveGenerator.getPossibleMoves(pieces));

    if (attacked1[whiteKing[0]][whiteKing[1]] || attacked2[whiteKing[0]][whiteKing[1]]) {
      gameState.setWhiteKingInCheck(true);
    } else {
      gameState.setWhiteKingInCheck(false);
    }
    if (attacked1[blackKing[0]][blackKing[1]] || attacked2[blackKing[0]][blackKing[1]]) {
      gameState.setBlackKingInCheck(true);
    } else {
      gameState.setBlackKingInCheck(false);
    }
  }

}
