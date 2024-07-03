package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

class QueenTest {

    private VariantChessBoard board;
    private Queen whiteQueen;
    private Queen blackQueen;

    @BeforeEach
    void setUp() {
        board = new VariantChessBoard();
        whiteQueen = new Queen(Color.WHITE);
        blackQueen = new Queen(Color.BLACK);
        // 手动设置测试位置，避免与初始化棋盘冲突
        board.getBoard()[4][4] = whiteQueen;
    }

    @Test
    void testValidMove() {
        // 有效的直线移动到空位 (4, 7)
        board.getBoard()[4][7] = null; // 确保目标位置为空
        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertTrue(whiteQueen.isValidMove(move, board), "Queen should be able to move in a straight line.");
        board.movePiece(move);
        assertEquals(whiteQueen, board.getPieceAt(4, 7), "Queen should be at the new position after move.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");

        // 有效的对角线移动到空位 (6, 6)
        board.getBoard()[6][6] = null; // 确保目标位置为空
        move = new VariantChessMove(4, 7, 6, 5);
        assertTrue(whiteQueen.isValidMove(move, board), "Queen should be able to move diagonally.");
        board.movePiece(move);
        assertEquals(whiteQueen, board.getPieceAt(6, 5), "Queen should be at the new position after move.");
        assertNull(board.getPieceAt(4, 7), "Original position should be empty after move.");
    }

    @Test
    void testInvalidMoveOutOfBoard() {
        // 移动到棋盘外
        VariantChessMove move = new VariantChessMove(4, 4, 8, 8);
        assertFalse(whiteQueen.isValidMove(move, board), "Queen should not be able to move outside the board.");
    }

    @Test
    void testInvalidMoveNotStraightOrDiagonal() {
        // 非直线或对角线移动
        VariantChessMove move = new VariantChessMove(4, 4, 5, 6);
        assertFalse(whiteQueen.isValidMove(move, board), "Queen should not be able to move non-straight or non-diagonally.");
    }

    @Test
    void testCaptureMove() {
        // 放置黑兵在 (4, 7)
        board.getBoard()[4][7] = new Pawn(Color.BLACK);

        // 尝试吃掉黑兵
        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertTrue(whiteQueen.isValidMove(move, board), "Queen should be able to capture an enemy piece by moving in a straight line.");
        assertTrue(move.isCapture(), "Move should be marked as a capture.");
        board.movePiece(move);
        assertEquals(whiteQueen, board.getPieceAt(4, 7), "Queen should be at the new position after capture.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after capture.");
    }

    @Test
    void testInvalidCaptureOwnPiece() {
        // 放置白兵在 (4, 7)
        board.getBoard()[4][7] = new Pawn(Color.WHITE);

        // 尝试吃掉自己的白兵
        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertFalse(whiteQueen.isValidMove(move, board), "Queen should not be able to capture its own piece.");
    }

    @Test
    void testQueenColorAfterMove() {
        // 移动白后到 (4, 7)
        board.getBoard()[4][7] = null; // 确保目标位置为空
        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertTrue(whiteQueen.isValidMove(move, board), "Queen should be able to move in a straight line.");
        board.movePiece(move);
        assertEquals(whiteQueen, board.getPieceAt(4, 7), "Queen should be at the new position after move.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");

        // 检查移动后的棋子颜色是否仍然是白色
        VariantChessPiece piece = board.getPieceAt(4, 7);
        assertNotNull(piece, "There should be a piece at the new position.");
        assertTrue(piece instanceof Queen, "The piece at the new position should be a Queen.");
        assertEquals(Color.WHITE, piece.getColor(), "The Queen should remain white after the move.");
    }
}
