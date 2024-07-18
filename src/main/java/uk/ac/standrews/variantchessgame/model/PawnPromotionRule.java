package uk.ac.standrews.variantchessgame.model;

import java.util.Random;

public class PawnPromotionRule implements GameRule {
    @Override
    public void applyRule(VariantChessMove move, VariantChessPiece piece, VariantChessBoard board) {
        if (piece instanceof Pawn) {
            Pawn pawn = (Pawn) piece;
            int captureCount = pawn.getCaptureCount();
            Random random = new Random();
            VariantChessPiece newPiece = null;

            if (captureCount == 1) {
                // 随机变身为骑士或主教
                if (random.nextBoolean()) {
                    newPiece = new Knight(piece.getColor());
                } else {
                    newPiece = new Bishop(piece.getColor());
                }
            } else if (captureCount == 2) {
                // 随机变身为大炮或车
                if (random.nextBoolean()) {
                    newPiece = new Cannon(piece.getColor());
                } else {
                    newPiece = new Rook(piece.getColor());
                }
            } else if (captureCount == 3) {
                // 变身为后
                newPiece = new Queen(piece.getColor());
            } else if (captureCount > 3) {
                // 捕获次数大于三次，不再升级
                newPiece = piece;
            } else {
                // 不变身，继续保持为Pawn
                newPiece = piece;
            }

            if (newPiece != piece) {
                // 如果进行了变身操作，则更新棋盘上的棋子
                board.setPieceAt(move.getEndX(), move.getEndY(), newPiece);
            }

            // 更新捕获次数
            pawn.incrementCaptureCount();
        }
    }
}
