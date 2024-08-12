//package uk.ac.standrews.variantchessgame;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import uk.ac.standrews.variantchessgame.model.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests for the Bishop piece in the Variant Chess Game.
// * This class contains various test cases to ensure that the Bishop's
// * movement and capture logic are implemented correctly.
// */
//class BishopTest {
//
//    private VariantChessBoard board;
//    private Bishop whiteBishop;
//    private Bishop blackBishop;
//
//    /**
//     * Set up the test environment before each test.
//     * Initializes a new chess board and places a white Bishop at a specific location.
//     */
//    @BeforeEach
//    void setUp() {
//        board = new VariantChessBoard();
//        whiteBishop = new Bishop(Color.WHITE);
//        blackBishop = new Bishop(Color.BLACK);
//        // Manually set the position of the white Bishop to avoid conflicts with the initialized board
//        board.getBoard()[2][2] = whiteBishop;
//    }
//    /**
//     * Test the validity of a Bishop's田字形 move with a piece in the middle.
//     * Ensures that the Bishop can perform a田字形 move even if there is a piece in the middle of the path.
//     */
//    @Test
//    void testValidFieldMoveWithPieceInMiddle() {
//        // Set up a 田字形 move with a piece in the middle
//        board.getBoard()[3][3] = new Pawn(Color.WHITE);
//        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
//        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to perform a 田字形 move with a piece in the middle of the path.");
//        board.movePiece(move);
//        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after the move.");
//        assertNull(board.getPieceAt(2, 2), "Original position should be empty after the move.");
//    }
//
//    /**
//     * Test that the Bishop cannot capture its own piece with a田字形 move.
//     * Ensures that the Bishop cannot capture a friendly piece.
//     */
//    @Test
//    void testInvalidCaptureOwnPieceWithFieldMove() {
//        // Place a friendly piece at the destination
//        board.getBoard()[4][4] = new Pawn(Color.WHITE);
//        board.getBoard()[3][3] = new Pawn(Color.WHITE); // Allow田字形 move
//        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
//        assertFalse(whiteBishop.isValidMove(move, board), "Bishop should not be able to capture its own piece with a田字形 move.");
//    }
//
//    /**
//     * Test that the Bishop can capture an enemy piece with a田字形 move.
//     * Ensures that the Bishop can capture a piece using a田字形 move.
//     */
//    @Test
//    void testCaptureEnemyPieceWithFieldMove() {
//        // Place an enemy piece at the destination
//        board.getBoard()[4][4] = new Pawn(Color.BLACK);
//        board.getBoard()[3][3] = new Pawn(Color.WHITE); // Allow田字形 move
//        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
//        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to capture an enemy piece with a田字形 move.");
//        assertTrue(move.isCapture(), "Move should be marked as a capture.");
//        board.movePiece(move);
//        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after capture.");
//        assertNull(board.getPieceAt(2, 2), "Original position should be empty after capture.");
//    }
//    /**
//     * Test that the Bishop maintains its color after multiple田字形 moves.
//     * Ensures that the Bishop does not change color during the game.
//     */
//    @Test
//    void testBishopColorAfterMultipleFieldMoves() {
//        // Perform a田字形 move
//        board.getBoard()[3][3] = new Pawn(Color.WHITE);
//        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
//        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to perform a 田字形 move.");
//        board.movePiece(move);
//
//        // Check color after the move
//        VariantChessPiece piece = board.getPieceAt(4, 4);
//        assertNotNull(piece, "There should be a piece at the new position.");
//        assertTrue(piece instanceof Bishop, "The piece at the new position should be a Bishop.");
//        assertEquals(Color.WHITE, piece.getColor(), "The Bishop should remain white after the move.");
//    }
//    /**
//     * Test the validity of a Bishop's initial田字形 move.
//     * Verifies that the Bishop can move to the new position using a 田字形 move.
//     */
//    @Test
//    void testValidFieldMove() {
//        // Place a piece in the middle to allow 田字形 move
//        board.getBoard()[3][3] = new Pawn(Color.WHITE);
//        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
//        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to perform a 田字形 move to an empty position.");
//        board.movePiece(move);
//        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after the move.");
//        assertNull(board.getPieceAt(2, 2), "Original position should be empty after the move.");
//    }
//}
