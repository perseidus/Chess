package chess.game.logic;

public class Move {

  private int[] from;
  private int[] to;
  private boolean capturingMove;
  private SpecialMoveType moveType;

  public Move(int[] from, int[] to) {
    this.from = from;
    this.to = to;
    this.moveType = SpecialMoveType.NONE;
  }

  public Move(int[] from, int[] to, SpecialMoveType moveType) {
    this.from = from;
    this.to = to;
    this.moveType = moveType;
  }

  public int[] getFrom() {
    return from;
  }

  public int[] getTo() {
    return to;
  }

  public boolean isCapturingMove() {
    return capturingMove;
  }

  public void setCapturingMove(boolean capturingMove) {
    this.capturingMove = capturingMove;
  }

  public SpecialMoveType getMoveType() {
    return moveType;
  }
}
