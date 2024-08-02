package uk.ac.standrews.variantchessgame.model;

/**
 * Represents a Cannon piece in the variant chess game.
 * The Cannon moves horizontally or vertically and can capture other pieces in specific conditions.
 * It also has special rules for detonation after a certain number of captures.
 */
public class Cannon extends VariantChessPiece {

    /**
     * Constructs a Cannon with a specified color.
     *
     * @param color The color of the Cannon (either WHITE or BLACK).
     */
    public Cannon(Color color) {
        super(color, "Cannon");
    }

    /**
     * Constructs a Cannon with a specified color and promotion status.
     *
     * @param color The color of the Cannon (either WHITE or BLACK).
     * @param promotedFromPawn Indicates if the Cannon was promoted from a Pawn.
     */
    public Cannon(Color color, boolean promotedFromPawn) {
        super(color, "Cannon", promotedFromPawn);
    }

    /**
     * Determines if a move is valid for the Cannon.
     * The Cannon moves in straight lines and has special capture rules.
     *
     * @param move The move to be validated.
     * @param board The current state of the chess board.
     * @return True if the move is valid, false otherwise.
     */
    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {

        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();

        // Ensure the target position is within the board boundaries.
        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            return false;
        }

        // Ensure the move is to a different position.
        if (startX == endX && startY == endY) {
            return false;
        }

        // Cannon moves in straight lines only (either horizontally or vertically).
        if (startX != endX && startY != endY) {
            return false;
        }

        boolean isCapture = board.getPieceAt(endX, endY) != null;
        int piecesInBetween = 0;

        // Handle vertical movement.
        if (startX == endX) {
            int step = (endY > startY) ? 1 : -1;
            for (int y = startY + step; y != endY; y += step) {
                if (board.getPieceAt(startX, y) != null) {
                    piecesInBetween++;
                }
            }
        } else { // Handle horizontal movement.
            int step = (endX > startX) ? 1 : -1;
            for (int x = startX + step; x != endX; x += step) {
                if (board.getPieceAt(x, startY) != null) {
                    piecesInBetween++;
                }
            }
        }

        // Determine if the move is valid based on capture and obstructions.
        if (isCapture) {
            if (piecesInBetween == 1 && board.getPieceAt(endX, endY).getColor() != this.getColor()) {
                move.setCapture(true); // Mark as a capture move.
                return true;
            }
        } else {
            if (piecesInBetween == 0) {
                return true; // Valid move with no obstructions.
            }
        }

        return false;
    }

    /**
     * Increments the capture count for the Cannon.
     * If the capture count reaches 3, the Cannon becomes immobile until it detonates.
     */
    @Override
    public void incrementCaptureCount() {
        super.incrementCaptureCount();
        if (getCaptureCount() == 3) {
            setImmobile(true);
        }
    }

    /**
     * Detonates the Cannon, removing it and any enemy pieces in the surrounding positions.
     * The Cannon is removed from the board after detonation.
     *
     * @param board The current state of the chess board.
     * @param x The x-coordinate of the Cannon's position.
     * @param y The y-coordinate of the Cannon's position.
     */
    public void detonate(VariantChessBoard board, int x, int y) {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        System.out.println("Detonating cannon at position: (" + x + ", " + y + ")");
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            if (board.isInBounds(newX, newY)) {
                VariantChessPiece piece = board.getPieceAt(newX, newY);
                if (piece != null && piece.getColor() != this.getColor()) {
                    System.out.println("Removing piece at position: (" + newX + ", " + newY + ")");
                    board.setPieceAt(newX, newY, null); // Remove enemy pieces.
                }
            }
        }
        System.out.println("Removing cannon at position: (" + x + ", " + y + ")");
        board.setPieceAt(x, y, null); // Remove the Cannon itself.
    }

}
