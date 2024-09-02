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

  public void flipCoords() {
    from[0] = 7 - from[0];
    from[1] = 7 - from[1];
    to[0] = 7 - to[0];
    to[1] = 7 - to[1];
  }

  public SpecialMoveType getMoveType() {
    return moveType;
  }
}
