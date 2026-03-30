package chess.game.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.scene.input.KeyCode;

public class Config {

  private static final Map<KeyCode, Set<String>> keyInteractions = Map.ofEntries(
      Map.entry(KeyCode.DIGIT1, Set.of("vsComputerButton", "whiteButton", "rematchButton", "b1")),
      Map.entry(KeyCode.DIGIT2, Set.of("vsPlayerButton", "randomButton", "mainMenuButton", "b2")),
      Map.entry(KeyCode.DIGIT3, Set.of("quitGameButton", "blackButton", "b3")),
      Map.entry(KeyCode.DIGIT4, Set.of("b4")),
      Map.entry(KeyCode.NUMPAD1, Set.of("vsComputerButton", "whiteButton", "rematchButton", "b1")),
      Map.entry(KeyCode.NUMPAD2, Set.of("vsPlayerButton", "randomButton", "mainMenuButton", "b2")),
      Map.entry(KeyCode.NUMPAD3, Set.of("quitGameButton", "blackButton", "b3")),
      Map.entry(KeyCode.NUMPAD4, Set.of("b4")),
      Map.entry(KeyCode.P, Set.of("settingsButton")),
      Map.entry(KeyCode.T, Set.of("themeButton")),
      Map.entry(KeyCode.F, Set.of("giveUpButton")),
      Map.entry(KeyCode.L, Set.of("drawButton")),
      Map.entry(KeyCode.ENTER, Set.of("vsComputerButton", "nextButton"))
  );

  public static final Map<String, Set<KeyCode>> keyInteractionsInv;

  static {
    keyInteractionsInv = new HashMap<>();

    for (KeyCode keyCode : keyInteractions.keySet()) {
      for (String id : keyInteractions.get(keyCode)) {

        Set<KeyCode> keySet = keyInteractionsInv.getOrDefault(id, new HashSet<>());
        keySet.add(keyCode);
        keyInteractionsInv.put(id, keySet);
      }
    }
  }

}
