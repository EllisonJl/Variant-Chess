package uk.ac.standrews.variantchessgame.model;

public class CannonSpecialRule implements GameRule {
    @Override
    public void applyRule(VariantChessMove move, VariantChessPiece piece, VariantChessBoard board) {
        if (piece instanceof Cannon) {
            Cannon cannon = (Cannon) piece;
            int captureCount = cannon.getCaptureCount();
            if (captureCount >= 3) {
                int x = move.getEndX();
                int y = move.getEndY();
                cannon.detonate(board, x, y);
            }
        }
    }
}
