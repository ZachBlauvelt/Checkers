package view;

import controller.Features;

/**
 * A representation of the visual and interactive components that the user will see while
 * playing the game.
 */
public interface IView {

  /**
   * Renders the board and pieces.
   */
  public void render();

  /**
   * Allows for the addition of a Features object as a Listener to the user input.
   * @param f
   */
  public void addFeatures(Features f);

  /**
   * Alerts the View to redraw due to a change in the state of the board.
   */
  public void update();
}
