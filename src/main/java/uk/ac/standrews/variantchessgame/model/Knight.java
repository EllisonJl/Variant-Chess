package uk.ac.standrews.variantchessgame.model;

public class Knight extends VariantChessPiece {
    public Knight(Color color) {
        super(color, "Knight");
    }

    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {
        if (isImmobile()) return false;  // Check if the piece is immobile

        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();

        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            return false;
        }

        if (!((Math.abs(endX - startX) == 2 && Math.abs(endY - startY) == 1) ||
                (Math.abs(endX - startX) == 1 && Math.abs(endY - startY) == 2))) {
            return false;
        }

        VariantChessPiece targetPiece = board.getPieceAt(endX, endY);
        if (targetPiece != null && targetPiece.getColor() == this.getColor()) {
            return false;
        }

        if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
            move.setCapture(true);
        }

        return true;
    }
}
