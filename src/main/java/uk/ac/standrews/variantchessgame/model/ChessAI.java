package uk.ac.standrews.variantchessgame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChessAI {

    private final Random random = new Random(); // Random object used for selecting among equally good moves

    /**
     * Evaluates the value of a piece based on its type and the current game rule.
     *
     * @param piece       The piece to evaluate.
     * @param currentRule The current rule of the game.
     * @return The evaluated value of the piece.
     */
    private int evaluatePieceValue(VariantChessPiece piece, GameRule currentRule) {
        if (piece == null) return 0; // If the piece is null, its value is 0
        if (piece instanceof Pawn) return 1; // Pawn is valued at 1
        if (piece instanceof Knight || piece instanceof Bishop) return 3; // Knight and Bishop are valued at 3
        if (piece instanceof Rook) return 5; // Rook is valued at 5
        if (piece instanceof Queen) return 50; // Queen is valued at 50
        if (piece instanceof King) return 4; // King is valued at 4

        if (piece instanceof Cannon) {
            Cannon cannon = (Cannon) piece;
            int baseValue = 5; // Base value for a Cannon
            int bonusValue = 0;

            // Increase value if the Cannon can explode due to the special rule
            if (currentRule instanceof CannonSpecialRule && cannon.getCaptureCount() >= 3) {
                bonusValue = 10; // Additional value if it can explode
            }

            return baseValue + bonusValue; // Return the total evaluated value for the Cannon
        }
        return 0; // Default case if the piece is not recognized
    }

    /**
     * Evaluates the board state for the AI's perspective.
     *
     * @param board      The current state of the chessboard.
     * @param aiColor    The color of the AI player.
     * @param currentRule The current rule of the game.
     * @return The evaluated score of the board.
     */
    private int evaluateBoard(VariantChessBoard board, Color aiColor, GameRule currentRule) {
        int score = 0; // Initialize the board score
        // Iterate over all positions on the board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                VariantChessPiece piece = board.getPieceAt(i, j); // Get the piece at the current position
                if (piece != null) {
                    int pieceValue = evaluatePieceValue(piece, currentRule); // Evaluate the piece's value
                    // Adjust the score based on the piece's color
                    score += (piece.getColor() == aiColor) ? pieceValue : -pieceValue;
                }
            }
        }
        return score; // Return the final score for the board state
    }

    /**
     * Finds the best move for the AI player given the current board state and game rule.
     *
     * @param board      The current state of the chessboard.
     * @param color      The color of the AI player.
     * @param currentRule The current rule of the game.
     * @return The best move found by the AI.
     */
    public VariantChessMove calculateBestMove(VariantChessBoard board, Color color, GameRule currentRule) {
        List<VariantChessMove> allPossibleMoves = new ArrayList<>(); // List to store all possible moves
        List<VariantChessMove> bestMoves = new ArrayList<>(); // List to store the best moves with the highest score
        int bestScore = Integer.MIN_VALUE; // Initialize the best score to the lowest possible value

        // Iterate through all possible moves on the board
        for (int startX = 0; startX < 8; startX++) {
            for (int startY = 0; startY < 8; startY++) {
                VariantChessPiece piece = board.getPieceAt(startX, startY); // Get the piece at the starting position
                if (piece != null && piece.getColor() == color) { // Check if the piece belongs to the AI
                    for (int endX = 0; endX < 8; endX++) {
                        for (int endY = 0; endY < 8; endY++) {
                            VariantChessMove move = new VariantChessMove(startX, startY, endX, endY); // Create a move object
                            if (piece.isValidMove(move, board)) { // Check if the move is valid
                                allPossibleMoves.add(move); // Add the move to the list of possible moves

                                // Execute the move and evaluate the score
                                VariantChessPiece originalEndPiece = board.getPieceAt(endX, endY); // Save the original piece at the end position
                                board.movePiece(move); // Execute the move
                                int score = evaluateBoard(board, color, currentRule); // Evaluate the new board state
                                board.movePiece(new VariantChessMove(endX, endY, startX, startY)); // Undo the move
                                board.setPieceAt(endX, endY, originalEndPiece); // Restore the original piece at the end position

                                // Choose the move with the highest score
                                if (score > bestScore) {
                                    bestScore = score; // Update the best score
                                    bestMoves.clear(); // Clear the list of best moves
                                    bestMoves.add(move); // Add the new best move
                                } else if (score == bestScore) {
                                    bestMoves.add(move); // Add the move to the list of best moves if it has the same score
                                }
                            }
                        }
                    }
                }
            }
        }

        // If no valid moves are found
        if (allPossibleMoves.isEmpty()) {
            return null; // Return null if there are no possible moves
        }

        // Randomly choose one of the highest-scoring moves
        return bestMoves.get(random.nextInt(bestMoves.size())); // Select a random move from the best moves
    }
}
