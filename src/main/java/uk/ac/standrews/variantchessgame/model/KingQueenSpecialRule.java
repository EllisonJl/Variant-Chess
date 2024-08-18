package uk.ac.standrews.variantchessgame.model;

/**
 * This class implements a special game rule for a variant chess game,
 * specifically handling special capture rules for the King and Queen pieces.
 */
public class KingQueenSpecialRule implements GameRule {
    // Indicates whether the White King has used its special capture ability
    private boolean hasWhiteKingUsedSpecialCapture = false;

    // Indicates whether the White Queen has used its special capture ability
    private boolean hasWhiteQueenUsedSpecialCapture = false;

    // Indicates whether the Black King has used its special capture ability
    private boolean hasBlackKingUsedSpecialCapture = false;

    // Indicates whether the Black Queen has used its special capture ability
    private boolean hasBlackQueenUsedSpecialCapture = false;

    /**
     * Applies the special capture rule during a move.
     * If the King or Queen has not yet used their special capture, and they are
     * in a valid position to do so, they will capture the target piece and
     * take a specific action depending on the piece type.
     *
     * @param move   The move being made.
     * @param piece  The piece making the move (either King or Queen).
     * @param board  The current state of the chessboard.
     */
    @Override
    public void applyRule(VariantChessMove move, VariantChessPiece piece, VariantChessBoard board) {
        System.out.println("Applying special capture rule...");
        System.out.println("Initial move: " + move.getStartX() + ", " + move.getStartY() + " to " + move.getEndX() + ", " + move.getEndY());
        System.out.println("Piece: " + piece.getClass().getSimpleName() + ", Color: " + piece.getColor());

        // Determine if the piece is eligible for a special capture
        boolean canUseSpecialCapture = false;

        if (piece instanceof King) {
            if (piece.getColor() == Color.WHITE && !hasWhiteKingUsedSpecialCapture) {
                canUseSpecialCapture = true;
            } else if (piece.getColor() == Color.BLACK && !hasBlackKingUsedSpecialCapture) {
                canUseSpecialCapture = true;
            }
        } else if (piece instanceof Queen) {
            if (piece.getColor() == Color.WHITE && !hasWhiteQueenUsedSpecialCapture) {
                canUseSpecialCapture = true;
            } else if (piece.getColor() == Color.BLACK && !hasBlackQueenUsedSpecialCapture) {
                canUseSpecialCapture = true;
            }
        }

        if (canUseSpecialCapture) {
            VariantChessPiece targetPiece = board.getPieceAt(move.getEndX(), move.getEndY());
            System.out.println("Target piece before capture: " + targetPiece + ", Color: " + (targetPiece != null ? targetPiece.getColor() : "null"));
            System.out.println("Current piece: " + piece.getClass().getSimpleName() + ", Color: " + piece.getColor());

            // Check if the target piece is valid for capture (exists and is of opposite color)
            if (targetPiece != null && targetPiece.getColor() != piece.getColor()) {
                System.out.println("Target piece valid for capture: " + targetPiece.getClass().getSimpleName() + ", Color: " + targetPiece.getColor());

                // Change the color of the captured piece to match the capturing piece
                System.out.println("Changing target piece color from " + targetPiece.getColor() + " to " + piece.getColor());
                targetPiece.setColor(piece.getColor());
                System.out.println("Target piece color after change: " + targetPiece.getColor());

                // If the captured piece is a Pawn, update its movement direction
                if (targetPiece instanceof Pawn) {
                    ((Pawn) targetPiece).updateDirection(piece.getColor());
                }

                // Calculate the new position for the King or Queen
                int deltaX = move.getEndX() - move.getStartX();
                int deltaY = move.getEndY() - move.getStartY();
                int newX, newY;

                if (piece instanceof King) {
                    // King stays in the same position
                    newX = move.getStartX();
                    newY = move.getStartY();
                } else { // For Queen
                    // Queen moves one step back in the direction of the move
                    newX = move.getEndX() - Integer.signum(deltaX);
                    newY = move.getEndY() - Integer.signum(deltaY);
                }

                // Update the board positions
                board.setPieceAt(newX, newY, piece); // Move the King or Queen to the new position
                board.setPieceAt(move.getEndX(), move.getEndY(), targetPiece); // Place the captured piece in its new position

                if (piece instanceof Queen) {
                    // Clear the original position for Queen
                    board.setPieceAt(move.getStartX(), move.getStartY(), null);
                }

                System.out.println("Updated board positions: " + piece.getClass().getSimpleName() + " to " + newX + ", " + newY);
                System.out.println("Captured piece after update: " + targetPiece.getClass().getSimpleName() + " at " + move.getEndX() + ", " + move.getEndY());

                // Print the board state for debugging
                printBoardState(board);

                // Set the appropriate special capture flag
                if (piece instanceof King) {
                    if (piece.getColor() == Color.WHITE) {
                        hasWhiteKingUsedSpecialCapture = true;
                    } else {
                        hasBlackKingUsedSpecialCapture = true;
                    }
                } else if (piece instanceof Queen) {
                    if (piece.getColor() == Color.WHITE) {
                        hasWhiteQueenUsedSpecialCapture = true;
                    } else {
                        hasBlackQueenUsedSpecialCapture = true;
                    }
                }
            } else {
                System.out.println("Invalid capture attempt: Target piece is null or the same color as the capturing piece.");
            }
        }
    }

    /**
     * Prints the current state of the chessboard for debugging purposes.
     *
     * @param board The current state of the chessboard.
     */
    private void printBoardState(VariantChessBoard board) {
        System.out.println("Current board state:");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                VariantChessPiece piece = board.getPieceAt(i, j);
                if (piece != null) {
                    System.out.print(piece.getClass().getSimpleName().charAt(0) + "" + piece.getColor().toString().charAt(0) + " ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Returns whether the White King has used its special capture ability.
     *
     * @return true if the White King has used its special capture, false otherwise.
     */
    public boolean hasWhiteKingUsedSpecialCapture() {
        return hasWhiteKingUsedSpecialCapture;
    }

    /**
     * Returns whether the White Queen has used its special capture ability.
     *
     * @return true if the White Queen has used its special capture, false otherwise.
     */
    public boolean hasWhiteQueenUsedSpecialCapture() {
        return hasWhiteQueenUsedSpecialCapture;
    }

    /**
     * Returns whether the Black King has used its special capture ability.
     *
     * @return true if the Black King has used its special capture, false otherwise.
     */
    public boolean hasBlackKingUsedSpecialCapture() {
        return hasBlackKingUsedSpecialCapture;
    }

    /**
     * Returns whether the Black Queen has used its special capture ability.
     *
     * @return true if the Black Queen has used its special capture, false otherwise.
     */
    public boolean hasBlackQueenUsedSpecialCapture() {
        return hasBlackQueenUsedSpecialCapture;
    }
}
