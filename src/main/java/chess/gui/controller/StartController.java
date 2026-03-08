package chess.gui.controller;

import chess.gui.view.PopOn;
import chess.gui.view.PopOnType;
import chess.gui.view.Screen;
import chess.game.state.MatchConfiguration;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

public class StartController {

  @FXML
  Button settingsButton;

  public void playerComputerClicked() {
    MatchConfiguration.getInstance().setPvpMode(false);
    ScreenManager.switchScene(Screen.CHOOSEGAME);
  }

  public void playerPlayerClicked() {
    MatchConfiguration.getInstance().setPvpMode(true);
    ScreenManager.switchScene(Screen.CHOOSEGAME);
  }

  public void quitGameClicked() {
    ScreenManager.closeApplication();
  }

  public void settingsClicked() {
    PopOver popOver = PopOn.getInstance(PopOnType.SETTINGS);
    popOver.setArrowLocation(ArrowLocation.TOP_LEFT);
    popOver.setDetachable(false);
    popOver.show(settingsButton);
  }

  public void themeClicked() {
    ScreenManager.switchTheme();
  }
}
