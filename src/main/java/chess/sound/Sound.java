package chess.sound;

public enum Sound {
  CLICK("/sounds/click.mp3"),
  DENIED("/sounds/denied.mp3"),
  DRAW("/sounds/draw.mp3"),
  MOVE("/sounds/move.mp3"),
  WIN("/sounds/win.mp3");

  private Sound(String path) {
    this.path = path;
  }

  private String path;

  public String getPath() {
    return path;
  }
}
