package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Bishop piece in the Variant Chess Game.
 * This class contains various test cases to ensure that the Bishop's
 * movement and capture logic are implemented correctly.
 */
class BishopTest {

    private VariantChessBoard board;
    private Bishop whiteBishop;
    private Bishop blackBishop;

    /**
     * Set up the test environment before each test.
     * Initializes a new chess board and places a white Bishop at a specific location.
     */
    @BeforeEach
    void setUp() {
        board = new VariantChessBoard();
        whiteBishop = new Bishop(Color.WHITE);
        blackBishop = new Bishop(Color.BLACK);
        // Manually set the position of the white Bishop to avoid conflicts with the initialized board
        board.getBoard()[2][2] = whiteBishop;
    }

    /**
     * Test the validity of a Bishop's move that is diagonal and within the board boundaries.
     * Verifies that the Bishop can move to the new position and that the move is executed correctly.
     */
    @Test
    void testValidMove() {
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to move diagonally to an empty position.");
        board.movePiece(move);
        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after the move.");
        assertNull(board.getPieceAt(2, 2), "Original position should be empty after the move.");
    }

    /**
     * Test the validity of a Bishop's move that is outside the board boundaries.
     * Ensures that the Bishop cannot move outside the board.
     */
    @Test
    void testInvalidMoveOutOfBoard() {
        VariantChessMove move = new VariantChessMove(2, 2, 8, 8);
        assertFalse(whiteBishop.isValidMove(move, board), "Bishop should not be able to move outside the board.");
    }

    /**
     * Test the validity of a Bishop's move that is not diagonal.
     * Ensures that the Bishop cannot move in a non-diagonal direction.
     */
    @Test
    void testInvalidMoveNotDiagonal() {
        VariantChessMove move = new VariantChessMove(2, 2, 2, 4);
        assertFalse(whiteBishop.isValidMove(move, board), "Bishop should not be able to move non-diagonally.");
    }

    /**
     * Test the validity of a Bishop's move when capturing an enemy piece.
     * Verifies that the Bishop can move to the position occupied by an enemy piece and that the move is marked as a capture.
     */
    @Test
    void testCaptureMove() {
        board.getBoard()[4][4] = new Pawn(Color.BLACK);
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to capture an enemy piece by moving diagonally.");
        assertTrue(move.isCapture(), "Move should be marked as a capture.");
        board.movePiece(move);
        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after capture.");
        assertNull(board.getPieceAt(2, 2), "Original position should be empty after capture.");
    }

    /**
     * Test the validity of a Bishop's move when attempting to capture its own piece.
     * Ensures that the Bishop cannot capture a piece of the same color.
     */
    @Test
    void testInvalidCaptureOwnPiece() {
        board.getBoard()[4][4] = new Pawn(Color.WHITE);
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertFalse(whiteBishop.isValidMove(move, board), "Bishop should not be able to capture its own piece by moving diagonally.");
    }

    /**
     * Test the validity of a Bishop's move when there is a piece in the middle of the path.
     * Ensures that the Bishop can still move diagonally to the destination even if a piece is present along the path.
     */
    @Test
    void testValidMoveWithPieceInMiddle() {
        board.getBoard()[3][3] = new Pawn(Color.WHITE);
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to move diagonally with a piece in the middle of the path.");
        board.movePiece(move);
        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after the move.");
        assertNull(board.getPieceAt(2, 2), "Original position should be empty after the move.");
    }

    /**
     * Test that the color of the Bishop remains the same after a move.
     * Ensures that the Bishop maintains its color after moving to a new position.
     */
    @Test
    void testBishopColorAfterMove() {
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to move diagonally to an empty position.");
        board.movePiece(move);
        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after the move.");
        assertNull(board.getPieceAt(2, 2), "Original position should be empty after the move.");
        VariantChessPiece piece = board.getPieceAt(4, 4);
        assertNotNull(piece, "There should be a piece at the new position.");
        assertTrue(piece instanceof Bishop, "The piece at the new position should be a Bishop.");
        assertEquals(Color.WHITE, piece.getColor(), "The Bishop should remain white after the move.");
    }

    /**
     * Test the validity of a Bishop's move when there is a piece blocking the path.
     * Ensures that the Bishop can still move diagonally even if a piece is blocking the path.
     */
    @Test
    void testMoveWithPieceBlockingPath() {
        board.getBoard()[3][3] = new Pawn(Color.BLACK);
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to move diagonally with a piece blocking the path.");
        board.movePiece(move);
        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after the move.");
        assertNull(board.getPieceAt(2, 2), "Original position should be empty after the move.");
    }
}
