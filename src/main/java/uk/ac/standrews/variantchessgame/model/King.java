package uk.ac.standrews.variantchessgame.model;

/**
 * Represents a King piece in the variant chess game.
 * The King piece is the most crucial piece, and its movement is strictly limited.
 */
public class King extends VariantChessPiece {

    /**
     * Constructs a King piece with the specified color.
     *
     * @param color The color of the King piece (either WHITE or BLACK).
     */
    public King(Color color) {
        super(color, "King");
    }

    /**
     * Determines if the move for this King piece is valid.
     * The King can move exactly one square in any direction (horizontally, vertically, or diagonally).
     * The move is considered invalid if:
     * - The King moves more than one square in any direction.
     * - The target square is occupied by a piece of the same color.
     *
     * @param move The move to be validated, encapsulating the start and end positions.
     * @param board The chessboard to validate the move against.
     * @return True if the move is valid, otherwise false.
     */
    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {
        if (isImmobile()) return false;  // Check if the piece is immobile.

        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();

        // Ensure the target position is within the bounds of the chessboard.
        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            return false;
        }

        // Ensure the start and end positions are different.
        if (startX == endX && startY == endY) {
            return false;
        }

        // Ensure the King moves exactly one square in any direction.
        if (Math.abs(endX - startX) > 1 || Math.abs(endY - startY) > 1) {
            return false;
        }

        // Check if the target position is occupied by a piece of the same color.
        VariantChessPiece targetPiece = board.getPieceAt(endX, endY);
        if (targetPiece != null && targetPiece.getColor() == this.getColor()) {
            return false;
        }

        // If the target position is occupied by an opponent's piece, mark the move as a capture.
        if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
            move.setCapture(true);
        }

        return true;
    }
}
