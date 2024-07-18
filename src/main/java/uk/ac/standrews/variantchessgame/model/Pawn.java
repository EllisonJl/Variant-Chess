package uk.ac.standrews.variantchessgame.model;

public class Pawn extends VariantChessPiece {
    private boolean isFirstMove;
    private int direction; // Add direction as a property

    public Pawn(Color color) {
        super(color, "Pawn");
        this.isFirstMove = true;
        this.direction = (color == Color.WHITE) ? -1 : 1; // Set direction based on initial color
    }

    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {
        if (isImmobile()) return false;  // Check if the piece is immobile

        int startRow = move.getStartX();
        int startCol = move.getStartY();
        int endRow = move.getEndX();
        int endCol = move.getEndY();

        if (endRow < 0 || endRow >= 8 || endCol < 0 || endCol >= 8) {
            return false;
        }

        if (isFirstMove) {
            if (startCol == endCol) {
                if ((endRow == startRow + direction || endRow == startRow + 2 * direction) && board.getPieceAt(endRow, endCol) == null) {
                    if (endRow == startRow + 2 * direction && board.getPieceAt(startRow + direction, startCol) != null) {
                        return false;
                    }
                    isFirstMove = false;
                    return true;
                }
            }

            if (endRow == startRow + direction && startCol == endCol) {
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);
                    return true;
                }
            }

            if (endRow == startRow + 2 * direction && startCol == endCol) {
                VariantChessPiece middlePiece = board.getPieceAt(startRow + direction, startCol);
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (middlePiece == null && targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);
                    return true;
                }
            }

            if (endRow == startRow && Math.abs(endCol - startCol) == 1) {
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);
                    return true;
                }
            }
        } else {
            if (startCol == endCol && endRow == startRow + direction && board.getPieceAt(endRow, endCol) == null) {
                return true;
            }

            if (endRow == startRow + direction && startCol == endCol) {
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);
                    return true;
                }
            }

            if (endRow == startRow && Math.abs(endCol - startCol) == 1) {
                VariantChessPiece targetPiece = board.getPieceAt(endRow, endCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    move.setCapture(true);
                    return true;
                }
            }

            if (endRow == startRow && Math.abs(endCol - startCol) == 1 && board.getPieceAt(endRow, endCol) == null) {
                return true;
            }
        }

        return false;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public void setFirstMove(boolean isFirstMove) {
        this.isFirstMove = isFirstMove;
    }

    public void updateDirection(Color newColor) {
        setColor(newColor);
        this.direction = (newColor == Color.WHITE) ? -1 : 1; // Update direction based on new color
    }
}
