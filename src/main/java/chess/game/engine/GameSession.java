package chess.game.engine;

import chess.ai.AI;
import chess.ai.StandardAI;
import chess.game.logic.Move;
import chess.game.logic.Piece;
import chess.game.state.MatchResult;
import chess.gui.model.BoardInteractionManager;
import chess.game.state.GameState;
import chess.game.state.MatchConfiguration;

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

  private long lastSavedTime;

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
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        firstSecond = false;
        lastSavedTime = System.currentTimeMillis();
      }

      try {
        sleep(70);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      if (System.currentTimeMillis() - lastSavedTime > 1000) {
        deductTime();
        lastSavedTime = System.currentTimeMillis();
      }
    }
  }

  public void sendMove(Move move) {
    if (whitePlayerTurn) {
      gameState.setLastWhiteMove(move);
      whiteTime += whiteInc;
    } else {
      blackTime += blackInc;
      gameState.setLastBlackMove(move);
    }
    whitePlayerTurn = !whitePlayerTurn;

    gameState.setBoard(BoardGenerator.getBoardAfterMove(pieces, move, true));
    gameState.handleMove(move);
    gameState.changeTurn();
    lastSavedTime = System.currentTimeMillis();

    MatchResultHandler.calculateKingsPos(gameState.getBoard());
    MatchResultHandler.calculateKingsInCheck(gameState.getBoard());
    MatchResult result = MatchResultHandler.checkIfMatchEnded();
    if (result != MatchResult.ONGOING) {
      endSession(result);
      return;
    }

    manager.refresh();

    if (configs.isPvpMode()) {    //2 human players
      playerTurn = true;
      manager.waitForMove();
    } else if (playerTurn) {      //ai moves next
      playerTurn = false;
      ai.getMove();
    } else {                      //human moves next
      playerTurn = true;
      manager.waitForMove();
    }
  }

  public void sendAIMove(Move move) {
    if (playerTurn || !matchRunning) {
      return;
    }
    sendMove(move);
  }

  private void deductTime() {
    if (whitePlayerTurn) {
      whiteTime -= 1;
    } else {
      blackTime -= 1;
    }
    manager.updateClocks(whiteTime, blackTime);

    if (whiteTime <= 0 || blackTime <= 0) {
      String opponentColor = whiteTime <= 0 ? "black" : "white";
      MatchResult result = MatchResultHandler.gameOverTimeOut(opponentColor);
      endSession(result);
    }
  }

  private void createComputerPlayer() {
    switch (configs.getComputer()) {
      case "Standard":
        this.ai = new StandardAI(this);
        break;
    }
  }

  //TODO match ended in draw, win, loss
  public void endSession(MatchResult result) {
    String primaryMessage;
    String secondaryMessage = result.getMessage();
    if (result == MatchResult.WHITE_WINS) {
      primaryMessage = "White wins";
    } else if (result == MatchResult.BLACK_WINS) {
      primaryMessage = "Black wins";
    } else {
      primaryMessage = "Draw";
    }

    matchRunning = false;
    playerTurn = false;
  }

  public int getWhiteTime() {
    return whiteTime;
  }

  public int getBlackTime() {
    return blackTime;
  }

  public boolean isPlayerTurn() {
    return playerTurn;
  }
}
