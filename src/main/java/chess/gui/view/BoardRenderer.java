package chess.gui.view;

import chess.game.logic.Move;
import chess.game.logic.Piece;
import chess.game.state.GameState;
import chess.game.state.Parameters;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

  private Label upperTimeLabel, lowerTimeLabel;

  public BoardRenderer(GridPane gridPane, Label upperTimeLabel, Label lowerTimeLabel) {
    this.gridPane = gridPane;
    this.gameState = GameState.getInstance();
    this.pieces = gameState.getBoard();
    this.buttons = new HashMap<>();
    this.images = new HashMap<>();
    this.upperTimeLabel = upperTimeLabel;
    this.lowerTimeLabel = lowerTimeLabel;

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
        } else {
          images.get("a" + j + i).setImage(null);
        }
      }
    }
  }

  public void drawChecks() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        String id;
        if (gameState.isBlackKingInCheck()) {
          int[] blackKing = gameState.getBlackKingPos();
          id = "a" + blackKing[1] + blackKing[0];
          buttons.get(id).setGraphic(NodeFactory.getCheckHighlight(images.get(id)));
        }
        if (gameState.isWhiteKingInCheck()) {
          int[] whiteKing = gameState.getWhiteKingPos();
          id = "a" + whiteKing[1] + whiteKing[0];
          buttons.get(id).setGraphic(NodeFactory.getCheckHighlight(images.get(id)));
        }
      }
    });
  }

  public void drawPossibleMoves(boolean[][] possibleMove, boolean[][] enemySquare, int x, int y) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        String id;
        for (int i = 0; i < 8; i++) {
          for (int j = 0; j < 8; j++) {
            if (possibleMove[i][j] && enemySquare[i][j]) {
              id = "a" + j + i;
              buttons.get(id).setGraphic(NodeFactory.getBigCircle(images.get(id)));
            } else if (possibleMove[i][j]) {
              id = "a" + j + i;
              buttons.get(id).setGraphic(NodeFactory.getSmallCircle(images.get(id)));
            }
          }
        }
        id = "a" + y + x;
        drawLastMove(true, id);
        buttons.get(id).setStyle("-fx-background-color: " + Parameters.moveToColor + ";");
      }
    });
  }

  public void drawLastMove(boolean pieceSelected, String id) {
    Move lastMove = gameState.getLastMove();
    if (lastMove == null) {
      return;
    }
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        for (Button button : buttons.values()) {
          button.setStyle("-fx-background-color: transparent;");
        }

        buttons.get("a" + lastMove.getFrom()[1] + lastMove.getFrom()[0])
            .setStyle("-fx-background-color: " + Parameters.moveFromColor + ";");
        buttons.get("a" + lastMove.getTo()[1] + lastMove.getTo()[0])
            .setStyle("-fx-background-color: " + Parameters.moveToColor + ";");

        if (pieceSelected) {
          buttons.get(id).setStyle("-fx-background-color: " + Parameters.moveToColor + ";");
        }
      }
    });
  }

  public void removeButtonGraphics() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        for (Button button : buttons.values()) {
          button.setGraphic(null);
          button.setStyle("-fx-background-color: transparent;");
        }
      }
    });
  }

  public void drawClocks(String upperClock, String lowerClock) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        upperTimeLabel.setText(upperClock);
        lowerTimeLabel.setText(lowerClock);
      }
    });
  }

  public void refresh() {
    removeButtonGraphics();
    drawPieces();
    drawLastMove(false, "");
    drawChecks();
  }

}
