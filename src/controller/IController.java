package controller;

/**
 * An interface to abstract the representation of a Controller for my version of Checkers.  Controllers
 * should handle all interaction between the view and model.
 */
public interface IController {

  /**
   * Essentially starts the game.  Tells the Controller to complete any setup steps needed and to
   * run the game.
   */
  public void go();
}
