package chess.game;

public class Move {

  private int[] from;
  private int[] to;
  private boolean specialMove;
  private SpecialMoveType moveType;

  public Move(int[] from, int[] to) {
    this.from = from;
    this.to = to;
    this.specialMove = false;
    this.moveType = SpecialMoveType.NONE;
  }

  public Move(int[] from, int[] to, SpecialMoveType moveType) {
    this.from = from;
    this.to = to;
    this.specialMove = true;
    this.moveType = moveType;
  }

  public int[] getFrom() {
    return from;
  }

  public int[] getTo() {
    return to;
  }

  public boolean isSpecialMove() {
    return specialMove;
  }
}
