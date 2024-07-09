package uk.ac.standrews.variantchessgame.model;

public class CannonSpecialRule implements GameRule {
    @Override
    public void applyRule(VariantChessMove move, VariantChessPiece piece, VariantChessBoard board) {
        if (piece instanceof Cannon) {
            int captureCount = ((Cannon) piece).getCaptureCount();
            if (captureCount >= 3) {
                // 自爆逻辑
                // 移除大炮
                board.setPieceAt(move.getEndX(), move.getEndY(), null);
                // 移除前后左右的棋子
                for (int[] dir : new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
                    int x = move.getEndX() + dir[0];
                    int y = move.getEndY() + dir[1];
                    if (board.isInBounds(x, y)) {
                        board.setPieceAt(x, y, null);
                    }
                }
            }
        }
    }
}
