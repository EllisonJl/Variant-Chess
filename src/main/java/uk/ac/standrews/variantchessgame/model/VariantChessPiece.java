package uk.ac.standrews.variantchessgame.model;

public abstract class VariantChessPiece {
    private Color color;
    private String type;
    private boolean immobile;

    public VariantChessPiece(Color color, String type) {
        this.color = color;
        this.type = type;
        this.immobile = false;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public boolean isImmobile() {
        return immobile;
    }

    public void setImmobile(boolean immobile) {
        this.immobile = immobile;
    }

    public abstract boolean isValidMove(VariantChessMove move, VariantChessBoard board);
}
