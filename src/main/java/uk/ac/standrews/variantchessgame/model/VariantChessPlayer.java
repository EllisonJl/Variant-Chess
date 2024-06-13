package uk.ac.standrews.variantchessgame.model;

import java.util.List;

public class VariantChessPlayer {
    private String name;
    private List<VariantChessPiece> pieces;

    public VariantChessPlayer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<VariantChessPiece> getPieces() {
        return pieces;
    }

    public void setPieces(List<VariantChessPiece> pieces) {
        this.pieces = pieces;
    }

    public void removePiece(VariantChessPiece piece) {
        pieces.remove(piece);
    }

    public void addPiece(VariantChessPiece piece) {
        pieces.add(piece);
    }
}
