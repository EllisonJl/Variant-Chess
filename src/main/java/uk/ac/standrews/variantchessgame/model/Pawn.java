package uk.ac.standrews.variantchessgame.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pawn extends VariantChessPiece {
    private static final Logger logger = LoggerFactory.getLogger(Pawn.class);
    private boolean isFirstMove;

    public Pawn(Color color) {
        super(color, "Pawn");
        this.isFirstMove = true;
    }

    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {
        int startRow = move.getStartX();  // x is row
        int startCol = move.getStartY();  // y is column
        int endRow = move.getEndX();      // x is row
        int endCol = move.getEndY();      // y is column
        logger.info("Validating move for Pawn: startRow={}, startCol={}, endRow={}, endCol={}", startRow, startCol, endRow, endCol);

        // 判断移动是否在棋盘内
        if (endRow < 0 || endRow >= 8 || endCol < 0 || endCol >= 8) {
            logger.warn("Move out of bounds.");
            return false;
        }

        int direction = (this.getColor() == Color.WHITE) ? 1 : -1;

        // 第一次移动
        if (isFirstMove) {
            if ((endRow == startRow + direction || endRow == startRow + 2 * direction) && startCol == endCol && board.getPieceAt(endRow, endCol) == null) {
                if (endRow == startRow + 2 * direction && board.getPieceAt(startRow + direction, startCol) == null) {
                    // 第一次移动两格
                    isFirstMove = false;
                    logger.info("First move registered, isFirstMove set to false.");
                } else if (endRow == startRow + direction) {
                    // 第一次移动一格
                    isFirstMove = false;
                    logger.info("First move registered, isFirstMove set to false.");
                }
                logger.info("First move is valid.");
                return true;
            }

            // 吃掉敌方棋子（第一次移动）
            if (endRow == startRow + direction && Math.abs(endCol - startCol) == 1) {
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    isFirstMove = false;
                    move.setCapture(true);
                    logger.info("First capture move is valid.");
                    return true;
                }
            }
        } else {
            // 之后的移动：向前
            if (endRow == startRow + direction && startCol == endCol && board.getPieceAt(endRow, endCol) == null) {
                logger.info("Move forward is valid.");
                return true;
            }

            // 吃掉敌方棋子
            if (endRow == startRow + direction && Math.abs(endCol - startCol) == 1) {
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);
                    logger.info("Capture move is valid.");
                    return true;
                }
            }

            // 横向移动（非吃子）
            if (endRow == startRow && Math.abs(endCol - startCol) == 1 && board.getPieceAt(endRow, endCol) == null) {
                logger.info("Sideways move is valid.");
                return true;
            }
        }

        logger.warn("Move is invalid.");
        return false;
    }
}
