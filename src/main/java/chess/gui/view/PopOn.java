package chess.gui.view;

import chess.game.engine.GameSession;
import chess.game.logic.PieceType;
import chess.game.state.GameState;
import chess.gui.controller.KeyBoardInteraction;
import chess.gui.model.BoardInteractionManager;
import chess.sound.SoundPlayer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.ToggleSwitch;

public class PopOn extends PopOver {

  private static PopOn instance;
  private static GameSession gameSession;
  private static BoardInteractionManager manager;

  private static int i, j;
  private static boolean toggle1 = true, toggle2 = true, toggle3 = true;

  public static PopOn getInstance(PopOnType type, GameSession session) {
    if (instance != null) {
      instance.hide();
    }

    gameSession = session;

    Node node = init(type);
    instance = new PopOn(node);
    return instance;
  }

  public static PopOn getInstance(PopOnType type, BoardInteractionManager m, int a, int b) {
    if (instance != null) {
      instance.hide();
    }

    manager = m;
    i = a;
    j = b;

    Node node = init(type);
    instance = new PopOn(node);
    return instance;
  }

  public static PopOn getInstance(PopOnType type) {
    if (instance != null) {
      instance.hide();
    }

    Node node = init(type);
    instance = new PopOn(node);
    return instance;
  }

  private PopOn(Node node) {
    super(node);
    this.setArrowSize(0);
  }

  private static Node init(PopOnType type) {
    Node node = null;

    switch (type) {
      case DRAW_OFFER:
        node = initDrawOfferView();
        break;
      case DRAW_ACCEPT:
        node = initDrawAcceptView();
        break;
        case FORFEIT:
        node = initForfeitView();
        break;
      case CHOOSE_PIECE:
        node = initChoosePieceView();
        break;
      case SETTINGS:
        node = initSettingsView();
        break;
    }

    if (node != null) {
      KeyBoardInteraction interaction = new KeyBoardInteraction((Parent) node);
      node.addEventFilter(KeyEvent.KEY_PRESSED, interaction::catchKeyPress);
    }

    return node;
  }

  private static Node initDrawOfferView() {
    VBox box = new VBox();
    box.getStyleClass().add("popOn");
    box.setAlignment(Pos.CENTER);
    box.setPadding(new Insets(10, 10, 0, 10));

    box.getChildren().add(new Label("Draw"));
    box.getChildren().add(new Label());

    Button b1 = new Button("Offer a draw"); // button 1
    b1.setPrefSize(175, 40);
    b1.setId("b1");
    b1.getStyleClass().add("popOnButtonsClass");
    b1.setOnAction(e -> {
      gameSession.offerDraw();
      instance.hide();
    });
    box.getChildren().add(b1);

    box.getChildren().add(new Label());

    Button b2 = new Button("Cancel"); // button 2
    b2.setPrefSize(175, 40);
    b2.setId("b2");
    b2.getStyleClass().add("popOnButtonsClass");
    b2.setOnAction(e -> {
      instance.hide();
    });
    box.getChildren().add(b2);

    box.getChildren().add(new Label());
    return box;
  }

  private static Node initDrawAcceptView() {
    VBox box = new VBox();
    box.getStyleClass().add("popOn");
    box.setAlignment(Pos.CENTER);
    box.setPadding(new Insets(10, 10, 0, 10));

    box.getChildren().add(new Label("Draw"));
    box.getChildren().add(new Label());

    Button b1 = new Button("Accept draw offer"); // button 1
    b1.setPrefSize(175, 40);
    b1.setId("b1");
    b1.getStyleClass().add("popOnButtonsClass");
    b1.setOnAction(e -> {
      gameSession.offerDraw();
      instance.hide();
    });
    box.getChildren().add(b1);

    box.getChildren().add(new Label());

    Button b2 = new Button("Cancel"); // button 2
    b2.setPrefSize(175, 40);
    b2.setId("b2");
    b2.getStyleClass().add("popOnButtonsClass");
    b2.setOnAction(e -> {
      instance.hide();
    });
    box.getChildren().add(b2);

    box.getChildren().add(new Label());
    return box;
  }

  private static Node initForfeitView() {
    VBox box = new VBox();
    box.getStyleClass().add("popOn");
    box.setAlignment(Pos.CENTER);
    box.setPadding(new Insets(10, 10, 0, 10));

    box.getChildren().add(new Label("Forfeit this match?"));
    box.getChildren().add(new Label());

    Button b1 = new Button("Forfeit"); // button 1
    b1.setPrefSize(175, 40);
    b1.setId("b1");
    b1.getStyleClass().add("popOnButtonsClass");
    b1.setOnAction(e -> {
      gameSession.giveUp();
      instance.hide();
    });
    box.getChildren().add(b1);

    box.getChildren().add(new Label());

    Button b2 = new Button("Cancel"); // button 2
    b2.setPrefSize(175, 40);
    b2.setId("b2");
    b2.getStyleClass().add("popOnButtonsClass");
    b2.setOnAction(e -> instance.hide());
    box.getChildren().add(b2);

    box.getChildren().add(new Label());
    return box;
  }

