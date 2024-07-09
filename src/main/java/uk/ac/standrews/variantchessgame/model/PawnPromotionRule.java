package uk.ac.standrews.variantchessgame.model;

public class PawnPromotionRule implements GameRule {
    @Override
    public void applyRule(VariantChessMove move, VariantChessPiece piece, VariantChessBoard board) {
        if (piece instanceof Pawn) {
            int captureCount = ((Pawn) piece).getCaptureCount();
            if (captureCount == 1) {
                // 变身为骑士或主教
                // 需要玩家选择，这里假设选择变身为骑士
                board.setPieceAt(move.getEndX(), move.getEndY(), new Knight(piece.getColor()));
            } else if (captureCount == 2) {
                // 变身为大炮或车
                // 需要玩家选择，这里假设选择变身为车
                board.setPieceAt(move.getEndX(), move.getEndY(), new Rook(piece.getColor()));
            } else if (captureCount >= 3) {
                // 变身为后
                board.setPieceAt(move.getEndX(), move.getEndY(), new Queen(piece.getColor()));
            }
        }
    }
}
