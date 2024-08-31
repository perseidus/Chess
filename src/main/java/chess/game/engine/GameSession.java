package chess.game.engine;

import chess.ai.AI;
import chess.ai.StandardAI;
import chess.game.logic.Move;
import chess.game.logic.Piece;
import chess.gui.model.BoardInteractionManager;
import chess.state.GameState;
import chess.state.MatchConfiguration;

public class GameSession extends Thread {

  private GameState gameState;
  private BoardInteractionManager manager;
  private MatchConfiguration configs;
  private AI ai;

  private boolean matchRunning;
  private boolean playerTurn;
  private boolean whitePlayerTurn;
  private boolean firstSecond;

  private int whiteTime;    //in seconds
  private int blackTime;    //in seconds
  private int whiteInc;     //in seconds
  private int blackInc;     //in seconds

  private Piece[][] pieces;

  public GameSession(BoardInteractionManager manager) {
    setDaemon(true);
    this.gameState = GameState.getInstance();
    this.manager = manager;
    this.configs = MatchConfiguration.getInstance();
    this.firstSecond = true;
    this.matchRunning = true;
    this.whitePlayerTurn = true;
    createComputerPlayer();

    whiteTime = configs.getTime() * 60;
    blackTime = whiteTime;
    whiteInc = configs.getIncrement();
    blackInc = whiteInc;

    pieces = gameState.getBoard();

    if (!configs.isPvpMode() && configs.isPlayerWhiteAtStart()) {
      this.playerTurn = true;     //pvc: first move is human (human is white)
    } else if (!configs.isPvpMode()) {
      this.playerTurn = false;    //pvc: first move is ai (human is black)
    } else {
      this.playerTurn = true;     //pvp: first move is human (white and black)
    }
  }

  @Override
  public void run() {
    while (matchRunning) {
      if (firstSecond) {
        if (playerTurn) {
          manager.waitForMove();
        } else {
          ai.getMove();
        }
        try {
          sleep(1000);
          firstSecond = false;
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }

      try {
        sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      deductTime();
    }
  }

  public void sendMove(Move move) {
    gameState.setBoard(BoardGenerator.getBoardAfterMove(pieces, move));
    gameState.resetFirstMove(move.getTo()[0], move.getTo()[1]);
    gameState.changeTurn();
    manager.refresh();

    if (whitePlayerTurn) {
      gameState.setLastWhiteMove(move);
    } else {
      gameState.setLastBlackMove(move);
    }
    whitePlayerTurn = !whitePlayerTurn;

    if (configs.isPvpMode()) {    //2 human players
      manager.waitForMove();
    } else if (playerTurn) {      //ai moves next
      ai.getMove();
      playerTurn = false;
    } else {                      //human moves next
      manager.waitForMove();
      playerTurn = true;
    }
  }

  private void deductTime() {
    if (whitePlayerTurn) {
      whiteTime -= 1;
    } else {
      blackTime -= 1;
    }

    if (whiteTime <= 0 || blackTime <= 0) {
      MatchResultHandler.gameOverTimeOut();
      endSession();
    }
  }

  private void createComputerPlayer() {
    switch (configs.getComputer()) {
      case "Standard":
        this.ai = new StandardAI();
        break;
    }
  }

  public void endSession() {
    matchRunning = false;
  }

}
