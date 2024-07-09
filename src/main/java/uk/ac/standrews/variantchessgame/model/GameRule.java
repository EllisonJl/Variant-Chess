package uk.ac.standrews.variantchessgame.model;

public interface GameRule {
    void applyRule(VariantChessMove move, VariantChessPiece piece, VariantChessBoard board);
}
