//package uk.ac.standrews.variantchessgame;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import uk.ac.standrews.variantchessgame.model.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests for the Cannon piece in variant chess. The tests validate the movement
// * and capturing rules specific to the Cannon piece, including its ability to move,
// capture, and interact with other pieces on the board.
// */
//class CannonTest {
//
//    private VariantChessBoard board;
//    private Cannon whiteCannon;
//    private Cannon blackCannon;
//
//    /**
//     * Initializes the test environment before each test is run.
//     * Creates a new VariantChessBoard instance and initializes Cannon pieces with
//     * different colors.
//     */
//    @BeforeEach
//    void setUp() {
//        board = new VariantChessBoard();
//        whiteCannon = new Cannon(Color.WHITE);
//        blackCannon = new Cannon(Color.BLACK);
//    }
//
//    /**
//     * Tests valid moves of the Cannon piece.
//     * Verifies that the Cannon can move in a straight line to empty positions.
//     */
//    @Test
//    void testValidMove() {
//        // Place the white Cannon at (2, 2)
//        board.getBoard()[2][2] = whiteCannon;
//
//        // Move in a straight line to an empty position (2, 5)
//        VariantChessMove move = new VariantChessMove(2, 2, 2, 5);
//        assertTrue(whiteCannon.isValidMove(move, board),
//                "Cannon should be able to move in a straight line to an empty position.");
//
//        // Move in a straight line to an empty position (5, 2)
//        move = new VariantChessMove(2, 2, 5, 2);
//        assertTrue(whiteCannon.isValidMove(move, board),
//                "Cannon should be able to move in a straight line to an empty position.");
//    }
//
//    /**
//     * Tests the Cannon's movement when blocked by another piece.
//     * Verifies that the Cannon cannot move past a blocking piece in its path.
//     */
//    @Test
//    void testMoveBlockedByPiece() {
//        // Place the white Cannon at (2, 2) and a white Pawn at (2, 4)
//        board.getBoard()[2][2] = whiteCannon;
//        board.getBoard()[2][4] = new Pawn(Color.WHITE);
//
//        // Attempt to move to (2, 5)
//        VariantChessMove move = new VariantChessMove(2, 2, 2, 5);
//        assertFalse(whiteCannon.isValidMove(move, board),
//                "Cannon should not be able to move past a blocking piece.");
//    }
//
//    /**
//     * Tests the Cannon's capture move.
//     * Verifies that the Cannon can capture an enemy piece by jumping over exactly one piece.
//     */
//    @Test
//    void testCaptureMove() {
//        // Place the white Cannon at (2, 2), a black Pawn at (2, 4), and another black Pawn at (2, 6)
//        board.getBoard()[2][2] = whiteCannon;
//        board.getBoard()[2][4] = new Pawn(Color.BLACK);
//        board.getBoard()[2][6] = new Pawn(Color.BLACK);
//
//        // Attempt to capture the black Pawn
//        VariantChessMove move = new VariantChessMove(2, 2, 2, 6);
//        assertTrue(whiteCannon.isValidMove(move, board),
//                "Cannon should be able to capture an enemy piece by jumping over exactly one piece.");
//        assertTrue(move.isCapture(), "Move should be marked as a capture.");
//    }
//
//    /**
//     * Tests invalid moves for the Cannon piece.
//     * Verifies that the Cannon cannot move non-linearly or to its own position.
//     */
//    @Test
//    void testInvalidMove() {
//        // Place the white Cannon at (2, 2)
//        board.getBoard()[2][2] = whiteCannon;
//
//        // Attempt to move non-linearly
//        VariantChessMove move = new VariantChessMove(2, 2, 3, 3);
//        assertFalse(whiteCannon.isValidMove(move, board),
//                "Cannon should not be able to move non-linearly.");
//
//        // Attempt to move to its own position
//        move = new VariantChessMove(2, 2, 2, 2);
//        assertFalse(whiteCannon.isValidMove(move, board),
//                "Cannon should not be able to move to its own position.");
//    }
//
//    /**
//     * Tests the Cannon's capture move when there is no piece to jump over.
//     * Verifies that the Cannon cannot capture an enemy piece without jumping over exactly one piece.
//     */
//    @Test
//    void testCaptureWithoutJump() {
//        // Place the white Cannon at (2, 2) and a black Pawn at (2, 5)
//        board.getBoard()[2][2] = whiteCannon;
//        board.getBoard()[2][5] = new Pawn(Color.BLACK);
//
//        // Attempt to capture the black Pawn directly
//        VariantChessMove move = new VariantChessMove(2, 2, 2, 5);
//        assertFalse(whiteCannon.isValidMove(move, board),
//                "Cannon should not be able to capture without jumping over exactly one piece.");
//    }
//
//    /**
//     * Tests the Cannon's ability to move to a new position after a capture.
//     * Verifies that the Cannon can move to an empty position after it has performed a capture.
//     */
//    @Test
//    void testMoveAfterCapture() {
//        // Place the white Cannon at (2, 2), a black Pawn at (2, 4), and another black Pawn at (2, 6)
//        board.getBoard()[2][2] = whiteCannon;
//        board.getBoard()[2][4] = new Pawn(Color.BLACK);
//        board.getBoard()[2][6] = new Pawn(Color.BLACK);
//
//        // Capture the black Pawn
//        VariantChessMove captureMove = new VariantChessMove(2, 2, 2, 6);
//        assertTrue(whiteCannon.isValidMove(captureMove, board),
//                "Cannon should be able to capture an enemy piece by jumping over exactly one piece.");
//        assertTrue(captureMove.isCapture(), "Move should be marked as a capture.");
//        board.movePiece(captureMove);
//
//        // Attempt to move to an empty position (2, 7)
//        VariantChessMove move = new VariantChessMove(2, 6, 2, 7);
//        assertTrue(whiteCannon.isValidMove(move, board),
//                "Cannon should be able to move to an empty position after capture.");
//    }
//
//    /**
//     * Tests the Cannon's attempt to capture its own piece.
//     * Verifies that the Cannon cannot capture a piece of the same color.
//     */
//    @Test
//    void testCaptureOwnPiece() {
//        // Place the white Cannon at (2, 2), and white Pawns at (2, 4) and (2, 6)
//        board.getBoard()[2][2] = whiteCannon;
//        board.getBoard()[2][4] = new Pawn(Color.WHITE);
//        board.getBoard()[2][6] = new Pawn(Color.WHITE);
//
//        // Attempt to capture its own Pawn
//        VariantChessMove move = new VariantChessMove(2, 2, 2, 6);
//        assertFalse(whiteCannon.isValidMove(move, board),
//                "Cannon should not be able to capture its own piece by jumping over exactly one piece.");
//    }
//
//    /**
//     * Tests the Cannon's color after it moves.
//     * Verifies that the Cannon retains its color after moving to a new position.
//     */
//    @Test
//    void testCannonColorAfterMove() {
//        // Place the white Cannon at (2, 2)
//        board.getBoard()[2][2] = whiteCannon;
//
//        // Move the white Cannon to (2, 5)
//        VariantChessMove move = new VariantChessMove(2, 2, 2, 5);
//        assertTrue(whiteCannon.isValidMove(move, board),
//                "Cannon should be able to move in a straight line to an empty position.");
//        board.movePiece(move);
//
//        // Check that the original position (2, 2) is empty
//        assertNull(board.getPieceAt(2, 2), "The original position should be empty after the move.");
//
//        // Check that the piece at the new position (2, 5) is still white
//        VariantChessPiece piece = board.getPieceAt(2, 5);
//        assertNotNull(piece, "There should be a piece at the new position.");
//        assertTrue(piece instanceof Cannon, "The piece at the new position should be a Cannon.");
//        assertEquals(Color.WHITE, piece.getColor(), "The Cannon should remain white after the move.");
//    }
//
//    /**
//     * Tests the Cannon's ability to capture an enemy piece.
//     * Verifies that the Cannon can capture an enemy piece by jumping over exactly one piece.
//     */
//    @Test
//    void testCaptureEnemyPiece() {
//        // Place the white Cannon at (2, 2), a white Pawn at (2, 4), and a black Pawn at (2, 6)
//        board.getBoard()[2][2] = whiteCannon;
//        board.getBoard()[2][4] = new Pawn(Color.WHITE);
//        board.getBoard()[2][6] = new Pawn(Color.BLACK);
//
//        // Attempt to capture the black Pawn
//        VariantChessMove move = new VariantChessMove(2, 2, 2, 6);
//        assertTrue(whiteCannon.isValidMove(move, board),
//                "Cannon should be able to capture an enemy piece by jumping over exactly one piece.");
//        assertTrue(move.isCapture(), "Move should be marked as a capture.");
//    }
//}
