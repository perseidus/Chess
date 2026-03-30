package chess.gui.model;

import chess.game.engine.BoardGenerator;
import chess.game.engine.GameSession;
import chess.game.engine.MoveGenerator;
import chess.game.logic.Move;
import chess.game.logic.Piece;
import chess.game.logic.PieceType;
import chess.game.logic.SpecialMoveType;
import chess.game.state.GameState;
import chess.game.state.MatchConfiguration;
import chess.gui.view.BoardRenderer;
import chess.gui.view.PopOn;
import chess.gui.view.PopOnType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.controlsfx.control.PopOver.ArrowLocation;

public class BoardInteractionManager {

  private static BoardInteractionManager instance;

  private BoardRenderer renderer;
  private GameState gameState;
  private GameSession gameSession;

  private Piece[][] pieces;
  private boolean pieceSelected;
  private int[] selectedPiecePos;
  private HashMap<String, List<Move>> moves;
  private List<Move> selectedPieceMoves;
  boolean[][] possibleMoves;

  private int currentI, currentJ;
  private boolean[][] currentSquareEnemy;

  private boolean tileFocused = false;
  private int focusedI = -1, focusedJ = -1;

  public static BoardInteractionManager getInstance() {
    return instance;
  }

  public static BoardInteractionManager getInstance(BoardRenderer renderer) {
    if (instance == null) {
      instance = new BoardInteractionManager(renderer);
      return instance;
    }

    if (!instance.gameState.isActive()) {
      instance.gameSession = new GameSession(instance);
      instance.gameSession.start();
      renderer.removeButtonGraphics();
    }
    instance.renderer = renderer;
    instance.rerender();
    return instance;
  }

  private BoardInteractionManager(BoardRenderer renderer) {
    this.renderer = renderer;
    this.gameState = GameState.getInstance();
    this.pieces = gameState.getBoard();
    this.moves = MoveGenerator.getPossibleMoves(pieces);
    renderer.drawPieces();

    this.gameSession = new GameSession(this);
    gameSession.start();
    gameState.setActive(true);

    currentI = -1;
    currentJ = -1;
  }

  public void waitForMove() {
    pieces = gameState.getBoard();
    moves = MoveGenerator.getPossibleMoves(pieces);
    renderer.refreshDragDrop();
  }

  public void resetInputs() {
    deselect();
    moves = null;
    possibleMoves = null;
  }

  public boolean handleButtonClick(int i, int j, PieceType type) {
    if (!gameSession.isPlayerTurn()) {
      return false;
    }

    boolean promotion = false;
    if (pieces[i][j] != null && pieces[i][j].getColor().equals(gameState.getColorToTurn())) {
      friendlySquareClicked(i, j);
    } else {
      promotion = emptyOrEnemySquareClicked(i, j, type);
    }

    return promotion;
  }

  private void friendlySquareClicked(int i, int j) {
    int[] newPos = new int[]{i, j};
    if (Arrays.equals(selectedPiecePos, newPos)) {
      deselect();
      return;
    }

    selectedPiecePos = newPos;
    selectedPieceMoves = moves.get("" + i + j);
    pieceSelected = true;
    possibleMoves = BoardGenerator.movesToBitboardBoolean(selectedPieceMoves);
    boolean[][] enemySquare = BoardGenerator.getEnemyPosBitboard(pieces, pieces[i][j].getColor());
    renderer.removeButtonGraphics();
    renderer.drawPossibleMoves(selectedPieceMoves, enemySquare, i, j);
    currentI = i;
    currentJ = j;
    currentSquareEnemy = enemySquare;
  }

  //  returns true if promotion
  private boolean emptyOrEnemySquareClicked(int i, int j, PieceType type) {
    if (!pieceSelected) {
      return false;
    }

    if (Arrays.equals(selectedPiecePos, new int[]{i, j})) {
      deselect();
      return false;
    }

    if (possibleMoves[i][j]) {
      Move move = null;
      List<Move> listOfMoves = moves.get("" + selectedPiecePos[0] + selectedPiecePos[1]);
      for (Move m : listOfMoves) {          // search clicked move
        if (m.getTo()[0] == i && m.getTo()[1] == j) {
          move = m;
        }
      }

      if ((move.getMoveType() == SpecialMoveType.PROMOTION) && (type == PieceType.NONE)) {
        return true;
      }

      if (type != PieceType.NONE) {
        move = new Move(move.getFrom(), move.getTo(), move.getMoveType(), type);
      }

      gameSession.sendMove(move);
    }

    deselect();
    return false;
  }

  public void moveTileFocus(int offI, int offJ) {
    tileFocused = true;

    if (focusedI == -1 || focusedJ == -1) {
      focusedI = 4;
      focusedJ = 4;
    } else {
      focusedI = Math.floorMod(focusedI + offI, 8);
      focusedJ = Math.floorMod(focusedJ + offJ, 8);
    }

    renderer.drawFocusedTile(focusedI, focusedJ);
  }

  public void removeTileFocus() {
    tileFocused = false;
    renderer.drawFocusedTile(-1, -1);
  }

  public void enterFocusedTile() {
    if (tileFocused) {
      boolean promotion = handleButtonClick(focusedI, focusedJ, PieceType.NONE);

      if (promotion) {
        PopOn popOver = PopOn.getInstance(PopOnType.CHOOSE_PIECE, this, focusedI, focusedJ);
        popOver.setDetachable(false);
        popOver.setArrowLocation(ArrowLocation.TOP_LEFT);
        popOver.show(renderer.getButton("a" + focusedJ + focusedI));
      }
    }
  }

  private void deselect() {
    selectedPiecePos = null;
    pieceSelected = false;
    selectedPieceMoves = null;
    renderer.removeButtonGraphics();
    renderer.drawLastMove(false, "");
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

  public void refresh() {
    if (MatchConfiguration.getInstance().isPvpMode()) {
      focusedI = -1;
      focusedJ = -1;
    }

    resetInputs();
    updateClocks(gameSession.getWhiteTime(), gameSession.getBlackTime());
    renderer.setGameSession(gameSession);
    renderer.refresh();
  }

  public void rerender() {
    renderer.setGameSession(gameSession);
    renderer.refresh();
    if (selectedPieceMoves == null) {
      selectedPieceMoves = new ArrayList<>();
    }
    renderer.drawPossibleMoves(selectedPieceMoves, currentSquareEnemy, currentI, currentJ);
  }

  public GameSession getGameSession() {
    return gameSession;
  }

  public void setGameSession(GameSession gameSession) {
    this.gameSession = gameSession;
  }

  public boolean isTileFocused() {
    return tileFocused;
  }

  public int getFocusedI() {
    return focusedI;
  }

  public int getFocusedJ() {
    return focusedJ;
  }

  public String getSelectedId() {
    if (pieceSelected && selectedPiecePos != null) {
      return "a" + selectedPiecePos[1] + selectedPiecePos[0];
    }
    return null;
  }

  public List<Move> getSelectedPieceMoves() {
    return selectedPieceMoves;
  }
}
