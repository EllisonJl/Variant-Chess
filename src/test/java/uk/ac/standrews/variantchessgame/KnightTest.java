//package uk.ac.standrews.variantchessgame;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import uk.ac.standrews.variantchessgame.model.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests for the Knight piece in the variant chess game.
// * These tests ensure that the Knight adheres to the rules for movement and capturing:
// * 1. Valid moves in an 'L' shape.
// * 2. Invalid moves that are not in the 'L' shape or exceed board boundaries.
// * 3. Correct handling of captures and color validation.
// */
//class KnightTest {
//
//    private VariantChessBoard board;
//    private Knight whiteKnight;
//    private Knight blackKnight;
//
//    /**
//     * Sets up the test environment before each test is run.
//     * Initializes the VariantChessBoard and creates white and black Knight pieces.
//     * Places the white Knight at a specific position on the board to avoid conflicts with other tests.
//     */
//    @BeforeEach
//    void setUp() {
//        board = new VariantChessBoard();
//        whiteKnight = new Knight(Color.WHITE);
//        blackKnight = new Knight(Color.BLACK);
//        // Place the white Knight at position (4, 4) on the board
//        board.getBoard()[4][4] = whiteKnight;
//    }
//
//    /**
//     * Tests valid moves for the Knight piece.
//     * Verifies that the Knight can move in an 'L' shape to an empty square.
//     * Tests two valid 'L' shape moves:
//     * 1. From (4, 4) to (6, 5)
//     * 2. From (6, 5) to (4, 6)
//     */
//    @Test
//    void testValidMove() {
//        // Ensure the target position is empty for valid 'L' shape move (6, 5)
//        board.getBoard()[6][5] = null;
//        VariantChessMove move = new VariantChessMove(4, 4, 6, 5);
//        assertTrue(whiteKnight.isValidMove(move, board), "Knight should be able to move in an 'L' shape.");
//        board.movePiece(move);
//        assertEquals(whiteKnight, board.getPieceAt(6, 5), "Knight should be at the new position after move.");
//        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");
//
//        // Ensure the target position is empty for valid 'L' shape move (4, 6)
//        board.getBoard()[4][6] = null;
//        move = new VariantChessMove(6, 5, 4, 6);
//        assertTrue(whiteKnight.isValidMove(move, board), "Knight should be able to move in an 'L' shape.");
//        board.movePiece(move);
//        assertEquals(whiteKnight, board.getPieceAt(4, 6), "Knight should be at the new position after move.");
//        assertNull(board.getPieceAt(6, 5), "Original position should be empty after move.");
//    }
//
//    /**
//     * Tests that the Knight retains its color after a move.
//     * Verifies that the Knight's color remains unchanged after it moves to a new position.
//     */
//    @Test
//    void testKnightColorAfterMove() {
//        // Ensure the target position is empty for valid 'L' shape move (6, 5)
//        board.getBoard()[6][5] = null;
//        VariantChessMove move = new VariantChessMove(4, 4, 6, 5);
//        assertTrue(whiteKnight.isValidMove(move, board), "Knight should be able to move in an 'L' shape.");
//        board.movePiece(move);
//        assertEquals(whiteKnight, board.getPieceAt(6, 5), "Knight should be at the new position after move.");
//        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");
//
//        // Check the color of the Knight at the new position
//        VariantChessPiece piece = board.getPieceAt(6, 5);
//        assertNotNull(piece, "There should be a piece at the new position.");
//        assertTrue(piece instanceof Knight, "The piece at the new position should be a Knight.");
//        assertEquals(Color.WHITE, piece.getColor(), "The Knight should remain white after the move.");
//    }
//
//    /**
//     * Tests the scenario where the Knight attempts to move outside the boundaries of the board.
//     * Verifies that the Knight cannot move to a position that is off the board.
//     */
//    @Test
//    void testInvalidMoveOutOfBoard() {
//        // Move to a position outside the board (8, 5)
//        VariantChessMove move = new VariantChessMove(4, 4, 8, 5);
//        assertFalse(whiteKnight.isValidMove(move, board), "Knight should not be able to move outside the board.");
//    }
//
//    /**
//     * Tests the scenario where the Knight attempts to move in a way that is not an 'L' shape.
//     * Verifies that the Knight cannot move in any pattern other than the 'L' shape.
//     */
//    @Test
//    void testInvalidMoveNotLShape() {
//        // Move in a non-'L' shape pattern (5, 5)
//        VariantChessMove move = new VariantChessMove(4, 4, 5, 5);
//        assertFalse(whiteKnight.isValidMove(move, board), "Knight should not be able to move in a non-'L' shape.");
//    }
//
//    /**
//     * Tests the scenario where the Knight attempts to capture an enemy piece.
//     * Verifies that the Knight can capture an enemy Pawn by moving in an 'L' shape.
//     */
//    @Test
//    void testCaptureMove() {
//        // Place a black Pawn at (6, 5)
//        board.getBoard()[6][5] = new Pawn(Color.BLACK);
//
//        // Attempt to capture the black Pawn
//        VariantChessMove move = new VariantChessMove(4, 4, 6, 5);
//        assertTrue(whiteKnight.isValidMove(move, board), "Knight should be able to capture an enemy piece by moving in an 'L' shape.");
//        assertTrue(move.isCapture(), "Move should be marked as a capture.");
//        board.movePiece(move);
//        assertEquals(whiteKnight, board.getPieceAt(6, 5), "Knight should be at the new position after capture.");
//        assertNull(board.getPieceAt(4, 4), "Original position should be empty after capture.");
//    }
//
//    /**
//     * Tests the scenario where the Knight attempts to capture its own piece.
//     * Verifies that the Knight cannot capture a friendly piece.
//     */
//    @Test
//    void testInvalidCaptureOwnPiece() {
//        // Place a white Pawn at (6, 5)
//        board.getBoard()[6][5] = new Pawn(Color.WHITE);
//
//        // Attempt to capture the white Pawn
//        VariantChessMove move = new VariantChessMove(4, 4, 6, 5);
//        assertFalse(whiteKnight.isValidMove(move, board), "Knight should not be able to capture its own piece.");
//    }
//}
