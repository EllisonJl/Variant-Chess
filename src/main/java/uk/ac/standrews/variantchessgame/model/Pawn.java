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
        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();
        logger.info("Validating move for Pawn: startX={}, startY={}, endX={}, endY={}", startX, startY, endX, endY);

        // 判断移动是否在棋盘内
        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            logger.warn("Move out of bounds.");
            return false;
        }

        int direction = (this.getColor() == Color.WHITE) ? 1 : -1;

        // 第一次移动
        if (isFirstMove) {
            if ((endY == startY + direction || endY == startY + 2 * direction) && startX == endX && board.getPieceAt(endX, endY) == null) {
                if (endY == startY + 2 * direction && board.getPieceAt(startX, startY + direction) == null) {
                    isFirstMove = false;
                }
                logger.info("First move is valid.");
                return true;
            }
        } else {
            // 之后的移动：向前
            if (endY == startY + direction && startX == endX && board.getPieceAt(endX, endY) == null) {
                isFirstMove = false;
                logger.info("Move forward is valid.");
                return true;
            }

            // 吃掉敌方棋子
            if (endY == startY + direction && Math.abs(endX - startX) == 1) {
                VariantChessPiece targetPiece = board.getPieceAt(endX, endY);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);
                    isFirstMove = false;
                    logger.info("Capture move is valid.");
                    return true;
                }
            }
        }

        logger.warn("Move is invalid.");
        return false;
    }
}
