package controller;

import model.Coord;
import model.ICheckers;
import view.IView;

/**
 * An implementation of the IController and Features interfaces that allows for setup of the game
 * as well as passing of user input to the Model.
 */
public class BasicController implements Features, IController {
  private IView view;
  private ICheckers model;

  public BasicController(IView view, ICheckers model) {
    this.view = view;
    this.model = model;
  }

  @Override
  public void moveTo(Coord selected, Coord newLocation) {
    this.model.movePiece(selected, newLocation);
    // must update the view in order to display an accurate depiction of the state of the board
    view.update();
  }

  @Override
  public void go() {
    this.view.render();
    this.view.addFeatures(this);
  }
}
