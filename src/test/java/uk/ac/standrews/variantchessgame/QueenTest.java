package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Queen class in a variant chess game.
 * These tests ensure that the Queen's movement and capturing abilities are correctly implemented.
 */
class QueenTest {

    private VariantChessBoard board;
    private Queen whiteQueen;
    private Queen blackQueen;

    /**
     * Sets up the board and initializes the queens before each test.
     * This ensures a fresh board state and proper setup for each test case.
     */
    @BeforeEach
    void setUp() {
        board = new VariantChessBoard();
        whiteQueen = new Queen(Color.WHITE);
        blackQueen = new Queen(Color.BLACK);
        // Place the white queen at a specific position to avoid conflicts with other pieces
        board.getBoard()[4][4] = whiteQueen;
    }

    /**
     * Tests if the Queen can make valid moves in straight lines and diagonally.
     * - Moves the Queen from (4, 4) to (4, 7) for a straight-line move.
     * - Moves the Queen from (4, 7) to (6, 5) for a diagonal move.
     * Ensures the Queen correctly updates its position and the original position is cleared.
     */
    @Test
    void testValidMove() {
        // Test straight-line move
        board.getBoard()[4][7] = null; // Ensure destination is empty
        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertTrue(whiteQueen.isValidMove(move, board), "Queen should be able to move in a straight line.");
        board.movePiece(move);
        assertEquals(whiteQueen, board.getPieceAt(4, 7), "Queen should be at the new position after move.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");

        // Test diagonal move
        board.getBoard()[6][5] = null; // Ensure destination is empty
        move = new VariantChessMove(4, 7, 6, 5);
        assertTrue(whiteQueen.isValidMove(move, board), "Queen should be able to move diagonally.");
        board.movePiece(move);
        assertEquals(whiteQueen, board.getPieceAt(6, 5), "Queen should be at the new position after move.");
        assertNull(board.getPieceAt(4, 7), "Original position should be empty after move.");
    }

    /**
     * Tests if the Queen correctly handles invalid moves that are outside the board boundaries.
     * - Attempts to move the Queen to (8, 8), which is outside the board.
     * Ensures that such moves are correctly identified as invalid.
     */
    @Test
    void testInvalidMoveOutOfBoard() {
        VariantChessMove move = new VariantChessMove(4, 4, 8, 8);
        assertFalse(whiteQueen.isValidMove(move, board), "Queen should not be able to move outside the board.");
    }

    /**
     * Tests if the Queen handles moves that are neither straight-line nor diagonal correctly.
     * - Attempts to move the Queen from (4, 4) to (5, 6), which is neither a straight-line nor a diagonal move.
     * Ensures that such moves are correctly identified as invalid.
     */
    @Test
    void testInvalidMoveNotStraightOrDiagonal() {
        VariantChessMove move = new VariantChessMove(4, 4, 5, 6);
        assertFalse(whiteQueen.isValidMove(move, board), "Queen should not be able to move non-straight or non-diagonally.");
    }

    /**
     * Tests if the Queen can capture an enemy piece.
     * - Places a black Pawn at (4, 7) and attempts to move the Queen to capture it.
     * Ensures that the Queen can correctly capture an enemy piece and that the original position is cleared.
     */
    @Test
    void testCaptureMove() {
        board.getBoard()[4][7] = new Pawn(Color.BLACK); // Place a black pawn for capture

        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertTrue(whiteQueen.isValidMove(move, board), "Queen should be able to capture an enemy piece by moving in a straight line.");
        assertTrue(move.isCapture(), "Move should be marked as a capture.");
        board.movePiece(move);
        assertEquals(whiteQueen, board.getPieceAt(4, 7), "Queen should be at the new position after capture.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after capture.");
    }

    /**
     * Tests if the Queen correctly identifies and rejects attempts to capture its own pieces.
     * - Places a white Pawn at (4, 7) and attempts to move the Queen to capture it.
     * Ensures that the Queen does not capture its own piece.
     */
    @Test
    void testInvalidCaptureOwnPiece() {
        board.getBoard()[4][7] = new Pawn(Color.WHITE); // Place a white pawn (same color) for capture

        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertFalse(whiteQueen.isValidMove(move, board), "Queen should not be able to capture its own piece.");
    }

    /**
     * Tests if the Queen retains its color after moving.
     * - Moves the white Queen from (4, 4) to (4, 7) and verifies the color of the Queen at the new position.
     * Ensures that the Queen's color remains consistent after the move.
     */
    @Test
    void testQueenColorAfterMove() {
        board.getBoard()[4][7] = null; // Ensure destination is empty
        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertTrue(whiteQueen.isValidMove(move, board), "Queen should be able to move in a straight line.");
        board.movePiece(move);
        assertEquals(whiteQueen, board.getPieceAt(4, 7), "Queen should be at the new position after move.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");

        // Verify the Queen's color at the new position
        VariantChessPiece piece = board.getPieceAt(4, 7);
        assertNotNull(piece, "There should be a piece at the new position.");
        assertTrue(piece instanceof Queen, "The piece at the new position should be a Queen.");
        assertEquals(Color.WHITE, piece.getColor(), "The Queen should remain white after the move.");
    }
}
