package uk.ac.standrews.variantchessgame.model;

/**
 * Represents a move in the variant chess game.
 * This class encapsulates the details of a move, including starting and ending positions and whether it is a capture.
 */
public class VariantChessMove {
    private int startX;  // The row index of the starting position
    private int startY;  // The column index of the starting position
    private int endX;    // The row index of the ending position
    private int endY;    // The column index of the ending position
    private boolean isCapture; // Indicates if the move involves capturing an opponent's piece
    private VariantChessPiece capturedPiece; // The piece captured during this move
    private boolean wasFirstMove; // Store the first move status of the piece


    /**
     * Constructs a new VariantChessMove with the specified starting and ending positions.
     * The capture flag is initialized to {@code false}.
     *
     * @param startX The row index of the starting position.
     * @param startY The column index of the starting position.
     * @param endX The row index of the ending position.
     * @param endY The column index of the ending position.
     */
    public VariantChessMove(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.isCapture = false;
        this.capturedPiece = null;
        this.wasFirstMove = false;
    }
    public boolean wasFirstMove() {
        return wasFirstMove;
    }

    public void setWasFirstMove(boolean wasFirstMove) {
        this.wasFirstMove = wasFirstMove;
    }
    public VariantChessPiece getCapturedPiece() {
        return capturedPiece;
    }

    public void setCapturedPiece(VariantChessPiece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }
    /**
     * Gets the row index of the starting position.
     *
     * @return The row index of the starting position.
     */
    public int getStartX() {
        return startX;
    }

    /**
     * Gets the column index of the starting position.
     *
     * @return The column index of the starting position.
     */
    public int getStartY() {
        return startY;
    }

    /**
     * Gets the row index of the ending position.
     *
     * @return The row index of the ending position.
     */
    public int getEndX() {
        return endX;
    }

    /**
     * Gets the column index of the ending position.
     *
     * @return The column index of the ending position.
     */
    public int getEndY() {
        return endY;
    }

    /**
     * Checks if the move involves capturing an opponent's piece.
     *
     * @return {@code true} if the move is a capture, {@code false} otherwise.
     */
    public boolean isCapture() {
        return isCapture;
    }

    /**
     * Sets whether the move involves capturing an opponent's piece.
     *
     * @param isCapture {@code true} if the move is a capture, {@code false} otherwise.
     */
    public void setCapture(boolean isCapture) {
        this.isCapture = isCapture;
    }
}
