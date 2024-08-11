package chess.gui.controller;

import chess.gui.Screen;
import chess.gui.ScreenManager;
import javafx.event.ActionEvent;

public class ChooseGameController {

  public void settingsClicked(ActionEvent actionEvent) {
  }

  public void themeClicked(ActionEvent actionEvent) {
  }

  public void backClicked(ActionEvent actionEvent) {
    ScreenManager.switchScene(Screen.START);
  }

  public void nextClicked(ActionEvent actionEvent) {
    ScreenManager.switchScene(Screen.CHESSBOARD);
  }
}
