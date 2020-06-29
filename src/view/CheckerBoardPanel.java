package view;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import controller.Features;
import model.Coord;
import model.PieceDesignation;
import model.ReadOnlyModel;

/**
 * A JPanel that contains not only the visual representation of the Checkerboard, but also allows
 * for user interaction.
 */
public class CheckerBoardPanel extends JPanel {
  private ReadOnlyModel model;
  private Coord selected;
  private Features f;
  private boolean cannotDeselect;
  private boolean gameEnded;

  public CheckerBoardPanel(ReadOnlyModel model) {
    super();
    this.model = model;
    this.setPreferredSize(new Dimension(800,800));
    this.setBackground(Color.WHITE);
    cannotDeselect = false;
    gameEnded = false;
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // this branch is only taken if the game has ended
    if (gameEnded) {
      //setup
      Graphics2D g2d = (Graphics2D) g;
      g2d.setColor(Color.BLACK);
      g2d.fillRect(0, 0, 800, 800);
      g2d.setColor(Color.WHITE);
      Font f = new Font("TimesRoman", Font.BOLD, 50);
      g2d.setFont(f);
      g2d.drawString("GAME OVER!", (800 - g2d.getFontMetrics().stringWidth("GAME OVER!")) / 2, 250);
      if (model.player1Locations().size() == 0) {
        g2d.drawString("PLAYER 2 WINS!", (800 - g2d.getFontMetrics().stringWidth("PLAYER 2 WINS!")) / 2, 400);
      }
      else if (model.player2Locations().size() == 0) {
        g2d.drawString("PLAYER 1 WINS!", (800 - g2d.getFontMetrics().stringWidth("PLAYER 1 WINS!")) / 2, 400);
      }
      else {
        if (model.whoseTurn().equals(PieceDesignation.PLAYER1)) {
          g2d.drawString("PLAYER 2 WINS!", (800 - g2d.getFontMetrics().stringWidth("PLAYER 2 WINS!")) / 2, 400);
        }
        else {
          g2d.drawString("PLAYER 1 WINS!", (800 - g2d.getFontMetrics().stringWidth("PLAYER 1 WINS!")) / 2, 400);
        }
      }
    }

    else {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setColor(new Color(85, 60, 42));
      g2d.fillRect(0, 0, 800, 800);
      g2d.setColor(new Color(253, 217, 181));

      // drawing the board
      for (int row = 0; row < 8; row++) {
        for (int col = 1 - row % 2; col < 8; col += 2) {
          g2d.fillRect(col * 100, row * 100, 100, 100);
        }
      }

      // highlighting selected space
      if (selected != null) {
        g2d.setColor(Color.YELLOW);
        g2d.fillRect(selected.col * 100, selected.row * 100, 100, 100);

        // highlighting all NON-JUMPING moves
        for (Coord c : model.moveSpaces(selected)) {
          g2d.setColor(Color.BLUE);
          g2d.fillRect(c.col * 100, c.row * 100, 100, 100);
        }

        //highlighting all JUMPING moves
        for (Coord c : model.jumpSpaces(selected)) {
          g2d.setColor(Color.RED);
          g2d.fillRect(c.col * 100, c.row * 100, 100, 100);
        }
      }

      // draw player 1 pieces
      for (Coord c : model.player1Locations()) {
        g2d.setColor(new Color(60, 60, 61));
        g2d.fillOval(c.col * 100 + 2, c.row * 100 + 2, 96, 96);
        g2d.setColor(Color.BLACK);
        g2d.fillOval(c.col * 100 + 10, c.row * 100 + 10, 80, 80);
      }

      // draw player 2 pieces
      for (Coord c : model.player2Locations()) {
        g2d.setColor(Color.WHITE);
        g2d.fillOval(c.col * 100 + 2, c.row * 100 + 2, 96, 96);
        g2d.setColor(new Color(246, 246, 246));
        g2d.fillOval(c.col * 100 + 10, c.row * 100 + 10, 80, 80);
      }

      // draw crowns on the kings
      g2d.setColor(new Color(204, 164, 61));
      for (Coord c : model.kingLocations()) {
        g2d.fillPolygon(this.crownMaker(c.col, c.row));
      }
    }
  }

  /**
   * A method to draw a crown shape for kinged pieces
   * @param x the x coordinate of the kinged piece's square
   * @param y the y coordinate of the kinged piece's square
   * @return
   */
  public Polygon crownMaker(int x, int y) {
    Polygon p = new Polygon();
    p.addPoint(x * 100 + 20,y * 100 + 35);
    p.addPoint(x * 100 + 20,y * 100 + 65);
    p.addPoint(x * 100 + 80,y * 100 + 65);
    p.addPoint(x * 100 + 80,y * 100 + 35);
    p.addPoint(x * 100 + 65,y * 100 + 50);
    p.addPoint(x * 100 + 50,y * 100 + 35);
    p.addPoint(x * 100 + 35,y * 100 + 50);
    return p;
  }

  /**
   * Converts a user's click to a Coord and notifies the Features object of this, as well as
   * handles the notion of selection.
   * @param x
   * @param y
   */
  public void handleMouseClick(int x, int y) {
    // clicks only matter if the game is still in play
    if (!gameEnded) {
      //convert to a Coord
      Coord where = new Coord(x / 100, y / 100);
      List<Coord> pieces;
      //chose correct set of pieces depending on whose turn it is
      if (model.whoseTurn().equals(PieceDesignation.PLAYER1)) {
        pieces = model.player1Locations();
      } else {
        pieces = model.player2Locations();
      }
      // allow for toggling of selection and selection of pieces
      if (pieces.contains(where)) {
        if (cannotDeselect) {
          // do nothing
        } else {
          if (selected != null) {
            if (selected.equals(where)) {
              selected = null;
              this.repaint();
            } else {
              this.selected = where;
              this.repaint();
            }
          } else {
            this.selected = where;
            this.repaint();
          }
        }
      } else {
        if (selected != null) {
          PieceDesignation whoseTurn = model.whoseTurn();
          f.moveTo(this.selected, where);
          if (whoseTurn.equals(model.whoseTurn()) && model.jumpSpaces(where).size() != 0) {
            this.selected = where;
            cannotDeselect = true;
          } else {
            this.selected = null;
            cannotDeselect = false;
          }
        }
      }
    }
  }

  /**
   * adds a Features object
   * @param f
   */
  public void addFeatures(Features f) {
    this.f = f;
  }

  /**
   * sets the game ended tag to true
   */
  public void gameEnded() {
    this.gameEnded = true;
  }
}
