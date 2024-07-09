package uk.ac.standrews.variantchessgame.model;

public class KingQueenSpecialRule implements GameRule {
    @Override
    public void applyRule(VariantChessMove move, VariantChessPiece piece, VariantChessBoard board) {
        if (piece instanceof King || piece instanceof Queen) {
            VariantChessPiece targetPiece = board.getPieceAt(move.getEndX(), move.getEndY());
            if (targetPiece != null && targetPiece.getColor() != piece.getColor()) {
                // 反击逻辑，敌方棋子变为己方棋子
                targetPiece.setColor(piece.getColor());
                // 国王或王后移动到前方的格子
                board.setPieceAt(move.getEndX(), move.getEndY(), null);
                board.setPieceAt(move.getStartX(), move.getStartY(), piece);
            }
        }
    }
}
