package chess.gui;

import chess.game.Piece;
import chess.state.GameState;
import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class BoardRenderer {

  private GameState gameState;

  private GridPane gridPane;
  private HashMap<String, Button> squares;
  private Piece[][] pieces;

  public BoardRenderer(GridPane gridPane) {
    this.gridPane = gridPane;
    this.gameState = GameState.getInstance();
    this.pieces = gameState.getBoard();
    this.squares = new HashMap<>();

    for (Node node : gridPane.getChildren()) {
      Button button = (Button) ((StackPane) node).getChildren().get(0);
      squares.put(button.getId(), button);
    }
  }

  public void drawPieces() {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Button square = squares.get("a" + j + i);
        Piece piece = pieces[i][j];
        if (piece != null) {
          String path = new StringBuilder().append("-fx-background-image: url('")
              .append(piece.getType().getPath()).append(piece.getColor()).append(".png');")
              .toString();
          square.setStyle(path);
        }
      }
    }
  }

  public void refresh() {
    gridPane.getChildren();
  }

}
