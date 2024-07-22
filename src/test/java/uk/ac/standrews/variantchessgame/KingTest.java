package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the King piece in the variant chess game.
 * These tests ensure that the King behaves correctly according to the rules:
 * 1. Valid moves within one square in any direction.
 * 2. Invalid moves that go outside the board or exceed the King's movement capability.
 * 3. Correct handling of capturing moves and color validation.
 */
class KingTest {

    private VariantChessBoard board;
    private King whiteKing;
    private King blackKing;

    /**
     * Sets up the test environment before each test is run.
     * Initializes the VariantChessBoard and creates white and black King pieces.
     * Places the white King at a specific position on the board to avoid conflicts with other tests.
     */
    @BeforeEach
    void setUp() {
        board = new VariantChessBoard();
        whiteKing = new King(Color.WHITE);
        blackKing = new King(Color.BLACK);
        // Place the white King at position (4, 4) on the board
        board.getBoard()[4][4] = whiteKing;
    }

    /**
     * Tests valid moves for the King piece.
     * Verifies that the King can move one square in both straight and diagonal directions to an empty square.
     */
    @Test
    void testValidMove() {
        // Valid straight move to an empty square (4, 5)
        VariantChessMove move = new VariantChessMove(4, 4, 4, 5);
        assertTrue(whiteKing.isValidMove(move, board), "King should be able to move one square.");
        board.movePiece(move);
        assertEquals(whiteKing, board.getPieceAt(4, 5), "King should be at the new position after move.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");

        // Valid diagonal move to an empty square (5, 5)
        move = new VariantChessMove(4, 5, 5, 5);
        assertTrue(whiteKing.isValidMove(move, board), "King should be able to move one square diagonally.");
        board.movePiece(move);
        assertEquals(whiteKing, board.getPieceAt(5, 5), "King should be at the new position after move.");
        assertNull(board.getPieceAt(4, 5), "Original position should be empty after move.");
    }

    /**
     * Tests the scenario where the King attempts to move outside the boundaries of the board.
     * Verifies that the King cannot move to a position that is off the board.
     */
    @Test
    void testInvalidMoveOutOfBoard() {
        // Move to a position outside the board (8, 4)
        VariantChessMove move = new VariantChessMove(4, 4, 8, 4);
        assertFalse(whiteKing.isValidMove(move, board), "King should not be able to move outside the board.");
    }

    /**
     * Tests the scenario where the King attempts to move more than one square in a straight line or diagonally.
     * Verifies that the King cannot make moves that exceed its movement capabilities.
     */
    @Test
    void testInvalidMoveMoreThanOneSquare() {
        // Move more than one square (6, 4)
        VariantChessMove move = new VariantChessMove(4, 4, 6, 4);
        assertFalse(whiteKing.isValidMove(move, board), "King should not be able to move more than one square.");
    }

    /**
     * Tests the scenario where the King attempts to capture an enemy piece.
     * Verifies that the King can capture an enemy Pawn and that the capture is correctly handled.
     */
    @Test
    void testCaptureMove() {
        // Place a black Pawn at (4, 5)
        board.getBoard()[4][5] = new Pawn(Color.BLACK);

        // Attempt to capture the black Pawn
        VariantChessMove move = new VariantChessMove(4, 4, 4, 5);
        assertTrue(whiteKing.isValidMove(move, board), "King should be able to capture an enemy piece by moving one square.");
        assertTrue(move.isCapture(), "Move should be marked as a capture.");
        board.movePiece(move);
        assertEquals(whiteKing, board.getPieceAt(4, 5), "King should be at the new position after capture.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after capture.");
    }

    /**
     * Tests the scenario where the King attempts to capture its own piece.
     * Verifies that the King cannot capture a friendly piece.
     */
    @Test
    void testInvalidCaptureOwnPiece() {
        // Place a white Pawn at (4, 5)
        board.getBoard()[4][5] = new Pawn(Color.WHITE);

        // Attempt to capture the white Pawn
        VariantChessMove move = new VariantChessMove(4, 4, 4, 5);
        assertFalse(whiteKing.isValidMove(move, board), "King should not be able to capture its own piece.");
    }

    /**
     * Tests that the King retains its color after a move.
     * Verifies that the King's color remains unchanged after it moves to a new position.
     */
    @Test
    void testKingColorAfterMove() {
        // Move the white King to (4, 5)
        VariantChessMove move = new VariantChessMove(4, 4, 4, 5);
        assertTrue(whiteKing.isValidMove(move, board), "King should be able to move one square.");
        board.movePiece(move);
        assertEquals(whiteKing, board.getPieceAt(4, 5), "King should be at the new position after move.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");

        // Check the color of the King at the new position
        VariantChessPiece piece = board.getPieceAt(4, 5);
        assertNotNull(piece, "There should be a piece at the new position.");
        assertTrue(piece instanceof King, "The piece at the new position should be a King.");
        assertEquals(Color.WHITE, piece.getColor(), "The King should remain white after the move.");
    }
}
