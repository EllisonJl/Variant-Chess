package uk.ac.standrews.variantchessgame;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

/**
 * Unit tests for the CannonSpecialRule class, which defines the special rules for
 * the Cannon piece in variant chess. The Cannon detonates and removes surrounding
 * enemy pieces when it has captured three pieces.
 */
class CannonSpecialRuleTest {

    private CannonSpecialRule cannonSpecialRule;
    private VariantChessBoard mockBoard;
    private Cannon cannon;

    /**
     * Initializes the test environment before each test is run.
     * Creates a new CannonSpecialRule instance, mocks a VariantChessBoard,
     * and sets up a Cannon with a capture count of 3.
     */
    @BeforeEach
    void setUp() {
        cannonSpecialRule = new CannonSpecialRule();
        mockBoard = mock(VariantChessBoard.class);
        cannon = new Cannon(Color.WHITE);
        cannon.setCaptureCount(3);  // Set capture count to 3 to trigger detonation

        // Configure the mock board to return true for valid positions within the bounds of the chessboard
        when(mockBoard.isInBounds(anyInt(), anyInt())).thenAnswer(invocation -> {
            int x = invocation.getArgument(0);
            int y = invocation.getArgument(1);
            return x >= 0 && x < 8 && y >= 0 && y < 8;
        });
    }

    /**
     * Tests the detonation of the Cannon when surrounded by enemy pieces.
     * Verifies that all enemy pieces around the Cannon are removed, along with the Cannon itself.
     */
    @Test
    void testDetonateWithoutFriendlyPieces() {
        setupBoardWithPieces(Color.BLACK, cannon);

        VariantChessMove move = new VariantChessMove(3, 3, 3, 3);
        cannonSpecialRule.applyRule(move, cannon, mockBoard);

        // Verify that enemy pieces around the Cannon are removed
        verify(mockBoard, times(1)).setPieceAt(2, 3, null);
        verify(mockBoard, times(1)).setPieceAt(4, 3, null);
        verify(mockBoard, times(1)).setPieceAt(3, 2, null);
        verify(mockBoard, times(1)).setPieceAt(3, 4, null);

        // Verify that the Cannon itself is removed
        verify(mockBoard, times(1)).setPieceAt(3, 3, null);
    }

    /**
     * Tests the detonation of the Cannon when surrounded by a mix of friendly and enemy pieces.
     * Verifies that only the enemy pieces are removed, and the Cannon itself is removed.
     */
    @Test
    void testDetonateWithFriendlyPieces() {
        setupBoardWithMixedPieces(Color.WHITE, cannon);

        VariantChessMove move = new VariantChessMove(3, 3, 3, 3);
        cannonSpecialRule.applyRule(move, cannon, mockBoard);

        // Verify that only enemy pieces around the Cannon are removed
        verify(mockBoard, times(1)).setPieceAt(2, 3, null);
        verify(mockBoard, times(1)).setPieceAt(4, 3, null);
        verify(mockBoard, times(1)).setPieceAt(3, 2, null);
        verify(mockBoard, times(1)).setPieceAt(3, 4, null);

        // Verify that the Cannon itself is removed
        verify(mockBoard, times(1)).setPieceAt(3, 3, null);

        // Verify that friendly pieces are not removed
        verify(mockBoard, never()).setPieceAt(2, 2, null);
        verify(mockBoard, never()).setPieceAt(4, 4, null);
    }

    /**
     * Tests the detonation of the Cannon when it is positioned at the edge of the board.
     * Verifies that only the enemy pieces within the board bounds are removed, and the Cannon itself is removed.
     */
    @Test
    void testDetonateAtEdgeOfBoard() {
        setupBoardAtEdge(Color.BLACK, cannon);

        VariantChessMove move = new VariantChessMove(0, 7, 0, 7);
        cannonSpecialRule.applyRule(move, cannon, mockBoard);

        // Verify that only enemy pieces within the board bounds are removed
        verify(mockBoard, times(1)).setPieceAt(0, 6, null);
        verify(mockBoard, times(1)).setPieceAt(1, 7, null);

        // Verify that the Cannon itself is removed
        verify(mockBoard, times(1)).setPieceAt(0, 7, null);
    }

    /**
     * Tests that the Cannon does not detonate if it has captured fewer than three pieces.
     * Verifies that no pieces are removed when the Cannon's capture count is less than three.
     */
    @Test
    void testNoDetonationBeforeThreeCaptures() {
        cannon.setCaptureCount(2);  // Set capture count to 2, which should not trigger detonation

        VariantChessMove move = new VariantChessMove(3, 3, 3, 3);
        cannonSpecialRule.applyRule(move, cannon, mockBoard);

        // Verify that no pieces are removed
        verify(mockBoard, never()).setPieceAt(anyInt(), anyInt(), any());
    }

