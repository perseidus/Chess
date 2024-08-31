package chess.ai;

import chess.game.engine.GameSession;
import chess.game.engine.MoveGenerator;
import chess.game.logic.Move;
import java.util.ArrayList;
import java.util.List;

public class StandardAI extends AI {

  public StandardAI(GameSession session) {
    super(session);
  }

  @Override
  public void getMove() {
    this.pieces = gameState.getBoard();
    this.moves = MoveGenerator.getPossibleMoves(pieces);
    pickMove();
  }

  private void pickMove() {
    List<Move> possibleMoves = new ArrayList<>();
    List<List<Move>> listOfLists = new ArrayList<>(moves.values());
    for (List<Move> listOfMoves : listOfLists) {
      for (Move move : listOfMoves) {
        possibleMoves.add(move);            //add all moves to one list
      }
    }
    SendAIMoveThread t = new SendAIMoveThread(session, possibleMoves.get(0));
    t.start();
  }
}
