package chess.gui.view;

public enum Screen {
  START("/Start.fxml"),
  CHOOSEGAME("/ChooseGame.fxml"),
  CHESSBOARD("/ChessBoard.fxml");

  private String path;

  Screen(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }
}
