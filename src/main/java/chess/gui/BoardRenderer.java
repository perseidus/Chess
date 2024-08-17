package chess.gui;

import chess.game.Piece;
import chess.state.GameState;
import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class BoardRenderer {

  private GameState gameState;

  private GridPane gridPane;
  private HashMap<String, Button> buttons;
  private HashMap<String, ImageView> images;
  private Piece[][] pieces;

  public BoardRenderer(GridPane gridPane) {
    this.gridPane = gridPane;
    this.gameState = GameState.getInstance();
    this.pieces = gameState.getBoard();
    this.buttons = new HashMap<>();
    this.images = new HashMap<>();

    for (Node node : gridPane.getChildren()) {
      Button button = (Button) ((StackPane) node).getChildren().get(1);
      buttons.put(button.getId(), button);
      ImageView image = (ImageView) ((StackPane) node).getChildren().get(0);
      images.put(button.getId(), image);
    }
  }

  public void drawPieces() {
    Piece piece;
    ImageView image;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        piece = pieces[i][j];
        if (piece != null) {
          image = images.get("a" + j + i);
          image.setImage(new Image(piece.getType().getPath() + piece.getColor() + ".png"));
          image.fitWidthProperty().bind(buttons.get("a" + j + i).widthProperty());
          image.fitHeightProperty().bind(buttons.get("a" + j + i).heightProperty());
        }
      }
    }
  }

  public void flipBoard() {
    Piece[] tmp;
    for (int i = 0, j = 7; i < 4; i++, j--) {
      tmp = pieces[i];
      pieces[i] = pieces[j];
      pieces[j] = tmp;
    }
    drawPieces();
  }

  public void refresh() {
    gridPane.getChildren();
  }

}
