package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {

    private VariantChessBoard board;
    private King whiteKing;
    private King blackKing;

    @BeforeEach
    void setUp() {
        board = new VariantChessBoard();
        whiteKing = new King(Color.WHITE);
        blackKing = new King(Color.BLACK);
        // 手动设置测试位置，避免与初始化棋盘冲突
        board.getBoard()[4][4] = whiteKing;
    }

    @Test
    void testValidMove() {
        // 有效的直线移动到空位 (4, 5)
        VariantChessMove move = new VariantChessMove(4, 4, 4, 5);
        assertTrue(whiteKing.isValidMove(move, board), "King should be able to move one square.");
        board.movePiece(move);
        assertEquals(whiteKing, board.getPieceAt(4, 5), "King should be at the new position after move.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");

        // 有效的对角线移动到空位 (5, 5)
        move = new VariantChessMove(4, 5, 5, 5);
        assertTrue(whiteKing.isValidMove(move, board), "King should be able to move one square diagonally.");
        board.movePiece(move);
        assertEquals(whiteKing, board.getPieceAt(5, 5), "King should be at the new position after move.");
        assertNull(board.getPieceAt(4, 5), "Original position should be empty after move.");
    }

    @Test
    void testInvalidMoveOutOfBoard() {
        // 移动到棋盘外
        VariantChessMove move = new VariantChessMove(4, 4, 8, 4);
        assertFalse(whiteKing.isValidMove(move, board), "King should not be able to move outside the board.");
    }

    @Test
    void testInvalidMoveMoreThanOneSquare() {
        // 移动超过一格
        VariantChessMove move = new VariantChessMove(4, 4, 6, 4);
        assertFalse(whiteKing.isValidMove(move, board), "King should not be able to move more than one square.");
    }

    @Test
    void testCaptureMove() {
        // 放置黑兵在 (4, 5)
        board.getBoard()[4][5] = new Pawn(Color.BLACK);

        // 尝试吃掉黑兵
        VariantChessMove move = new VariantChessMove(4, 4, 4, 5);
        assertTrue(whiteKing.isValidMove(move, board), "King should be able to capture an enemy piece by moving one square.");
        assertTrue(move.isCapture(), "Move should be marked as a capture.");
        board.movePiece(move);
        assertEquals(whiteKing, board.getPieceAt(4, 5), "King should be at the new position after capture.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after capture.");
    }

    @Test
    void testInvalidCaptureOwnPiece() {
        // 放置白兵在 (4, 5)
        board.getBoard()[4][5] = new Pawn(Color.WHITE);

        // 尝试吃掉自己的白兵
        VariantChessMove move = new VariantChessMove(4, 4, 4, 5);
        assertFalse(whiteKing.isValidMove(move, board), "King should not be able to capture its own piece.");
    }

    @Test
    void testKingColorAfterMove() {
        // 移动白王到 (4, 5)
        VariantChessMove move = new VariantChessMove(4, 4, 4, 5);
        assertTrue(whiteKing.isValidMove(move, board), "King should be able to move one square.");
        board.movePiece(move);
        assertEquals(whiteKing, board.getPieceAt(4, 5), "King should be at the new position after move.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");

        // 检查移动后的棋子颜色是否仍然是白色
        VariantChessPiece piece = board.getPieceAt(4, 5);
        assertNotNull(piece, "There should be a piece at the new position.");
        assertTrue(piece instanceof King, "The piece at the new position should be a King.");
        assertEquals(Color.WHITE, piece.getColor(), "The King should remain white after the move.");
    }
}
