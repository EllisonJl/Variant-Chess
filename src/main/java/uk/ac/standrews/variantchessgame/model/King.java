package uk.ac.standrews.variantchessgame.model;

public class King extends VariantChessPiece {
    public King(Color color) {
        super(color, "King");
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

        // 判断移动是否为一格
        if (Math.abs(endX - startX) > 1 || Math.abs(endY - startY) > 1) {
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

        return true;
    }

    // 移动国王的方法
    public void move(VariantChessMove move, VariantChessBoard board) {
        if (isValidMove(move, board)) {
            // 移除捕捉的敌方棋子
            if (move.isCapture()) {
                board.getBoard()[move.getEndX()][move.getEndY()] = null;
            }
            // 移动国王
            board.getBoard()[move.getEndX()][move.getEndY()] = this;
            board.getBoard()[move.getStartX()][move.getStartY()] = null;
        }
    }
}
