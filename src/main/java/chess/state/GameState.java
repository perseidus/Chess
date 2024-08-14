package chess.state;

import chess.game.MoveValidator;
import chess.game.Piece;

public class GameState {

  private static GameState instance;

  private MatchConfiguration matchConfiguration;
  private Piece[][] board;
  private int whiteTime;
  private int blackTime;
  private boolean whiteTurn;

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
    whiteTurn = true;
    board = MoveValidator.setUpBoard();
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

  public boolean isWhiteTurn() {
    return whiteTurn;
  }
}
