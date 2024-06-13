package uk.ac.standrews.variantchessgame.model;

public class Pawn extends VariantChessPiece {
    private boolean isFirstMove;

    public Pawn(Color color) {
        super(color, "Pawn");
        this.isFirstMove = true;
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

        int direction = (this.getColor() == Color.WHITE) ? -1 : 1;

        // 第一次移动
        if (isFirstMove) {
            if (endX == startX + direction && startY == endY && board.getPieceAt(endX, endY) == null) {
                isFirstMove = false;
                return true;
            }
            return false;
        }

        // 之后的移动
        if (endX == startX + direction && startY == endY && board.getPieceAt(endX, endY) == null) {
            return true;
        }

        // 吃掉敌方棋子
        if (endX == startX + direction && Math.abs(endY - startY) == 1) {
            VariantChessPiece targetPiece = board.getPieceAt(endX, endY);
            if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                move.setCapture(true);
                return true;
            }
        }

        return false; // 如果不符合任何合法移动规则，返回 false
    }
}
