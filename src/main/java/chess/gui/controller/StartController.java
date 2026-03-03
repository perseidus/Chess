package chess.gui.controller;

import chess.gui.view.PopOn;
import chess.gui.view.PopOnType;
import chess.gui.view.Screen;
import chess.game.state.MatchConfiguration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

public class StartController {

  @FXML
  Button settingsButton;

  public void playerComputerClicked(ActionEvent actionEvent) {
    MatchConfiguration.getInstance().setPvpMode(false);
    ScreenManager.switchScene(Screen.CHOOSEGAME);
  }

  public void playerPlayerClicked(ActionEvent actionEvent) {
    MatchConfiguration.getInstance().setPvpMode(true);
    ScreenManager.switchScene(Screen.CHOOSEGAME);
  }

  public void quitGameClicked(ActionEvent actionEvent) {
    ScreenManager.closeApplication();
  }

  public void settingsClicked(ActionEvent actionEvent) {
    PopOver popOver = PopOn.getInstance(PopOnType.SETTINGS);
    popOver.setArrowLocation(ArrowLocation.TOP_LEFT);
    popOver.setDetachable(false);
    popOver.show(settingsButton);
  }

  public void themeClicked(ActionEvent actionEvent) {
    ScreenManager.switchTheme();
  }
}
