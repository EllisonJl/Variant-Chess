package uk.ac.standrews.variantchessgame.model;

public class Bishop extends VariantChessPiece {
    public Bishop(Color color) {
        super(color, "Bishop");
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

        if (Math.abs(endX - startX) == 2 && Math.abs(endY - startY) == 2) {
            VariantChessPiece targetPiece = board.getPieceAt(endX, endY);
            if (targetPiece == null || targetPiece.getColor() != this.getColor()) {
                // 中心点坐标
                int midX = (startX + endX) / 2;
                int midY = (startY + endY) / 2;
                // 中心点可以有棋子
                move.setCapture(targetPiece != null && targetPiece.getColor() != this.getColor());
                return true;
            }
        }

        return false;
    }
}
