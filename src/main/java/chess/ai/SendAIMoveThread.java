package chess.ai;

import chess.game.engine.GameSession;
import chess.game.logic.Move;
import java.util.Random;

public class SendAIMoveThread extends Thread {

  private GameSession session;
  private Move move;

  public SendAIMoveThread(GameSession session, Move move) {
    setDaemon(true);
    this.session = session;
    this.move = move;
  }

  @Override
  public void run() {
    int sleepDuration = (int) (Math.random() * ((2000) + 1)) + 1000;  //sleep for 1-3 seconds
    try {
      Thread.sleep(sleepDuration);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    session.sendAIMove(move);
  }

}
