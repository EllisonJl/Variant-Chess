package uk.ac.standrews.variantchessgame.model;

import java.util.Random;

/**
 * The PawnPromotionRule class implements the GameRule interface and
 * defines the rules for pawn promotion in a variant chess game.
 * When a pawn reaches a certain capture count, it can be promoted
 * to a stronger piece based on predefined rules.
 */
public class PawnPromotionRule implements GameRule {

    /**
     * Applies the pawn promotion rule based on the move made, the piece involved,
     * and the current state of the chessboard. If the piece is a pawn or was
     * promoted from a pawn, it checks the capture count and promotes the piece
     * to a new type based on that count.
     *
     * @param move   The move that was made in the game.
     * @param piece  The chess piece that is being evaluated for promotion.
     * @param board  The chessboard on which the game is being played.
     */
    @Override
    public void applyRule(VariantChessMove move, VariantChessPiece piece, VariantChessBoard board) {
        // Check if the piece is a pawn or was promoted from a pawn
        if (piece instanceof Pawn || piece.isPromotedFromPawn()) {
            piece.incrementCaptureCount();  // Increment the capture count of the piece

            int captureCount = piece.getCaptureCount();  // Get the current capture count of the piece
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
                newPiece.setCaptureCount(captureCount);  // Maintain capture count in the new piece
                newPiece.setPromotedFromPawn(true);  // Mark the new piece as promoted from a pawn
                board.setPieceAt(move.getEndX(), move.getEndY(), newPiece);  // Place the new piece on the board
            }
        }
    }

}
