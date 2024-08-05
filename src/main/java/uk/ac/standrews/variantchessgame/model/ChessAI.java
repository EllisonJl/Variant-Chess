package uk.ac.standrews.variantchessgame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChessAI {

    private final Random random = new Random();

    /**
     * Evaluates the value of a piece based on its type and the current game rule.
     *
     * @param piece       The piece to evaluate.
     * @param currentRule The current rule of the game.
     * @return The evaluated value of the piece.
     */
    private int evaluatePieceValue(VariantChessPiece piece, GameRule currentRule) {
        if (piece == null) return 0;
        if (piece instanceof Pawn) return 1;
        if (piece instanceof Knight || piece instanceof Bishop) return 3;
        if (piece instanceof Rook) return 5;
        if (piece instanceof Queen) return 50;
        if (piece instanceof King) return 4;

        if (piece instanceof Cannon) {
            Cannon cannon = (Cannon) piece;
            int baseValue = 5; // Base value for a Cannon
            int bonusValue = 0;

            // Increase value if the Cannon can explode due to the special rule
            if (currentRule instanceof CannonSpecialRule && cannon.getCaptureCount() >= 3) {
                bonusValue = 10; // Additional value if it can explode
            }

            return baseValue + bonusValue;
        }
        return 0;
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
        int score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                VariantChessPiece piece = board.getPieceAt(i, j);
                if (piece != null) {
                    int pieceValue = evaluatePieceValue(piece, currentRule);
                    score += (piece.getColor() == aiColor) ? pieceValue : -pieceValue;
                }
            }
        }
        return score;
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
        List<VariantChessMove> allPossibleMoves = new ArrayList<>();
        List<VariantChessMove> bestMoves = new ArrayList<>();
        int bestScore = Integer.MIN_VALUE;

        // Iterate through all possible moves
        for (int startX = 0; startX < 8; startX++) {
            for (int startY = 0; startY < 8; startY++) {
                VariantChessPiece piece = board.getPieceAt(startX, startY);
                if (piece != null && piece.getColor() == color) {
                    for (int endX = 0; endX < 8; endX++) {
                        for (int endY = 0; endY < 8; endY++) {
                            VariantChessMove move = new VariantChessMove(startX, startY, endX, endY);
                            if (piece.isValidMove(move, board)) {
                                allPossibleMoves.add(move);

                                // Execute the move and evaluate the score
                                VariantChessPiece originalEndPiece = board.getPieceAt(endX, endY);
                                board.movePiece(move);
                                int score = evaluateBoard(board, color, currentRule);
                                board.movePiece(new VariantChessMove(endX, endY, startX, startY)); // Undo the move
                                board.setPieceAt(endX, endY, originalEndPiece);

                                // Choose the move with the highest score
                                if (score > bestScore) {
                                    bestScore = score;
                                    bestMoves.clear();
                                    bestMoves.add(move);
                                } else if (score == bestScore) {
                                    bestMoves.add(move);
                                }
                            }
                        }
                    }
                }
            }
        }

        // If no valid moves are found
        if (allPossibleMoves.isEmpty()) {
            return null;
        }

        // Randomly choose one of the highest-scoring moves
        return bestMoves.get(random.nextInt(bestMoves.size()));
    }
}
