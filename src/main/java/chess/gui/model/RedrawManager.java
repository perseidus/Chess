package chess.gui.model;

public class RedrawManager extends Thread {

  private boolean running;

  public RedrawManager() {
    setDaemon(true);
    running = false; //TODO set to true when ready
  }

  @Override
  public void run() {
    while (running) {
      //TODO redraw times for both players
    }
  }

  public void stopRunning() {
    running = false;
  }

}
