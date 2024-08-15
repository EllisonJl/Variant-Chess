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
     * Tests that a pawn cannot move sideways on its first move.
     * Ensures that pawns can only move forward and not sideways initially.
     */
    @Test
    void testCannotMoveSidewaysOnFirstMove() {
        VariantChessMove moveLeft = new VariantChessMove(6, 0, 6, 1);  // Attempt to move white pawn to the right
        assertFalse(whitePawn.isValidMove(moveLeft, board), "White pawn should not be able to move sideways on its first move.");

        VariantChessMove moveRight = new VariantChessMove(6, 0, 6, -1);  // Attempt to move white pawn to the left
        assertFalse(whitePawn.isValidMove(moveRight, board), "White pawn should not be able to move sideways on its first move.");

        moveLeft = new VariantChessMove(1, 0, 1, 1);  // Attempt to move black pawn to the right
        assertFalse(blackPawn.isValidMove(moveLeft, board), "Black pawn should not be able to move sideways on its first move.");

        moveRight = new VariantChessMove(1, 0, 1, -1);  // Attempt to move black pawn to the left
        assertFalse(blackPawn.isValidMove(moveRight, board), "Black pawn should not be able to move sideways on its first move.");
    }

    /**
     * Tests that a pawn can capture an opponent's piece directly in front of it.
     * Ensures that the pawn can capture correctly when the opponent's piece is directly in the path.
     */
    @Test
    void testCaptureMove() {
        // Place a black rook directly in front of the white pawn
        board.getBoard()[5][0] = new Rook(Color.BLACK);
        VariantChessMove move = new VariantChessMove(6, 0, 5, 0);
        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to capture forward after first move.");
        board.movePiece(move);

        // Place another black rook directly in front of the white pawn
        board.getBoard()[4][0] = new Rook(Color.BLACK);
        move = new VariantChessMove(5, 0, 4, 0);
        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to capture forward one square after first move.");
        board.movePiece(move);

        // Place a white rook directly in front of the black pawn
        board.getBoard()[2][0] = new Rook(Color.WHITE);
        move = new VariantChessMove(1, 0, 2, 0);
        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to capture forward after first move.");
        board.movePiece(move);

        // Place another white rook directly in front of the black pawn
        board.getBoard()[3][0] = new Rook(Color.WHITE);
        move = new VariantChessMove(2, 0, 3, 0);
        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to capture forward one square after first move.");
        board.movePiece(move);
    }

    /**
     * Tests that a pawn cannot move sideways after the first move.
     * Ensures that pawns can only move forward and not sideways.
     */
    @Test
    void testMoveSidewaysAfterFirstMove() {
        board.movePiece(new VariantChessMove(6, 0, 5, 0));  // First move for white pawn
        whitePawn.setFirstMove(false);  // Ensure the first move is registered

        VariantChessMove move = new VariantChessMove(5, 0, 5, 1);
        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to move sideways after the first move.");

        board.movePiece(new VariantChessMove(1, 0, 2, 0));  // First move for black pawn
        blackPawn.setFirstMove(false);  // Ensure the first move is registered

        move = new VariantChessMove(2, 0, 2, 1);
        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to move sideways after the first move.");
    }

    /**
     * Tests that a pawn can capture an opponent's piece after moving two squares forward on its first move.
     * Ensures that pawns can perform captures after advancing two squares initially.
     */
    @Test
    void testPawnCaptureAfterFirstTwoSquaresMove() {
        // Move white pawn two squares forward
        VariantChessMove firstMove = new VariantChessMove(6, 0, 4, 0);
        assertTrue(whitePawn.isValidMove(firstMove, board), "White pawn should be able to move forward two squares on its first move.");
        board.movePiece(firstMove);
        whitePawn.setFirstMove(false);  // Mark the first move as completed

        // Place a black rook to the right of the white pawn
        board.getBoard()[4][1] = new Rook(Color.BLACK);

        // Attempt to capture the black rook
        VariantChessMove captureMove = new VariantChessMove(4, 0, 4, 1);
        assertTrue(whitePawn.isValidMove(captureMove, board), "White pawn should be able to capture right after first move.");
        board.movePiece(captureMove);

        // Verify the position and status after capturing
        assertEquals(whitePawn, board.getPieceAt(4, 1), "White pawn should be at (4, 1) after capturing.");
        assertNull(board.getPieceAt(4, 0), "The original position (4, 0) should be empty after capturing.");
    }

    /**
     * Tests that a pawn can capture an opponent's piece in the forward, left, or right position
     * after moving forward. Ensures that after a pawn moves forward, it can capture enemy pieces
     * located in the forward, left, or right squares.
     */
    @Test
    void testCaptureMoveLeftRightForwardAfterMove() {
        // Move the white pawn forward by one square
        VariantChessMove moveForward = new VariantChessMove(6, 0, 5, 0);
        assertTrue(whitePawn.isValidMove(moveForward, board), "White pawn should be able to move forward one square.");
        board.movePiece(moveForward);
        whitePawn.setFirstMove(false);

        // Place a black rook directly in front of the white pawn
        board.getBoard()[4][0] = new Rook(Color.BLACK);
        VariantChessMove moveCaptureForward = new VariantChessMove(5, 0, 4, 0);
        assertTrue(whitePawn.isValidMove(moveCaptureForward, board), "White pawn should be able to capture forward.");
        board.movePiece(moveCaptureForward);

        // Reset white pawn position for sideways capture testing
        board.getBoard()[5][0] = whitePawn;
        board.getBoard()[4][0] = null;

        // Place a black rook to the right of the white pawn
        board.getBoard()[5][1] = new Rook(Color.BLACK);
        VariantChessMove moveCaptureRight = new VariantChessMove(5, 0, 5, 1);
        assertTrue(whitePawn.isValidMove(moveCaptureRight, board), "White pawn should be able to capture right.");
        board.movePiece(moveCaptureRight);

        // Reset white pawn position for left capture testing
        board.getBoard()[5][0] = whitePawn;
        board.getBoard()[5][1] = null;

        // Place a black rook to the left of the white pawn
        board.getBoard()[5][1] = new Rook(Color.BLACK);
        VariantChessMove moveCaptureLeft = new VariantChessMove(5, 0, 5, 1);
        assertTrue(whitePawn.isValidMove(moveCaptureLeft, board), "White pawn should be able to capture left.");
        board.movePiece(moveCaptureLeft);

        // Move the black pawn forward by one square
        moveForward = new VariantChessMove(1, 0, 2, 0);
        assertTrue(blackPawn.isValidMove(moveForward, board), "Black pawn should be able to move forward one square.");
        board.movePiece(moveForward);
        blackPawn.setFirstMove(false);

        // Place a white rook directly in front of the black pawn
        board.getBoard()[3][0] = new Rook(Color.WHITE);
        moveCaptureForward = new VariantChessMove(2, 0, 3, 0);
        assertTrue(blackPawn.isValidMove(moveCaptureForward, board), "Black pawn should be able to capture forward.");
        board.movePiece(moveCaptureForward);

        // Reset black pawn position for sideways capture testing
        board.getBoard()[2][0] = blackPawn;
        board.getBoard()[3][0] = null;

        // Place a white rook to the right of the black pawn
        board.getBoard()[2][1] = new Rook(Color.WHITE);
        moveCaptureRight = new VariantChessMove(2, 0, 2, 1);
        assertTrue(blackPawn.isValidMove(moveCaptureRight, board), "Black pawn should be able to capture right.");
        board.movePiece(moveCaptureRight);

        // Reset black pawn position for left capture testing
        board.getBoard()[2][0] = blackPawn;
        board.getBoard()[2][1] = null;

        // Place a white rook to the left of the black pawn
        board.getBoard()[2][1] = new Rook(Color.WHITE);
        moveCaptureLeft = new VariantChessMove(2, 0, 2, 1);
        assertTrue(blackPawn.isValidMove(moveCaptureLeft, board), "Black pawn should be able to capture left.");
        board.movePiece(moveCaptureLeft);
    }

    /**
     * Tests that a pawn can move forward one square on its first move.
     * Ensures that pawns can advance one square if there is no obstacle in the path.
     */
    @Test
    void testFirstMoveForwardOneSquare() {
        // Move white pawn one square forward
        VariantChessMove move = new VariantChessMove(6, 0, 5, 0);
        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to move forward one square on its first move.");
        board.movePiece(move);
        whitePawn.setFirstMove(false);  // Mark the first move as completed

        // Move black pawn one square forward
        move = new VariantChessMove(1, 0, 2, 0);
        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to move forward one square on its first move.");
        board.movePiece(move);
        blackPawn.setFirstMove(false);  // Mark the first move as completed
    }

    /**
     * Tests that a pawn can move forward two squares on its first move.
     * Ensures that pawns can advance two squares from their starting position if there is no obstacle.
     */
    @Test
    void testFirstMoveForwardTwoSquares() {
        // Move white pawn two squares forward
        VariantChessMove move = new VariantChessMove(6, 0, 4, 0);
        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to move forward two squares on its first move.");
        board.movePiece(move);
        whitePawn.setFirstMove(false);  // Mark the first move as completed

        // Move black pawn two squares forward
        move = new VariantChessMove(1, 0, 3, 0);
        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to move forward two squares on its first move.");
        board.movePiece(move);
        blackPawn.setFirstMove(false);  // Mark the first move as completed
    }

    /**
     * Tests that a pawn cannot move outside the board boundaries.
     * Ensures that pawns remain within the board limits.
     */
    @Test
    void testInvalidMoveOutOfBoard() {
        // Attempt to move the white pawn outside the board
        VariantChessMove move = new VariantChessMove(6, 0, 8, 0);
        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move out of the board.");

        // Attempt to move the black pawn outside the board
        move = new VariantChessMove(1, 0, -1, 0);
        assertFalse(blackPawn.isValidMove(move, board), "Pawn should not be able to move out of the board.");
    }

    /**
     * Tests that a pawn cannot move backward.
     * Ensures that pawns only move forward and cannot move to a previous row.
     */
    @Test
    void testInvalidMoveBackward() {
        // Attempt to move the white pawn backward
        VariantChessMove move = new VariantChessMove(6, 0, 7, 0);
        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move backward.");

        // Attempt to move the black pawn backward
        move = new VariantChessMove(1, 0, 0, 0);
        assertFalse(blackPawn.isValidMove(move, board), "Pawn should not be able to move backward.");
    }

    /**
     * Tests that a pawn cannot move forward if blocked by another piece.
     * Ensures that pawns cannot advance if there is another piece directly in front.
     */
    @Test
    void testMoveForwardBlockedByPiece() {
        // Place a white rook directly in front of the white pawn
        board.getBoard()[5][0] = new Rook(Color.WHITE);

        // Attempt to move the white pawn two squares forward
        VariantChessMove move = new VariantChessMove(6, 0, 4, 0);
        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move forward two squares if blocked by another piece at one square.");

        // Attempt to move the white pawn one square forward
        move = new VariantChessMove(6, 0, 5, 0);
        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move forward one square if blocked by another piece directly in front.");
    }

    /**
     * Tests that a pawn can move forward one square after its first move.
     * Ensures that pawns can move one square forward in subsequent moves after the first move.
     */
    @Test
    void testMoveForwardAfterFirstMove() {
        // Perform the first move for white pawn
        board.movePiece(new VariantChessMove(6, 0, 5, 0));
        whitePawn.setFirstMove(false);  // Mark the first move as completed

        // Move the white pawn one square forward after the first move
        VariantChessMove move = new VariantChessMove(5, 0, 4, 0);
        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to move forward one square after the first move.");
        board.movePiece(move);

        // Perform the first move for black pawn
        board.movePiece(new VariantChessMove(1, 0, 2, 0));
        blackPawn.setFirstMove(false);  // Mark the first move as completed

        // Move the black pawn one square forward after the first move
        move = new VariantChessMove(2, 0, 3, 0);
        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to move forward one square after the first move.");
        board.movePiece(move);
    }

    /**
     * Tests that a pawn cannot move past an obstacle.
     * Ensures that pawns cannot move forward if there is another piece blocking their path.
     */
    @Test
    void testBlockedMoveStopsBeforeObstacle() {
        // Place a white rook directly in front of the white pawn
        board.getBoard()[5][0] = new Rook(Color.WHITE);

        // Attempt to move the white pawn two squares forward
        VariantChessMove move = new VariantChessMove(6, 0, 4, 0);
        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move two squares if blocked by another piece at one square.");

        // Attempt to move the white pawn one square forward
        move = new VariantChessMove(6, 0, 5, 0);
        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move one square if blocked by another piece directly in front.");
    }

    /**
     * Tests that a black pawn cannot move sideways to an empty square on its first move.
     * Ensures that the pawn is restricted to moving forward or capturing diagonally on the first move.
     */
    @Test
    void testBlackPawnCannotMoveSidewaysWithoutCaptureOnFirstMove() {
        // Attempt to move the black pawn one square to the right to an empty square
        VariantChessMove moveRight = new VariantChessMove(1, 0, 1, 1);
        assertFalse(blackPawn.isValidMove(moveRight, board), "Black pawn should not be able to move sideways to the right on its first move without capture.");

        // Attempt to move the black pawn one square to the left to an empty square
        VariantChessMove moveLeft = new VariantChessMove(1, 0, 1, -1);
        assertFalse(blackPawn.isValidMove(moveLeft, board), "Black pawn should not be able to move sideways to the left on its first move without capture.");
    }


    /**
     * Tests that a black pawn cannot capture pieces directly to its left or right on its first move.
     * This test clears the board and sets up a specific scenario to verify the pawn's movement restrictions.
     */
    @Test
    void testBlackPawnCannotCaptureSidewaysOnFirstMove() {
        // Clear the board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board.getBoard()[row][col] = null;
            }
        }

        // Place the black pawn at position (1, 3)
        board.getBoard()[1][3] = blackPawn;

        // Place white pieces directly to the left and right of the black pawn
        board.getBoard()[1][2] = new Rook(Color.WHITE); // White piece to the left
        board.getBoard()[1][4] = new Rook(Color.WHITE); // White piece to the right

        // Attempt to capture the piece to the right
        VariantChessMove captureRight = new VariantChessMove(1, 3, 1, 4);
        assertFalse(blackPawn.isValidMove(captureRight, board), "Black pawn should not be able to capture directly to the right on its first move.");

        // Attempt to capture the piece to the left
        VariantChessMove captureLeft = new VariantChessMove(1, 3, 1, 2);
        assertFalse(blackPawn.isValidMove(captureLeft, board), "Black pawn should not be able to capture directly to the left on its first move.");
    }
    /**
     * Tests that after White's first move, the Black pawn can move forward two squares on its first move.
     * This test ensures that Black can still perform the initial two-square move after White has made a move.
     */
    @Test
    void testBlackPawnCanMoveTwoSquaresAfterWhiteMove() {
        // White pawn moves one square forward
        VariantChessMove whiteMove = new VariantChessMove(6, 0, 5, 0);
        assertTrue(whitePawn.isValidMove(whiteMove, board), "White pawn should be able to move forward one square.");
        board.movePiece(whiteMove);
        whitePawn.setFirstMove(false);  // Mark White's first move as completed

        // Black pawn attempts to move two squares forward
        VariantChessMove blackMove = new VariantChessMove(1, 0, 3, 0);
        assertTrue(blackPawn.isValidMove(blackMove, board), "Black pawn should be able to move forward two squares on its first move after White's move.");
        board.movePiece(blackMove);
        blackPawn.setFirstMove(false);  // Mark Black's first move as completed

        // Verify that the black pawn is in the correct position
        assertEquals(blackPawn, board.getPieceAt(3, 0), "Black pawn should be at (3, 0) after moving two squares.");
        assertNull(board.getPieceAt(1, 0), "The original position (1, 0) should be empty after Black pawn moves.");
    }
    /**
     * Tests that a black pawn cannot move sideways after the white pawn has made its first move.
     * This test checks if the black pawn's first move is restricted to forward movement,
     * even after the white pawn has moved. The black pawn should not be able to move
     * left or right on its first move.
     */
    @Test
    void testBlackPawnCannotMoveSidewaysAfterWhiteMove() {
        // Clear the board before setting up the test scenario
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board.getBoard()[row][col] = null;
            }
        }
        // Place the white pawn at its starting position (6, 0)
        board.getBoard()[6][0] = whitePawn;

        // Place the black pawn at its starting position (1, 0)
        board.getBoard()[1][0] = blackPawn;

        // Perform the first move for the white pawn (moving it one square forward)
        VariantChessMove whiteMove = new VariantChessMove(6, 0, 5, 0);
        assertTrue(whitePawn.isValidMove(whiteMove, board), "White pawn should be able to move forward one square.");
        board.movePiece(whiteMove);
        whitePawn.setFirstMove(false);  // Mark the white pawn's first move as completed

        // Attempt to move the black pawn one square to the right (this should fail)
        VariantChessMove blackMoveRight = new VariantChessMove(1, 0, 1, 1);
        assertFalse(blackPawn.isValidMove(blackMoveRight, board), "Black pawn should not be able to move sideways to the right on its first move after White's move.");

        // Attempt to move the black pawn one square to the left (this should fail)
        VariantChessMove blackMoveLeft = new VariantChessMove(1, 0, 1, -1);
        assertFalse(blackPawn.isValidMove(blackMoveLeft, board), "Black pawn should not be able to move sideways to the left on its first move after White's move.");

        // Verify that the black pawn has not moved from its original position (1, 0)
        assertEquals(blackPawn, board.getPieceAt(1, 0), "Black pawn should remain at its original position (1, 0).");

        // Verify that the positions (1, 1) and (1, -1) are empty (i.e., the pawn did not move sideways)
        assertNull(board.getPieceAt(1, 1), "The position (1, 1) should be empty as the pawn should not have moved to the right.");
        assertNull(board.getPieceAt(1, -1), "The position (1, -1) should be empty as the pawn should not have moved to the left.");
    }


}

