package chess.game.engine;

public class Material {

  private int knightCount;
  private int bishopCount;
  private int otherPiecesCount;

  public Material() {
    knightCount = 0;
    bishopCount = 0;
    otherPiecesCount = 0;
  }

  public int getKnightCount() {
    return knightCount;
  }

  public void incKnightCount() {
    this.knightCount++;
  }

  public int getBishopCount() {
    return bishopCount;
  }

  public void incBishopCount() {
    this.bishopCount++;
  }

  public int getOtherPiecesCount() {
    return otherPiecesCount;
  }

  public void incOtherPiecesCount() {
    this.otherPiecesCount++;
  }

  public int getAllCount(){
    return knightCount + bishopCount + otherPiecesCount;
  }
}
