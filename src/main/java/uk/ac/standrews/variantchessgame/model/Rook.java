package uk.ac.standrews.variantchessgame.model;

public class Rook extends VariantChessPiece {
    public Rook(Color color) {
        super(color, "Rook");
    }

    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {
        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();

        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            return false;
        }

        if (startX != endX && startY != endY) {
            return false;
        }

        int dx = Integer.compare(endX, startX);
        int dy = Integer.compare(endY, startY);

        int x = startX + dx;
        int y = startY + dy;

        while (x != endX || y != endY) {
            VariantChessPiece piece = board.getPieceAt(x, y);
            if (piece != null) {
                if (piece.getColor() == this.getColor()) {
                    return false;
                } else if (piece.getColor() != this.getColor() && (x == endX && y == endY)) {
                    move.setCapture(true);
                    return true;
                } else {
                    return false;
                }
            }
            x += dx;
            y += dy;
        }

        return true;
    }
}
