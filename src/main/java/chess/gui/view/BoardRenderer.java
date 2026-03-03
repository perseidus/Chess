package chess.gui.view;

import chess.game.engine.BoardGenerator;
import chess.game.engine.GameSession;
import chess.game.logic.Move;
import chess.game.logic.Piece;
import chess.game.state.GameState;
import chess.game.state.MatchConfiguration;
import chess.game.state.Parameters;
import java.util.HashMap;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class BoardRenderer {

  public static boolean showMoves = true;

  private GameState gameState;
  private MatchConfiguration configs;
  private GameSession gameSession;

  private GridPane gridPane;
  private Button drawButton;
  private HashMap<String, Button> buttons;
  private HashMap<String, ImageView> images;
  private Piece[][] pieces;

  private Label upperTimeLabel, lowerTimeLabel;

  public BoardRenderer(GridPane gridPane, Button drawButton, Label upperTimeLabel,
      Label lowerTimeLabel) {
    this.gridPane = gridPane;
    this.drawButton = drawButton;
    this.gameState = GameState.getInstance();
    this.configs = MatchConfiguration.getInstance();
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

    if (gameState.getLastMove() == null) {
      drawButton.setDisable(true);
    }
  }

  public void drawPieces() {
    drawTiles();

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

  private void drawTiles() {
    gridPane.setStyle("-fx-background-color: " + Parameters.darkTile() + ";");
    String lightClass;

    // get direction of board (to color tiles correctly)
    MatchConfiguration config = MatchConfiguration.getInstance();
    if (config.isPvpMode()) {
      if (gameState.getColorToTurn().equals("white")) {
        lightClass = "a";
      } else {
        lightClass = "b";
      }
    } else if (config.isPlayerWhiteAtStart()) {
      lightClass = "a";
    } else {
      lightClass = "b";
    }

    ObservableList<Node> panes = gridPane.getChildren();
    for (Node pane : panes) {
      StackPane tile = (StackPane) pane;
      if (tile.getStyleClass().contains(lightClass)) {
        tile.setStyle("-fx-background-color: " + Parameters.lightTile() + ";");
      } else {
        tile.setStyle("-fx-background-color: " + Parameters.darkTile() + ";");
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

  public void drawPossibleMoves(List<Move> possibleMoves, boolean[][] enemySquare, int x, int y) {
    if (!showMoves) {
      return;
    }

    int[][] moveOnBoard = BoardGenerator.movesToBitboard(possibleMoves);

    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        String id;
        for (int i = 0; i < 8; i++) {
          for (int j = 0; j < 8; j++) {
            int move = moveOnBoard[i][j]; // 0 -> no move, 1 -> regular move, 2 -> en passant
            if (move == 1 && enemySquare[i][j]) {
              id = "a" + j + i;
              buttons.get(id).setGraphic(NodeFactory.getBigCircle(images.get(id)));
            } else if (move == 2) { // en passant: highlight square behind
              id = "a" + j + i;
              buttons.get(id).setGraphic(NodeFactory.getBigCircle(images.get(id)));
            } else if (move == 1) {
              id = "a" + j + i;
              buttons.get(id).setGraphic(NodeFactory.getSmallCircle(images.get(id)));
            }
          }
        }
        id = "a" + y + x;
        if (!id.equals("a-1-1")) {
          drawLastMove(true, id);
          buttons.get(id).setStyle("-fx-background-color: " + Parameters.moveToColor() + ";");
          drawChecks();
        }
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
            .setStyle("-fx-background-color: " + Parameters.moveFromColor() + "; "
                + "-fx-border-radius: 0;");
        buttons.get("a" + lastMove.getTo()[1] + lastMove.getTo()[0])
            .setStyle("-fx-background-color: " + Parameters.moveToColor() + "; "
                + "-fx-border-radius: 0;");

        if (pieceSelected) {
          buttons.get(id).setStyle("-fx-background-color: " + Parameters.moveToColor() + "; "
              + "-fx-border-radius: 0;");
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

  private void enableDrawButton() {
    if (!configs.isPvpMode() || gameState.getLastMove() == null) {
      drawButton.setDisable(true);
      return;
    }

    if (gameSession.currentPlayerOfferedDraw()) {
      drawButton.setDisable(true);
    } else {
      drawButton.setDisable(false);
    }
  }

  public void refresh() {
    removeButtonGraphics();
    drawPieces();
    drawLastMove(false, "");
    drawChecks();
    enableDrawButton();
  }

  public void setGameSession(GameSession gameSession) {
    this.gameSession = gameSession;
  }
}
