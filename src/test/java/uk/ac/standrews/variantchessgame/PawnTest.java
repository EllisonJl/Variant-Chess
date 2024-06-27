//package uk.ac.standrews.variantchessgame;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import uk.ac.standrews.variantchessgame.model.Color;
//import uk.ac.standrews.variantchessgame.model.Pawn;
//import uk.ac.standrews.variantchessgame.model.VariantChessBoard;
//import uk.ac.standrews.variantchessgame.model.VariantChessMove;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class PawnTest {
//
//    private VariantChessBoard board;
//    private Pawn whitePawn;
//    private Pawn blackPawn;
//
//    @BeforeEach
//    void setUp() {
//        board = new VariantChessBoard();
//        whitePawn = new Pawn(Color.WHITE);
//        blackPawn = new Pawn(Color.BLACK);
//        board.getBoard()[1][0] = whitePawn;
//        board.getBoard()[6][0] = blackPawn;
//    }
//
//    @Test
//    void testFirstMoveForwardOneSquare() {
//        VariantChessMove move = new VariantChessMove(1, 0, 2, 0);
//        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to move forward one square on its first move.");
//
//        move = new VariantChessMove(6, 0, 5, 0);
//        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to move forward one square on its first move.");
//    }
//
//    @Test
//    void testFirstMoveForwardTwoSquares() {
//        VariantChessMove move = new VariantChessMove(1, 0, 3, 0);
//        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to move forward two squares on its first move.");
//
//        move = new VariantChessMove(6, 0, 4, 0);
//        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to move forward two squares on its first move.");
//    }
//
//    @Test
//    void testInvalidMoveOutOfBoard() {
//        VariantChessMove move = new VariantChessMove(1, 0, 8, 0);
//        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move out of the board.");
//
//        move = new VariantChessMove(6, 0, -1, 0);
//        assertFalse(blackPawn.isValidMove(move, board), "Pawn should not be able to move out of the board.");
//    }
//
//    @Test
//    void testInvalidMoveBackward() {
//        VariantChessMove move = new VariantChessMove(1, 0, 0, 0);
//        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move backward.");
//
//        move = new VariantChessMove(6, 0, 7, 0);
//        assertFalse(blackPawn.isValidMove(move, board), "Pawn should not be able to move backward.");
//    }
//
//    @Test
//    void testMoveForwardBlockedByPiece() {
//        board.movePiece(new VariantChessMove(1, 0, 2, 0));  // Place a piece in front of the pawn
//
//        VariantChessMove move = new VariantChessMove(1, 0, 2, 0);
//        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move forward if blocked by another piece.");
//    }
//
//    @Test
//    void testCaptureMove() {
//        board.movePiece(new VariantChessMove(6, 1, 5, 1));  // Place a black piece diagonally in front of the white pawn
//
//        VariantChessMove move = new VariantChessMove(1, 0, 2, 1);
//        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to capture diagonally.");
//
//        board.movePiece(new VariantChessMove(1, 1, 2, 1));  // Place a white piece diagonally in front of the black pawn
//
//        move = new VariantChessMove(6, 0, 5, 1);
//        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to capture diagonally.");
//    }
//
//    @Test
//    void testMoveForwardAfterFirstMove() {
//        board.movePiece(new VariantChessMove(1, 0, 2, 0));  // First move for white pawn
//        whitePawn.isValidMove(new VariantChessMove(2, 0, 3, 0), board);  // Ensure the first move is registered
//
//        VariantChessMove move = new VariantChessMove(2, 0, 3, 0);
//        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to move forward one square after the first move.");
//
//        board.movePiece(new VariantChessMove(6, 0, 5, 0));  // First move for black pawn
//        blackPawn.isValidMove(new VariantChessMove(5, 0, 4, 0), board);  // Ensure the first move is registered
//
//        move = new VariantChessMove(5, 0, 4, 0);
//        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to move forward one square after the first move.");
//    }
//
//    @Test
//    void testMoveSidewaysAfterFirstMove() {
//        board.movePiece(new VariantChessMove(1, 0, 2, 0));  // First move for white pawn
//        whitePawn.isValidMove(new VariantChessMove(2, 0, 2, 1), board);  // Ensure the first move is registered
//
//        VariantChessMove move = new VariantChessMove(2, 0, 2, 1);
//        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to move sideways after the first move.");
//
//        board.movePiece(new VariantChessMove(6, 0, 5, 0));  // First move for black pawn
//        blackPawn.isValidMove(new VariantChessMove(5, 0, 5, 1), board);  // Ensure the first move is registered
//
//        move = new VariantChessMove(5, 0, 5, 1);
//        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to move sideways after the first move.");
//    }
//}
