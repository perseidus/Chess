package chess.gui.model;

import chess.game.engine.BoardGenerator;
import chess.game.engine.MoveGenerator;
import chess.game.logic.Move;
import chess.game.logic.Piece;
import chess.gui.view.BoardRenderer;
import chess.state.GameState;
import java.util.HashMap;
import java.util.List;

public class BoardInteractionManager {

  private BoardRenderer renderer;
  private GameState gameState;

  private Piece[][] pieces;
  private int[] selectedPiecePos;
  private HashMap<String, List<Move>> moves;
  private List<Move> selectedPieceMoves;

  public BoardInteractionManager(BoardRenderer renderer) {
    this.renderer = renderer;
    this.gameState = GameState.getInstance();
    this.pieces = gameState.getBoard();
    this.moves = MoveGenerator.getPossibleMoves(pieces);
  }

  private void makeMove(Move move) {

  }

  public void resetInputs() {
    selectedPiecePos = null;
    selectedPieceMoves = null;
    moves = null;
  }

  public void handleButtonClick(int i, int j) {
    if (pieces[i][j] != null && pieces[i][j].getColor().equals(gameState.getColorToTurn())) {
      friendlySquareClicked(i, j);
    } else {

    }
  }

  private void emptySquareClicked() {

  }

  private void friendlySquareClicked(int i, int j) {
    selectedPiecePos = new int[]{i, j};
    selectedPieceMoves = moves.get("" + i + j);
    boolean[][] possibleMove = BoardGenerator.movesToBitboard(selectedPieceMoves);
    boolean[][] enemySquare = BoardGenerator.getEnemyPosBitboard(pieces, pieces[i][j].getColor());
    renderer.removePossibleMoves();
    renderer.drawPossibleMoves(possibleMove, enemySquare);
  }

  private void enemySquareClicked() {

  }
}
