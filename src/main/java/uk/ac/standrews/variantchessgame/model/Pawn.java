package uk.ac.standrews.variantchessgame.model;

public class Pawn extends VariantChessPiece {
    private int captureCount;
    private boolean isFirstMove;

    public Pawn(Color color) {
        super(color, "Pawn");
        this.captureCount = 0;
        this.isFirstMove = true;
    }

    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {
        int startRow = move.getStartX();  // x is row
        int startCol = move.getStartY();  // y is column
        int endRow = move.getEndX();      // x is row
        int endCol = move.getEndY();      // y is column

        // 判断移动是否在棋盘内
        if (endRow < 0 || endRow >= 8 || endCol < 0 || endCol >= 8) {
            return false;
        }

        int direction = (this.getColor() == Color.WHITE) ? 1 : -1;

        // 检查路径上是否有棋子
        if (startCol == endCol) { // 直线移动
            int step = (endRow > startRow) ? 1 : -1;
            for (int row = startRow + step; row != endRow; row += step) {
                if (board.getPieceAt(row, startCol) != null) {
                    return false;
                }
            }
        }

        // 第一次移动
        if (isFirstMove) {
            if ((endRow == startRow + direction || endRow == startRow + 2 * direction) && startCol == endCol && board.getPieceAt(endRow, endCol) == null) {
                if (endRow == startRow + 2 * direction && board.getPieceAt(startRow + direction, startCol) == null) {
                    // 第一次移动两格
                    isFirstMove = false;
                } else if (endRow == startRow + direction) {
                    // 第一次移动一格
                    isFirstMove = false;
                }
                return true;
            }

            // 吃掉敌方棋子（第一次移动）
            if (endRow == startRow + direction && Math.abs(endCol - startCol) == 1) {
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    isFirstMove = false;
                    move.setCapture(true);
                    captureCount++;
                    return true;
                }
            }
        } else {
            // 之后的移动：向前
            if (endRow == startRow + direction && startCol == endCol && board.getPieceAt(endRow, endCol) == null) {
                return true;
            }

            // 吃掉敌方棋子
            if (endRow == startRow + direction && Math.abs(endCol - startCol) == 1) {
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);
                    captureCount++;
                    return true;
                }
            }

            // 横向移动（非吃子）
            if (endRow == startRow && Math.abs(endCol - startCol) == 1 && board.getPieceAt(endRow, endCol) == null) {
                return true;
            }
        }

        return false;
    }

    public int getCaptureCount() {
        return captureCount;
    }

    public void incrementCaptureCount() {
        captureCount++;
    }
}
