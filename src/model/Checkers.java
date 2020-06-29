package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A representation of a Checkers game.  Note that all logic on what moves can be preformed is
 * passed to an Object of type IMoves to allow for better abstraction of the rules as well as
 * greater flexibility.
 */
public class Checkers implements ICheckers {
  private CheckerPiece[][] board;
  private IMoves ruleSet;

  public Checkers() {
    board = new CheckerPiece[8][8];

    // assign Player 2 pieces to appropriate locations
    for (int row = 0; row < 3; row++) {
      for (int col = 1 - row % 2; col < 8; col += 2) {
        board[col][row] = new CheckerPiece(PieceDesignation.PLAYER2);
      }
    }

    // assign Player 1 pieces to appropriate locations
    for (int row = 5; row < 8; row++) {
      for (int col = 1 - row % 2; col < 8; col+=2) {
        board[col][row] = new CheckerPiece(PieceDesignation.PLAYER1);
      }
    }
    this.ruleSet = new RegularMoves();
  }

  @Override
  public void movePiece(Coord toMove, Coord whereTo) {
    ruleSet.move(this.board, toMove, whereTo);
  }

  @Override
  public PieceDesignation whoseTurn() {
    return ruleSet.whoseTurn();
  }

  @Override
  public List<Coord> player1Locations() {
    List<Coord> locations = new ArrayList<>();
    for (int c = 0; c < 8; c++) {
      for (int r = 0; r < 8; r++) {
        if (board[c][r] == null) {
          // do nothing
        }
        else if (board[c][r].designation.equals(PieceDesignation.PLAYER1)) {
          locations.add(new Coord(c,r));
        }
        else {
          // do nothing
        }
      }
    }
    return locations;
  }

  @Override
  public List<Coord> player2Locations() {
    List<Coord> locations = new ArrayList<>();
    for (int c = 0; c < 8; c++) {
      for (int r = 0; r < 8; r++) {
        if (board[c][r] == null) {
          // do nothing
        }
        else if (board[c][r].designation.equals(PieceDesignation.PLAYER2)) {
          locations.add(new Coord(c,r));
        }
        else {
          // do nothing
        }
      }
    }
    return locations;
  }

  @Override
  public List<Coord> kingLocations() {
    List<Coord> locations = new ArrayList<>();
    for (int c = 0; c < 8; c++) {
      for (int r = 0; r < 8; r++) {
        if (board[c][r] == null) {
          // do nothing
        }
        else if (board[c][r].isKinged() == true) {
          locations.add(new Coord(c,r));
        }
        else {
          // do nothing
        }
      }
    }
    return locations;
  }

  @Override
  public List<Coord> jumpSpaces(Coord start) {
    return this.ruleSet.potentialJumps(this.board, start);
  }

  @Override
  public boolean isGameOver() {

    // if one Player has no pieces left, the game is over
    if (this.player1Locations().size() == 0 || this.player2Locations().size() == 0) {
      return true;
    }

    //to avoid a stalemate, we now check to see if the player whose turn it is can make any moves
    boolean movesLeft = false;
    List<Coord> pieces;
    if (this.whoseTurn().equals(PieceDesignation.PLAYER1)) {
      pieces = this.player1Locations();
    }
    else {
      pieces = this.player2Locations();
    }
    for (Coord c: pieces) {
      movesLeft = movesLeft || (this.jumpSpaces(c).size() != 0 || this.moveSpaces(c).size() != 0);
    }
    return !movesLeft;
  }


  @Override
  public List<Coord> moveSpaces(Coord start) {
    return this.ruleSet.potentialMoves(this.board, start);
  }
}
