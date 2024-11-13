package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChessAITest {

    private ChessAI chessAI; // Instance of ChessAI to test
    private VariantChessBoard board; // Instance of VariantChessBoard for setting up test scenarios
    private CannonSpecialRule cannonSpecialRule; // Real instance of CannonSpecialRule for specific testing
    private PawnPromotionRule pawnPromotionRule; // Real instance of PawnPromotionRule for testing
    private KingQueenSpecialRule kingQueenSpecialRule; // Real instance of KingQueenSpecialRule for testing

    /**
     * Sets up the testing environment before each test.
     */
    @BeforeEach
    void setUp() {
        chessAI = new ChessAI(); // Initialize ChessAI instance
        board = new VariantChessBoard(); // Initialize VariantChessBoard instance
        cannonSpecialRule = new CannonSpecialRule(); // Real instance of CannonSpecialRule for testing
        pawnPromotionRule = new PawnPromotionRule(); // Real instance of PawnPromotionRule for testing
        kingQueenSpecialRule = new KingQueenSpecialRule(); // Real instance of KingQueenSpecialRule for testing
    }

    /**
     * Tests the evaluation of different piece values.
     */
    @Test
    void testEvaluatePieceValue() {
        // Testing the value of different pieces
        assertEquals(1, callEvaluatePieceValue(new Pawn(Color.WHITE), mock(GameRule.class))); // Pawn value
        assertEquals(3, callEvaluatePieceValue(new Knight(Color.WHITE), mock(GameRule.class))); // Knight value
        assertEquals(3, callEvaluatePieceValue(new Bishop(Color.WHITE), mock(GameRule.class))); // Bishop value
        assertEquals(6, callEvaluatePieceValue(new Rook(Color.WHITE), mock(GameRule.class))); // Rook value
        assertEquals(7, callEvaluatePieceValue(new Queen(Color.WHITE), mock(GameRule.class))); // Queen value
        assertEquals(4, callEvaluatePieceValue(new King(Color.WHITE), mock(GameRule.class))); // King value

        // Test Cannon value without special rule
        Cannon cannon = new Cannon(Color.WHITE);
        assertEquals(5, callEvaluatePieceValue(cannon, mock(GameRule.class))); // Cannon base value

        // Test Cannon value with special rule
        cannon.incrementCaptureCount(); // Increase capture count
        cannon.incrementCaptureCount(); // Increase capture count
        assertEquals(8, callEvaluatePieceValue(cannon, cannonSpecialRule)); // Base + bonus for capture count
    }

    /**
     * Tests the evaluation of Pawn piece values with PawnPromotionRule.
     */
    @Test
    void testEvaluatePawnWithPawnPromotionRule() {
        // Testing Pawn with different capture counts under PawnPromotionRule
        Pawn pawn = new Pawn(Color.WHITE);

        // No captures
        assertEquals(2, callEvaluatePieceValue(pawn, pawnPromotionRule)); // 1 base + 1 bonus

        // One capture
        pawn.incrementCaptureCount();
        assertEquals(4, callEvaluatePieceValue(pawn, pawnPromotionRule)); // 1 base + 3 bonus

        // Two captures
        pawn.incrementCaptureCount();
        assertEquals(5, callEvaluatePieceValue(pawn, pawnPromotionRule)); // 1 base + 4 bonus
    }

    /**
     * Tests the calculation of the best move under pressure.
     */
    @Test
    void testCalculateBestMoveUnderPressure() {
        // Setup a scenario where the AI must either defend or capture
        board.setPieceAt(0, 0, new King(Color.WHITE)); // Place White King on the board
        board.setPieceAt(1, 1, new Queen(Color.BLACK)); // Place Black Queen threatening the White King

        VariantChessMove bestMoveWhite = chessAI.calculateBestMove(board, Color.WHITE, mock(GameRule.class)); // Calculate best move for White

        // Ensure the best move is a defensive move or captures the threat
        assertNotNull(bestMoveWhite, "White should have a valid defensive move"); // Check if a move is found
        assertTrue((bestMoveWhite.getEndX() == 1 && bestMoveWhite.getEndY() == 1) ||
                (bestMoveWhite.getEndX() == 1 && bestMoveWhite.getEndY() == 0) ||
                (bestMoveWhite.getEndX() == 0 && bestMoveWhite.getEndY() == 1));
    }

    /**
     * Calls the private evaluatePieceValue method of ChessAI using reflection.
     *
     * @param piece The piece to evaluate.
     * @param rule  The game rule to use for evaluation.
     * @return The evaluated value of the piece.
     */
    private int callEvaluatePieceValue(VariantChessPiece piece, GameRule rule) {
        try {
            var method = ChessAI.class.getDeclaredMethod("evaluatePieceValue", VariantChessPiece.class, GameRule.class);
            method.setAccessible(true);
            return (int) method.invoke(chessAI, piece, rule); // Invoke the private method
        } catch (Exception e) {
            throw new RuntimeException(e); // Handle potential exceptions
        }
    }

}
