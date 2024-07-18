package uk.ac.standrews.variantchessgame.model;

public class Cannon extends VariantChessPiece {
    public Cannon(Color color) {
        super(color, "Cannon");
    }

    public Cannon(Color color, boolean promotedFromPawn) {
        super(color, "Cannon", promotedFromPawn);
    }

    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {
        if (isImmobile()) return false;  // Check if the piece is immobile

        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();

        // Ensure target position is within the board
        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            return false;
        }

        // Ensure the start and end positions are different
        if (startX == endX && startY == endY) {
            return false;
        }

        // Ensure the move is straight
        if (startX != endX && startY != endY) {
            return false;
        }

        boolean isCapture = board.getPieceAt(endX, endY) != null;
        int piecesInBetween = 0;

        // Handle vertical movement
        if (startX == endX) {
            int step = (endY > startY) ? 1 : -1;
            for (int y = startY + step; y != endY; y += step) {
                if (board.getPieceAt(startX, y) != null) {
                    piecesInBetween++;
                }
            }
        } else { // Handle horizontal movement
            int step = (endX > startX) ? 1 : -1;
            for (int x = startX + step; x != endX; x += step) {
                if (board.getPieceAt(x, startY) != null) {
                    piecesInBetween++;
                }
            }
        }

        // Determine if the move is valid
        if (isCapture) {
            if (piecesInBetween == 1 && board.getPieceAt(endX, endY).getColor() != this.getColor()) {
                move.setCapture(true);
                return true;
            }
        } else {
            if (piecesInBetween == 0) {
                return true;
            }
        }

        return false;
    }
}
