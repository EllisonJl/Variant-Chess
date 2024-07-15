package uk.ac.standrews.variantchessgame.model;

import java.util.Random;

public class PawnPromotionRule implements GameRule {
    @Override
    public void applyRule(VariantChessMove move, VariantChessPiece piece, VariantChessBoard board) {
        if (piece instanceof Pawn) {
            int captureCount = ((Pawn) piece).getCaptureCount();
            Random random = new Random();
            VariantChessPiece newPiece;

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
            } else if (captureCount >= 3) {
                // 变身为后
                newPiece = new Queen(piece.getColor());
            } else {
                // 不变身，继续保持为Pawn
                newPiece = piece;
            }

            board.setPieceAt(move.getEndX(), move.getEndY(), newPiece);
        }
    }
}
