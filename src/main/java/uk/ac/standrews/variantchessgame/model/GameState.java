package uk.ac.standrews.variantchessgame.model;

import java.util.Random;

public class GameState {
    private int whiteMoveCount;
    private int blackMoveCount;
    private int movesWithoutCapture;
    private Color currentTurn;
    private final VariantChessBoard board;
    private GameRule selectedRule;

    public GameState(VariantChessBoard board) {
        this.board = board;
        this.currentTurn = Color.WHITE;
        this.whiteMoveCount = 0;
        this.blackMoveCount = 0;
        this.movesWithoutCapture = 0;
        this.selectedRule = selectRandomRule();
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }

    public void switchTurn() {
        currentTurn = (currentTurn == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    public void incrementMoveCount() {
        if (currentTurn == Color.WHITE) {
            whiteMoveCount++;
        } else {
            blackMoveCount++;
        }
    }

    public void incrementMoveWithoutCapture() {
        movesWithoutCapture++;
    }

    public void resetMoveWithoutCapture() {
        movesWithoutCapture = 0;
    }

    public boolean isWin() {
        int whiteCount = 0;
        int blackCount = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                VariantChessPiece piece = board.getPieceAt(i, j);
                if (piece != null) {
                    if (piece.getColor() == Color.WHITE) {
                        whiteCount++;
                    } else {
                        blackCount++;
                    }
                }
            }
        }
        return whiteCount == 0 || blackCount == 0;
    }

    public boolean isDraw() {
        if (movesWithoutCapture >= 60) {
            return true;
        }

        int whiteCount = 0;
        int blackCount = 0;
        boolean whiteHasKing = false;
        boolean blackHasKing = false;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                VariantChessPiece piece = board.getPieceAt(i, j);
                if (piece != null) {
                    if (piece.getColor() == Color.WHITE) {
                        whiteCount++;
                        if (piece instanceof King) {
                            whiteHasKing = true;
                        }
                    } else {
                        blackCount++;
                        if (piece instanceof King) {
                            blackHasKing = true;
                        }
                    }
                }
            }
        }

        if (whiteCount == 1 && blackCount == 1 && whiteHasKing && blackHasKing) {
            return true;
        }

        return false;
    }

    public GameRule getSelectedRule() {
        return selectedRule;
    }

    private GameRule selectRandomRule() {
        Random random = new Random();
        int ruleIndex = random.nextInt(3);
        switch (ruleIndex) {
            case 0:
                return new CannonSpecialRule();
            case 1:
                return new KingQueenSpecialRule();
            case 2:
                return new PawnPromotionRule();
            default:
                return null; // This should never happen
        }
    }
}
