package uk.ac.standrews.variantchessgame.model;

public class Bishop extends VariantChessPiece {
    public Bishop(Color color) {
        super(color, "Bishop");
    }

    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {
        int startX = move.getStartX(); // 起始行
        int startY = move.getStartY(); // 起始列
        int endX = move.getEndX();     // 目标行
        int endY = move.getEndY();     // 目标列

        // 确保目标位置在棋盘范围内
        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            return false;
        }

        // 确保起始位置和目标位置不同
        if (startX == endX && startY == endY) {
            return false;
        }

        // 检查是否为田字形移动
        if (!(Math.abs(endX - startX) == 2 && Math.abs(endY - startY) == 2)) {
            return false;
        }

        // 检查目标位置是否为空或有敌方棋子
        VariantChessPiece targetPiece = board.getPieceAt(endX, endY);
        if (targetPiece != null && targetPiece.getColor() == this.getColor()) {
            return false;
        }

        // 检查中心点是否有棋子
        int midX = (startX + endX) / 2;
        int midY = (startY + endY) / 2;
        VariantChessPiece midPiece = board.getPieceAt(midX, midY);

        // 设置是否吃子
        move.setCapture(targetPiece != null && targetPiece.getColor() != this.getColor());
        return true;
    }
}
