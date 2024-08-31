package chess.state;

import chess.game.engine.BoardGenerator;
import chess.game.logic.Move;
import chess.game.logic.Piece;

public class GameState {

  private static GameState instance;

  private MatchConfiguration matchConfiguration;
  private Piece[][] board;
  private String colorToTurn;
  private int moveDirWhite;
  private Move lastWhiteMove;
  private Move lastBlackMove;

  private GameState() {
    matchConfiguration = MatchConfiguration.getInstance();
    lastWhiteMove = new Move(new int[]{1, 1}, new int[]{0, 0});   //dummy move
    lastBlackMove = new Move(new int[]{1, 1}, new int[]{0, 0});   //dummy move
  }

  public static GameState getInstance() {
    if (instance == null) {
      instance = new GameState();
    }
    return instance;
  }

  public void loadMatchConfiguration() {
    colorToTurn = "white";
    board = BoardGenerator.setUpBoard();

    if (matchConfiguration.isPlayerWhiteAtStart()) {
      moveDirWhite = 1;
    } else {
      moveDirWhite = -1;
    }

    if (!matchConfiguration.isPvpMode() && !matchConfiguration.isPlayerWhiteAtStart()) {
      moveDirWhite *= -1;
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
      moveDirWhite *= -1;
      board = BoardGenerator.flipBoard(board);
    }
  }

  public void resetFirstMove(int i, int j) {
    board[i][j].setFirstMove(false);
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

  public Move getLastWhiteMove() {
    return lastWhiteMove;
  }

  public void setLastWhiteMove(Move lastWhiteMove) {
    this.lastWhiteMove = lastWhiteMove;
  }

  public void setLastBlackMove(Move lastBlackMove) {
    this.lastBlackMove = lastBlackMove;
  }

  public Move getLastBlackMove() {
    return lastBlackMove;
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
}
