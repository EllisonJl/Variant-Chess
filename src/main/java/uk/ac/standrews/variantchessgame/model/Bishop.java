package uk.ac.standrews.variantchessgame.model;

/**
 * Represents a Bishop piece in the variant chess game.
 * The Bishop moves diagonally and can be promoted from a Pawn.
 */
public class Bishop extends VariantChessPiece {

    /**
     * Constructs a Bishop with a specified color.
     *
     * @param color The color of the Bishop (either WHITE or BLACK).
     */
    public Bishop(Color color) {
        super(color, "Bishop");
    }

    /**
     * Constructs a Bishop with a specified color and promotion status.
     *
     * @param color The color of the Bishop (either WHITE or BLACK).
     * @param promotedFromPawn Indicates if the Bishop was promoted from a Pawn.
     */
    public Bishop(Color color, boolean promotedFromPawn) {
        super(color, "Bishop", promotedFromPawn);
    }
    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {

        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();

        // Ensure the move is within board boundaries.
        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            return false;
        }

        // The Bishop must move to a different position.
        if (startX == endX && startY == endY) {
            return false;
        }

        // Move two squares diagonally
        if (Math.abs(endX - startX) == 2 && Math.abs(endY - startY) == 2) {
            VariantChessPiece targetPiece = board.getPieceAt(endX, endY);
            // The move is valid if the target position is empty or occupied by an opponent's piece.
            if (targetPiece == null || targetPiece.getColor() != this.getColor()) {
                if (targetPiece != null) {
                    move.setCapture(true); // Mark as a capture move.
                }
                return true;
            }
            return false; // Blocked by a piece of the same color.
        }

        // No further checks needed for other types of movements
        return false;
    }

}
