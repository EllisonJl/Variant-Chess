package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChessAITest {

    private ChessAI chessAI;
    private VariantChessBoard board;
    private CannonSpecialRule cannonSpecialRuleMock;
    private GameRule mockRule;

    @BeforeEach
    void setUp() {
        chessAI = new ChessAI();
        board = new VariantChessBoard();
        cannonSpecialRuleMock = mock(CannonSpecialRule.class); // Mocking the CannonSpecialRule class
        mockRule = mock(GameRule.class); // Mocking the GameRule interface for specific tests
    }

    @Test
    void testEvaluatePieceValue() {
        // Testing different piece values
        assertEquals(1, callEvaluatePieceValue(new Pawn(Color.WHITE), mockRule));
        assertEquals(3, callEvaluatePieceValue(new Knight(Color.WHITE), mockRule));
        assertEquals(3, callEvaluatePieceValue(new Bishop(Color.WHITE), mockRule));
        assertEquals(5, callEvaluatePieceValue(new Rook(Color.WHITE), mockRule));
        assertEquals(50, callEvaluatePieceValue(new Queen(Color.WHITE), mockRule));
        assertEquals(4, callEvaluatePieceValue(new King(Color.WHITE), mockRule));

        // Test Cannon value without capture
        Cannon cannon = new Cannon(Color.WHITE);
        assertEquals(5, callEvaluatePieceValue(cannon, mockRule));

        // Test Cannon value with special rule
        cannon.incrementCaptureCount();
        cannon.incrementCaptureCount();
        cannon.incrementCaptureCount();
        assertEquals(15, callEvaluatePieceValue(cannon, cannonSpecialRuleMock)); // Base + explosion bonus
    }


    @Test
    void testCalculateBestMoveUnderPressure() {
        // Setup scenario where AI must defend or capture
        board.setPieceAt(0, 0, new King(Color.WHITE));
        board.setPieceAt(1, 1, new Queen(Color.BLACK)); // Threatening the white King

        VariantChessMove bestMoveWhite = chessAI.calculateBestMove(board, Color.WHITE, mockRule);

        // Expect the best move to move the King to safety or capture the threat
        assertNotNull(bestMoveWhite, "White should have a valid defensive move");
        assertTrue((bestMoveWhite.getEndX() == 1 && bestMoveWhite.getEndY() == 1) ||
                (bestMoveWhite.getEndX() == 1 && bestMoveWhite.getEndY() == 0) ||
                (bestMoveWhite.getEndX() == 0 && bestMoveWhite.getEndY() == 1));
    }

    private int callEvaluatePieceValue(VariantChessPiece piece, GameRule rule) {
        try {
            var method = ChessAI.class.getDeclaredMethod("evaluatePieceValue", VariantChessPiece.class, GameRule.class);
            method.setAccessible(true);
            return (int) method.invoke(chessAI, piece, rule);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int callEvaluateBoard(VariantChessBoard board, Color aiColor, GameRule rule) {
        try {
            var method = ChessAI.class.getDeclaredMethod("evaluateBoard", VariantChessBoard.class, Color.class, GameRule.class);
            method.setAccessible(true);
            return (int) method.invoke(chessAI, board, aiColor, rule);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
