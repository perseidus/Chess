package chess.gui.controller;

import chess.game.engine.GameSession;
import chess.game.logic.PieceType;
import chess.game.state.MatchConfiguration;
import chess.gui.model.BoardInteractionManager;
import chess.gui.view.BoardRenderer;
import chess.gui.view.PopOn;
import chess.gui.view.PopOnType;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver.ArrowLocation;

public class ChessBoardController implements Initializable {

  private BoardRenderer renderer;
  private BoardInteractionManager manager;

  @FXML
  GridPane grid;
  @FXML
  Button drawButton, giveUpButton, settingsButton;
  @FXML
  HBox innerHBox;
  @FXML
  VBox outerVBox;
  @FXML
  Label upperTimeLabel, lowerTimeLabel;
  PopOn popOver;

  private GameSession gameSession;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    final NumberBinding minLength = Bindings.min(outerVBox.widthProperty(),
        outerVBox.heightProperty());
    innerHBox.prefHeightProperty().bind(minLength);
    innerHBox.prefWidthProperty().bind(minLength);

    renderer = new BoardRenderer(grid, drawButton, upperTimeLabel, lowerTimeLabel);
    renderer.setGameSession(gameSession);
    manager = BoardInteractionManager.getInstance(renderer);
    gameSession = manager.getGameSession();

    String currentTime = BoardInteractionManager.formatTime(
        gameSession.getCurrentPlayerTime() * 60);
    String oppositeTime = BoardInteractionManager.formatTime(
        gameSession.getOppositePlayerTime() * 60);
    upperTimeLabel.setText(oppositeTime);
    lowerTimeLabel.setText(currentTime);

    if (!MatchConfiguration.getInstance().isPvpMode()) {
      drawButton.setDisable(true);
    }
  }

  public void squareClicked(ActionEvent actionEvent) {
    Button b = (Button) actionEvent.getSource();
    String id = b.getId();
    boolean promotion = manager.handleButtonClick(id.charAt(2) - '0', id.charAt(1) - '0',
        PieceType.NONE);

    if (promotion) {
      popOver = PopOn.getInstance(PopOnType.CHOOSE_PIECE, manager, id.charAt(2) - '0',
          id.charAt(1) - '0');
      popOver.setDetachable(false);
      popOver.setArrowLocation(ArrowLocation.TOP_LEFT);
      popOver.show(b);
    }
  }

  public void drawClicked(ActionEvent actionEvent) {
    if (gameSession.enemyOfferedDraw()) {
      popOver = PopOn.getInstance(PopOnType.DRAW_ACCEPT, gameSession);
    } else {
      popOver = PopOn.getInstance(PopOnType.DRAW_OFFER, gameSession);
    }
    popOver.setArrowLocation(ArrowLocation.RIGHT_BOTTOM);
    popOver.setDetachable(false);
    popOver.show(drawButton);
  }

  public void giveUpClicked(ActionEvent actionEvent) {
    popOver = PopOn.getInstance(PopOnType.FORFEIT, gameSession);
    popOver.setArrowLocation(ArrowLocation.RIGHT_BOTTOM);
    popOver.setDetachable(false);
    popOver.show(giveUpButton);
  }

  public void settingsClicked(ActionEvent actionEvent) {
    popOver = PopOn.getInstance(PopOnType.SETTINGS, gameSession);
    popOver.setArrowLocation(ArrowLocation.TOP_LEFT);
    popOver.setDetachable(false);
    popOver.show(settingsButton);
  }

  public void themeClicked(ActionEvent actionEvent) {
    ScreenManager.switchTheme();
    renderer.refresh();
  }
}
