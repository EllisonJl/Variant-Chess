package uk.ac.standrews.variantchessgame.model;

public class KingQueenSpecialRule implements GameRule {
    private boolean hasKingUsedSpecialCapture = false;
    private boolean hasQueenUsedSpecialCapture = false;

    @Override
    public void applyRule(VariantChessMove move, VariantChessPiece piece, VariantChessBoard board) {
        System.out.println("Applying special capture rule...");
        System.out.println("Initial move: " + move.getStartX() + ", " + move.getStartY() + " to " + move.getEndX() + ", " + move.getEndY());
        System.out.println("Piece: " + piece.getClass().getSimpleName() + ", Color: " + piece.getColor());

        // Determine if the piece is eligible for a special capture
        boolean canUseSpecialCapture = (piece instanceof King && !hasKingUsedSpecialCapture) ||
                (piece instanceof Queen && !hasQueenUsedSpecialCapture);

        if (canUseSpecialCapture) {
            VariantChessPiece targetPiece = board.getPieceAt(move.getEndX(), move.getEndY());
            System.out.println("111111");
            System.out.println("Target piece before capture: " + targetPiece + ", Color: " + (targetPiece != null ? targetPiece.getColor() : "null"));
            System.out.println("Current piece: " + piece.getClass().getSimpleName() + ", Color: " + piece.getColor());

            // Check if the target piece is valid for capture (exists and is of opposite color)
            if (targetPiece != null && targetPiece.getColor() != piece.getColor()) {
                System.out.println("Target piece valid for capture: " + targetPiece.getClass().getSimpleName() + ", Color: " + targetPiece.getColor());

                // Change the target piece's color to the current player's color
                targetPiece.setColor(piece.getColor());

                // Update direction if the captured piece is a pawn
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
                    hasKingUsedSpecialCapture = true;
                } else if (piece instanceof Queen) {
                    hasQueenUsedSpecialCapture = true;
                }
            } else {
                System.out.println("Invalid capture attempt: Target piece is null or the same color as the capturing piece.");
            }
        }
    }

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

    public boolean hasKingUsedSpecialCapture() {
        return hasKingUsedSpecialCapture;
    }

    public boolean hasQueenUsedSpecialCapture() {
        return hasQueenUsedSpecialCapture;
    }
}
