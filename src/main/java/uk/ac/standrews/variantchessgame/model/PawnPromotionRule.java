package uk.ac.standrews.variantchessgame.model;

import java.util.Random;

public class PawnPromotionRule implements GameRule {

    @Override
    public void applyRule(VariantChessMove move, VariantChessPiece piece, VariantChessBoard board) {
        // Check if the piece is a pawn or was promoted from a pawn
        if (piece instanceof Pawn || piece.isPromotedFromPawn()) {
            piece.incrementCaptureCount();  // Increment the capture count

            int captureCount = piece.getCaptureCount();  // Get the current capture count
            System.out.println("Capture count for piece: " + captureCount);

            Random random = new Random();
            VariantChessPiece newPiece = piece;  // Default the new piece to the current piece

            // Determine the new piece based on capture count
            if (captureCount == 1) {
                if (random.nextBoolean()) {
                    newPiece = new Knight(piece.getColor(), true);
                } else {
                    newPiece = new Bishop(piece.getColor(), true);
                }
            } else if (captureCount == 2) {
                if (random.nextBoolean()) {
                    newPiece = new Cannon(piece.getColor(), true);
                } else {
                    newPiece = new Rook(piece.getColor(), true);
                }
            } else if (captureCount >= 3) {
                newPiece = new Queen(piece.getColor(), true);
            }

            // Only replace if the new piece differs from the current
            if (newPiece != piece) {
                newPiece.setCaptureCount(captureCount);  // Maintain capture count
                newPiece.setPromotedFromPawn(true);  // Set promoted status
                board.setPieceAt(move.getEndX(), move.getEndY(), newPiece);  // Place new piece on board
            }
        }
    }

}
