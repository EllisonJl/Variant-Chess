package uk.ac.standrews.variantchessgame.model;

/**
 * Implements a special game rule for the Cannon piece.
 * When a Cannon has captured three or more pieces, it will detonate.
 */
public class CannonSpecialRule implements GameRule {

    /**
     * Applies the special rule for the Cannon piece.
     * If the Cannon has captured three or more pieces, it will detonate and remove itself and enemy pieces
     * from the surrounding positions on the board.
     *
     * @param move The move that triggered the rule application.
     * @param piece The piece that is affected by the rule.
     * @param board The current state of the chess board.
     */
    @Override
    public void applyRule(VariantChessMove move, VariantChessPiece piece, VariantChessBoard board) {
        // Check if the piece is a Cannon.
        if (piece instanceof Cannon) {
            Cannon cannon = (Cannon) piece;
            int captureCount = cannon.getCaptureCount();
            // If the Cannon has captured three or more pieces, apply the detonation.
            if (captureCount >= 3) {
                int x = move.getEndX();
                int y = move.getEndY();
                cannon.detonate(board, x, y); // Detonate the Cannon and clear the surrounding pieces.
            }
        }
    }
}
