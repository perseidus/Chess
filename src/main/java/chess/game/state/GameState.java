package chess.game.state;

import chess.game.engine.BoardGenerator;
import chess.game.logic.Move;
import chess.game.logic.Piece;
import chess.game.logic.PieceType;

public class GameState {

  private static GameState instance;

  private MatchConfiguration matchConfiguration;
  private Piece[][] board;
  private String colorToTurn;
  private int moveDirWhite;

  private Move lastWhiteMove, lastBlackMove, lastMove;
  private int[] whiteKingPos, blackKingPos;
  private boolean whiteKingInCheck, blackKingInCheck;

  private int count50move;

  private GameState() {
    loadMatchConfiguration();
  }

  public static GameState getInstance() {
    if (instance == null) {
      instance = new GameState();
    }
    return instance;
  }

  public void loadMatchConfiguration() {
    matchConfiguration = MatchConfiguration.getInstance();
    lastWhiteMove = null;   //dummy move
    lastBlackMove = null;   //dummy move
    lastMove = null;        //dummy move
    whiteKingInCheck = false;
    blackKingInCheck = false;
    count50move = 0;
    colorToTurn = "white";
    board = BoardGenerator.setUpBoard();

    if (matchConfiguration.isPlayerWhiteAtStart()) {
      moveDirWhite = 1;
    } else {
      moveDirWhite = -1;
    }

    if (!matchConfiguration.isPvpMode() && !matchConfiguration.isPlayerWhiteAtStart()) {
      moveDirWhite = -1;
      board = BoardGenerator.flipBoard(board);
    }
  }

  public void changeTurn() {
    if (colorToTurn.equals("white")) {
      colorToTurn = "black";
    } else {
      colorToTurn = "white";
    }

    if (matchConfiguration.isPvpMode()) {
      if (lastMove != null) {
        lastMove.flipCoords();
      }
      moveDirWhite *= -1;
      board = BoardGenerator.flipBoard(board);
    }
  }

  public void handleMove(Move move) {
    Piece moved = board[move.getTo()[0]][move.getTo()[1]];
    moved.setFirstMove(false);
    if (move.isCapturingMove() || moved.getType() == PieceType.PAWN) {
      count50move = 0;
    } else {
      count50move++;
    }
  }

  public int getCount50move() {
    return count50move;
  }

  public Piece[][] getBoard() {
    return board;
  }

  public void setBoard(Piece[][] board) {
    this.board = board;
  }

  public String getColorToTurn() {
    return colorToTurn;
  }

  public boolean isColorToTurnInCheck() {
    if (colorToTurn.equals("white")) {
      return whiteKingInCheck;
    }
    return blackKingInCheck;
  }

  public Move getLastMove() {
    return lastMove;
  }

  public void setLastWhiteMove(Move lastWhiteMove) {
    this.lastWhiteMove = lastWhiteMove;
    this.lastMove = lastWhiteMove;
  }

  public void setLastBlackMove(Move lastBlackMove) {
    this.lastBlackMove = lastBlackMove;
    this.lastMove = lastBlackMove;
  }

  public Move getLastEnemyMove() {
    if (colorToTurn.equals("white")) {
      return lastBlackMove;
    }
    return lastWhiteMove;
  }

  public int getMoveDirWhite() {
    return moveDirWhite;
  }

  public int[] getWhiteKingPos() {
    return whiteKingPos;
  }

  public void setWhiteKingPos(int[] whiteKingPos) {
    this.whiteKingPos = whiteKingPos;
  }

  public int[] getBlackKingPos() {
    return blackKingPos;
  }

  public void setBlackKingPos(int[] blackKingPos) {
    this.blackKingPos = blackKingPos;
  }

  public boolean isWhiteKingInCheck() {
    return whiteKingInCheck;
  }

  public void setWhiteKingInCheck(boolean whiteKingInCheck) {
    this.whiteKingInCheck = whiteKingInCheck;
  }

  public boolean isBlackKingInCheck() {
    return blackKingInCheck;
  }

  public void setBlackKingInCheck(boolean blackKingInCheck) {
    this.blackKingInCheck = blackKingInCheck;
  }
}
