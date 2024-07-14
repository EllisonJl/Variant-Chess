package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {

    private VariantChessBoard board;
    private Pawn whitePawn;
    private Pawn blackPawn;

    @BeforeEach
    void setUp() {
        board = new VariantChessBoard();
        whitePawn = new Pawn(Color.WHITE);
        blackPawn = new Pawn(Color.BLACK);
        board.getBoard()[6][0] = whitePawn;  // 白棋兵从第六行开始
        board.getBoard()[1][0] = blackPawn;  // 黑棋兵从第一行开始
    }
//    @Test
//    void testCaptureMove() {
//        // 放置黑棋子在前方一格 (5, 0)
//        board.getBoard()[5][0] = new Rook(Color.BLACK);
//        VariantChessMove move = new VariantChessMove(6, 0, 5, 0);
//        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to capture forward after first move.");
//        board.movePiece(move);
//
//        // 放置黑棋子在前方两格 (3, 0)，并清空中间位置
//        board.getBoard()[4][0] = null;
//        board.getBoard()[3][0] = new Rook(Color.BLACK);
//        move = new VariantChessMove(5, 0, 3, 0);
//        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to capture forward two squares after first move.");
//        board.movePiece(move);
//
//        // 放置白棋子在前方一格 (2, 0)
//        board.getBoard()[2][0] = new Rook(Color.WHITE);
//        move = new VariantChessMove(1, 0, 2, 0);
//        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to capture forward after first move.");
//        board.movePiece(move);
//
//        // 放置白棋子在前方两格 (4, 0)，并清空中间位置
//        board.getBoard()[3][0] = null;
//        board.getBoard()[4][0] = new Rook(Color.WHITE);
//        move = new VariantChessMove(2, 0, 4, 0);
//        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to capture forward two squares after first move.");
//        board.movePiece(move);
//    }

    @Test
    void testCaptureMoveAfterFirstMove() {
        // 白兵第一次移动到 (4, 0)
        VariantChessMove firstMove = new VariantChessMove(6, 0, 4, 0);
        assertTrue(whitePawn.isValidMove(firstMove, board), "White pawn should be able to move forward two squares on its first move.");
        board.movePiece(firstMove);
        whitePawn.setFirstMove(false);

        // 放置黑棋子在前方一格 (3, 0)
        board.getBoard()[3][0] = new Rook(Color.BLACK);
        VariantChessMove move = new VariantChessMove(4, 0, 3, 0);
        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to capture forward after first move.");
        board.movePiece(move);

        // 黑兵第一次移动到 (3, 0)
        VariantChessMove firstMoveBlack = new VariantChessMove(1, 0, 3, 0);
        assertTrue(blackPawn.isValidMove(firstMoveBlack, board), "Black pawn should be able to move forward two squares on its first move.");
        board.movePiece(firstMoveBlack);
        blackPawn.setFirstMove(false);

        // 放置白棋子在前方一格 (4, 0)
        board.getBoard()[4][0] = new Rook(Color.WHITE);
        move = new VariantChessMove(3, 0, 4, 0);
        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to capture forward after first move.");
        board.movePiece(move);
    }
        @Test
    void testCaptureMoveLeftRight() {
        // 放置黑棋子在左侧 (6, 1)
        board.getBoard()[6][1] = new Rook(Color.BLACK);
        VariantChessMove move = new VariantChessMove(6, 0, 6, 1);
        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to capture right after first move.");
        board.movePiece(move);

        // 放置黑棋子在右侧 (6, 1)
        board.getBoard()[6][1] = new Rook(Color.BLACK);
        move = new VariantChessMove(6, 0, 6, 1);
        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to capture right after first move.");
        board.movePiece(move);

        // 放置白棋子在左侧 (1, 1)
        board.getBoard()[1][1] = new Rook(Color.WHITE);
        move = new VariantChessMove(1, 0, 1, 1);
        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to capture right after first move.");
        board.movePiece(move);

        // 放置白棋子在右侧 (1, 1)
        board.getBoard()[1][1] = new Rook(Color.WHITE);
        move = new VariantChessMove(1, 0, 1, 1);
        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to capture right after first move.");
        board.movePiece(move);
    }
    @Test
    void testFirstMoveForwardOneSquare() {
        VariantChessMove move = new VariantChessMove(6, 0, 5, 0);
        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to move forward one square on its first move.");
        board.movePiece(move);
        whitePawn.setFirstMove(false);

        move = new VariantChessMove(1, 0, 2, 0);
        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to move forward one square on its first move.");
        board.movePiece(move);
        blackPawn.setFirstMove(false);
    }

    @Test
    void testFirstMoveForwardTwoSquares() {
        VariantChessMove move = new VariantChessMove(6, 0, 4, 0);
        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to move forward two squares on its first move.");
        board.movePiece(move);
        whitePawn.setFirstMove(false);

        move = new VariantChessMove(1, 0, 3, 0);
        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to move forward two squares on its first move.");
        board.movePiece(move);
        blackPawn.setFirstMove(false);
    }

    @Test
    void testInvalidMoveOutOfBoard() {
        VariantChessMove move = new VariantChessMove(6, 0, 8, 0);
        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move out of the board.");

        move = new VariantChessMove(1, 0, -1, 0);
        assertFalse(blackPawn.isValidMove(move, board), "Pawn should not be able to move out of the board.");
    }

    @Test
    void testInvalidMoveBackward() {
        VariantChessMove move = new VariantChessMove(6, 0, 7, 0);
        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move backward.");

        move = new VariantChessMove(1, 0, 0, 0);
        assertFalse(blackPawn.isValidMove(move, board), "Pawn should not be able to move backward.");
    }

    @Test
    void testMoveForwardBlockedByPiece() {
        board.getBoard()[5][0] = new Rook(Color.WHITE);  // Place a piece in front of the white pawn

        VariantChessMove move = new VariantChessMove(6, 0, 4, 0);
        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move forward two squares if blocked by another piece.");

        move = new VariantChessMove(6, 0, 5, 0);
        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move forward one square if blocked by another piece directly in front.");
    }

    @Test
    void testMoveForwardAfterFirstMove() {
        board.movePiece(new VariantChessMove(6, 0, 5, 0));  // First move for white pawn
        whitePawn.setFirstMove(false);  // Ensure the first move is registered

        VariantChessMove move = new VariantChessMove(5, 0, 4, 0);
        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to move forward one square after the first move.");
        board.movePiece(move);

        board.movePiece(new VariantChessMove(1, 0, 2, 0));  // First move for black pawn
        blackPawn.setFirstMove(false);  // Ensure the first move is registered

        move = new VariantChessMove(2, 0, 3, 0);
        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to move forward one square after the first move.");
        board.movePiece(move);
    }

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



    @Test
    void testBlockedMoveStopsBeforeObstacle() {
        board.getBoard()[5][0] = new Rook(Color.WHITE);  // Place a piece in front of the white pawn

        VariantChessMove move = new VariantChessMove(6, 0, 4, 0);
        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move two squares if blocked by another piece at one square.");

        move = new VariantChessMove(6, 0, 5, 0);
        assertFalse(whitePawn.isValidMove(move, board), "Pawn should not be able to move one square if blocked by another piece directly in front.");
    }

    @Test
    void testPawnCaptureAfterFirstTwoSquaresMove() {
        // 白兵第一次移动到 (4, 0)
        VariantChessMove firstMove = new VariantChessMove(6, 0, 4, 0);
        assertTrue(whitePawn.isValidMove(firstMove, board), "White pawn should be able to move forward two squares on its first move.");
        board.movePiece(firstMove);
        whitePawn.setFirstMove(false);

        // 在(4, 1)放置一个黑色棋子
        board.getBoard()[4][1] = new Rook(Color.BLACK);

        // 尝试向右吃掉黑色棋子
        VariantChessMove captureMove = new VariantChessMove(4, 0, 4, 1);
        assertTrue(whitePawn.isValidMove(captureMove, board), "White pawn should be able to capture right after first move.");
        board.movePiece(captureMove);

        // 验证吃掉后的位置和状态
        assertEquals(whitePawn, board.getPieceAt(4, 1), "White pawn should be at (4, 1) after capturing.");
        assertNull(board.getPieceAt(4, 0), "The original position (4, 0) should be empty after capturing.");
    }

    @Test
    void testSecondMoveAfterFirstMove() {
        // 白兵第一次移动到 (4, 0)
        VariantChessMove firstMove = new VariantChessMove(6, 0, 4, 0);
        assertTrue(whitePawn.isValidMove(firstMove, board), "White pawn should be able to move forward two squares on its first move.");
        board.movePiece(firstMove);
        whitePawn.setFirstMove(false);

        // 尝试第二次移动到 (3, 0)
        VariantChessMove secondMove1 = new VariantChessMove(4, 0, 3, 0);
        assertTrue(whitePawn.isValidMove(secondMove1, board), "White pawn should be able to move forward one square on its second move.");
        board.movePiece(secondMove1);

        // 验证第二次移动后的位置和状态
        assertEquals(whitePawn, board.getPieceAt(3, 0), "White pawn should be at (3, 0) after the second move.");
        assertNull(board.getPieceAt(4, 0), "The original position (4, 0) should be empty after the second move.");

        // 黑兵第一次移动到 (3, 0)
        VariantChessMove firstMoveBlack = new VariantChessMove(1, 0, 3, 0);
        assertTrue(blackPawn.isValidMove(firstMoveBlack, board), "Black pawn should be able to move forward two squares on its first move.");
        board.movePiece(firstMoveBlack);
        blackPawn.setFirstMove(false);

        // 尝试第二次移动到 (4, 0)
        VariantChessMove secondMoveBlack = new VariantChessMove(3, 0, 4, 0);
        assertTrue(blackPawn.isValidMove(secondMoveBlack, board), "Black pawn should be able to move forward one square on its second move.");
        board.movePiece(secondMoveBlack);

        // 验证第二次移动后的位置和状态
        assertEquals(blackPawn, board.getPieceAt(4, 0), "Black pawn should be at (4, 0) after the second move.");
        assertNull(board.getPieceAt(3, 0), "The original position (3, 0) should be empty after the second move.");
    }
}
