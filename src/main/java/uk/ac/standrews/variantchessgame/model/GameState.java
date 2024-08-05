package uk.ac.standrews.variantchessgame.model;

import java.util.Random;

/**
 * Represents the current state of the chess game.
 * This includes tracking the number of moves made by each player,
 * the number of moves without captures, the current turn, and the
 * game rules applicable to the current game.
 */
public class GameState {

    private int whiteMoveCount;          // Counter for the number of moves made by the white player.
    private int blackMoveCount;          // Counter for the number of moves made by the black player.
    public int movesWithoutCapture;     // Counter for the number of moves made without capturing an opponent's piece.
    private Color currentTurn;           // The color of the player whose turn it is.

    public int getWhiteMoveCount() {
        return whiteMoveCount;
    }

    public int getBlackMoveCount() {
        return blackMoveCount;
    }

    private final VariantChessBoard board; // The chess board representing the current game state.
    private GameRule selectedRule;       // The game rule currently in effect for special game conditions.

    /**
     * Constructs a new GameState object with the specified chess board.
     * Initializes the game state with the current turn set to WHITE and
     * selects a random game rule to be applied.
     *
     * @param board The chess board representing the initial state of the game.
     */
    public GameState(VariantChessBoard board) {
        this.board = board;
        this.currentTurn = Color.WHITE;
        this.whiteMoveCount = 0;
        this.blackMoveCount = 0;
        this.movesWithoutCapture = 0;
        selectRandomRule(); // Select a random game rule at game initialization.
    }

    /**
     * Gets the color of the player whose turn it is.
     *
     * @return The color of the current player's turn.
     */
    public Color getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Switches the turn to the other player.
     * Changes the current turn from WHITE to BLACK or vice versa.
     */
    public void switchTurn() {
        currentTurn = (currentTurn == Color.WHITE) ? Color.BLACK : Color.WHITE;
        System.out.println("回合切换到：" + currentTurn);
    }

    /**
     * Increments the move count for the player whose turn it is.
     * This method updates the move count for either WHITE or BLACK player
     * based on the current turn.
     */
    public void incrementMoveCount() {
        if (currentTurn == Color.WHITE) {
            whiteMoveCount++;
        } else {
            blackMoveCount++;
        }
    }

    /**
     * Increments the counter for moves made without capturing an opponent's piece.
     */
    public void incrementMoveWithoutCapture() {
        movesWithoutCapture++;
    }

    /**
     * Resets the counter for moves made without capturing an opponent's piece.
     * This is typically called after a capture move.
     */
    public void resetMoveWithoutCapture() {
        movesWithoutCapture = 0;
    }

    /**
     * Determines if the game has been won.
     * A win occurs if all pieces of one color have been captured.
     *
     * @return True if the game is won by one player, otherwise false.
     */
    public boolean isWin() {
        int whiteCount = 0;
        int blackCount = 0;

        // Count the number of pieces for each color.
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

        // Win condition: One color has no pieces left on the board.
        return whiteCount == 0 || blackCount == 0;
    }

    /**
     * Determines if the game is a draw.
     * The game is a draw if:
     * 1. There have been 60 or more moves without a capture.
     * 2. Both players have exactly one piece left, and each has a King.
     *
     * @return True if the game is a draw, otherwise false.
     */
    public boolean isDraw() {
        // Draw condition: 60 or more moves without capture.
        if (movesWithoutCapture >= 60) {
            return true;
        }

        int whiteCount = 0;
        int blackCount = 0;
        boolean whiteHasKing = false;
        boolean blackHasKing = false;

        // Count the number of pieces for each color and check for Kings.
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

        // Draw condition: Each player has exactly one piece and each has a King.
        return whiteCount == 1 && blackCount == 1 && whiteHasKing && blackHasKing;
    }

    /**
     * Gets the currently selected game rule.
     *
     * @return The currently selected game rule.
     */
    public GameRule getSelectedRule() {
        return selectedRule;
    }

    /**
     * Selects a random game rule from available rules.
     * This method initializes the `selectedRule` field with a randomly chosen rule.
     * The rules available are CannonSpecialRule, KingQueenSpecialRule, and PawnPromotionRule.
     */
    private void selectRandomRule() {
        Random random = new Random();
        int ruleIndex = random.nextInt(3); // Randomly select an index between 0 and 2.
        switch (ruleIndex) {
            case 0:
                this.selectedRule = new CannonSpecialRule(); // Assign CannonSpecialRule if index is 0.
                break;
            case 1:
                this.selectedRule = new KingQueenSpecialRule(); // Assign KingQueenSpecialRule if index is 1.
                break;
            case 2:
                this.selectedRule = new PawnPromotionRule(); // Assign PawnPromotionRule if index is 2.
                break;
            default:
                this.selectedRule = null; // Default case; should never occur.
                break;
        }
    }


}
