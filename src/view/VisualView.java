package view;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import controller.Features;
import model.ReadOnlyModel;

/**
 * A graphical representation of the Checkers game, complete with handling of user input events.
 */
public class VisualView extends JFrame implements IView {
  private ReadOnlyModel model;
  private CheckerBoardPanel boardPanel;

  public VisualView(ReadOnlyModel model) {
    super();
    this.setTitle("Checkers");
    this.setSize(815, 900);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.model = model;

    this.setLayout(new BorderLayout());

    boardPanel = new CheckerBoardPanel(model);
    boardPanel.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        boardPanel.handleMouseClick(e.getPoint().x, e.getPoint().y);
      }

      @Override
      public void mousePressed(MouseEvent e) {

      }

      @Override
      public void mouseReleased(MouseEvent e) {

      }

      @Override
      public void mouseEntered(MouseEvent e) {

      }

      @Override
      public void mouseExited(MouseEvent e) {

      }
    });
    this.add(boardPanel, BorderLayout.CENTER);
  }

  @Override
  public void render() {
    this.setVisible(true);
  }

  @Override
  public void addFeatures(Features f) {
    boardPanel.addFeatures(f);
  }

  @Override
  public void update() {
    if (model.isGameOver()) {
      this.boardPanel.gameEnded();
    }
    this.repaint();
  }
}
