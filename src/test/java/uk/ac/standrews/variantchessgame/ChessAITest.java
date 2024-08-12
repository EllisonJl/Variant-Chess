//package uk.ac.standrews.variantchessgame;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import uk.ac.standrews.variantchessgame.model.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class ChessAITest {
//
//    private ChessAI chessAI; // Instance of ChessAI to test
//    private VariantChessBoard board; // Instance of VariantChessBoard for setting up test scenarios
//    private CannonSpecialRule cannonSpecialRuleMock; // Mock of CannonSpecialRule for specific testing
//    private PawnPromotionRule pawnPromotionRuleMock; // Mock of PawnPromotionRule for testing
//    private KingQueenSpecialRule kingQueenSpecialRuleMock; // Mock of KingQueenSpecialRule for testing
//    private GameRule mockRule; // Mock of GameRule for general testing
//
//    /**
//     * Sets up the testing environment before each test.
//     */
//    @BeforeEach
//    void setUp() {
//        chessAI = new ChessAI(); // Initialize ChessAI instance
//        board = new VariantChessBoard(); // Initialize VariantChessBoard instance
//        cannonSpecialRuleMock = mock(CannonSpecialRule.class); // Mock CannonSpecialRule for testing
//        pawnPromotionRuleMock = mock(PawnPromotionRule.class); // Mock PawnPromotionRule for testing
//        kingQueenSpecialRuleMock = mock(KingQueenSpecialRule.class); // Mock KingQueenSpecialRule for testing
//        mockRule = mock(GameRule.class); // Mock GameRule for testing
//    }
//
//    /**
//     * Tests the evaluation of different piece values.
//     */
//    @Test
//    void testEvaluatePieceValue() {
//        // Testing the value of different pieces
//        assertEquals(1, callEvaluatePieceValue(new Pawn(Color.WHITE), mockRule)); // Pawn value
//        assertEquals(3, callEvaluatePieceValue(new Knight(Color.WHITE), mockRule)); // Knight value
//        assertEquals(3, callEvaluatePieceValue(new Bishop(Color.WHITE), mockRule)); // Bishop value
//        assertEquals(5, callEvaluatePieceValue(new Rook(Color.WHITE), mockRule)); // Rook value
//        assertEquals(50, callEvaluatePieceValue(new Queen(Color.WHITE), mockRule)); // Queen value
//        assertEquals(4, callEvaluatePieceValue(new King(Color.WHITE), mockRule)); // King value
//
//        // Test Cannon value without capture
//        Cannon cannon = new Cannon(Color.WHITE);
//        assertEquals(5, callEvaluatePieceValue(cannon, mockRule)); // Cannon base value
//
//        // Test Cannon value with special rule
//        cannon.incrementCaptureCount(); // Increase capture count
//        cannon.incrementCaptureCount(); // Increase capture count
//        cannon.incrementCaptureCount(); // Increase capture count
//        assertEquals(15, callEvaluatePieceValue(cannon, cannonSpecialRuleMock)); // Base + bonus for explosion
//    }
//
//    /**
//     * Tests the evaluation of Pawn piece values with PawnPromotionRule.
//     */
//    @Test
//    void testEvaluatePawnWithPawnPromotionRule() {
//        // Testing Pawn with different capture counts under PawnPromotionRule
//        Pawn pawn = new Pawn(Color.WHITE);
//
//        // No captures
//        assertEquals(4, callEvaluatePieceValue(pawn, pawnPromotionRuleMock)); // 1 base + 3 bonus
//
//        // One capture
//        pawn.incrementCaptureCount();
//        assertEquals(6, callEvaluatePieceValue(pawn, pawnPromotionRuleMock)); // 1 base + 5 bonus
//
//        // Two captures
//        pawn.incrementCaptureCount();
//        assertEquals(11, callEvaluatePieceValue(pawn, pawnPromotionRuleMock)); // 1 base + 10 bonus
//    }
//
//
//    /**
//     * Tests the calculation of the best move under pressure.
//     */
//    @Test
//    void testCalculateBestMoveUnderPressure() {
//        // Setup a scenario where the AI must either defend or capture
//        board.setPieceAt(0, 0, new King(Color.WHITE)); // Place White King on the board
//        board.setPieceAt(1, 1, new Queen(Color.BLACK)); // Place Black Queen threatening the White King
//
//        VariantChessMove bestMoveWhite = chessAI.calculateBestMove(board, Color.WHITE, mockRule); // Calculate best move for White
//
//        // Ensure the best move is a defensive move or captures the threat
//        assertNotNull(bestMoveWhite, "White should have a valid defensive move"); // Check if a move is found
//        assertTrue((bestMoveWhite.getEndX() == 1 && bestMoveWhite.getEndY() == 1) ||
//                (bestMoveWhite.getEndX() == 1 && bestMoveWhite.getEndY() == 0) ||
//                (bestMoveWhite.getEndX() == 0 && bestMoveWhite.getEndY() == 1));
//    }
//
//    /**
//     * Calls the private evaluatePieceValue method of ChessAI using reflection.
//     *
//     * @param piece The piece to evaluate.
//     * @param rule  The game rule to use for evaluation.
//     * @return The evaluated value of the piece.
//     */
//    private int callEvaluatePieceValue(VariantChessPiece piece, GameRule rule) {
//        try {
//            var method = ChessAI.class.getDeclaredMethod("evaluatePieceValue", VariantChessPiece.class, GameRule.class);
//            method.setAccessible(true);
//            return (int) method.invoke(chessAI, piece, rule); // Invoke the private method
//        } catch (Exception e) {
//            throw new RuntimeException(e); // Handle potential exceptions
//        }
//    }
//
//}
