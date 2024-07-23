package uk.ac.standrews.variantchessgame.model;

/**
 * Represents a Rook piece in the variant chess game.
 * The Rook can move any number of squares vertically or horizontally.
 */
public class Rook extends VariantChessPiece {

    /**
     * Constructs a Rook with the specified color.
     *
     * @param color The color of the Rook (either WHITE or BLACK).
     */
    public Rook(Color color) {
        super(color, "Rook");
    }

    /**
     * Constructs a Rook with the specified color and promotion status.
     *
     * @param color The color of the Rook (either WHITE or BLACK).
     * @param promotedFromPawn A flag indicating if this Rook was promoted from a Pawn.
     */
    public Rook(Color color, boolean promotedFromPawn) {
        super(color, "Rook", promotedFromPawn);
    }

    /**
     * Checks if the move is valid for a Rook piece.
     * The Rook can move any number of squares vertically or horizontally.
     *
     * @param move The move to be validated, including start and end positions.
     * @param board The chessboard where the move is being applied.
     * @return {@code true} if the move is valid for a Rook, {@code false} otherwise.
     */
    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {
        // Check if the piece is immobile
        if (isImmobile()) return false;

        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();

        // Ensure the end position is within the board's boundaries
        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            return false;
        }

        // Ensure the move is not a zero-length move
        if (startX == endX && startY == endY) {
            return false;
        }

        // Check if the move is either vertical or horizontal
        if (startX != endX && startY != endY) {
            return false;
        }

        // Determine the step direction for the move
        int stepX = Integer.compare(endX - startX, 0);
        int stepY = Integer.compare(endY - startY, 0);

        // Check for any pieces blocking the path
        for (int i = 1; i < Math.max(Math.abs(endX - startX), Math.abs(endY - startY)); i++) {
            if (board.getPieceAt(startX + i * stepX, startY + i * stepY) != null) {
                return false;
            }
        }

        // Check if the target position contains a piece of the same color
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
