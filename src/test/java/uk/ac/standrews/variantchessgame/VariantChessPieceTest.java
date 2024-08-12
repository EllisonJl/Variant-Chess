package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.Color;
import uk.ac.standrews.variantchessgame.model.VariantChessBoard;
import uk.ac.standrews.variantchessgame.model.VariantChessMove;
import uk.ac.standrews.variantchessgame.model.VariantChessPiece;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the VariantChessPiece class.
 * These tests verify the correct behavior and properties of chess piece objects.
 */
class VariantChessPieceTest {

    private VariantChessPiece piece;

    /**
     * A test subclass of VariantChessPiece for testing purposes.
     * This subclass implements the abstract isValidMove method to allow instantiation and testing.
     */
    private static class TestChessPiece extends VariantChessPiece {
        public TestChessPiece(Color color, String type) {
            super(color, type);
        }

        public TestChessPiece(Color color, String type, boolean promotedFromPawn) {
            super(color, type, promotedFromPawn);
        }

        @Override
        public boolean isValidMove(VariantChessMove move, VariantChessBoard board) {
            // Simple implementation for testing purposes; always returns true
            return true;
        }
    }

    /**
     * Sets up the test environment before each test.
     * Initializes a TestChessPiece instance with known values for testing.
     */
    @BeforeEach
    void setUp() {
        // Initialize a test chess piece of type "TestPiece" and color WHITE
        piece = new TestChessPiece(Color.WHITE, "TestPiece");
    }

    /**
     * Tests the initialization of a VariantChessPiece object.
     * Verifies that the color and type are correctly assigned.
     */
    @Test
    void testInitialization() {
        assertEquals(Color.WHITE, piece.getColor(), "Color should be WHITE");
        assertEquals("TestPiece", piece.getType(), "Type should be 'TestPiece'");
    }

    /**
     * Tests the default capture count of a VariantChessPiece object.
     * Verifies that the capture count is zero by default.
     */
    @Test
    void testDefaultCaptureCount() {
        assertEquals(0, piece.getCaptureCount(), "Capture count should be 0 by default");
    }

    /**
     * Tests the increment of the capture count.
     * Verifies that the capture count increases correctly.
     */
    @Test
    void testIncrementCaptureCount() {
        piece.incrementCaptureCount();
        assertEquals(1, piece.getCaptureCount(), "Capture count should be 1 after increment");
        piece.incrementCaptureCount();
        assertEquals(2, piece.getCaptureCount(), "Capture count should be 2 after another increment");
    }

    /**
     * Tests setting the capture count directly.
     * Verifies that the capture count can be assigned correctly.
     */
    @Test
    void testSetCaptureCount() {
        piece.setCaptureCount(5);
        assertEquals(5, piece.getCaptureCount(), "Capture count should be set to 5");
    }

    /**
     * Tests the default promoted from pawn state.
     * Verifies that the piece is not promoted from a pawn by default.
     */
    @Test
    void testDefaultPromotedFromPawn() {
        assertFalse(piece.isPromotedFromPawn(), "Piece should not be promoted from pawn by default");
    }

    /**
     * Tests setting the promoted from pawn flag.
     * Verifies that the promoted from pawn status can be changed and retrieved correctly.
     */
    @Test
    void testSetPromotedFromPawn() {
        piece.setPromotedFromPawn(true);
        assertTrue(piece.isPromotedFromPawn(), "Piece should be promoted from pawn after setting");
        piece.setPromotedFromPawn(false);
        assertFalse(piece.isPromotedFromPawn(), "Piece should not be promoted from pawn after resetting");
    }

    /**
     * Tests the default immobile state of a VariantChessPiece object.
     * Verifies that the piece is mobile by default.
     */
    @Test
    void testDefaultImmobile() {
        assertFalse(piece.isImmobile(), "Piece should be mobile by default");
    }

    /**
     * Tests setting the immobile flag.
     * Verifies that the immobility status can be assigned and retrieved correctly.
     */
    @Test
    void testSetImmobile() {
        piece.setImmobile(true);
        assertTrue(piece.isImmobile(), "Piece should be immobile after setting");
        piece.setImmobile(false);
        assertFalse(piece.isImmobile(), "Piece should be mobile after resetting");
    }

    /**
     * Tests the isValidMove method implementation.
     * Verifies that the move validation returns true for the TestChessPiece, as defined.
     */
    @Test
    void testIsValidMove() {
        VariantChessBoard board = new VariantChessBoard(); // Mocked or simple implementation of board
        VariantChessMove move = new VariantChessMove(0, 0, 1, 1);
        assertTrue(piece.isValidMove(move, board), "Move should be valid for TestChessPiece");
    }
}
