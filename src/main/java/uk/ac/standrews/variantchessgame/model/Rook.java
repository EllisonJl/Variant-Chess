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

        // 判断目标位置是否在棋盘内
        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            return false;
        }

        // 判断起始位置和目标位置是否相同
        if (startX == endX && startY == endY) {
            return false;
        }

        // 判断是否为直线移动
        if (startX != endX && startY != endY) {
            return false;
        }

        int dx = Integer.compare(endX, startX);
        int dy = Integer.compare(endY, startY);

        int x = startX + dx;
        int y = startY + dy;

        // 检查路径上的每个格子
        while (x != endX || y != endY) {
            VariantChessPiece piece = board.getPieceAt(x, y);
            if (piece != null) {
                return false;
            }
            x += dx;
            y += dy;
        }

        // 检查目标位置是否有己方棋子
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
}
