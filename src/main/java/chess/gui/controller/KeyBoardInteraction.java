package chess.gui.controller;

import chess.game.state.Config;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.ToggleSwitch;

public class KeyBoardInteraction {

  private Parent root;

  private Map<KeyCode, Node> actions;

  public KeyBoardInteraction(Parent root) {
    this.root = root;
    actions = new HashMap<>();
    addAllChildren(root);
  }

  private void addAllChildren(Parent parent) {
    for (Node node : parent.getChildrenUnmodifiable()) {
      addIfAction(node);
      if (node instanceof Parent)
        addAllChildren((Parent) node);
    }
  }

  private void addIfAction(Node node) {
    String id = node.getId();

    if (id != null && Config.keyInteractionsInv.containsKey(id)) {
      for (KeyCode keyCode : Config.keyInteractionsInv.get(id)) {
        actions.put(keyCode, node);
      }
    }

  }

  public void catchKeyPress(KeyEvent e) {
    KeyCode key = e.getCode();

    if (key == KeyCode.F11) {
      ScreenManager.toggleMaximized();
    }

    if (!actions.containsKey(key)) {
      return;
    }

    Node node = actions.get(key);
    if (node instanceof ButtonBase) {
      ((ButtonBase) node).fire();
    }

    if (node instanceof ToggleSwitch) {
      ((ToggleSwitch) node).fire();
    }

    e.consume();
  }
}
