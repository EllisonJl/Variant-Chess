package uk.ac.standrews.variantchessgame.model;

/**
 * Implements a special game rule for the King and Queen pieces.
 * This rule allows the King or Queen to perform a special capture move under specific conditions.
 */
public class KingQueenSpecialRule implements GameRule {
    // Flag to track whether the special capture has been used.
    private boolean hasUsedSpecialCapture = false;

    /**
     * Applies the special capture rule to a move.
     * If the move is performed by a King or Queen and it captures an opponent's piece,
     * the captured piece's color is changed to the current player's color,
     * and the King or Queen is moved to a new position.
     * The special capture can only be used once per game.
     *
     * @param move The move to be applied, including start and end positions.
     * @param piece The piece performing the move.
     * @param board The chessboard on which the move is being applied.
     */
    @Override
    public void applyRule(VariantChessMove move, VariantChessPiece piece, VariantChessBoard board) {
        System.out.println("Applying special capture rule...");
        System.out.println("Initial move: " + move.getStartX() + ", " + move.getStartY() + " to " + move.getEndX() + ", " + move.getEndY());
        System.out.println("Piece: " + piece.getClass().getSimpleName() + ", Color: " + piece.getColor());

        // Check if the special capture rule has already been used or if the piece is neither a King nor a Queen
        if (!hasUsedSpecialCapture && (piece instanceof King || piece instanceof Queen)) {
            // Get the piece at the target position
            VariantChessPiece targetPiece = board.getPieceAt(move.getEndX(), move.getEndY());

            // Check if the target position contains an opponent's piece
            if (targetPiece != null && targetPiece.getColor() != piece.getColor()) {
                System.out.println("Target piece: " + targetPiece.getClass().getSimpleName() + ", Color: " + targetPiece.getColor());

                // Change the target piece's color to match the current player's color
                targetPiece.setColor(piece.getColor());

                // If the captured piece is a pawn, update its movement direction
                if (targetPiece instanceof Pawn) {
                    ((Pawn) targetPiece).updateDirection(piece.getColor());
                }

                // Allow the captured piece to move
                targetPiece.setImmobile(false);

                // Calculate the new position for the King or Queen
                int deltaX = move.getEndX() - move.getStartX();
                int deltaY = move.getEndY() - move.getStartY();
                int newX, newY;

                if (piece instanceof King) {
                    // The King remains in its current position
                    newX = move.getStartX();
                    newY = move.getStartY();
                } else { // For Queen
                    // The Queen moves one step back in the direction of the move
                    newX = move.getEndX() - Integer.signum(deltaX);
                    newY = move.getEndY() - Integer.signum(deltaY);
                }

                // Update the board with the new positions
                board.setPieceAt(newX, newY, piece); // Move the King or Queen to the new position
                board.setPieceAt(move.getEndX(), move.getEndY(), targetPiece); // Place the captured piece in its new position

                // For Queen, clear the original position
                if (piece instanceof Queen) {
                    board.setPieceAt(move.getStartX(), move.getStartY(), null);
                }

                System.out.println("Updated board positions: " + piece.getClass().getSimpleName() + " to " + newX + ", " + newY);
                System.out.println("Captured piece: " + targetPiece.getClass().getSimpleName() + " at " + move.getEndX() + ", " + move.getEndY());

                // Print the board state for debugging purposes
                printBoardState(board);

                // Mark the special capture rule as used
                hasUsedSpecialCapture = true;
            }
        }
    }

    /**
     * Prints the current state of the chessboard.
     * This method provides a visual representation of the board for debugging purposes.
     *
     * @param board The chessboard whose state is to be printed.
     */
    private void printBoardState(VariantChessBoard board) {
        System.out.println("Current board state:");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                VariantChessPiece piece = board.getPieceAt(i, j);
                if (piece != null) {
                    // Print the first character of the piece's class and color for identification
                    System.out.print(piece.getClass().getSimpleName().charAt(0) + "" + piece.getColor().toString().charAt(0) + " ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Checks if the special capture rule has been used.
     *
     * @return True if the special capture has been used, otherwise false.
     */
    public boolean hasUsedSpecialCapture() {
        return hasUsedSpecialCapture;
    }
}
