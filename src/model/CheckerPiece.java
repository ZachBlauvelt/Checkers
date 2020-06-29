package model;

/**
 * A representation of a Checker piece that contains basic information about the piece's designation
 * and whether it is kinged or not.
 */
public class CheckerPiece {
  public final PieceDesignation designation;
  private boolean kinged;

  public CheckerPiece(PieceDesignation designation) {
    this.designation = designation;
    this.kinged = false;
  }

  /**
   * Returns if this piece is kinged.
   * @return
   */
  public boolean isKinged() {
    return this.kinged;
  }

  /**
   * Sets the kinged flag for this piece to true.
   */
  public void king() {
    this.kinged = true;
  }
}
