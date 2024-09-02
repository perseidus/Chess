package chess.sound;

import java.net.URISyntaxException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;;

public class SoundPlayer {

  private static SoundPlayer instance;

  private MediaPlayer clickPlayer, deniedPlayer, drawPlayer, movePlayer, winPlayer;

  private SoundPlayer() {
    setUpMediaPlayers();
  }

  public static SoundPlayer getInstance() {
    if (instance == null) {
      instance = new SoundPlayer();
    }
    return instance;
  }

  private void setUpMediaPlayers() {
    try {
      clickPlayer = new MediaPlayer(
          new Media(SoundPlayer.class.getResource(Sound.CLICK.getPath()).toURI().toString()));
      deniedPlayer = new MediaPlayer(
          new Media(SoundPlayer.class.getResource(Sound.DENIED.getPath()).toURI().toString()));
      drawPlayer = new MediaPlayer(
          new Media(SoundPlayer.class.getResource(Sound.DRAW.getPath()).toURI().toString()));
      movePlayer = new MediaPlayer(
          new Media(SoundPlayer.class.getResource(Sound.MOVE.getPath()).toURI().toString()));
      winPlayer = new MediaPlayer(
          new Media(SoundPlayer.class.getResource(Sound.WIN.getPath()).toURI().toString()));
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public void playSound(Sound sound) {
    switch (sound) {
      case CLICK:
        clickPlayer.play();
        clickPlayer.seek(clickPlayer.getStartTime());
        break;
      case DENIED:
        deniedPlayer.play();
        deniedPlayer.seek(deniedPlayer.getStartTime());
        break;
      case DRAW:
        drawPlayer.play();
        drawPlayer.seek(drawPlayer.getStartTime());
        break;
      case MOVE:
        movePlayer.play();
        movePlayer.seek(movePlayer.getStartTime());
        break;
      case WIN:
        winPlayer.play();
        winPlayer.seek(winPlayer.getStartTime());
        break;
    }
  }

}
