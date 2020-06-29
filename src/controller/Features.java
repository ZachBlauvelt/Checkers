package controller;

import model.Coord;

/**
 * This interface is intended to allow for the View to dictate user input to the Controller (since
 * I chose to use Swing, all user input is handled within my View and thus needs to be passed to
 * the Controller).  This interface allows me to expose the Controller interface to the user without
 * fear of them inputting moves.
 */
public interface Features {

  /**
   * Alerts the class implementing Features that a move was attempted to the given Coord.
   * @param selected the Coord of the currently selected piece (null if there is none)
   * @param newLocation the location to move the selected piece to (chosen by the player)
   */
  public void moveTo(Coord selected, Coord newLocation);
}
