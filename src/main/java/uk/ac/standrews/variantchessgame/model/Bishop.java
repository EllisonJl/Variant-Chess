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

        // 确保目标位置在棋盘范围内
        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            return false;
        }

        // 确保起始位置和目标位置不同
        if (startX == endX && startY == endY) {
            return false;
        }

        // 检查是否为距离为2的对角线移动
        if (Math.abs(endX - startX) == 2 && Math.abs(endY - startY) == 2) {
            VariantChessPiece targetPiece = board.getPieceAt(endX, endY);

            // 确保目标位置为空或有敌方棋子
            if (targetPiece == null || targetPiece.getColor() != this.getColor()) {
                // 中心点坐标
                int midX = (startX + endX) / 2;
                int midY = (startY + endY) / 2;

                // 检查中心点是否有棋子
                VariantChessPiece midPiece = board.getPieceAt(midX, midY);

                // 设置是否吃子
                move.setCapture(targetPiece != null && targetPiece.getColor() != this.getColor());
                return true;
            }
        }

        return false;
    }
}
