package uk.ac.standrews.variantchessgame.model;

public class Queen extends VariantChessPiece {
    public Queen(Color color) {
        super(color, "Queen");
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

        if (!(startX == endX || startY == endY || Math.abs(startX - endX) == Math.abs(startY - endY))) {
            return false;
        }

        int dx = Integer.compare(endX, startX);
        int dy = Integer.compare(endY, startY);

        int x = startX + dx;
        int y = startY + dy;

        while (x != endX || y != endY) {
            VariantChessPiece piece = board.getPieceAt(x, y);
            if (piece != null) {
                return false;
            }
            x += dx;
            y += dy;
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
