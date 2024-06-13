package uk.ac.standrews.variantchessgame.model;

public class Queen extends VariantChessPiece {
    public Queen(Color color) {
        super(color, "Queen");
    }

    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {
        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();

        // 判断移动是否在棋盘内
        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            return false;
        }

        // 判断是否为直线或对角线移动
        if (!(startX == endX || startY == endY || Math.abs(startX - endX) == Math.abs(startY - endY))) {
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
