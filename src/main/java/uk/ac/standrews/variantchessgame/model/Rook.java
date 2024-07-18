package uk.ac.standrews.variantchessgame.model;

public class Rook extends VariantChessPiece {
    public Rook(Color color) {
        super(color, "Rook");
    }

    public Rook(Color color, boolean promotedFromPawn) {
        super(color, "Rook", promotedFromPawn);
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

        if (startX == endX && startY == endY) {
            return false;
        }

        if (startX != endX && startY != endY) {
            return false;
        }

        int stepX = Integer.compare(endX - startX, 0);
        int stepY = Integer.compare(endY - startY, 0);
        for (int i = 1; i < Math.max(Math.abs(endX - startX), Math.abs(endY - startY)); i++) {
            if (board.getPieceAt(startX + i * stepX, startY + i * stepY) != null) {
                return false;
            }
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
