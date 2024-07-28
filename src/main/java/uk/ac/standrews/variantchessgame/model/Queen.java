package uk.ac.standrews.variantchessgame.model;

public class Queen extends VariantChessPiece {

    /**
     * Constructor for the Queen piece.
     *
     * @param color The color of the queen (White or Black).
     */
    public Queen(Color color) {
        super(color, "Queen");
    }

    /**
     * Constructor for the Queen piece with a flag indicating if it was promoted from a pawn.
     *
     * @param color The color of the queen (White or Black).
     * @param promotedFromPawn True if the queen was promoted from a pawn, false otherwise.
     */
    public Queen(Color color, boolean promotedFromPawn) {
        super(color, "Queen", promotedFromPawn);
    }

    /**
     * Determines if the move is valid for the queen.
     *
     * @param move The move being validated.
     * @param board The current state of the chessboard.
     * @return True if the move is valid, false otherwise.
     */
    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {

        int startX = move.getStartX();

        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();

        // Check if the move is within the bounds of the board
        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            return false;
        }

        // A queen cannot move to its own starting position
        if (startX == endX && startY == endY) {
            return false;
        }

        // Determine if the move is straight (horizontal or vertical) or diagonal
        boolean isStraight = startX == endX || startY == endY;
        boolean isDiagonal = Math.abs(endX - startX) == Math.abs(endY - startY);

        // A valid queen move must be either straight or diagonal
        if (!isStraight && !isDiagonal) {
            return false;
        }

        // Determine the direction of movement
        int stepX = Integer.compare(endX - startX, 0);
        int stepY = Integer.compare(endY - startY, 0);

        // Check for obstacles along the path of the move
        for (int i = 1; i < Math.max(Math.abs(endX - startX), Math.abs(endY - startY)); i++) {
            if (board.getPieceAt(startX + i * stepX, startY + i * stepY) != null) {
                return false;
            }
        }

        // Check if the target position is occupied by a piece of the same color
        VariantChessPiece targetPiece = board.getPieceAt(endX, endY);
        if (targetPiece != null && targetPiece.getColor() == this.getColor()) {
            return false;
        }

        // If the target position is occupied by an opponent's piece, set the move as a capture
        if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
            move.setCapture(true);
        }

        return true;
    }
}
