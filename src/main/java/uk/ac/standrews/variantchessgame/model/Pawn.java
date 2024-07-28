package uk.ac.standrews.variantchessgame.model;

public class Pawn extends VariantChessPiece {
    private boolean isFirstMove;  // Flag to track if the pawn is on its first move
    private int direction;        // Direction of movement: -1 for White, 1 for Black

    /**
     * Constructor to initialize the Pawn with color and default values.
     * Sets the direction of movement based on the color of the pawn.
     *
     * @param color The color of the pawn (White or Black).
     */
    public Pawn(Color color) {
        super(color, "Pawn");       // Initialize base class with color and name
        this.isFirstMove = true;    // By default, a pawn is on its first move
        this.direction = (color == Color.WHITE) ? -1 : 1;  // White moves up (-1), Black moves down (1)
    }

    /**
     * Validates whether the pawn's move is valid.
     * This method checks different move scenarios including first move, normal moves, and captures.
     *
     * @param move The move to be validated.
     * @param board The current state of the chessboard.
     * @return True if the move is valid, false otherwise.
     */
    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {
//        if (isImmobile()) return false;  // Check if the piece is immobile (e.g., due to being blocked or other constraints)

        int startRow = move.getStartX();  // Starting row of the move
        int startCol = move.getStartY();  // Starting column of the move
        int endRow = move.getEndX();      // Ending row of the move
        int endCol = move.getEndY();      // Ending column of the move

        // Check if the move is out of board bounds
        if (endRow < 0 || endRow >= 8 || endCol < 0 || endCol >= 8) {
            return false;
        }

        if (isFirstMove) {  // Check moves when the pawn is on its first move
            // Pawn moves forward one or two squares if the path is clear
            if (startCol == endCol) {
                if ((endRow == startRow + direction || endRow == startRow + 2 * direction) && board.getPieceAt(endRow, endCol) == null) {
                    // Check if moving two squares is blocked by a piece in the middle
                    if (endRow == startRow + 2 * direction && board.getPieceAt(startRow + direction, startCol) != null) {
                        return false;
                    }
                    isFirstMove = false;  // Mark the first move as used
                    return true;
                }
            }

            // Pawn captures diagonally
            if (endRow == startRow + direction && startCol == endCol) {
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);  // Set capture flag if capturing an opponent's piece
                    return true;
                }
            }

            // Pawn captures diagonally after moving two squares
            if (endRow == startRow + 2 * direction && startCol == endCol) {
                VariantChessPiece middlePiece = board.getPieceAt(startRow + direction, startCol);
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (middlePiece == null && targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);  // Set capture flag if capturing an opponent's piece
                    return true;
                }
            }

            // Pawn captures diagonally when moving one square forward
            if (endRow == startRow && Math.abs(endCol - startCol) == 1) {
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);  // Set capture flag if capturing an opponent's piece
                    return true;
                }
            }
        } else {  // Check moves when the pawn is not on its first move
            // Pawn moves forward one square if the path is clear
            if (startCol == endCol && endRow == startRow + direction && board.getPieceAt(endRow, endCol) == null) {
                return true;
            }

            // Pawn captures diagonally
            if (endRow == startRow + direction && startCol == endCol) {
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);  // Set capture flag if capturing an opponent's piece
                    return true;
                }
            }

            // Pawn captures diagonally even after moving
            if (endRow == startRow && Math.abs(endCol - startCol) == 1) {
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);  // Set capture flag if capturing an opponent's piece
                    return true;
                }
            }

            // Pawn attempts to move sideways when the destination is empty
            if (endRow == startRow && Math.abs(endCol - startCol) == 1 && board.getPieceAt(endRow, endCol) == null) {
                return true;
            }
        }

        return false;  // Default case: the move is invalid
    }

    /**
     * Gets the first move status of the pawn.
     *
     * @return True if it's the pawn's first move, false otherwise.
     */
    public boolean isFirstMove() {
        return isFirstMove;
    }

    /**
     * Sets the first move status of the pawn.
     *
     * @param isFirstMove True if it's the pawn's first move, false otherwise.
     */
    public void setFirstMove(boolean isFirstMove) {
        this.isFirstMove = isFirstMove;
    }

    /**
     * Updates the direction of the pawn based on its color.
     * This is useful if the pawn's color changes, e.g., when it's promoted or in a special rule scenario.
     *
     * @param newColor The new color of the pawn.
     */
    public void updateDirection(Color newColor) {
        setColor(newColor);
        this.direction = (newColor == Color.WHITE) ? -1 : 1;  // Update direction based on new color
    }
}
