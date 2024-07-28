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

    /**
     * Determines if a move is valid for the Bishop.
     * The Bishop moves diagonally and can capture opposing pieces.
     *
     * @param move The move to be validated.
     * @param board The current state of the chess board.
     * @return True if the move is valid, false otherwise.
     */
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

        // Check if the move is diagonal.
        if (Math.abs(endX - startX) != Math.abs(endY - startY)) {
            return false;
        }

        // Special case for 田字形 (3x3 grid move with a piece in the middle).
        if (Math.abs(endX - startX) == 2 && Math.abs(endY - startY) == 2) {
            int midX = (startX + endX) / 2;
            int midY = (startY + endY) / 2;
            if (board.getPieceAt(midX, midY) != null) {
                VariantChessPiece targetPiece = board.getPieceAt(endX, endY);
                // The move is valid if there is a piece in the middle and
                // the target position is empty or occupied by an opponent's piece.
                if (targetPiece == null || targetPiece.getColor() != this.getColor()) {
                    if (targetPiece != null) {
                        move.setCapture(true); // Mark as a capture move.
                    }
                    return true;
                }
            }
        }

        // Check for pieces obstructing the diagonal path.
        int xDirection = (endX - startX) > 0 ? 1 : -1;
        int yDirection = (endY - startY) > 0 ? 1 : -1;

        for (int i = 1; i < Math.abs(endX - startX); i++) {
            if (board.getPieceAt(startX + i * xDirection, startY + i * yDirection) != null) {
                return false;
            }
        }

        // Check if the destination is occupied by a friendly piece.
        VariantChessPiece targetPiece = board.getPieceAt(endX, endY);
        if (targetPiece != null && targetPiece.getColor() == this.getColor()) {
            return false;
        }

        // Mark as a capture move if the destination is occupied by an opponent's piece.
        if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
            move.setCapture(true);
        }

        return true;
    }
}
