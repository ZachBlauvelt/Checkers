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

  // maybe return list of coord of jumped pieces
  @Override
  public void move(CheckerPiece[][] board, Coord start, Coord end) {
    if (board[start.col][start.row].designation.equals(PieceDesignation.PLAYER2) == whoseTurn) {
      for (Coord c : this.potentialMoves(board, start)) {
        if (c.equals(end)) {
          CheckerPiece temp = board[start.col][start.row];
          board[start.col][start.row] = null;
          board[end.col][end.row] = temp;
          if (board[end.col][end.row].designation.equals(PieceDesignation.PLAYER1) && end.row == 0) {
            board[end.col][end.row].king();
          }
          if (board[end.col][end.row].designation.equals(PieceDesignation.PLAYER2) && end.row == 7) {
            board[end.col][end.row].king();
          }
          whoseTurn = !whoseTurn;
          onADouble = false;
        }
      }
      for (Coord c : this.potentialJumps(board, start)) {
        if (c.equals(end)) {
          CheckerPiece temp = board[start.col][start.row];
          board[start.col][start.row] = null;
          board[start.col - ((start.col - end.col) / 2)][start.row - ((start.row - end.row) / 2)] = null;
          board[end.col][end.row] = temp;
          if (board[end.col][end.row].designation.equals(PieceDesignation.PLAYER1) && end.row == 0) {
            board[end.col][end.row].king();
          }
          if (board[end.col][end.row].designation.equals(PieceDesignation.PLAYER2) && end.row == 7) {
            board[end.col][end.row].king();
          }
          if (this.potentialJumps(board, end).size() != 0) {
            this.onADouble = true;
          } else {
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
    if (this.potentialJumps(board, whereFrom).size() != 0) {
      return new ArrayList<Coord>();
    }
    // Player 1 shall play from the bottom
    int directionMult = 1;
    int forwardsBound = 0;
    int backwardsBound = 7;
    if (board[whereFrom.col][whereFrom.row] != null) {
      if (board[whereFrom.col][whereFrom.row].designation.equals(PieceDesignation.PLAYER2)) {
        directionMult = -1;
        forwardsBound = 7;
        backwardsBound = 0;
      }
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
    return moves;
  }

  @Override
  public List<Coord> potentialJumps(CheckerPiece[][] board, Coord whereFrom) {
    ArrayList<Coord> jumps = new ArrayList();
    PieceDesignation whoToKill = PieceDesignation.PLAYER2;
    if (board[whereFrom.col][whereFrom.row] != null) {
      if (board[whereFrom.col][whereFrom.row].designation.equals(PieceDesignation.PLAYER2)) {
        whoToKill = PieceDesignation.PLAYER1;
      }
    }

    if (board[whereFrom.col][whereFrom.row] != null) {
      if (board[whereFrom.col][whereFrom.row].designation.equals(PieceDesignation.PLAYER1) ||
              board[whereFrom.col][whereFrom.row].isKinged()) {
        if (whereFrom.row >= 2 && whereFrom.col >= 2) {
          if (board[whereFrom.col - 1][whereFrom.row - 1] != null) {
            if (board[whereFrom.col - 1][whereFrom.row - 1].designation.equals(whoToKill)) {
              if (board[whereFrom.col - 2][whereFrom.row - 2] == null) {
                jumps.add(new Coord(whereFrom.col - 2, whereFrom.row - 2));
              }
            }
          }
        }
        if (whereFrom.row >= 2 && whereFrom.col <= 5) {
          if (board[whereFrom.col + 1][whereFrom.row - 1] != null) {
            if (board[whereFrom.col + 1][whereFrom.row - 1].designation.equals(whoToKill)) {
              if (board[whereFrom.col + 2][whereFrom.row - 2] == null) {
                jumps.add(new Coord(whereFrom.col + 2, whereFrom.row - 2));
              }
            }
          }
        }
      }
    }
    if (board[whereFrom.col][whereFrom.row] != null) {
      if (board[whereFrom.col][whereFrom.row].designation.equals(PieceDesignation.PLAYER2) ||
              board[whereFrom.col][whereFrom.row].isKinged()) {
        if (whereFrom.row <= 5 && whereFrom.col >= 2) {
          if (board[whereFrom.col - 1][whereFrom.row + 1] != null) {
            if (board[whereFrom.col - 1][whereFrom.row + 1].designation.equals(whoToKill)) {
              if (board[whereFrom.col - 2][whereFrom.row + 2] == null) {
                jumps.add(new Coord(whereFrom.col - 2, whereFrom.row + 2));
              }
            }
          }
        }
        if (whereFrom.row <= 5 && whereFrom.col <= 5) {
          if (board[whereFrom.col + 1][whereFrom.row + 1] != null) {
            if (board[whereFrom.col + 1][whereFrom.row + 1].designation.equals(whoToKill)) {
              if (board[whereFrom.col + 2][whereFrom.row + 2] == null) {
                jumps.add(new Coord(whereFrom.col + 2, whereFrom.row + 2));
              }
            }
          }
        }
      }
    }
    return jumps;
  }

  public PieceDesignation whoseTurn() {
    if (!whoseTurn) {
      return PieceDesignation.PLAYER1;
    }
    return PieceDesignation.PLAYER2;
  }
}
