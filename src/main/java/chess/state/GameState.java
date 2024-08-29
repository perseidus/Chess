package chess.state;

import chess.game.BoardGenerator;
import chess.game.Move;
import chess.game.Piece;

public class GameState {

  private static GameState instance;

  private MatchConfiguration matchConfiguration;
  private Piece[][] board;
  private int whiteTime;
  private int blackTime;
  private String colorToTurn;
  private Move lastWhiteMove;
  private Move lastBlackMove;

  private GameState() {
    matchConfiguration = MatchConfiguration.getInstance();
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
}
