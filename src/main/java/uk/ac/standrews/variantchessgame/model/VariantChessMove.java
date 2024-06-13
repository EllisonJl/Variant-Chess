package uk.ac.standrews.variantchessgame.model;

public class VariantChessMove {
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private boolean isCapture;


    public VariantChessMove(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.isCapture = false;

    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }
    public boolean isCapture() {
        return isCapture;
    }
    public void setCapture(boolean isCapture) {
        this.isCapture = isCapture;
    }
}
