package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.Color;
import uk.ac.standrews.variantchessgame.model.Pawn;
import uk.ac.standrews.variantchessgame.model.VariantChessMove;
import uk.ac.standrews.variantchessgame.model.VariantChessPiece;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the VariantChessMove class.
 * These tests verify the correct creation of move objects and the behavior of their properties.
 */
class VariantChessMoveTest {

    private VariantChessMove move;

    /**
     * Sets up the test environment before each test.
     * Initializes a VariantChessMove instance with known values for testing.
     */
    @BeforeEach
    void setUp() {
        // Initialize a move from (1, 1) to (2, 2)
        move = new VariantChessMove(1, 1, 2, 2);
    }

    /**
     * Tests the initialization of a VariantChessMove object.
     * Verifies that the starting and ending positions are correctly assigned.
     */
    @Test
    void testInitialization() {
        assertEquals(1, move.getStartX(), "Start X should be 1");
        assertEquals(1, move.getStartY(), "Start Y should be 1");
        assertEquals(2, move.getEndX(), "End X should be 2");
        assertEquals(2, move.getEndY(), "End Y should be 2");
    }

    /**
     * Tests the default capture state of a VariantChessMove object.
     * Verifies that a new move is not a capture by default.
     */
    @Test
    void testDefaultCaptureState() {
        assertFalse(move.isCapture(), "Move should not be a capture by default");
    }

    /**
     * Tests the setting and getting of the capture flag.
     * Verifies that the capture state can be changed and retrieved correctly.
     */
    @Test
    void testSetCapture() {
        move.setCapture(true);
        assertTrue(move.isCapture(), "Move should be marked as a capture");
        move.setCapture(false);
        assertFalse(move.isCapture(), "Move should not be marked as a capture");
    }

    /**
     * Tests the default captured piece state of a VariantChessMove object.
     * Verifies that a new move has no captured piece by default.
     */
    @Test
    void testDefaultCapturedPiece() {
        assertNull(move.getCapturedPiece(), "Captured piece should be null by default");
    }

    /**
     * Tests setting and getting the captured piece in a VariantChessMove object.
     * Verifies that the captured piece can be assigned and retrieved correctly.
     */
    @Test
    void testSetCapturedPiece() {
        VariantChessPiece piece = new Pawn(Color.BLACK);
        move.setCapturedPiece(piece);
        assertEquals(piece, move.getCapturedPiece(), "Captured piece should be set correctly");
    }

    /**
     * Tests the default state of the first move flag.
     * Verifies that the first move flag is false by default.
     */
    @Test
    void testDefaultWasFirstMove() {
        assertFalse(move.wasFirstMove(), "wasFirstMove should be false by default");
    }

    /**
     * Tests setting and getting the first move flag in a VariantChessMove object.
     * Verifies that the first move flag can be assigned and retrieved correctly.
     */
    @Test
    void testSetWasFirstMove() {
        move.setWasFirstMove(true);
        assertTrue(move.wasFirstMove(), "wasFirstMove should be true after being set");
        move.setWasFirstMove(false);
        assertFalse(move.wasFirstMove(), "wasFirstMove should be false after being reset");
    }
}
