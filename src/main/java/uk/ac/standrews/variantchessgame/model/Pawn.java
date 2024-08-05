package uk.ac.standrews.variantchessgame.model;

/**
 * Pawn类，表示国际象棋游戏中的一个兵。
 */
public class Pawn extends VariantChessPiece {
    private int captureCount; // 记录此兵吃子的次数
    private boolean isFirstMove; // 标记此兵是否是第一次移动
    private int direction; // 标记此兵的移动方向

    /**
     * 构造函数，初始化指定颜色的兵。
     *
     * @param color 兵的颜色（白色或黑色）。
     */
    public Pawn(Color color) {
        super(color, "Pawn"); // 调用父类构造函数，设置颜色和类型为“Pawn”
        this.captureCount = 0; // 初始化吃子次数为0
        this.isFirstMove = true; // 标记此兵尚未移动
        this.direction = (color == Color.WHITE) ? -1 : 1; // 根据颜色设置方向，白色为-1，黑色为1
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

        System.out.println("Pawn开始位置:(" + startRow +","+startCol+")" );
        System.out.println("Pawn结束位置:(" + endRow +","+endCol+")" );
        System.out.println("Direction: " + direction);
        System.out.println("Piece isFirstMove: " + isFirstMove);

        // 判断目标位置是否在棋盘内
        if (endRow < 0 || endRow >= 8 || endCol < 0 || endCol >= 8) {
            return false; // 如果目标位置超出棋盘范围，返回false
        }

        // 处理第一次移动
        if (isFirstMove) {
            // 检查直线前进一格或两格的情况
            if (startCol == endCol && (endRow == startRow + direction || endRow == startRow + 2 * direction)) {
                // 检查路径上的障碍物
                if (board.getPieceAt(endRow, endCol) == null) {
                    if (endRow == startRow + 2 * direction && board.getPieceAt(startRow + direction, startCol) != null) {
                        return false; // 如果中间有障碍物，返回false
                    }
                    return true; // 合法移动
                }

                // 检查捕获逻辑
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);
                    return true; // 捕获敌方棋子
                }
            }
            return false; // 第一次移动时，不允许向左右空格移动
        } else {
            // 处理第一次移动后的情况：向前移动一格
            if (startCol == endCol && endRow == startRow + direction && board.getPieceAt(endRow, endCol) == null) {
                return true; // 合法移动
            }
        }

        // 向前捕获
        if (endRow == startRow + direction && startCol == endCol) {
            VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
            // 如果目标位置有棋子且是敌方棋子，则捕获
            if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                move.setCapture(true);
                return true;
            }
        }

        // 向左右捕获
        if (endRow == startRow && Math.abs(endCol - startCol) == 1) {
            VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
            // 如果目标位置有棋子且是敌方棋子，则捕获
            if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                move.setCapture(true);
                return true;
            }
        }

        // 向左右移动（不捕获）
        if (!isFirstMove && endRow == startRow && Math.abs(endCol - startCol) == 1 && board.getPieceAt(endRow, endCol) == null) {
            return true; // 合法移动到空位置
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
     * 获取此兵是否是第一次移动的状态。
     *
     * @return 是否是第一次移动。
     */
    public boolean isFirstMove() {
        return isFirstMove;
    }

    /**
     * 设置此兵是否是第一次移动。
     *
     * @param isFirstMove 是否是第一次移动。
     */
    public void setFirstMove(boolean isFirstMove) {
        this.isFirstMove = isFirstMove;
    }

    /**
     * 更新兵的移动方向。
     *
     * @param newColor 新颜色
     */
    public void updateDirection(Color newColor) {
        setColor(newColor);
        this.direction = (newColor == Color.WHITE) ? -1 : 1; // 根据新的颜色更新方向
    }
}
