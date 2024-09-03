package chess.gui.model;

import chess.game.engine.BoardGenerator;
import chess.game.engine.MoveGenerator;
import chess.game.engine.GameSession;
import chess.game.logic.Move;
import chess.game.logic.Piece;
import chess.game.state.MatchConfiguration;
import chess.gui.view.BoardRenderer;
import chess.game.state.GameState;
import java.util.HashMap;
import java.util.List;

public class BoardInteractionManager {

  private BoardRenderer renderer;
  private GameState gameState;
  private GameSession gameSession;

  private Piece[][] pieces;
  private boolean pieceSelected;
  private int[] selectedPiecePos;
  private HashMap<String, List<Move>> moves;
  private List<Move> selectedPieceMoves;
  boolean[][] possibleMoves;

  public BoardInteractionManager(BoardRenderer renderer) {
    this.renderer = renderer;
    this.gameState = GameState.getInstance();
    this.pieces = gameState.getBoard();
    this.moves = MoveGenerator.getPossibleMoves(pieces);
    renderer.drawPieces();

    this.gameSession = new GameSession(this);
    gameSession.start();
  }

  public void waitForMove() {
    pieces = gameState.getBoard();
    moves = MoveGenerator.getPossibleMoves(pieces);
  }

  public void resetInputs() {
    selectedPiecePos = null;
    pieceSelected = false;
    selectedPieceMoves = null;
    moves = null;
    possibleMoves = null;
  }

  public void handleButtonClick(int i, int j) {
    if (!gameSession.isPlayerTurn()) {
      return;
    }

    if (pieces[i][j] != null && pieces[i][j].getColor().equals(gameState.getColorToTurn())) {
      friendlySquareClicked(i, j);
    } else {
      emptyOrEnemySquareClicked(i, j);
    }
  }

  private void friendlySquareClicked(int i, int j) {
    selectedPiecePos = new int[]{i, j};
    selectedPieceMoves = moves.get("" + i + j);
    pieceSelected = true;
    possibleMoves = BoardGenerator.movesToBitboard(selectedPieceMoves);
    boolean[][] enemySquare = BoardGenerator.getEnemyPosBitboard(pieces, pieces[i][j].getColor());
    renderer.removeButtonGraphics();
    renderer.drawPossibleMoves(possibleMoves, enemySquare, i, j);
  }

  private void emptyOrEnemySquareClicked(int i, int j) {
    if (!pieceSelected) {
      return;
    }

    if (possibleMoves[i][j]) {
      Move move = null;
      List<Move> listOfMoves = moves.get("" + selectedPiecePos[0] + selectedPiecePos[1]);
      for (Move m : listOfMoves) {          // search clicked move
        if (m.getTo()[0] == i && m.getTo()[1] == j) {
          move = m;
        }
      }
      gameSession.sendMove(move);
    }
  }

  public void updateClocks(int whiteTime, int blackTime) {
    String whiteTimeFormatted = formatTime(whiteTime);
    String blackTimeFormatted = formatTime(blackTime);
    if (gameState.getMoveDirWhite() == 1) {
      renderer.drawClocks(blackTimeFormatted, whiteTimeFormatted);
    } else {
      renderer.drawClocks(whiteTimeFormatted, blackTimeFormatted);
    }
  }

  public static String formatTime(int totalSeconds) {
    int firstPart;
    int secondPart;
    String format = "%02d:%02d";

    if (totalSeconds >= 3600) {
      firstPart = totalSeconds / 3600;
      secondPart = (totalSeconds % 3600) / 60;
      if (totalSeconds < 36000) {
        format = "%2d:%02d";
      }
    } else {
      firstPart = totalSeconds / 60;
      secondPart = totalSeconds % 60;
      if (totalSeconds < 600) {
        format = "%2d:%02d";
      }
    }

    return String.format(format, firstPart, secondPart);
  }

  public void giveUp() {
    gameSession.giveUp();
  }

  public void refresh() {
    resetInputs();
    updateClocks(gameSession.getWhiteTime(), gameSession.getBlackTime());
    renderer.refresh();
  }
}
