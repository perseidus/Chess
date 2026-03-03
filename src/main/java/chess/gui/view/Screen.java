package chess.gui.view;

public enum Screen {
  START("/fxml/Start.fxml"),
  CHOOSEGAME("/fxml/ChooseGame.fxml"),
  CHESSBOARD("/fxml/ChessBoard.fxml"),
  MATCHENDING("/fxml/MatchEndedPopUp.fxml"),
  SETTINGS_POPOVER("/fxml/SettingsPopOver.fxml"),
  ;

  private String path;

  Screen(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }
}
