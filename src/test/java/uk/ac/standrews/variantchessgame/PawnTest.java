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
        board.getBoard()[1][0] = whitePawn;
        board.getBoard()[6][0] = blackPawn;
    }

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

//    @Test
//    void testMoveSidewaysAfterFirstMove() {
//        VariantChessBoard board = new VariantChessBoard();
//        Pawn whitePawn = new Pawn(Color.WHITE);
//        Pawn blackPawn = new Pawn(Color.BLACK);
//
//        // 放置白兵在 (1, 0)
//        board.getBoard()[1][0] = whitePawn;
//        // 让白兵向前移动到 (2, 0)
//        VariantChessMove firstMove = new VariantChessMove(1, 0, 2, 0);
//        assertTrue(whitePawn.isValidMove(firstMove, board), "First move for white pawn should be valid.");
//        board.movePiece(firstMove);
//
//        // 确保isFirstMove已经更新
//        whitePawn.isValidMove(new VariantChessMove(2, 0, 2, 1), board);
//
//        // 测试白兵向右移动到 (2, 1)
//        VariantChessMove move = new VariantChessMove(2, 0, 2, 1);
//        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to move sideways after the first move.");
//        // 执行移动以模拟实际棋局
//        board.movePiece(move);
//
//        // 放置黑兵在 (6, 0)
//        board.getBoard()[6][0] = blackPawn;
//        // 让黑兵向前移动到 (5, 0)
//        firstMove = new VariantChessMove(6, 0, 5, 0);
//        assertTrue(blackPawn.isValidMove(firstMove, board), "First move for black pawn should be valid.");
//        board.movePiece(firstMove);
//
//        // 确保isFirstMove已经更新
//        blackPawn.isValidMove(new VariantChessMove(5, 0, 5, 1), board);
//
//        // 测试黑兵向左移动到 (5, 1)
//        move = new VariantChessMove(5, 0, 5, 1);
//        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to move sideways after the first move.");
//        // 执行移动以模拟实际棋局
//        board.movePiece(move);
//    }

    @Test
    void testCaptureMove() {
        VariantChessBoard board = new VariantChessBoard();
        Pawn whitePawn = new Pawn(Color.WHITE);
        Pawn blackPawn = new Pawn(Color.BLACK);

        // 放置白兵在 (1, 0) 和黑棋子在 (2, 1)
        board.getBoard()[1][0] = whitePawn;
        board.getBoard()[2][1] = new Rook(Color.BLACK);  // 任意非兵的黑棋子

        // 测试白兵对角线吃子
        VariantChessMove move = new VariantChessMove(1, 0, 2, 1);
        assertTrue(whitePawn.isValidMove(move, board), "White pawn should be able to capture diagonally.");

        // 放置黑兵在 (6, 0) 和白棋子在 (5, 1)
        board.getBoard()[6][0] = blackPawn;
        board.getBoard()[5][1] = new Rook(Color.WHITE);  // 任意非兵的白棋子

        // 测试黑兵对角线吃子
        move = new VariantChessMove(6, 0, 5, 1);
        assertTrue(blackPawn.isValidMove(move, board), "Black pawn should be able to capture diagonally.");
    }

}
