package uk.ac.standrews.variantchessgame.model;

/**
 * Pawn类，表示国际象棋游戏中的一个兵。
 */
public class Pawn extends VariantChessPiece {
    private int captureCount; // 记录此兵吃子的次数
    private boolean isFirstMove; // 标记此兵是否是第一次移动

    /**
     * 构造函数，初始化指定颜色的兵。
     *
     * @param color 兵的颜色（白色或黑色）。
     */
    public Pawn(Color color) {
        super(color, "Pawn"); // 调用父类构造函数，设置颜色和类型为“Pawn”
        this.captureCount = 0; // 初始化吃子次数为0
        this.isFirstMove = true; // 标记此兵尚未移动
    }

    /**
     * 判断兵的移动是否合法。
     *
     * @param move 移动信息，包括起始位置和目标位置。
     * @param board 棋盘对象，包含当前棋盘状态。
     * @return 如果移动合法，返回true；否则返回false。
     */
    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {
        int startRow = move.getStartX();  // 获取起始行号
        int startCol = move.getStartY();  // 获取起始列号
        int endRow = move.getEndX();      // 获取目标行号
        int endCol = move.getEndY();      // 获取目标列号

        // 判断目标位置是否在棋盘内
        if (endRow < 0 || endRow >= 8 || endCol < 0 || endCol >= 8) {
            return false; // 如果目标位置超出棋盘范围，返回false
        }

        int direction = (this.getColor() == Color.WHITE) ? -1 : 1;  // 白棋向上移动（行号减小），黑棋向下移动（行号增大）

        // 处理第一次移动
        if (isFirstMove) {
            // 检查直线前进一格或两格的情况
            if (startCol == endCol) {
                if ((endRow == startRow + direction || endRow == startRow + 2 * direction) && board.getPieceAt(endRow, endCol) == null) {
                    // 如果是前进两格，还需检查中间是否有障碍物
                    if (endRow == startRow + 2 * direction && board.getPieceAt(startRow + direction, startCol) != null) {
                        return false; // 如果中间有障碍物，返回false
                    }
                    isFirstMove = false;
                    return true; // 合法移动
                }
            }

            // 吃掉敌方棋子（前方一格）
            if (endRow == startRow + direction && startCol == endCol) {
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);
                    captureCount++;
                    isFirstMove = false;
                    return true; // 合法吃子
                }
            }

            // 吃掉敌方棋子（前方两格）
            if (endRow == startRow + 2 * direction && startCol == endCol) {
                VariantChessPiece middlePiece = board.getPieceAt(startRow + direction, startCol);
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (middlePiece == null && targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);
                    captureCount++;
                    isFirstMove = false;
                    return true; // 合法吃子
                }
            }

            // 吃掉敌方棋子（左右）
            if (endRow == startRow && Math.abs(endCol - startCol) == 1) {
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);
                    captureCount++;
                    isFirstMove = false;
                    return true; // 合法吃子
                }
            }
        } else {
            // 处理第一次移动后的情况：向前移动一格
            if (startCol == endCol && endRow == startRow + direction && board.getPieceAt(endRow, endCol) == null) {
                return true; // 合法移动
            }

            // 吃掉敌方棋子（前方）
            if (endRow == startRow + direction && startCol == endCol) {
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true); // 设置为吃子移动
                    captureCount++; // 增加吃子计数
                    return true; // 合法吃子
                }
            }

            // 吃掉敌方棋子（左右）
            if (endRow == startRow && Math.abs(endCol - startCol) == 1) {
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);
                    captureCount++;
                    return true; // 合法吃子
                }
            }

            // 横向移动（非吃子）
            if (endRow == startRow && Math.abs(endCol - startCol) == 1 && board.getPieceAt(endRow, endCol) == null) {
                return true; // 合法横向移动
            }
        }

        return false; // 非法移动
    }

    /**
     * 获取此兵的吃子次数。
     *
     * @return 吃子次数。
     */
    public int getCaptureCount() {
        return captureCount;
    }

    /**
     * 增加此兵的吃子次数。
     */
    public void incrementCaptureCount() {
        captureCount++;
    }

    /**
     * 设置此兵是否是第一次移动。
     *
     * @param isFirstMove 是否是第一次移动。
     */
    public void setFirstMove(boolean isFirstMove) {
        this.isFirstMove = isFirstMove;
    }
}