  private static Node initChoosePieceView() {
    GridPane pane = new GridPane();
    pane.getStyleClass().add("popOn");
    // --- Define 2 columns/rows ---
    for (int i = 0; i < 2; i++) {
      ColumnConstraints col = new ColumnConstraints();
      RowConstraints row = new RowConstraints();
      row.setPercentHeight(100.0 / 2); // optional, make them equal height
      col.setPercentWidth(100.0 / 2); // optional, make them equal width
      pane.getRowConstraints().add(row);
      pane.getColumnConstraints().add(col);
    }

    StackPane stack1 = new StackPane();
    StackPane stack2 = new StackPane();
    StackPane stack3 = new StackPane();
    StackPane stack4 = new StackPane();
    pane.add(stack1, 0, 0);
    pane.add(stack2, 0, 1);
    pane.add(stack3, 1, 0);
    pane.add(stack4, 1, 1);

    String color = GameState.getInstance().getColorToTurn();
    ImageView iView1 = new ImageView();
    ImageView iView2 = new ImageView();
    ImageView iView3 = new ImageView();
    ImageView iView4 = new ImageView();
    iView1.setImage(new Image(PieceType.QUEEN.getPath() + color + ".png"));
    iView2.setImage(new Image(PieceType.ROOK.getPath() + color + ".png"));
    iView3.setImage(new Image(PieceType.BISHOP.getPath() + color + ".png"));
    iView4.setImage(new Image(PieceType.KNIGHT.getPath() + color + ".png"));
    iView1.fitWidthProperty().bind(stack1.widthProperty());
    iView1.fitHeightProperty().bind(stack1.heightProperty());
    iView2.fitWidthProperty().bind(stack2.widthProperty());
    iView2.fitHeightProperty().bind(stack2.heightProperty());
    iView3.fitWidthProperty().bind(stack3.widthProperty());
    iView3.fitHeightProperty().bind(stack3.heightProperty());
    iView4.fitWidthProperty().bind(stack4.widthProperty());
    iView4.fitHeightProperty().bind(stack4.heightProperty());
    stack1.getChildren().add(iView1);
    stack2.getChildren().add(iView2);
    stack3.getChildren().add(iView3);
    stack4.getChildren().add(iView4);

    Button b1 = new Button();
    Button b2 = new Button();
    Button b3 = new Button();
    Button b4 = new Button();
    b1.setId("b1");
    b2.setId("b3");
    b3.setId("b2");
    b4.setId("b4");
    b1.prefWidthProperty().bind(stack1.widthProperty());
    b1.prefHeightProperty().bind(stack1.heightProperty());
    b2.prefWidthProperty().bind(stack2.widthProperty());
    b2.prefHeightProperty().bind(stack2.heightProperty());
    b3.prefWidthProperty().bind(stack3.widthProperty());
    b3.prefHeightProperty().bind(stack3.heightProperty());
    b4.prefWidthProperty().bind(stack4.widthProperty());
    b4.prefHeightProperty().bind(stack4.heightProperty());
    b1.setOpacity(0);
    b2.setOpacity(0);
    b3.setOpacity(0);
    b4.setOpacity(0);
    b1.setOnAction(e -> handleButtonEvent(PieceType.QUEEN));
    b2.setOnAction(e -> handleButtonEvent(PieceType.ROOK));
    b3.setOnAction(e -> handleButtonEvent(PieceType.BISHOP));
    b4.setOnAction(e -> handleButtonEvent(PieceType.KNIGHT));
    stack1.getChildren().add(b1);
    stack2.getChildren().add(b2);
    stack3.getChildren().add(b3);
    stack4.getChildren().add(b4);

    return pane;
  }

  private static Node initSettingsView() {
    VBox box = new VBox();
    box.getStyleClass().add("popOn");
    box.setPadding(new Insets(10, 20, 10, 20));
    box.setAlignment(Pos.CENTER);

    HBox hbox1 = new HBox();
    hbox1.setAlignment(Pos.CENTER_RIGHT);
    hbox1.setPadding(new Insets(10, 20, 10, 20));
    box.getChildren().add(hbox1);
    HBox hbox2 = new HBox();
    hbox2.setAlignment(Pos.CENTER_RIGHT);
    hbox2.setPadding(new Insets(10, 20, 10, 20));
    box.getChildren().add(hbox2);
    HBox hbox3 = new HBox();
    hbox3.setAlignment(Pos.CENTER_RIGHT);
    hbox3.setPadding(new Insets(10, 20, 10, 20));
    box.getChildren().add(hbox3);

    ToggleSwitch switch1 = new ToggleSwitch("Play sounds");
    switch1.setId("b1");
    ToggleSwitch switch2 = new ToggleSwitch("Highlight moves");
    switch2.setId("b2");
    ToggleSwitch switch3 = new ToggleSwitch("Show last move");
    switch3.setId("b3");
    switch1.setSelected(true);
    switch2.setSelected(true);
    switch3.setSelected(true);
    switch1.selectedProperty().addListener((o, oValue, nValue) -> {
      toggle1 = nValue;
      SoundPlayer.playEnabled = toggle1;
    });
    switch2.selectedProperty().addListener((o, oValue, nValue) -> {
      toggle2 = nValue;
      BoardRenderer.showMoves = toggle2;
    });
    switch3.selectedProperty().addListener((o, oValue, nValue) -> {
      toggle3 = nValue;
      BoardRenderer.showLastMove = toggle3;
    });
    hbox1.getChildren().add(switch1);
    hbox2.getChildren().add(switch2);
    hbox3.getChildren().add(switch3);

    return box;
  }

  private static void handleButtonEvent(PieceType pieceType) {
    instance.hide();
    manager.handleButtonClick(i, j, pieceType);
  }
}
