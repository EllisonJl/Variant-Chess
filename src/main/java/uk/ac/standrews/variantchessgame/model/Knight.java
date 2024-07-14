package uk.ac.standrews.variantchessgame.model;

public class Knight extends VariantChessPiece {
    public Knight(Color color) {
        super(color, "Knight");
    }

    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {
        int startX = move.getStartX();  // 起始位置行
        int startY = move.getStartY();  // 起始位置列
        int endX = move.getEndX();      // 目标位置行
        int endY = move.getEndY();      // 目标位置列

        // 检查移动是否在棋盘内
        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            return false;
        }

        // 判断移动是否为“日”字形
        if (!((Math.abs(endX - startX) == 2 && Math.abs(endY - startY) == 1) ||
                (Math.abs(endX - startX) == 1 && Math.abs(endY - startY) == 2))) {
            return false;
        }

        // 检查目标位置是否有自己的棋子
        VariantChessPiece targetPiece = board.getPieceAt(endX, endY);
        if (targetPiece != null && targetPiece.getColor() == this.getColor()) {
            return false;
        }

        // 如果目标位置有对方的棋子，标记为捕捉
        if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
            move.setCapture(true);
        }

        return true; // 移动合法
    }

}