    /**
     * Tests the detonation of the Cannon when it is surrounded by empty squares.
     * Verifies that no pieces are removed except the Cannon itself.
     */
    @Test
    void testDetonateWhenSurroundedByEmptySquares() {
        setupBoardWithEmptySquares(cannon);

        VariantChessMove move = new VariantChessMove(3, 3, 3, 3);
        cannonSpecialRule.applyRule(move, cannon, mockBoard);

        // Verify that no pieces are removed except the Cannon itself
        verify(mockBoard, never()).setPieceAt(2, 3, null);
        verify(mockBoard, never()).setPieceAt(4, 3, null);
        verify(mockBoard, never()).setPieceAt(3, 2, null);
        verify(mockBoard, never()).setPieceAt(3, 4, null);

        // Verify that the Cannon itself is removed
        verify(mockBoard, times(1)).setPieceAt(3, 3, null);
    }

    /**
     * Tests the detonation of the Cannon when there is only one enemy piece nearby.
     * Verifies that only the single enemy piece is removed, along with the Cannon itself.
     */
    @Test
    void testDetonateWithOnlyOneEnemyPieceNearby() {
        setupBoardWithOneEnemyPiece(Color.BLACK, cannon);

        VariantChessMove move = new VariantChessMove(3, 3, 3, 3);
        cannonSpecialRule.applyRule(move, cannon, mockBoard);

        // Verify that only the single enemy piece is removed
        verify(mockBoard, times(1)).setPieceAt(3, 4, null);

        // Verify that the Cannon itself is removed
        verify(mockBoard, times(1)).setPieceAt(3, 3, null);
    }

    /**
     * Tests the detonation of multiple Cannons that can each detonate.
     * Verifies that enemy pieces around both Cannons are removed, and both Cannons are removed.
     */
    @Test
    void testDetonateWithMultipleCannonDetonations() {
        // Setup a second Cannon to test multiple detonation scenarios
        Cannon anotherCannon = new Cannon(Color.WHITE);
        anotherCannon.setCaptureCount(3);
        setupBoardWithMultipleCannons(Color.BLACK, cannon, anotherCannon);

        VariantChessMove move1 = new VariantChessMove(3, 3, 3, 3);
        VariantChessMove move2 = new VariantChessMove(5, 5, 5, 5);

        cannonSpecialRule.applyRule(move1, cannon, mockBoard);
        cannonSpecialRule.applyRule(move2, anotherCannon, mockBoard);

        // Verify that enemy pieces around both Cannons are removed
        verify(mockBoard, times(1)).setPieceAt(2, 3, null);
        verify(mockBoard, times(1)).setPieceAt(4, 3, null);
        verify(mockBoard, times(1)).setPieceAt(3, 2, null);
        verify(mockBoard, times(1)).setPieceAt(3, 4, null);
        verify(mockBoard, times(1)).setPieceAt(4, 5, null);
        verify(mockBoard, times(1)).setPieceAt(6, 5, null);
        verify(mockBoard, times(1)).setPieceAt(5, 4, null);
        verify(mockBoard, times(1)).setPieceAt(5, 6, null);

        // Verify that both Cannons themselves are removed
        verify(mockBoard, times(1)).setPieceAt(3, 3, null);
        verify(mockBoard, times(1)).setPieceAt(5, 5, null);
    }

    /**
     * Tests the detonation of the Cannon when it is completely surrounded by pieces.
     * Verifies that only enemy pieces are removed, and the Cannon itself is removed.
     */
    @Test
    void testDetonateWhenCannonIsBlocked() {
        setupBoardWithBlockedCannon(Color.WHITE, cannon);

        VariantChessMove move = new VariantChessMove(3, 3, 3, 3);
        cannonSpecialRule.applyRule(move, cannon, mockBoard);

        // Verify that only enemy pieces are removed
        verify(mockBoard, times(1)).setPieceAt(2, 3, null);
        verify(mockBoard, times(1)).setPieceAt(4, 3, null);
        verify(mockBoard, times(1)).setPieceAt(3, 2, null);
        verify(mockBoard, times(1)).setPieceAt(3, 4, null);

        // Verify that the Cannon itself is removed
        verify(mockBoard, times(1)).setPieceAt(3, 3, null);

        // Verify that friendly pieces are not removed
        verify(mockBoard, never()).setPieceAt(2, 2, null);
        verify(mockBoard, never()).setPieceAt(4, 4, null);
    }

    // Helper methods for setting up various board configurations for testing

    /**
     * Sets up the mock board with enemy pieces around the Cannon.
     */
    private void setupBoardWithPieces(Color enemyColor, Cannon cannon) {
        when(mockBoard.getPieceAt(3, 3)).thenReturn(cannon);
        when(mockBoard.getPieceAt(2, 3)).thenReturn(new Pawn(enemyColor));
        when(mockBoard.getPieceAt(4, 3)).thenReturn(new Pawn(enemyColor));
        when(mockBoard.getPieceAt(3, 2)).thenReturn(new Pawn(enemyColor));
        when(mockBoard.getPieceAt(3, 4)).thenReturn(new Pawn(enemyColor));
    }

