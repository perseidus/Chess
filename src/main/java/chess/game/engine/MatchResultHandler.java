package chess.game.engine;

import chess.game.logic.Move;
import chess.game.logic.Piece;
import chess.game.logic.PieceType;
import chess.game.state.GameState;
import chess.game.state.MatchResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MatchResultHandler {

  private static List<String> positions = new ArrayList<>();

  //decisive: checkmate; draw: threefold rep, 50 move rule, stalemate, insufficient material
  public static MatchResult checkIfMatchEnded() {
    GameState gameState = GameState.getInstance();
    Piece[][] pieces = gameState.getBoard();
    HashMap<String, List<Move>> moves = MoveGenerator.getPossibleMoves(pieces);
    MatchResult result = null;

    if (gameState.getCount50move() >= 100) {    //50 move rule
      result = MatchResult.DRAW;
      result.setMessage("by fifty-move rule");
      return result;
    }

    //checkmate + stalemate
    int numOfMoves = 0;
    for (List<Move> movesOfPiece : moves.values()) {
      numOfMoves += movesOfPiece.size();
    }
    if (numOfMoves == 0 && gameState.isColorToTurnInCheck()) {    //checkmate
      if (gameState.getColorToTurn().equals("white")) {
        result = MatchResult.BLACK_WINS;
      } else {
        result = MatchResult.WHITE_WINS;
      }
      result.setMessage("by checkmate");
      return result;
    } else if (numOfMoves == 0 && !gameState.isColorToTurnInCheck()) {    //stalemate
      result = MatchResult.DRAW;
      result.setMessage("by stalemate");
      return result;
    }

    if (isThreefoldRepetition(gameState)) {      //threefold repetition
      result = MatchResult.DRAW;
      result.setMessage("by threefold repetition");
      return result;
    }

    if (isInsufficientMaterial(gameState)) {     //insufficient material
      result = MatchResult.DRAW;
      result.setMessage("by insufficient material");
      return result;
    }

    return MatchResult.ONGOING;
  }

  private static boolean isThreefoldRepetition(GameState gameState) {
    String currentPosition = convertPositionToString(gameState.getBoard());
    int count = 1;
    for (String position : positions) {
      if (position.equals(currentPosition)) {
        count++;
      }
    }
    positions.add(currentPosition);

    if (count >= 3) {
      return true;
    } else {
      return false;
    }
  }

  private static String convertPositionToString(Piece[][] pieces) {
    StringBuilder sb = new StringBuilder("");
    boolean boardFlipped = false;
    if (GameState.getInstance().getMoveDirWhite() == -1) {
      pieces = BoardGenerator.flipBoard(pieces);
      boardFlipped = true;
    }
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (pieces[i][j] == null) {
          sb.append('0');
        } else {
          switch (pieces[i][j].getType()) {
            case KNIGHT:
              sb.append('n');
              break;
            case BISHOP:
              sb.append('b');
              break;
            case QUEEN:
              sb.append('q');
              break;
            case KING:
              sb.append('k');
              break;
            case ROOK:
              sb.append('r');
              break;
            case PAWN:
              sb.append('p');
              break;
          }
        }
      }
    }
    if (boardFlipped) {
      BoardGenerator.flipBoard(pieces);
    }
    return sb.toString();
  }

  private static boolean isInsufficientMaterial(GameState gameState) {
    Material white = calculateMaterial(gameState.getBoard(), "white");
    Material black = calculateMaterial(gameState.getBoard(), "black");
    boolean whiteInsuf = false, blackInsuf = false;

    if (white.getOtherPiecesCount() > 0 || black.getOtherPiecesCount() > 0) {
      return false;       //at least one has sufficient material
    }

    if ((white.getBishopCount() <= 1 && white.getKnightCount() == 0)
        || (white.getBishopCount() == 0 && white.getKnightCount() <= 1)) {
      whiteInsuf = true;
    }
    if ((black.getBishopCount() <= 1 && black.getKnightCount() == 0)
        || (black.getBishopCount() == 0 && black.getKnightCount() <= 1)) {
      blackInsuf = true;
    }

    if (whiteInsuf && blackInsuf) {     //both insufficient material
      return true;
    }

    if ((white.getAllCount() == 0 && black.getAllCount() == 2 && black.getKnightCount() == 2)
        || (black.getAllCount() == 0 && white.getAllCount() == 2 && white.getKnightCount() == 2)) {
      return true;      //king + 2 knights vs lone king -> insufficient
    }

    return false;   //else
  }

  private static Material calculateMaterial(Piece[][] pieces, String color) {
    Material material = new Material();
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (pieces[i][j] == null || !pieces[i][j].getColor().equals(color)) {
          continue;
        }
        switch (pieces[i][j].getType()) {
          case KING:
            break;
          case BISHOP:
            material.incBishopCount();
            break;
          case KNIGHT:
            material.incKnightCount();
            break;
          default:
            material.incOtherPiecesCount();
            break;
        }
      }
    }
    return material;
  }


  //decisive: if opponent has sufficient material; draw: king vs anything
  public static MatchResult gameOverTimeOut(String opponentColor) {
    MatchResult result;
    Material opponent = calculateMaterial(GameState.getInstance().getBoard(), opponentColor);

    if (opponent.getOtherPiecesCount() > 0) {   //opponent has sufficient material
      if (opponentColor.equals("white")) {
        result = MatchResult.WHITE_WINS;
      } else {
        result = MatchResult.BLACK_WINS;
      }
      result.setMessage("by time-out");
      return result;
    }

    if ((opponent.getBishopCount() <= 1 && opponent.getKnightCount() == 0)   //insufficient material
        || (opponent.getBishopCount() == 0 && opponent.getKnightCount() <= 1)) {
      result = MatchResult.DRAW;
      result.setMessage("by insufficient material");
      return result;
    }

    if (opponentColor.equals("white")) {    //else: opponent has sufficient material
      result = MatchResult.WHITE_WINS;
    } else {
      result = MatchResult.BLACK_WINS;
    }
    result.setMessage("by time-out");
    return result;
  }

  //decisive: resignation
  public static MatchResult gameOverResignation(String resigningColor) {
    MatchResult result;
    if (resigningColor.equals("white")) {
      result = MatchResult.BLACK_WINS;
    } else {
      result = MatchResult.WHITE_WINS;
    }
    result.setMessage("by resignation");
    return result;
  }

  //draw agreement
  public static MatchResult gameOverDrawAgreement() {
    MatchResult result = MatchResult.DRAW;
    result.setMessage("by agreement");
    return result;
  }

  public static void calculateKingsPos(Piece[][] pieces) {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (pieces[i][j] != null && pieces[i][j].getType() == PieceType.KING) {
          if (pieces[i][j].getColor().equals("white")) {
            GameState.getInstance().setWhiteKingPos(new int[]{i, j});
          } else {
            GameState.getInstance().setBlackKingPos(new int[]{i, j});
          }
        }
      }
    }
  }

  public static void calculateKingsInCheck(Piece[][] pieces) {
    GameState gameState = GameState.getInstance();
    int[] whiteKing = gameState.getWhiteKingPos();
    int[] blackKing = gameState.getBlackKingPos();

    //enemy attacks own king
    MoveGenerator.switchColors();
    boolean[][] attacked1 = MoveGenerator.getEnemyAttackedSquares(pieces, false);
    MoveGenerator.switchColors();

    //enemy kings gets attacked
    boolean[][] attacked2 = BoardGenerator.movesToBitboard(MoveGenerator.getPossibleMoves(pieces));

    if (attacked1[whiteKing[0]][whiteKing[1]] || attacked2[whiteKing[0]][whiteKing[1]]) {
      gameState.setWhiteKingInCheck(true);
    } else {
      gameState.setWhiteKingInCheck(false);
    }
    if (attacked1[blackKing[0]][blackKing[1]] || attacked2[blackKing[0]][blackKing[1]]) {
      gameState.setBlackKingInCheck(true);
    } else {
      gameState.setBlackKingInCheck(false);
    }
  }

}
