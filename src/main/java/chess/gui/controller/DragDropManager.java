package chess.gui.controller;

import chess.game.logic.Move;
import chess.game.logic.Piece;
import chess.game.state.GameState;
import chess.game.state.Parameters;
import chess.gui.model.BoardInteractionManager;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class DragDropManager {

  private List<Move> moves;
  private String lastButtonStyle;

  public DragDropManager(Map<String, Button> buttons) {
    initializeActions(buttons);
  }

  private void initializeActions(Map<String, Button> buttons) {
    for (Button button : buttons.values()) {

      button.setOnDragEntered(event -> {
        if (!event.getGestureSource().equals(button)) {
          lastButtonStyle = button.getStyle();
          button.setStyle("-fx-background-color: " + Parameters.dragToColor());
        }
      });

      button.setOnDragExited(event -> {
        if (!event.getGestureSource().equals(button)) {
          button.setStyle(lastButtonStyle);
        }
      });

      button.setOnDragDropped(event -> {
        if (!event.getGestureSource().equals(button) && moves != null) {
          String id = button.getId();
          boolean validMove = moves.stream().anyMatch(m -> id.equals("a" + m.getTo()[1] + m.getTo()[0]));
          if (validMove) {
            button.fire();
          }
        }
      });
    }
  }

  public void setDraggable(Map<String, Button> buttons, Map<String, ImageView> images, Piece[][] pieces) {
    if (!BoardInteractionManager.getInstance().getGameSession().isPlayerTurn()) {
      return;
    }

    for (int i = 0; i < pieces.length; i++) {
      for (int j = 0; j < pieces[i].length; j++) {
        if (pieces[i][j] != null
            && pieces[i][j].getColor().equals(GameState.getInstance().getColorToTurn())) {
          String id = "a" + j + i;
          setDraggable(buttons.get(id), images.get(id));
        }
      }
    }
  }

  private void setDraggable(Button source, ImageView image) {
    source.setOnDragDetected(event -> {
      BoardInteractionManager board = BoardInteractionManager.getInstance();

      // simulate click if not already selected
      if (!source.getId().equals(board.getSelectedId())) {
        source.fire();
      }

      moves = board.getSelectedPieceMoves();

      Dragboard db = source.startDragAndDrop(TransferMode.ANY);
      ClipboardContent content = new ClipboardContent();
      content.putImage(image.getImage());
      db.setContent(content);

      event.consume();
    });
  }

  public void removeDraggable(Map<String, Button> buttons) {
    for (Button button : buttons.values()) {
      button.setOnDragDetected(null);
    }
  }


}
