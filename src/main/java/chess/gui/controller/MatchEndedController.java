package chess.gui.controller;

import chess.game.engine.GameSession;
import chess.game.state.GameState;
import chess.game.state.MatchConfiguration;
import chess.gui.view.Screen;
import chess.sound.Sound;
import chess.sound.SoundPlayer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class MatchEndedController implements Initializable {

  @FXML
  Label topText, bottomText;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    topText.setText(GameSession.primaryMessage);
    bottomText.setText(GameSession.secondaryMessage);
  }

  public void rematchClicked(ActionEvent actionEvent) {
    ScreenManager.closeMatchEndingPopUp();
    MatchConfiguration config = MatchConfiguration.getInstance();
    GameState gameState = GameState.getInstance();
    if (!config.isPvpMode()) {
      if (config.isPlayerWhiteAtStart()) {
        config.setSelectedColorButton(2);
      } else {
        config.setSelectedColorButton(0);
      }
    }
    gameState.loadMatchConfiguration();

    ScreenManager.switchScene(Screen.CHESSBOARD);
    SoundPlayer.getInstance().playSound(Sound.CLICK);
  }

  public void mainMenuClicked(ActionEvent actionEvent) {
    ScreenManager.closeMatchEndingPopUp();
    ScreenManager.switchScene(Screen.START);
    SoundPlayer.getInstance().playSound(Sound.CLICK);
  }

  public void quitGameClicked(ActionEvent actionEvent) {
    ScreenManager.closeMatchEndingPopUp();
    ScreenManager.closeApplication();
  }

}
