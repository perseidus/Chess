package chess.gui.controller;

import chess.gui.Screen;
import chess.gui.ScreenManager;
import chess.state.MatchConfiguration;
import javafx.event.ActionEvent;

public class StartController {

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
  }

  public void themeClicked(ActionEvent actionEvent) {
  }
}
