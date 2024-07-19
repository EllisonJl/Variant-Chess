package uk.ac.standrews.variantchessgame;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

class CannonSpecialRuleTest {

    private CannonSpecialRule cannonSpecialRule;
    private VariantChessBoard mockBoard;
    private Cannon cannon;

    @BeforeEach
    void setUp() {
        cannonSpecialRule = new CannonSpecialRule();
        mockBoard = mock(VariantChessBoard.class);
        cannon = new Cannon(Color.WHITE);
        cannon.setCaptureCount(3);  // Set capture count to 3 to trigger detonation

        // Setup the bounds checks to return true for valid positions
        when(mockBoard.isInBounds(anyInt(), anyInt())).thenAnswer(invocation -> {
            int x = invocation.getArgument(0);
            int y = invocation.getArgument(1);
            return x >= 0 && x < 8 && y >= 0 && y < 8;
        });
    }

    @Test
    void testDetonateWithoutFriendlyPieces() {
        // Setup the board with enemy pieces around the cannon
        setupBoardWithPieces(Color.BLACK, cannon);

        VariantChessMove move = new VariantChessMove(3, 3, 3, 3);
        cannonSpecialRule.applyRule(move, cannon, mockBoard);

        // Verify that enemy pieces around the cannon are removed
        verify(mockBoard, times(1)).setPieceAt(2, 3, null);
        verify(mockBoard, times(1)).setPieceAt(4, 3, null);
        verify(mockBoard, times(1)).setPieceAt(3, 2, null);
        verify(mockBoard, times(1)).setPieceAt(3, 4, null);

        // Verify that the cannon itself is removed
        verify(mockBoard, times(1)).setPieceAt(3, 3, null);
    }

    @Test
    void testDetonateWithFriendlyPieces() {
        // Setup the board with mixed enemy and friendly pieces around the cannon
        setupBoardWithMixedPieces(Color.WHITE, cannon);

        VariantChessMove move = new VariantChessMove(3, 3, 3, 3);
        cannonSpecialRule.applyRule(move, cannon, mockBoard);

        // Verify that only enemy pieces around the cannon are removed
        verify(mockBoard, times(1)).setPieceAt(2, 3, null);
        verify(mockBoard, times(1)).setPieceAt(4, 3, null);
        verify(mockBoard, times(1)).setPieceAt(3, 2, null);
        verify(mockBoard, times(1)).setPieceAt(3, 4, null);

        // Verify that the cannon itself is removed
        verify(mockBoard, times(1)).setPieceAt(3, 3, null);

        // Verify that friendly pieces are not removed
        verify(mockBoard, never()).setPieceAt(2, 2, null);
        verify(mockBoard, never()).setPieceAt(4, 4, null);
    }

    @Test
    void testDetonateAtEdgeOfBoard() {
        // Setup the cannon at the edge of the board
        setupBoardAtEdge(Color.BLACK, cannon);

        VariantChessMove move = new VariantChessMove(0, 7, 0, 7);
        cannonSpecialRule.applyRule(move, cannon, mockBoard);

        // Verify that only enemy pieces within the board bounds are removed
        verify(mockBoard, times(1)).setPieceAt(0, 6, null);
        verify(mockBoard, times(1)).setPieceAt(1, 7, null);

        // Verify that the cannon itself is removed
        verify(mockBoard, times(1)).setPieceAt(0, 7, null);
    }

    @Test
    void testNoDetonationBeforeThreeCaptures() {
        // Setup the cannon with less than 3 captures
        cannon.setCaptureCount(2);

        VariantChessMove move = new VariantChessMove(3, 3, 3, 3);
        cannonSpecialRule.applyRule(move, cannon, mockBoard);

        // Verify that no pieces are removed
        verify(mockBoard, never()).setPieceAt(anyInt(), anyInt(), any());
    }

    @Test
    void testDetonateWhenSurroundedByEmptySquares() {
        // Setup the board with empty squares around the cannon
        setupBoardWithEmptySquares(cannon);

        VariantChessMove move = new VariantChessMove(3, 3, 3, 3);
        cannonSpecialRule.applyRule(move, cannon, mockBoard);

        // Verify that no pieces are removed except the cannon itself
        verify(mockBoard, never()).setPieceAt(2, 3, null);
        verify(mockBoard, never()).setPieceAt(4, 3, null);
        verify(mockBoard, never()).setPieceAt(3, 2, null);
        verify(mockBoard, never()).setPieceAt(3, 4, null);

        // Verify that the cannon itself is removed
        verify(mockBoard, times(1)).setPieceAt(3, 3, null);
    }

    @Test
    void testDetonateWithOnlyOneEnemyPieceNearby() {
        // Setup the board with one enemy piece around the cannon
        setupBoardWithOneEnemyPiece(Color.BLACK, cannon);

        VariantChessMove move = new VariantChessMove(3, 3, 3, 3);
        cannonSpecialRule.applyRule(move, cannon, mockBoard);

        // Verify that only the single enemy piece is removed
        verify(mockBoard, times(1)).setPieceAt(3, 4, null);

        // Verify that the cannon itself is removed
        verify(mockBoard, times(1)).setPieceAt(3, 3, null);
    }

