package uk.ac.standrews.variantchessgame.model;

import java.util.Random;

public class PawnPromotionRule implements GameRule {
    @Override
    public void applyRule(VariantChessMove move, VariantChessPiece piece, VariantChessBoard board) {
        if (piece == null || !(piece instanceof Pawn || piece.isPromotedFromPawn())) {
            return;
        }

        // 增加捕获计数
        piece.incrementCaptureCount();

        int captureCount = piece.getCaptureCount();
        System.out.println("棋子的捕获次数为：" + captureCount);

        Random random = new Random();
        VariantChessPiece newPiece = piece;

        if (captureCount == 1) {
            if (random.nextBoolean()) {
                newPiece = new Knight(piece.getColor(), true);
            } else {
                newPiece = new Bishop(piece.getColor(), true);
            }
        } else if (captureCount == 2) {
            if (random.nextBoolean()) {
                newPiece = new Cannon(piece.getColor(), true);
            } else {
                newPiece = new Rook(piece.getColor(), true);
            }
        } else if (captureCount == 3) {
            newPiece = new Queen(piece.getColor(), true);
        }

        if (newPiece != piece) {
            newPiece.setCaptureCount(captureCount);
            board.setPieceAt(move.getEndX(), move.getEndY(), newPiece);
        }
    }
}
