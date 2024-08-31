package chess.ai;

import chess.game.engine.GameSession;
import chess.game.logic.Move;
import chess.game.logic.Piece;
import chess.game.state.GameState;
import java.util.HashMap;
import java.util.List;

abstract public class AI {

  protected GameState gameState;
  protected GameSession session;

  protected HashMap<String, List<Move>> moves;
  protected Piece[][] pieces;

  public AI(GameSession session) {
    this.session = session;
    this.gameState = GameState.getInstance();
  }

  public abstract void getMove();

}