    @Test
    void testDetonateWithMultipleCannonDetonations() {
        // Setup the board with multiple cannons that can detonate
        Cannon anotherCannon = new Cannon(Color.WHITE);
        anotherCannon.setCaptureCount(3);
        setupBoardWithMultipleCannons(Color.BLACK, cannon, anotherCannon);

        VariantChessMove move1 = new VariantChessMove(3, 3, 3, 3);
        VariantChessMove move2 = new VariantChessMove(5, 5, 5, 5);

        cannonSpecialRule.applyRule(move1, cannon, mockBoard);
        cannonSpecialRule.applyRule(move2, anotherCannon, mockBoard);

        // Verify that the enemy pieces around both cannons are removed
        verify(mockBoard, times(1)).setPieceAt(2, 3, null);
        verify(mockBoard, times(1)).setPieceAt(4, 3, null);
        verify(mockBoard, times(1)).setPieceAt(3, 2, null);
        verify(mockBoard, times(1)).setPieceAt(3, 4, null);
        verify(mockBoard, times(1)).setPieceAt(4, 5, null);
        verify(mockBoard, times(1)).setPieceAt(6, 5, null);
        verify(mockBoard, times(1)).setPieceAt(5, 4, null);
        verify(mockBoard, times(1)).setPieceAt(5, 6, null);

        // Verify that both cannons themselves are removed
        verify(mockBoard, times(1)).setPieceAt(3, 3, null);
        verify(mockBoard, times(1)).setPieceAt(5, 5, null);
    }

    @Test
    void testDetonateWhenCannonIsBlocked() {
        // Setup the board with cannon completely surrounded by pieces
        setupBoardWithBlockedCannon(Color.WHITE, cannon);

        VariantChessMove move = new VariantChessMove(3, 3, 3, 3);
        cannonSpecialRule.applyRule(move, cannon, mockBoard);

        // Verify that only enemy pieces are removed
        verify(mockBoard, times(1)).setPieceAt(2, 3, null);
        verify(mockBoard, times(1)).setPieceAt(4, 3, null);
        verify(mockBoard, times(1)).setPieceAt(3, 2, null);
        verify(mockBoard, times(1)).setPieceAt(3, 4, null);

        // Verify that the cannon itself is removed
        verify(mockBoard, times(1)).setPieceAt(3, 3, null);

        // Verify that friendly pieces are not removed
        verify(mockBoard, never()).setPieceAt(2, 2, null);
        verify(mockBoard, never()).setPieceAt(4, 4, null);
    }

    private void setupBoardWithPieces(Color enemyColor, Cannon cannon) {
        when(mockBoard.getPieceAt(3, 3)).thenReturn(cannon);
        when(mockBoard.getPieceAt(2, 3)).thenReturn(new Pawn(enemyColor));
        when(mockBoard.getPieceAt(4, 3)).thenReturn(new Pawn(enemyColor));
        when(mockBoard.getPieceAt(3, 2)).thenReturn(new Pawn(enemyColor));
        when(mockBoard.getPieceAt(3, 4)).thenReturn(new Pawn(enemyColor));
    }

    private void setupBoardWithMixedPieces(Color friendlyColor, Cannon cannon) {
        when(mockBoard.getPieceAt(3, 3)).thenReturn(cannon);
        when(mockBoard.getPieceAt(2, 3)).thenReturn(new Pawn(Color.BLACK));
        when(mockBoard.getPieceAt(4, 3)).thenReturn(new Pawn(Color.BLACK));
        when(mockBoard.getPieceAt(3, 2)).thenReturn(new Pawn(Color.BLACK));
        when(mockBoard.getPieceAt(3, 4)).thenReturn(new Pawn(Color.BLACK));
        when(mockBoard.getPieceAt(2, 2)).thenReturn(new Pawn(friendlyColor));
        when(mockBoard.getPieceAt(4, 4)).thenReturn(new Pawn(friendlyColor));
    }

    private void setupBoardAtEdge(Color enemyColor, Cannon cannon) {
        when(mockBoard.getPieceAt(0, 7)).thenReturn(cannon);
        when(mockBoard.getPieceAt(0, 6)).thenReturn(new Pawn(enemyColor));
        when(mockBoard.getPieceAt(1, 7)).thenReturn(new Pawn(enemyColor));
    }

    private void setupBoardWithEmptySquares(Cannon cannon) {
        when(mockBoard.getPieceAt(3, 3)).thenReturn(cannon);
        when(mockBoard.getPieceAt(2, 3)).thenReturn(null);
        when(mockBoard.getPieceAt(4, 3)).thenReturn(null);
        when(mockBoard.getPieceAt(3, 2)).thenReturn(null);
        when(mockBoard.getPieceAt(3, 4)).thenReturn(null);
    }

    private void setupBoardWithOneEnemyPiece(Color enemyColor, Cannon cannon) {
        when(mockBoard.getPieceAt(3, 3)).thenReturn(cannon);
        when(mockBoard.getPieceAt(2, 3)).thenReturn(null);
        when(mockBoard.getPieceAt(4, 3)).thenReturn(null);
        when(mockBoard.getPieceAt(3, 2)).thenReturn(null);
        when(mockBoard.getPieceAt(3, 4)).thenReturn(new Pawn(enemyColor));
    }

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
