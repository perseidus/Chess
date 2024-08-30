package chess.state;

import chess.game.engine.BoardGenerator;
import chess.game.logic.Move;
import chess.game.logic.Piece;

public class GameState {

  private static GameState instance;

  private MatchConfiguration matchConfiguration;
  private Piece[][] board;
  private int whiteTime;
  private int blackTime;
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
    whiteTime = matchConfiguration.getTime();
    blackTime = matchConfiguration.getTime();
    colorToTurn = "white";
    board = BoardGenerator.setUpBoard();
    if (matchConfiguration.isPlayerWhiteAtStart()) {
      moveDirWhite = 1;
    } else {
      moveDirWhite = -1;
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

  public Piece[][] getBoard() {
    return board;
  }

  public int getWhiteTime() {
    return whiteTime;
  }

  public int getBlackTime() {
    return blackTime;
  }

  public String getColorToTurn() {
    return colorToTurn;
  }

  public Move getLastWhiteMove() {
    return lastWhiteMove;
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
