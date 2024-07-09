package uk.ac.standrews.variantchessgame.model;

import uk.ac.standrews.variantchessgame.model.Color;
import uk.ac.standrews.variantchessgame.model.VariantChessBoard;
import uk.ac.standrews.variantchessgame.model.VariantChessMove;

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

    public void setColor(Color color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public abstract boolean isValidMove(VariantChessMove move, VariantChessBoard board);
}
