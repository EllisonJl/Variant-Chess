package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for evaluating the piece values in ChessAI.
 * These tests specifically focus on Pawn and Queen piece evaluations under different game rules.
 */
class ChessAITest {

    private ChessAI chessAI; // Instance of ChessAI to test
<<<<<<< HEAD
    private VariantChessBoard board; // Instance of VariantChessBoard for setting up test scenarios
    private CannonSpecialRule cannonSpecialRuleMock; // Mock of CannonSpecialRule for specific testing
    private PawnPromotionRule pawnPromotionRuleMock; // Mock of PawnPromotionRule for testing
    private KingQueenSpecialRule kingQueenSpecialRuleMock; // Mock of KingQueenSpecialRule for testing
    private GameRule mockRule; // Mock of GameRule for general testing
=======
    private GameRule pawnPromotionRule; // Mock rule for Pawn promotion
    private GameRule kingQueenSpecialRule; // Mock rule for King and Queen special rules
>>>>>>> origin/Added-function-to-front-end

    @BeforeEach
    void setUp() {
        chessAI = new ChessAI(); // Initialize ChessAI instance
<<<<<<< HEAD
        board = new VariantChessBoard(); // Initialize VariantChessBoard instance
        cannonSpecialRuleMock = mock(CannonSpecialRule.class); // Mock CannonSpecialRule for testing
        pawnPromotionRuleMock = mock(PawnPromotionRule.class); // Mock PawnPromotionRule for testing
        kingQueenSpecialRuleMock = mock(KingQueenSpecialRule.class); // Mock KingQueenSpecialRule for testing
        mockRule = mock(GameRule.class); // Mock GameRule for testing
=======
        pawnPromotionRule = new PawnPromotionRule(); // Initialize PawnPromotionRule
        kingQueenSpecialRule = new KingQueenSpecialRule(); // Initialize KingQueenSpecialRule
>>>>>>> origin/Added-function-to-front-end
    }

    /**
     * Tests the evaluation of a Pawn's value with no captures.
     * Verifies that the Pawn's value is correct with a base value and bonus under the PawnPromotionRule.
     */
    @Test
    void testEvaluatePawnValueWithNoCaptures() {
        Pawn pawn = new Pawn(Color.WHITE);
        int expectedValue = 1 + 3; // Base value 1 + bonus 3
        int evaluatedValue = callEvaluatePieceValue(pawn, pawnPromotionRule);

        assertEquals(expectedValue, evaluatedValue, "Pawn value with no captures should be " + expectedValue);
    }

    /**
<<<<<<< HEAD
     * Tests the evaluation of Pawn piece values with PawnPromotionRule.
     */
    @Test
    void testEvaluatePawnWithPawnPromotionRule() {
        // Testing Pawn with different capture counts under PawnPromotionRule
        Pawn pawn = new Pawn(Color.WHITE);

        // No captures
        assertEquals(4, callEvaluatePieceValue(pawn, pawnPromotionRuleMock)); // 1 base + 3 bonus

        // One capture
        pawn.incrementCaptureCount();
        assertEquals(6, callEvaluatePieceValue(pawn, pawnPromotionRuleMock)); // 1 base + 5 bonus

        // Two captures
        pawn.incrementCaptureCount();
        assertEquals(11, callEvaluatePieceValue(pawn, pawnPromotionRuleMock)); // 1 base + 10 bonus
    }


    /**
     * Tests the calculation of the best move under pressure.
=======
     * Tests the evaluation of a Pawn's value with one capture.
     * Verifies that the Pawn's value is correct with a base value and bonus under the PawnPromotionRule.
>>>>>>> origin/Added-function-to-front-end
     */
    @Test
    void testEvaluatePawnValueWithOneCapture() {
        Pawn pawn = new Pawn(Color.WHITE);
        pawn.incrementCaptureCount(); // Simulate one capture
        int expectedValue = 1 + 5; // Base value 1 + bonus 5
        int evaluatedValue = callEvaluatePieceValue(pawn, pawnPromotionRule);

        assertEquals(expectedValue, evaluatedValue, "Pawn value with one capture should be " + expectedValue);
    }

    /**
     * Tests the evaluation of a Pawn's value with two captures.
     * Verifies that the Pawn's value is correct with a base value and bonus under the PawnPromotionRule.
     */
    @Test
    void testEvaluatePawnValueWithTwoCaptures() {
        Pawn pawn = new Pawn(Color.WHITE);
        pawn.incrementCaptureCount();
        pawn.incrementCaptureCount(); // Simulate two captures
        int expectedValue = 1 + 10; // Base value 1 + bonus 10
        int evaluatedValue = callEvaluatePieceValue(pawn, pawnPromotionRule);

        assertEquals(expectedValue, evaluatedValue, "Pawn value with two captures should be " + expectedValue);
    }

    /**
     * Helper method to access the private evaluatePieceValue method in ChessAI.
     *
     * @param piece The chess piece to evaluate.
     * @param rule The game rule to use for evaluation.
     * @return The evaluated piece value.
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
