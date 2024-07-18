package uk.ac.standrews.variantchessgame.model;

public abstract class VariantChessPiece {
    private Color color;
    private String type;
    private int captureCount;
    private boolean promotedFromPawn;
    private boolean immobile;  // New attribute to track if the piece is immobile

    public VariantChessPiece(Color color, String type) {
        this.color = color;
        this.type = type;
        this.captureCount = 0;
        this.promotedFromPawn = false;
        this.immobile = false;  // Default is not immobile
    }

    public VariantChessPiece(Color color, String type, boolean promotedFromPawn) {
        this.color = color;
        this.type = type;
        this.captureCount = 0;
        this.promotedFromPawn = promotedFromPawn;
        this.immobile = false;  // Default is not immobile
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

    public int getCaptureCount() {
        return captureCount;
    }

    public void incrementCaptureCount() {
        captureCount++;
    }

    public boolean isPromotedFromPawn() {
        return promotedFromPawn;
    }

    public void setPromotedFromPawn(boolean promotedFromPawn) {
        this.promotedFromPawn = promotedFromPawn;
    }

    public boolean isImmobile() {
        return immobile;
    }

    public void setImmobile(boolean immobile) {
        this.immobile = immobile;
    }
    public void setCaptureCount(int captureCount) {
        this.captureCount = captureCount;
    }

    public abstract boolean isValidMove(VariantChessMove move, VariantChessBoard board);
}
