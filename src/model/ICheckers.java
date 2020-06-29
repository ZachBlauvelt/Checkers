package model;

import java.util.List;

/**
 * An representation of my Model that extends all methods of the Read Only version but allows for the
 * moving of pieces.
 */
public interface ICheckers extends ReadOnlyModel {
  /**
   * Attempts to move the piece at the given Coord to the second Coord.  If the move is not possible
   * (piece in the way, too far, etc), nothing will happen.
   * @param toMove the Coord of the piece that is being attempted to move
   * @param whereTo the destination for this piece
   */
  public void movePiece(Coord toMove, Coord whereTo);
}
