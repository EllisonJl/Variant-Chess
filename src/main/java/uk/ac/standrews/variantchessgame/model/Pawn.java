package uk.ac.standrews.variantchessgame.model;

/**
 * The Pawn class represents a pawn piece in a chess game.
 */
public class Pawn extends VariantChessPiece {
    private int captureCount; // Number of captures made by this pawn
    private boolean isFirstMove; // Indicates if this is the pawn's first move
    private int direction; // Direction of movement for this pawn (1 for black, -1 for white)

    /**
     * Constructs a Pawn with a specified color.
     *
     * @param color The color of the pawn (either white or black).
     */
    public Pawn(Color color) {
        super(color, "Pawn"); // Calls the superclass constructor to set the color and type to "Pawn"
        this.captureCount = 0; // Initializes the capture count to 0
        this.isFirstMove = true; // Marks the pawn as having not moved yet
        this.direction = (color == Color.WHITE) ? -1 : 1; // Sets the direction based on the color (white moves up, black moves down)
    }

    /**
     * Determines if the pawn's move is valid.
     *
     * @param move The move information, including start and end positions.
     * @param board The chessboard object containing the current state of the board.
     * @return True if the move is valid; otherwise, false.
     */
    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {
        int startRow = move.getStartX();  // Gets the starting row
        int startCol = move.getStartY();  // Gets the starting column
        int endRow = move.getEndX();      // Gets the ending row
        int endCol = move.getEndY();      // Gets the ending column

        // Print debug information
        System.out.println("Pawn start position:(" + startRow + "," + startCol + ")");
        System.out.println("Pawn end position:(" + endRow + "," + endCol + ")");
        System.out.println("Direction: " + direction);
        System.out.println("Piece isFirstMove: " + isFirstMove);

        // Check if the destination is within the board boundaries
        if (endRow < 0 || endRow >= 8 || endCol < 0 || endCol >= 8) {
            return false; // If the destination is out of board bounds, return false
        }

        // Handle the first move case
        if (isFirstMove) {
            // Check for moving one or two squares forward
            if (startCol == endCol && (endRow == startRow + direction || endRow == startRow + 2 * direction)) {
                // Check for obstacles in the path
                if (board.getPieceAt(endRow, endCol) == null) {
                    if (endRow == startRow + 2 * direction && board.getPieceAt(startRow + direction, startCol) != null) {
                        return false; // If there is an obstacle in the path, return false
                    }
                    return true; // Valid move
                }

                // Check for capture logic
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);
                    return true; // Capture an opponent's piece
                }
            }
            return false; // First move does not allow side-to-side movement
        } else {
            // Handle the case after the first move: moving one square forward
            if (startCol == endCol && endRow == startRow + direction && board.getPieceAt(endRow, endCol) == null) {
                return true; // Valid move
            }
        }

        // Check for forward capture
        if (endRow == startRow + direction && startCol == endCol) {
            VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
            // Capture if there is an opponent's piece at the destination
            if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                move.setCapture(true);
                return true;
            }
        }

        // Check for diagonal capture
        if (endRow == startRow && Math.abs(endCol - startCol) == 1) {
            VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
            // Capture if there is an opponent's piece at the destination
            if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                move.setCapture(true);
                return true;
            }
        }

        // Check for side-to-side movement (without capture)
        if (!isFirstMove && endRow == startRow && Math.abs(endCol - startCol) == 1 && board.getPieceAt(endRow, endCol) == null) {
            return true; // Valid move to an empty square
        }

        return false; // Invalid move
    }

    /**
     * Gets the number of captures made by this pawn.
     *
     * @return The capture count.
     */
    public int getCaptureCount() {
        return captureCount;
    }

    /**
     * Increments the capture count of this pawn.
     */
    public void incrementCaptureCount() {
        captureCount++;
    }

    /**
     * Gets whether this pawn has moved before.
     *
     * @return True if this is the pawn's first move; otherwise, false.
     */
    public boolean isFirstMove() {
        return isFirstMove;
    }

    /**
     * Sets whether this pawn is making its first move.
     *
     * @param isFirstMove True if this is the pawn's first move; otherwise, false.
     */
    public void setFirstMove(boolean isFirstMove) {
        this.isFirstMove = isFirstMove;
    }

    /**
     * Updates the pawn's movement direction based on its new color.
     *
     * @param newColor The new color of the pawn.
     */
    public void updateDirection(Color newColor) {
        setColor(newColor);
        this.direction = (newColor == Color.WHITE) ? -1 : 1; // Updates the direction based on the new color
    }
}
