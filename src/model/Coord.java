package model;

import java.util.HashMap;

/**
 * Represents a coordinate on the checkers board, specifically only the playable tiles.
 */
public class Coord {
  public final int row;
  public final int col;

  public Coord (int col, int row) {
    this.row = row;
    this.col = col;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Coord)) {
      return false;
    }
    Coord temp = (Coord)o;
    return temp.row == this.row && temp.col == this.col;
  }

  @Override
  public int hashCode() {
    return this.col * this.row;
  }
}
