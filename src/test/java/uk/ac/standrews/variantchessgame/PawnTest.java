package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {

    private VariantChessBoard board;
    private Pawn whitePawn;
    private Pawn blackPawn;

    /**
     * Sets up the test environment before each test case.
     * Initializes the board and places a white and black pawn in their starting positions.
     */
    @BeforeEach
    void setUp() {
        board = new VariantChessBoard();
        whitePawn = new Pawn(Color.WHITE);
        blackPawn = new Pawn(Color.BLACK);
        board.getBoard()[6][0] = whitePawn;  // White pawn starts from row 6
        board.getBoard()[1][0] = blackPawn;  // Black pawn starts from row 1
    }

    /**
     * Tests that a pawn can move forward one square after the initial two-square move.
     * Ensures that pawns can advance one square on subsequent moves after their initial move.
     */
    @Test
    void testSecondMoveAfterFirstMove() {
        // Move white pawn two squares forward
        VariantChessMove firstMove = new VariantChessMove(6, 0, 4, 0);
        assertTrue(whitePawn.isValidMove(firstMove, board), "White pawn should be able to move forward two squares on its first move.");
        board.movePiece(firstMove);
        // Ensure the pawn's first move status is updated correctly
        whitePawn.setFirstMove(false);

        // Move the white pawn one square forward
        VariantChessMove secondMove1 = new VariantChessMove(4, 0, 3, 0);
        assertTrue(whitePawn.isValidMove(secondMove1, board), "White pawn should be able to move forward one square on its second move.");
        board.movePiece(secondMove1);

        // Verify the position and status after the second move
        assertEquals(whitePawn, board.getPieceAt(3, 0), "White pawn should be at (3, 0) after the second move.");
        assertNull(board.getPieceAt(4, 0), "The original position (4, 0) should be empty after the second move.");

        // Move black pawn two squares forward
        VariantChessMove firstMoveBlack = new VariantChessMove(1, 0, 3, 0);
        assertTrue(blackPawn.isValidMove(firstMoveBlack, board), "Black pawn should be able to move forward two squares on its first move.");
        board.movePiece(firstMoveBlack);
        // Ensure the pawn's first move status is updated correctly
        blackPawn.setFirstMove(false);

        // Move the black pawn one square forward
        VariantChessMove secondMoveBlack = new VariantChessMove(3, 0, 4, 0);
        assertTrue(blackPawn.isValidMove(secondMoveBlack, board), "Black pawn should be able to move forward one square on its second move.");
        board.movePiece(secondMoveBlack);

        // Verify the position and status after the second move
        assertEquals(blackPawn, board.getPieceAt(4, 0), "Black pawn should be at (4, 0) after the second move.");
        assertNull(board.getPieceAt(3, 0), "The original position (3, 0) should be empty after the second move.");
    }

    /**
     * Tests that a pawn can capture an opponent's piece after its first move.
     * Ensures that the pawn's capturing ability is valid even after it has moved two squares forward initially.
     */
    @Test
    void testCaptureMoveAfterFirstMove() {
        // Move white pawn two squares forward on its first move
        VariantChessMove firstMove = new VariantChessMove(6, 0, 4, 0);
        assertTrue(whitePawn.isValidMove(firstMove, board), "White pawn should be able to move forward two squares on its first move.");
        board.movePiece(firstMove);
        whitePawn.setFirstMove(false);  // Mark the first move as completed

        // Place a black rook directly in front of the white pawn
        board.setPieceAt(3, 0, new Rook(Color.BLACK));
        VariantChessMove move = new VariantChessMove(4, 0, 3, 0);
        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to capture forward after first move.");
        board.movePiece(move);

        // Verify that the black rook has been captured
        assertEquals(whitePawn, board.getPieceAt(3, 0), "White pawn should be at (3, 0) after capturing the black rook.");
        assertNull(board.getPieceAt(4, 0), "The original position (4, 0) should be empty after the capture.");

        // Move black pawn two squares forward on its first move
        VariantChessMove firstMoveBlack = new VariantChessMove(1, 0, 3, 0);
        assertTrue(blackPawn.isValidMove(firstMoveBlack, board), "Black pawn should be able to move forward two squares on its first move.");
        board.movePiece(firstMoveBlack);
        blackPawn.setFirstMove(false);  // Mark the first move as completed

        // Place a white rook directly in front of the black pawn
        board.setPieceAt(4, 0, new Rook(Color.WHITE));
        move = new VariantChessMove(3, 0, 4, 0);
        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to capture forward after first move.");
        board.movePiece(move);

        // Verify that the white rook has been captured
        assertEquals(blackPawn, board.getPieceAt(4, 0), "Black pawn should be at (4, 0) after capturing the white rook.");
        assertNull(board.getPieceAt(3, 0), "The original position (3, 0) should be empty after the capture.");
    }
//    /**
//     * Tests that a pawn can capture an opponent's piece directly in front of it.
//     * Ensures that the pawn can capture correctly when the opponent's piece is directly in the path.
//     */
//    @Test
//    void testCaptureMove() {
//        // Place a black rook directly in front of the white pawn
//        board.getBoard()[5][0] = new Rook(Color.BLACK);
//        VariantChessMove move = new VariantChessMove(6, 0, 5, 0);
//        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to capture forward after first move.");
//        board.movePiece(move);
//
//        // Place another black rook directly in front of the white pawn
//        board.getBoard()[4][0] = new Rook(Color.BLACK);
//        move = new VariantChessMove(5, 0, 4, 0);
//        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to capture forward one square after first move.");
//        board.movePiece(move);
//
//        // Place a white rook directly in front of the black pawn
//        board.getBoard()[2][0] = new Rook(Color.WHITE);
//        move = new VariantChessMove(1, 0, 2, 0);
//        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to capture forward after first move.");
//        board.movePiece(move);
//
//        // Place another white rook directly in front of the black pawn
//        board.getBoard()[3][0] = new Rook(Color.WHITE);
//        move = new VariantChessMove(2, 0, 3, 0);
//        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to capture forward one square after first move.");
//        board.movePiece(move);
//    }
//    /**
//     * Tests that a pawn cannot move sideways after the first move.
//     * Ensures that pawns can only move forward and not sideways.
//     */
//    @Test
//    void testMoveSidewaysAfterFirstMove() {
//        board.movePiece(new VariantChessMove(6, 0, 5, 0));  // First move for white pawn
//        whitePawn.setFirstMove(false);  // Ensure the first move is registered
//
//        VariantChessMove move = new VariantChessMove(5, 0, 5, 1);
//        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to move sideways after the first move.");
//
//        board.movePiece(new VariantChessMove(1, 0, 2, 0));  // First move for black pawn
//        blackPawn.setFirstMove(false);  // Ensure the first move is registered
//
//        move = new VariantChessMove(2, 0, 2, 1);
//        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to move sideways after the first move.");
//    }
//    /**
//     * Tests that a pawn can capture an opponent's piece after moving two squares forward on its first move.
//     * Ensures that pawns can perform captures after advancing two squares initially.
//     */
//    @Test
//    void testPawnCaptureAfterFirstTwoSquaresMove() {
//        // Move white pawn two squares forward
//        VariantChessMove firstMove = new VariantChessMove(6, 0, 4, 0);
//        assertTrue(whitePawn.isValidMove(firstMove, board), "White pawn should be able to move forward two squares on its first move.");
//        board.movePiece(firstMove);
//        whitePawn.setFirstMove(false);  // Mark the first move as completed
//
//        // Place a black rook to the right of the white pawn
//        board.getBoard()[4][1] = new Rook(Color.BLACK);
//
//        // Attempt to capture the black rook
//        VariantChessMove captureMove = new VariantChessMove(4, 0, 4, 1);
//        assertTrue(whitePawn.isValidMove(captureMove, board), "White pawn should be able to capture right after first move.");
//        board.movePiece(captureMove);
//
//        // Verify the position and status after capturing
//        assertEquals(whitePawn, board.getPieceAt(4, 1), "White pawn should be at (4, 1) after capturing.");
//        assertNull(board.getPieceAt(4, 0), "The original position (4, 0) should be empty after capturing.");
//    }
//
//
//    /**
//     * Tests that a pawn can capture an opponent's piece to the left or right of its position.
//     * Ensures that pawns can capture diagonally left or right when the opponent's piece is in the adjacent diagonal square.
//     */
//    @Test
//    void testCaptureMoveLeftRight() {
//        // Place a black rook to the right of the white pawn
//        board.getBoard()[6][1] = new Rook(Color.BLACK);
//        VariantChessMove move = new VariantChessMove(6, 0, 6, 1);
//        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to capture right after first move.");
//        board.movePiece(move);
//
//        // Place a black rook to the left of the white pawn
//        board.getBoard()[6][1] = new Rook(Color.BLACK);
//        move = new VariantChessMove(6, 0, 6, 1);
//        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to capture right after first move.");
//        board.movePiece(move);
//
//        // Place a white rook to the right of the black pawn
//        board.getBoard()[1][1] = new Rook(Color.WHITE);
//        move = new VariantChessMove(1, 0, 1, 1);
//        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to capture right after first move.");
//        board.movePiece(move);
//
//        // Place a white rook to the left of the black pawn
//        board.getBoard()[1][1] = new Rook(Color.WHITE);
//        move = new VariantChessMove(1, 0, 1, 1);
//        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to capture right after first move.");
//        board.movePiece(move);
//    }
//
//    /**
//     * Tests that a pawn can move forward one square on its first move.
//     * Ensures that pawns can advance one square if there is no obstacle in the path.
//     */
//    @Test
//    void testFirstMoveForwardOneSquare() {
//        // Move white pawn one square forward
//        VariantChessMove move = new VariantChessMove(6, 0, 5, 0);
//        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to move forward one square on its first move.");
//        board.movePiece(move);
//        whitePawn.setFirstMove(false);  // Mark the first move as completed
//
//        // Move black pawn one square forward
//        move = new VariantChessMove(1, 0, 2, 0);
//        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to move forward one square on its first move.");
//        board.movePiece(move);
//        blackPawn.setFirstMove(false);  // Mark the first move as completed
//    }
//
//    /**
//     * Tests that a pawn can move forward two squares on its first move.
//     * Ensures that pawns can advance two squares from their starting position if there is no obstacle.
//     */
//    @Test
//    void testFirstMoveForwardTwoSquares() {
//        // Move white pawn two squares forward
//        VariantChessMove move = new VariantChessMove(6, 0, 4, 0);
//        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to move forward two squares on its first move.");
//        board.movePiece(move);
//        whitePawn.setFirstMove(false);  // Mark the first move as completed
//
//        // Move black pawn two squares forward
//        move = new VariantChessMove(1, 0, 3, 0);
//        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to move forward two squares on its first move.");
//        board.movePiece(move);
//        blackPawn.setFirstMove(false);  // Mark the first move as completed
//    }
//
//    /**
//     * Tests that a pawn cannot move outside the board boundaries.
//     * Ensures that pawns remain within the board limits.
//     */
//    @Test
//    void testInvalidMoveOutOfBoard() {
//        // Attempt to move the white pawn outside the board
//        VariantChessMove move = new VariantChessMove(6, 0, 8, 0);
//        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move out of the board.");
//
//        // Attempt to move the black pawn outside the board
//        move = new VariantChessMove(1, 0, -1, 0);
//        assertFalse(blackPawn.isValidMove(move, board), "Pawn should not be able to move out of the board.");
//    }
//
//    /**
//     * Tests that a pawn cannot move backward.
//     * Ensures that pawns only move forward and cannot move to a previous row.
//     */
//    @Test
//    void testInvalidMoveBackward() {
//        // Attempt to move the white pawn backward
//        VariantChessMove move = new VariantChessMove(6, 0, 7, 0);
//        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move backward.");
//
//        // Attempt to move the black pawn backward
//        move = new VariantChessMove(1, 0, 0, 0);
//        assertFalse(blackPawn.isValidMove(move, board), "Pawn should not be able to move backward.");
//    }
//
//    /**
//     * Tests that a pawn cannot move forward if blocked by another piece.
//     * Ensures that pawns cannot advance if there is another piece directly in front.
//     */
//    @Test
//    void testMoveForwardBlockedByPiece() {
//        // Place a white rook directly in front of the white pawn
//        board.getBoard()[5][0] = new Rook(Color.WHITE);
//
//        // Attempt to move the white pawn two squares forward
//        VariantChessMove move = new VariantChessMove(6, 0, 4, 0);
//        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move forward two squares if blocked by another piece at one square.");
//
//        // Attempt to move the white pawn one square forward
//        move = new VariantChessMove(6, 0, 5, 0);
//        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move forward one square if blocked by another piece directly in front.");
//    }
//
//    /**
//     * Tests that a pawn can move forward one square after its first move.
//     * Ensures that pawns can move one square forward in subsequent moves after the first move.
//     */
//    @Test
//    void testMoveForwardAfterFirstMove() {
//        // Perform the first move for white pawn
//        board.movePiece(new VariantChessMove(6, 0, 5, 0));
//        whitePawn.setFirstMove(false);  // Mark the first move as completed
//
//        // Move the white pawn one square forward after the first move
//        VariantChessMove move = new VariantChessMove(5, 0, 4, 0);
//        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to move forward one square after the first move.");
//        board.movePiece(move);
//
//        // Perform the first move for black pawn
//        board.movePiece(new VariantChessMove(1, 0, 2, 0));
//        blackPawn.setFirstMove(false);  // Mark the first move as completed
//
//        // Move the black pawn one square forward after the first move
//        move = new VariantChessMove(2, 0, 3, 0);
//        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to move forward one square after the first move.");
//        board.movePiece(move);
//    }
//
//    /**
//     * Tests that a pawn cannot move past an obstacle.
//     * Ensures that pawns cannot move forward if there is another piece blocking their path.
//     */
//    @Test
//    void testBlockedMoveStopsBeforeObstacle() {
//        // Place a white rook directly in front of the white pawn
//        board.getBoard()[5][0] = new Rook(Color.WHITE);
//
//        // Attempt to move the white pawn two squares forward
//        VariantChessMove move = new VariantChessMove(6, 0, 4, 0);
//        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move two squares if blocked by another piece at one square.");
//
//        // Attempt to move the white pawn one square forward
//        move = new VariantChessMove(6, 0, 5, 0);
//        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move one square if blocked by another piece directly in front.");
//    }
}
