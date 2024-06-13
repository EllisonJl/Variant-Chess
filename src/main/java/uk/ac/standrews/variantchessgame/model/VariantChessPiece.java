package uk.ac.standrews.variantchessgame.model;

public abstract class VariantChessPiece {
    private Color color;
    private String type;

    public VariantChessPiece(Color color, String type) {
        this.color = color;
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    public abstract boolean isValidMove(VariantChessMove move, VariantChessBoard board);
}
