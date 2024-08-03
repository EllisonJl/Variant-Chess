package uk.ac.standrews.variantchessgame.model;

import java.util.Random;

public class PawnPromotionRule implements GameRule {

    /**
     * Applies the pawn promotion rule to the board when a pawn is moved.
     * Promotes the pawn based on the number of captures it has made.
     *
     * @param move The move being applied.
     * @param piece The piece being moved.
     * @param board The current state of the chessboard.
     */
    @Override
    public void applyRule(VariantChessMove move, VariantChessPiece piece, VariantChessBoard board) {
        // Check if the piece is a pawn
        if (piece instanceof Pawn) {
            Pawn pawn = (Pawn) piece;

            // Ensure capture logic is only triggered once per move
            if (move.isCapture()) {
                pawn.incrementCaptureCount();  // Increment capture count
            }

            int captureCount = pawn.getCaptureCount();  // Get the current capture count
            System.out.println("棋子的捕获次数为：" + captureCount);  // Log capture count (for debugging purposes)

            Random random = new Random();  // Random object for making random decisions
            VariantChessPiece newPiece = pawn;  // Default new piece to the current piece

            // Decide new piece based on the capture count
            if (captureCount == 1) {
                // If capture count is 1, randomly choose between Knight or Bishop
                if (random.nextBoolean()) {
                    newPiece = new Knight(pawn.getColor(), true);
                } else {
                    newPiece = new Bishop(pawn.getColor(), true);
                }
            } else if (captureCount == 2) {
                // If capture count is 2, randomly choose between Cannon or Rook
                if (random.nextBoolean()) {
                    newPiece = new Cannon(pawn.getColor(), true);
                } else {
                    newPiece = new Rook(pawn.getColor(), true);
                }
            } else if (captureCount >= 3) {
                // If capture count is 3 or more, promote to Queen
                newPiece = new Queen(pawn.getColor(), true);
            }

            // If the new piece is different from the current piece, update the board
            if (newPiece != pawn) {
                newPiece.setCaptureCount(captureCount);  // Set the capture count on the new piece
                board.setPieceAt(move.getEndX(), move.getEndY(), newPiece);  // Place the new piece on the board
            }
        }
    }
}
