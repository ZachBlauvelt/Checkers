package model;

import java.util.List;

/**
 * Represents the logic behind moves (what moves are legal and where).  This interface is a basic
 * implementation of the Strategy design pattern and is in place for greater flexibility in rulesets
 * that could be implemented.
 */
public interface IMoves {

  /**
   * Completes the moves from the given Coordinate to the other if possible.
   * @param board the board being used
   * @param start the coordinate to start at
   * @param end the coordinate to end at
   */
  public void move(CheckerPiece[][] board, Coord start, Coord end);

  /**
   * Get the potential NON-JUMPING moves.
   * @param whereFrom
   * @return
   */
  public List<Coord> potentialMoves(CheckerPiece[][] board, Coord whereFrom);

  /**
   * Get the potential JUMPING moves.
   * @param whereFrom
   * @return
   */
  public List<Coord> potentialJumps(CheckerPiece[][] board, Coord whereFrom);

  /**
   * Returns which Player can currently make a move.
   * @return
   */
  public PieceDesignation whoseTurn();
}
