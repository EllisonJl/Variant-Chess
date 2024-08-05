package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameStateTest {

    private VariantChessBoard board;
    private GameState gameState;

    @BeforeEach
    void setUp() {
        // Mocking the VariantChessBoard to isolate the GameState class for testing
        board = Mockito.mock(VariantChessBoard.class);
        gameState = new GameState(board);
    }

    /**
     * Tests the initial turn setup.
     * Verifies that the game starts with WHITE's turn.
     */
    @Test
    void testInitialTurn() {
        assertEquals(Color.WHITE, gameState.getCurrentTurn(), "Initial turn should be WHITE.");
    }

    /**
     * Tests the switch turn functionality.
     * Verifies that turns correctly switch from WHITE to BLACK and vice versa.
     */
    @Test
    void testSwitchTurn() {
        // Switch from WHITE to BLACK
        gameState.switchTurn();
        assertEquals(Color.BLACK, gameState.getCurrentTurn(), "Turn should switch to BLACK.");

        // Switch back to WHITE
        gameState.switchTurn();
        assertEquals(Color.WHITE, gameState.getCurrentTurn(), "Turn should switch back to WHITE.");
    }

    /**
     * Tests the incrementMoveCount method.
     * Verifies that the move count for the current player is correctly incremented.
     */
    @Test
    void testIncrementMoveCount() {
        // Initial move counts should be zero
        assertEquals(0, gameState.getWhiteMoveCount(), "Initial white move count should be 0.");
        assertEquals(0, gameState.getBlackMoveCount(), "Initial black move count should be 0.");

        // Increment for WHITE turn
        gameState.incrementMoveCount();
        assertEquals(1, gameState.getWhiteMoveCount(), "White move count should increment by 1.");
        assertEquals(0, gameState.getBlackMoveCount(), "Black move count should remain unchanged.");

        // Switch to BLACK and increment
        gameState.switchTurn();
        gameState.incrementMoveCount();
        assertEquals(1, gameState.getWhiteMoveCount(), "White move count should remain unchanged.");
        assertEquals(1, gameState.getBlackMoveCount(), "Black move count should increment by 1.");
    }

    /**
     * Tests the incrementMoveWithoutCapture method.
     * Verifies that the counter for moves without capture increments correctly.
     */
    @Test
    void testIncrementMovesWithoutCapture() {
        gameState.incrementMoveWithoutCapture();
        assertEquals(1, gameState.movesWithoutCapture, "Moves without capture should be 1 after incrementing once.");

        gameState.incrementMoveWithoutCapture();
        assertEquals(2, gameState.movesWithoutCapture, "Moves without capture should be 2 after incrementing twice.");
    }

    /**
     * Tests the resetMoveWithoutCapture method.
     * Verifies that the counter for moves without capture resets to zero.
     */
    @Test
    void testResetMovesWithoutCapture() {
        gameState.incrementMoveWithoutCapture();
        gameState.incrementMoveWithoutCapture();
        gameState.resetMoveWithoutCapture();
        assertEquals(0, gameState.movesWithoutCapture, "Moves without capture should reset to 0.");
    }

    /**
     * Tests the win condition.
     * Verifies that the game is won when all pieces of one color are captured.
     */
    @Test
    void testWinCondition() {
        // Mock board to simulate all black pieces captured
        when(board.getPieceAt(anyInt(), anyInt())).thenReturn(null);

        // Place a white King to represent all other pieces being captured
        when(board.getPieceAt(anyInt(), anyInt())).thenAnswer(invocation -> {
            int x = invocation.getArgument(0);
            int y = invocation.getArgument(1);
            return (x == 0 && y == 0) ? new King(Color.WHITE) : null;
        });
        assertTrue(gameState.isWin(), "Game should be won if all black pieces are captured.");

        // Simulate all white pieces captured
        when(board.getPieceAt(anyInt(), anyInt())).thenAnswer(invocation -> {
            int x = invocation.getArgument(0);
            int y = invocation.getArgument(1);
            return (x == 0 && y == 0) ? new King(Color.BLACK) : null;
        });
        assertTrue(gameState.isWin(), "Game should be won if all white pieces are captured.");
    }

    /**
     * Tests the draw condition by the number of moves without capture.
     * Verifies that the game is a draw after 60 moves without any captures.
     */
    @Test
    void testDrawConditionByMoves() {
        // Set the movesWithoutCapture to 60
        for (int i = 0; i < 60; i++) {
            gameState.incrementMoveWithoutCapture();
        }
        assertTrue(gameState.isDraw(), "Game should be a draw after 60 moves without a capture.");
    }

    /**
     * Tests the draw condition by the presence of only Kings.
     * Verifies that the game is a draw if each player has only one King left.
     */
    @Test
    void testDrawConditionByKings() {
        // Mock board to have only one King of each color
        when(board.getPieceAt(anyInt(), anyInt())).thenAnswer(invocation -> {
            int x = invocation.getArgument(0);
            int y = invocation.getArgument(1);
            if (x == 0 && y == 0) return new King(Color.WHITE);
            if (x == 7 && y == 7) return new King(Color.BLACK);
            return null;
        });

        assertTrue(gameState.isDraw(), "Game should be a draw if each player has only one King.");
    }

    /**
     * Tests the random rule selection during game initialization.
     * Verifies that a game rule is selected and is one of the known rules.
     */
    @Test
    void testRandomRuleSelection() {
        GameRule rule = gameState.getSelectedRule();
        assertNotNull(rule, "A game rule should be selected at initialization.");
        assertTrue(rule instanceof CannonSpecialRule || rule instanceof KingQueenSpecialRule || rule instanceof PawnPromotionRule,
                "Selected rule should be one of the known rules.");
    }

    /**
     * Tests the win condition with a mix of pieces.
     * Verifies that the game correctly identifies a win when all pieces of a color are eliminated.
     */
    @Test
    void testWinConditionWithMixedPieces() {
        // Mock board with a mix of pieces, but all black pieces will be removed
        when(board.getPieceAt(anyInt(), anyInt())).thenAnswer(invocation -> {
            int x = invocation.getArgument(0);
            int y = invocation.getArgument(1);
            return (x == 0 && y == 0) ? new King(Color.WHITE) : null;
        });

        assertTrue(gameState.isWin(), "Game should be won if all black pieces are captured with mixed pieces.");
    }

    /**
     * Tests the draw condition when both sides have non-king pieces.
     * Ensures that the game is not a draw when both sides have more than just Kings.
     */
    @Test
    void testNonDrawWithNonKingPieces() {
        // Mock board to have more than just Kings on both sides
        when(board.getPieceAt(anyInt(), anyInt())).thenAnswer(invocation -> {
            int x = invocation.getArgument(0);
            int y = invocation.getArgument(1);
            if (x == 0 && y == 0) return new King(Color.WHITE);
            if (x == 7 && y == 7) return new King(Color.BLACK);
            if (x == 0 && y == 1) return new Pawn(Color.WHITE);
            if (x == 7 && y == 6) return new Pawn(Color.BLACK);
            return null;
        });

        assertFalse(gameState.isDraw(), "Game should not be a draw if both sides have more than just Kings.");
    }

    /**
     * Tests the scenario where the game is not won due to pieces being present.
     * Verifies that the game does not declare a win if both sides still have pieces.
     */
    @Test
    void testNonWinWhenPiecesPresent() {
        // Mock board with pieces present on both sides
        when(board.getPieceAt(anyInt(), anyInt())).thenAnswer(invocation -> {
            int x = invocation.getArgument(0);
            int y = invocation.getArgument(1);
            if (x == 0 && y == 0) return new King(Color.WHITE);
            if (x == 7 && y == 7) return new King(Color.BLACK);
            if (x == 0 && y == 1) return new Rook(Color.WHITE);
            return null;
        });

        assertFalse(gameState.isWin(), "Game should not be won if both sides have pieces.");
    }

    /**
     * Tests multiple turn switches.
     * Verifies that the turn switching logic works consistently over several switches.
     */
    @Test
    void testMultipleTurnSwitches() {
        // Initial turn is WHITE
        assertEquals(Color.WHITE, gameState.getCurrentTurn(), "Initial turn should be WHITE.");

        // Switch to BLACK
        gameState.switchTurn();
        assertEquals(Color.BLACK, gameState.getCurrentTurn(), "Turn should switch to BLACK.");

        // Switch back to WHITE
        gameState.switchTurn();
        assertEquals(Color.WHITE, gameState.getCurrentTurn(), "Turn should switch back to WHITE.");

        // Switch again to BLACK
        gameState.switchTurn();
        assertEquals(Color.BLACK, gameState.getCurrentTurn(), "Turn should switch to BLACK again.");
    }
}
