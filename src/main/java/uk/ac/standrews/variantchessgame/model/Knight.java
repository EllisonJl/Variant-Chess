package uk.ac.standrews.variantchessgame.model;

/**
 * Represents a Knight piece in the variant chess game.
 * The Knight moves in an L-shape: two squares in one direction and then one square perpendicular,
 * or one square in one direction and then two squares perpendicular.
 */
public class Knight extends VariantChessPiece {

    /**
     * Constructs a Knight piece with a specified color.
     *
     * @param color The color of the Knight piece (WHITE or BLACK).
     */
    public Knight(Color color) {
        super(color, "Knight");
    }

    /**
     * Constructs a Knight piece with a specified color and promotion status.
     *
     * @param color The color of the Knight piece (WHITE or BLACK).
     * @param promotedFromPawn Indicates whether this Knight was promoted from a pawn.
     */
    public Knight(Color color, boolean promotedFromPawn) {
        super(color, "Knight", promotedFromPawn);
    }

    /**
     * Checks if a move for this Knight is valid according to the rules of Knight movement.
     * The Knight moves in an L-shape: two squares in one direction and then one square perpendicular,
     * or one square in one direction and then two squares perpendicular.
     *
     * @param move The move to be validated, including start and end positions.
     * @param board The chessboard on which the move is being applied.
     * @return True if the move is valid, false otherwise.
     */
    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {

        // Extract start and end coordinates
        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();

        // Check if the end position is within the board boundaries
        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            return false;
        }

        // Validate the L-shape movement of the Knight
        boolean isLShapeMove = (Math.abs(endX - startX) == 2 && Math.abs(endY - startY) == 1) ||
                (Math.abs(endX - startX) == 1 && Math.abs(endY - startY) == 2);
        if (!isLShapeMove) {
            return false;
        }

        // Check if the target position is occupied by a piece of the same color
        VariantChessPiece targetPiece = board.getPieceAt(endX, endY);
        if (targetPiece != null && targetPiece.getColor() == this.getColor()) {
            return false;
        }

        // If the target position contains an opponent's piece, mark the move as a capture
        if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
            move.setCapture(true);
        }

        return true;
    }
}
