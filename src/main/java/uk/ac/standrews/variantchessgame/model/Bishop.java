package uk.ac.standrews.variantchessgame.model;

public class Bishop extends VariantChessPiece {
    public Bishop(Color color) {
        super(color, "Bishop");
    }

    public Bishop(Color color, boolean promotedFromPawn) {
        super(color, "Bishop", promotedFromPawn);
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

        // Check if the move is diagonal
        if (Math.abs(endX - startX) != Math.abs(endY - startY)) {
            return false;
        }

        // Check if the move is a 田字形 (3x3 grid move with a piece in the middle)
        if (Math.abs(endX - startX) == 2 && Math.abs(endY - startY) == 2) {
            int midX = (startX + endX) / 2;
            int midY = (startY + endY) / 2;
            if (board.getPieceAt(midX, midY) != null) {
                VariantChessPiece targetPiece = board.getPieceAt(endX, endY);
                if (targetPiece == null || targetPiece.getColor() != this.getColor()) {
                    if (targetPiece != null) {
                        move.setCapture(true);
                    }
                    return true;
                }
            }
        }

        // Check for pieces in the way for regular diagonal moves
        int xDirection = (endX - startX) > 0 ? 1 : -1;
        int yDirection = (endY - startY) > 0 ? 1 : -1;

        for (int i = 1; i < Math.abs(endX - startX); i++) {
            if (board.getPieceAt(startX + i * xDirection, startY + i * yDirection) != null) {
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
