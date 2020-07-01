package model;

import java.util.ArrayList;
import java.util.List;

/**
 * A representation of basic Checkers rules. NOTE: At the time of this comment, there is much room for
 * abstraction in this class as many of the methods share similar code.  This was a poor design choice
 * made before taking classes that focused heavily on design decisions.
 */
public class RegularMoves implements IMoves {
  // signifies if this piece is on a double jump, in which case only another jump is allowed
  private boolean onADouble;
  // false is Player1, true is Player2
  private boolean whoseTurn;

  public RegularMoves() {
    this.onADouble = false;
    this.whoseTurn = false;
  }

  @Override
  public void move(CheckerPiece[][] board, Coord start, Coord end) {
    // checks that the piece corresponds to the player whose turn it is
    if (board[start.col][start.row].designation.equals(PieceDesignation.PLAYER2) == whoseTurn) {
      for (Coord c : this.potentialMoves(board, start)) {
        if (c.equals(end)) {
          this.commitMove(board, start, end, false);
          // swap turns and reset double count
          whoseTurn = !whoseTurn;
          onADouble = false;
        }
      }
      for (Coord c : this.potentialJumps(board, start)) {
        if (c.equals(end)) {
          this.commitMove(board, start, end, true);
          if (this.potentialJumps(board, end).size() != 0) {
            this.onADouble = true;
          } else {
            // swap turns and reset double count
            this.onADouble = false;
            this.whoseTurn = !whoseTurn;
          }
        }
      }
    }
  }

  @Override
  public List<Coord> potentialMoves(CheckerPiece[][] board, Coord whereFrom) {
    ArrayList<Coord> moves = new ArrayList<>();
    // if there is a jump, the player must make it
    if (this.potentialJumps(board, whereFrom).size() != 0) {
      return new ArrayList<>();
    }
    // Player 1 shall play from the bottom
    int directionMult = 1;
    int forwardsBound = 0;
    int backwardsBound = 7;
    if (board[whereFrom.col][whereFrom.row] != null) {
      //swap multiplier and bounds as needed to move in opposite direction
      if (board[whereFrom.col][whereFrom.row].designation.equals(PieceDesignation.PLAYER2)) {
        directionMult = -1;
        forwardsBound = 7;
        backwardsBound = 0;
      }
      this.findMoves(board, whereFrom, forwardsBound, directionMult, backwardsBound, moves);
    }
    return moves;
  }

  @Override
  public List<Coord> potentialJumps(CheckerPiece[][] board, Coord whereFrom) {
    ArrayList<Coord> jumps = new ArrayList<>();
    PieceDesignation whoToKill = PieceDesignation.PLAYER2;
    // changes the team of the pieces that can be jumped based on the designation of the piece at
    // where from
    if (board[whereFrom.col][whereFrom.row] != null) {
      if (board[whereFrom.col][whereFrom.row].designation.equals(PieceDesignation.PLAYER2)) {
        whoToKill = PieceDesignation.PLAYER1;
      }
    }

    if (board[whereFrom.col][whereFrom.row] != null) {
      if (board[whereFrom.col][whereFrom.row].designation.equals(PieceDesignation.PLAYER1) ||
              board[whereFrom.col][whereFrom.row].isKinged()) {
        if (whereFrom.row >= 2 && whereFrom.col >= 2) {
          this.addValidJump(board, whereFrom, whoToKill, jumps, -1, -1);
        }
        if (whereFrom.row >= 2 && whereFrom.col <= 5) {
          this.addValidJump(board, whereFrom, whoToKill, jumps, -1, 1);
        }
      }
    }
    if (board[whereFrom.col][whereFrom.row] != null) {
      if (board[whereFrom.col][whereFrom.row].designation.equals(PieceDesignation.PLAYER2) ||
              board[whereFrom.col][whereFrom.row].isKinged()) {
        if (whereFrom.row <= 5 && whereFrom.col >= 2) {
          this.addValidJump(board, whereFrom, whoToKill, jumps, 1, -1);
        }
        if (whereFrom.row <= 5 && whereFrom.col <= 5) {
          this.addValidJump(board, whereFrom, whoToKill, jumps, 1, 1);
        }
      }
    }
    return jumps;
  }

  /**
   * Returns the Piece Designation of whose turn it is.
   */
  public PieceDesignation whoseTurn() {
    if (!whoseTurn) {
      return PieceDesignation.PLAYER1;
    }
    return PieceDesignation.PLAYER2;
  }

  /**
   * A method to abstract out the workings of a move, to remove duplicate code between a non-jumping
   * move and a jump.
   */
  private void commitMove(CheckerPiece[][] board, Coord start, Coord end, boolean isAJump) {
    CheckerPiece temp = board[start.col][start.row];
    //set start to null as piece has left the spot
    board[start.col][start.row] = null;
    // if the move is a jump, remove the jumped piece
    if (isAJump) {
      board[start.col - ((start.col - end.col) / 2)][start.row - ((start.row - end.row) / 2)] = null;
    }
    //transfer piece to new location
    board[end.col][end.row] = temp;

    // king the piece if needed
    if (board[end.col][end.row].designation.equals(PieceDesignation.PLAYER1) && end.row == 0) {
      board[end.col][end.row].king();
    }
    if (board[end.col][end.row].designation.equals(PieceDesignation.PLAYER2) && end.row == 7) {
      board[end.col][end.row].king();
    }
  }

  /**
   * A helper method to avoid congestion in the main moves method.  Checks that each possible move
   * is within the bounds of the board and has no pieces blocking the way.
   */
  private void findMoves(CheckerPiece[][] board, Coord whereFrom, int forwardsBound, int directionMult,
                         int backwardsBound, List<Coord> moves) {
    if (whereFrom.row != forwardsBound && whereFrom.col != 0) {
      if (board[whereFrom.col - 1][whereFrom.row - directionMult] == null) {
        moves.add(new Coord(whereFrom.col - 1, whereFrom.row - directionMult));
      }
    }
    if (whereFrom.row != forwardsBound && whereFrom.col != 7) {
      if (board[whereFrom.col + 1][whereFrom.row - directionMult] == null) {
        moves.add(new Coord(whereFrom.col + 1, whereFrom.row - directionMult));
      }
    }
    if (board[whereFrom.col][whereFrom.row].isKinged()) {
      if (whereFrom.row != backwardsBound && whereFrom.col != 0) {
        if (board[whereFrom.col - 1][whereFrom.row + directionMult] == null) {
          moves.add(new Coord(whereFrom.col - 1, whereFrom.row + directionMult));
        }
      }
      if (whereFrom.row != backwardsBound && whereFrom.col != 7) {
        if (board[whereFrom.col + 1][whereFrom.row + directionMult] == null) {
          moves.add(new Coord(whereFrom.col + 1, whereFrom.row + directionMult));
        }
      }
    }
  }

  /**
   * Abstracts out the process of checking all criteria to see if a jump is possible to avoid code
   * duplication.
   */
  private void addValidJump(CheckerPiece[][] board, Coord whereFrom, PieceDesignation whoToKill,
                            List<Coord> jumps, int rowInc, int colInc) {
    if (board[whereFrom.col + colInc][whereFrom.row + rowInc] != null) {
      if (board[whereFrom.col + colInc][whereFrom.row + rowInc].designation.equals(whoToKill)) {
        if (board[whereFrom.col + 2 * colInc][whereFrom.row + 2 * rowInc] == null) {
          jumps.add(new Coord(whereFrom.col + 2 * colInc, whereFrom.row + 2 * rowInc));
        }
      }
    }
  }
}
