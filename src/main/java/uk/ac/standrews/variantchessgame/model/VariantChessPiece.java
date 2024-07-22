package uk.ac.standrews.variantchessgame.model;

/**
 * Represents a piece in the variant chess game.
 * This class is the base class for all chess pieces and contains common attributes and methods for chess pieces.
 */
public abstract class VariantChessPiece {
    private Color color;               // The color of the piece (e.g., WHITE or BLACK)
    private String type;              // The type of the piece (e.g., Knight, Queen)
    private int captureCount;         // The number of captures made by this piece
    private boolean promotedFromPawn; // Indicates if the piece was promoted from a pawn
    private boolean immobile;         // Indicates if the piece is immobile

    /**
     * Constructs a new VariantChessPiece with the specified color and type.
     * The capture count is initialized to 0, and the piece is not promoted from a pawn or immobile by default.
     *
     * @param color The color of the piece.
     * @param type The type of the piece.
     */
    public VariantChessPiece(Color color, String type) {
        this.color = color;
        this.type = type;
        this.captureCount = 0;
        this.promotedFromPawn = false;
        this.immobile = false;  // Default to not immobile
    }

    /**
     * Constructs a new VariantChessPiece with the specified color, type, and promotion status.
     * The capture count is initialized to 0 and the immobile status is set to false by default.
     *
     * @param color The color of the piece.
     * @param type The type of the piece.
     * @param promotedFromPawn Indicates if the piece was promoted from a pawn.
     */
    public VariantChessPiece(Color color, String type, boolean promotedFromPawn) {
        this.color = color;
        this.type = type;
        this.captureCount = 0;
        this.promotedFromPawn = promotedFromPawn;
        this.immobile = false;  // Default to not immobile
    }

    /**
     * Gets the color of the piece.
     *
     * @return The color of the piece.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of the piece.
     *
     * @param color The new color of the piece.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets the type of the piece.
     *
     * @return The type of the piece.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the number of captures made by this piece.
     *
     * @return The capture count.
     */
    public int getCaptureCount() {
        return captureCount;
    }

    /**
     * Increments the capture count of the piece by 1.
     */
    public void incrementCaptureCount() {
        captureCount++;
    }

    /**
     * Checks if the piece was promoted from a pawn.
     *
     * @return {@code true} if the piece was promoted from a pawn, {@code false} otherwise.
     */
    public boolean isPromotedFromPawn() {
        return promotedFromPawn;
    }

    /**
     * Sets whether the piece was promoted from a pawn.
     *
     * @param promotedFromPawn {@code true} if the piece was promoted from a pawn, {@code false} otherwise.
     */
    public void setPromotedFromPawn(boolean promotedFromPawn) {
        this.promotedFromPawn = promotedFromPawn;
    }

    /**
     * Checks if the piece is immobile.
     *
     * @return {@code true} if the piece is immobile, {@code false} otherwise.
     */
    public boolean isImmobile() {
        return immobile;
    }

    /**
     * Sets the immobility status of the piece.
     *
     * @param immobile {@code true} if the piece is to be marked as immobile, {@code false} otherwise.
     */
    public void setImmobile(boolean immobile) {
        this.immobile = immobile;
    }

    /**
     * Sets the capture count of the piece.
     *
     * @param captureCount The new capture count.
     */
    public void setCaptureCount(int captureCount) {
        this.captureCount = captureCount;
    }

    /**
     * Determines if a move is valid for this piece.
     * This method must be implemented by subclasses to define the specific move rules for each type of piece.
     *
     * @param move The move to validate.
     * @param board The current state of the board.
     * @return {@code true} if the move is valid for this piece, {@code false} otherwise.
     */
    public abstract boolean isValidMove(VariantChessMove move, VariantChessBoard board);
}