    /**
     * Sets up the mock board with a mix of enemy and friendly pieces around the Cannon.
     */
    private void setupBoardWithMixedPieces(Color friendlyColor, Cannon cannon) {
        when(mockBoard.getPieceAt(3, 3)).thenReturn(cannon);
        when(mockBoard.getPieceAt(2, 3)).thenReturn(new Pawn(Color.BLACK));
        when(mockBoard.getPieceAt(4, 3)).thenReturn(new Pawn(Color.BLACK));
        when(mockBoard.getPieceAt(3, 2)).thenReturn(new Pawn(Color.BLACK));
        when(mockBoard.getPieceAt(3, 4)).thenReturn(new Pawn(Color.BLACK));
        when(mockBoard.getPieceAt(2, 2)).thenReturn(new Pawn(friendlyColor));
        when(mockBoard.getPieceAt(4, 4)).thenReturn(new Pawn(friendlyColor));
    }

    /**
     * Sets up the mock board with the Cannon at the edge of the board and enemy pieces around it.
     */
    private void setupBoardAtEdge(Color enemyColor, Cannon cannon) {
        when(mockBoard.getPieceAt(0, 7)).thenReturn(cannon);
        when(mockBoard.getPieceAt(0, 6)).thenReturn(new Pawn(enemyColor));
        when(mockBoard.getPieceAt(1, 7)).thenReturn(new Pawn(enemyColor));
    }

    /**
     * Sets up the mock board with empty squares around the Cannon.
     */
    private void setupBoardWithEmptySquares(Cannon cannon) {
        when(mockBoard.getPieceAt(3, 3)).thenReturn(cannon);
        when(mockBoard.getPieceAt(2, 3)).thenReturn(null);
        when(mockBoard.getPieceAt(4, 3)).thenReturn(null);
        when(mockBoard.getPieceAt(3, 2)).thenReturn(null);
        when(mockBoard.getPieceAt(3, 4)).thenReturn(null);
    }

    /**
     * Sets up the mock board with one enemy piece around the Cannon.
     */
    private void setupBoardWithOneEnemyPiece(Color enemyColor, Cannon cannon) {
        when(mockBoard.getPieceAt(3, 3)).thenReturn(cannon);
        when(mockBoard.getPieceAt(2, 3)).thenReturn(null);
        when(mockBoard.getPieceAt(4, 3)).thenReturn(null);
        when(mockBoard.getPieceAt(3, 2)).thenReturn(null);
        when(mockBoard.getPieceAt(3, 4)).thenReturn(new Pawn(enemyColor));
    }

    /**
     * Sets up the mock board with multiple Cannons and their surrounding pieces.
     */
    private void setupBoardWithMultipleCannons(Color enemyColor, Cannon cannon1, Cannon cannon2) {
        when(mockBoard.getPieceAt(3, 3)).thenReturn(cannon1);
        when(mockBoard.getPieceAt(2, 3)).thenReturn(new Pawn(enemyColor));
        when(mockBoard.getPieceAt(4, 3)).thenReturn(new Pawn(enemyColor));
        when(mockBoard.getPieceAt(3, 2)).thenReturn(new Pawn(enemyColor));
        when(mockBoard.getPieceAt(3, 4)).thenReturn(new Pawn(enemyColor));

        when(mockBoard.getPieceAt(5, 5)).thenReturn(cannon2);
        when(mockBoard.getPieceAt(4, 5)).thenReturn(new Pawn(enemyColor));
        when(mockBoard.getPieceAt(6, 5)).thenReturn(new Pawn(enemyColor));
        when(mockBoard.getPieceAt(5, 4)).thenReturn(new Pawn(enemyColor));
        when(mockBoard.getPieceAt(5, 6)).thenReturn(new Pawn(enemyColor));
    }

    /**
     * Sets up the mock board with the Cannon completely surrounded by pieces, blocking its detonation.
     */
    private void setupBoardWithBlockedCannon(Color friendlyColor, Cannon cannon) {
        when(mockBoard.getPieceAt(3, 3)).thenReturn(cannon);
        when(mockBoard.getPieceAt(2, 3)).thenReturn(new Pawn(Color.BLACK));
        when(mockBoard.getPieceAt(4, 3)).thenReturn(new Pawn(Color.BLACK));
        when(mockBoard.getPieceAt(3, 2)).thenReturn(new Pawn(Color.BLACK));
        when(mockBoard.getPieceAt(3, 4)).thenReturn(new Pawn(Color.BLACK));
        when(mockBoard.getPieceAt(2, 2)).thenReturn(new Pawn(friendlyColor));
        when(mockBoard.getPieceAt(4, 4)).thenReturn(new Pawn(friendlyColor));
    }
}
