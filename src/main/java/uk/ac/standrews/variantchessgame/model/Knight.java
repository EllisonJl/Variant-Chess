package uk.ac.standrews.variantchessgame.model;

public class Knight extends VariantChessPiece {
    public Knight(Color color) {
        super(color, "Knight");
    }

    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {
        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();

        // 判断目标位置是否在棋盘内
        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            return false;
        }

        // 判断起始位置和目标位置是否相同
        if (startX == endX && startY == endY) {
            return false;
        }

        // 判断移动是否为“日”字形
        if (!(Math.abs(endX - startX) == 2 && Math.abs(endY - startY) == 1) &&
                !(Math.abs(endX - startX) == 1 && Math.abs(endY - startY) == 2)) {
            return false;
        }

        // 判断目标位置是否有己方棋子
        VariantChessPiece targetPiece = board.getPieceAt(endX, endY);
        if (targetPiece != null && targetPiece.getColor() == this.getColor()) {
            return false;
        }

        // 设置捕捉状态
        if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
            move.setCapture(true);
        }

        return true; // 符合合法移动规则，返回 true
    }
}
