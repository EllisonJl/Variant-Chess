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
        // Check if the piece is a pawn or has been promoted from a pawn
        if (piece == null || !(piece instanceof Pawn || piece.isPromotedFromPawn())) {
            return;  // Do nothing if the piece is not a pawn or hasn't been promoted from a pawn
        }

        // Increment the capture count for the piece
        piece.incrementCaptureCount();

        int captureCount = piece.getCaptureCount();  // Get the current capture count
        System.out.println("棋子的捕获次数为：" + captureCount);  // Log capture count (for debugging purposes)

        Random random = new Random();  // Random object for making random decisions
        VariantChessPiece newPiece = piece;  // Default new piece to the current piece

        // Decide new piece based on the capture count
        if (captureCount == 1) {
            // If capture count is 1, randomly choose between Knight or Bishop
            if (random.nextBoolean()) {
                newPiece = new Knight(piece.getColor(), true);
            } else {
                newPiece = new Bishop(piece.getColor(), true);
            }
        } else if (captureCount == 2) {
            // If capture count is 2, randomly choose between Cannon or Rook
            if (random.nextBoolean()) {
                newPiece = new Cannon(piece.getColor(), true);
            } else {
                newPiece = new Rook(piece.getColor(), true);
            }
        } else if (captureCount == 3) {
            // If capture count is 3, promote to Queen
            newPiece = new Queen(piece.getColor(), true);
        }

        // If the new piece is different from the current piece, update the board
        if (newPiece != piece) {
            newPiece.setCaptureCount(captureCount);  // Set the capture count on the new piece
            board.setPieceAt(move.getEndX(), move.getEndY(), newPiece);  // Place the new piece on the board
        }
    }
}
