package uk.ac.standrews.variantchessgame.model;

import uk.ac.standrews.variantchessgame.model.*;

import java.util.List;
import java.util.Random;

public class ChessAI {
    private final Random random = new Random();

    public VariantChessMove calculateBestMove(VariantChessBoard board, Color aiColor) {
        List<VariantChessMove> allPossibleMoves = board.getAllPossibleMoves(aiColor);

        if (allPossibleMoves.isEmpty()) {
            return null; // No possible moves
        }

        // Randomly select a move (you can implement a better evaluation function here)
        return allPossibleMoves.get(random.nextInt(allPossibleMoves.size()));
    }
}
