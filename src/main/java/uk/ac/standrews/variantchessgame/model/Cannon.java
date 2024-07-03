package uk.ac.standrews.variantchessgame.model;

public class Cannon extends VariantChessPiece {
    public Cannon(Color color) {
        super(color, "Cannon");
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

        // 确保是直线移动
        if (startX != endX && startY != endY) {
            return false;
        }

        boolean isCapture = board.getPieceAt(endX, endY) != null;
        int piecesInBetween = 0;

        // 处理纵向移动
        if (startX == endX) {
            int step = (endY > startY) ? 1 : -1;
            for (int y = startY + step; y != endY; y += step) {
                if (board.getPieceAt(startX, y) != null) {
                    piecesInBetween++;
                }
            }
        }
        // 处理横向移动
        else {
            int step = (endX > startX) ? 1 : -1;
            for (int x = startX + step; x != endX; x += step) {
                if (board.getPieceAt(x, startY) != null) {
                    piecesInBetween++;
                }
            }
        }

        // 判断是否符合吃子或移动的规则
        if (isCapture) {
            if (piecesInBetween == 1 && board.getPieceAt(endX, endY).getColor() != this.getColor()) {
                move.setCapture(true);
                return true;
            }
        } else {
            if (piecesInBetween == 0) {
                return true;
            }
        }

        return false;
    }
}
