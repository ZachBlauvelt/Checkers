package model;

import java.util.List;

/**
 * A Read Only version of the model, specifically intended to be passed to the View to eliminate the
 * risk of accidental interaction between the View and model.
 */
public interface ReadOnlyModel {

  /**
   * Returns which player is currently allowed to make moves.
   * @return
   */
  public PieceDesignation whoseTurn();

  /**
   * Returns a List of the Coords of all of Player 1's pieces for the purpose of displaying them.
   * @return
   */
  public List<Coord> player1Locations();

  /**
   * Returns a List of the Coords of all of Player 2's pieces for the purpose of displaying them.
   * @return
   */
  public List<Coord> player2Locations();

  /**
   * Returns the location of all kinged pieces to allow for special visual designation.
   * @return
   */
  public List<Coord> kingLocations();

  /**
   * Returns a List of Coords for all possible NON-JUMPING moves that can be made by the piece at
   * the given Coord. (This allows for highlighting of these spaces on the board if desired).
   * @param start
   * @return
   */
  public List<Coord> moveSpaces(Coord start);

  /**
   * Returns a List of Coords for all possible JUMPING moves that can be made by the piece at
   * the given Coord. (This allows for highlighting of these spaces on the board if desired).
   * @param start
   * @return
   */
  public List<Coord> jumpSpaces(Coord start);

  /**
   * Returns a boolean designating if the game has ended.
   * @return
   */
  boolean isGameOver();
}
