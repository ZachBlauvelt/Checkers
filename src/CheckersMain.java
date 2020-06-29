import controller.BasicController;
import controller.IController;
import model.Checkers;
import model.ICheckers;
import view.IView;
import view.VisualView;

public class CheckersMain {
  public static void main(String[] args) {
    ICheckers checkers = new Checkers();
    IView view = new VisualView(checkers);
    IController cont = new BasicController(view, checkers);
    cont.go();
  }
}
