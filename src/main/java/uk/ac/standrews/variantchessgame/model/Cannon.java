package uk.ac.standrews.variantchessgame.model;

public class Cannon extends VariantChessPiece {
    public Cannon(Color color) {
        super(color, "Cannon");
    }

    @Override
    public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {
        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();

        if (endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            return false;
        }

        if (startX != endX && startY != endY) {
            return false;
        }

        boolean isCapture = board.getPieceAt(endX, endY) != null;

        if (startX == endX) {
            int step = (endY > startY) ? 1 : -1;
            int y = startY + step;
            int piecesInBetween = 0;
            while (y != endY) {
                if (board.getPieceAt(startX, y) != null) {
                    piecesInBetween++;
                }
                y += step;
            }
            if (isCapture) {
                if (piecesInBetween == 1) {
                    move.setCapture(true);
                    return true;
                }
            } else {
                if (piecesInBetween == 0) {
                    return true;
                }
            }
        } else {
            int step = (endX > startX) ? 1 : -1;
            int x = startX + step;
            int piecesInBetween = 0;
            while (x != endX) {
                if (board.getPieceAt(x, startY) != null) {
                    piecesInBetween++;
                }
                x += step;
            }
            if (isCapture) {
                if (piecesInBetween == 1) {
                    move.setCapture(true);
                    return true;
                }
            } else {
                if (piecesInBetween == 0) {
                    return true;
                }
            }
        }

        return false;
    }
}
