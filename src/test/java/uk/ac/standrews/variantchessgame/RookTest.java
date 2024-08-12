package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {

    private VariantChessBoard board;
    private Rook whiteRook;
    private Rook blackRook;

    /**
     * Sets up the test environment before each test case.
     * Initializes the board and places a white and black rook in their starting positions.
     */
    @BeforeEach
    void setUp() {
        board = new VariantChessBoard();
        whiteRook = new Rook(Color.WHITE);
        blackRook = new Rook(Color.BLACK);
        // Set initial positions for the rooks
        board.getBoard()[4][4] = whiteRook;
        board.getBoard()[7][7] = blackRook;
    }

    /**
     * Tests that a rook can make a valid straight-line move to an empty position.
     * Ensures that the rook can move horizontally or vertically if the target position is empty.
     */
    @Test
    void testValidMove() {
        // Move rook to an empty position (4, 7)
        board.getBoard()[4][7] = null; // Ensure the target position is empty
        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertTrue(whiteRook.isValidMove(move, board), "Rook should be able to move in a straight line.");
        board.movePiece(move);
        assertEquals(whiteRook, board.getPieceAt(4, 7), "Rook should be at the new position after move.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");

        // Attempt to move to a position occupied by its own piece
        move = new VariantChessMove(4, 7, 7, 7);
        assertFalse(whiteRook.isValidMove(move, board), "Rook should not be able to move to a position occupied by its own piece.");
        if (whiteRook.isValidMove(move, board)) {
            board.movePiece(move);
        }
        assertNotEquals(whiteRook, board.getPieceAt(7, 7), "Rook should not be at the new position after invalid move.");
        assertEquals(whiteRook, board.getPieceAt(4, 7), "Original position should be the same after invalid move.");
    }

    /**
     * Tests that a rook cannot move outside the board.
     * Ensures that the rook stays within the board limits.
     */
    @Test
    void testInvalidMoveOutOfBoard() {
        // Move to outside the board
        VariantChessMove move = new VariantChessMove(4, 4, 8, 8);
        assertFalse(whiteRook.isValidMove(move, board), "Rook should not be able to move outside the board.");
    }

    /**
     * Tests that a rook cannot make non-straight moves.
     * Ensures that the rook can only move in straight lines.
     */
    @Test
    void testInvalidMoveNotStraight() {
        // Attempt to make a non-straight move
        VariantChessMove move = new VariantChessMove(4, 4, 5, 6);
        assertFalse(whiteRook.isValidMove(move, board), "Rook should not be able to move non-straight.");
    }

    /**
     * Tests that a rook can capture an opponent's piece by moving to its position.
     * Ensures that the rook can capture an enemy piece when moving in a straight line.
     */
    @Test
    void testCaptureMove() {
        // Place a black pawn at (4, 7)
        board.getBoard()[4][7] = new Pawn(Color.BLACK);

        // Attempt to capture the black pawn
        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertTrue(whiteRook.isValidMove(move, board), "Rook should be able to capture an enemy piece by moving in a straight line.");
        assertTrue(move.isCapture(), "Move should be marked as a capture.");
        board.movePiece(move);
        assertEquals(whiteRook, board.getPieceAt(4, 7), "Rook should be at the new position after capture.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after capture.");
    }

    /**
     * Tests that a rook cannot capture its own piece.
     * Ensures that the rook does not move to a position occupied by a piece of the same color.
     */
    @Test
    void testInvalidCaptureOwnPiece() {
        // Place a white pawn at (4, 7)
        board.getBoard()[4][7] = new Pawn(Color.WHITE);

        // Attempt to capture the white pawn
        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertFalse(whiteRook.isValidMove(move, board), "Rook should not be able to capture its own piece.");
    }

    /**
     * Tests that a rook maintains its color after moving.
     * Ensures that the rook retains its color attribute after a valid move.
     */
    @Test
    void testRookColorAfterMove() {
        // Move the white rook to (4, 7)
        board.getBoard()[4][7] = null; // Ensure the target position is empty
        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertTrue(whiteRook.isValidMove(move, board), "Rook should be able to move in a straight line.");
        board.movePiece(move);
        assertEquals(whiteRook, board.getPieceAt(4, 7), "Rook should be at the new position after move.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");

        // Check the color of the rook at the new position
        VariantChessPiece piece = board.getPieceAt(4, 7);
        assertNotNull(piece, "There should be a piece at the new position.");
        assertTrue(piece instanceof Rook, "The piece at the new position should be a Rook.");
        assertEquals(Color.WHITE, piece.getColor(), "The Rook should remain white after the move.");
    }

    /**
     * Tests that a rook cannot move through a blocking piece.
     * Ensures that the rook stops moving if it encounters a piece in its path.
     */
    @Test
    void testMoveBlockedByPiece() {
        // Place a white pawn at (4, 5)
        board.getBoard()[4][5] = new Pawn(Color.WHITE);

        // Attempt to move past the blocking piece to (4, 7)
        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertFalse(whiteRook.isValidMove(move, board), "Rook should not be able to move past a blocking piece.");
    }

    /**
     * Tests that a rook can move to an empty position after capturing an opponent's piece.
     * Ensures that the rook can continue moving in straight lines after a capture.
     */
    @Test
    void testMoveAfterCapture() {
        // Place a black pawn at (4, 7)
        board.getBoard()[4][7] = new Pawn(Color.BLACK);

        // Capture the black pawn
        VariantChessMove captureMove = new VariantChessMove(4, 4, 4, 7);
        assertTrue(whiteRook.isValidMove(captureMove, board), "Rook should be able to capture an enemy piece by moving in a straight line.");
        assertTrue(captureMove.isCapture(), "Move should be marked as a capture.");
        board.movePiece(captureMove);

        // Attempt to move to an empty position (4, 6) after capture
        VariantChessMove move = new VariantChessMove(4, 7, 4, 6);
        assertTrue(whiteRook.isValidMove(move, board), "Rook should be able to move to an empty position after capture.");
    }
}
