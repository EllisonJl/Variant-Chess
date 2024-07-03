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
        // 有效的移动到空位 (3, 4)
        VariantChessMove move = new VariantChessMove(4, 4, 3, 4);
        assertTrue(whiteKing.isValidMove(move, board), "King should be able to move one square vertically.");
        whiteKing.move(move, board);
        assertEquals(whiteKing, board.getPieceAt(3, 4), "King should be at the new position after move.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");

        // 有效的移动到空位 (4, 3)
        move = new VariantChessMove(3, 4, 4, 3);
        assertTrue(whiteKing.isValidMove(move, board), "King should be able to move one square horizontally.");
        whiteKing.move(move, board);
        assertEquals(whiteKing, board.getPieceAt(4, 3), "King should be at the new position after move.");
        assertNull(board.getPieceAt(3, 4), "Original position should be empty after move.");

        // 有效的移动到空位 (3, 3)
        move = new VariantChessMove(4, 3, 3, 3);
        assertTrue(whiteKing.isValidMove(move, board), "King should be able to move one square diagonally.");
        whiteKing.move(move, board);
        assertEquals(whiteKing, board.getPieceAt(3, 3), "King should be at the new position after move.");
        assertNull(board.getPieceAt(4, 3), "Original position should be empty after move.");
    }

    @Test
    void testInvalidMoveOutOfBoard() {
        // 移动到棋盘外
        VariantChessMove move = new VariantChessMove(4, 4, 8, 8);
        assertFalse(whiteKing.isValidMove(move, board), "King should not be able to move outside the board.");
    }

    @Test
    void testInvalidMoveNotOneSquare() {
        // 移动超过一格
        VariantChessMove move = new VariantChessMove(4, 4, 6, 4);
        assertFalse(whiteKing.isValidMove(move, board), "King should not be able to move more than one square.");
    }

    @Test
    void testCaptureMove() {
        // 放置黑兵在 (3, 4)
        board.getBoard()[3][4] = new Pawn(Color.BLACK);

        // 尝试吃掉黑兵
        VariantChessMove move = new VariantChessMove(4, 4, 3, 4);
        assertTrue(whiteKing.isValidMove(move, board), "King should be able to capture an enemy piece by moving one square.");
        assertTrue(move.isCapture(), "Move should be marked as a capture.");
        whiteKing.move(move, board);
        assertEquals(whiteKing, board.getPieceAt(3, 4), "King should be at the new position after capture.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after capture.");
    }

    @Test
    void testInvalidCaptureOwnPiece() {
        // 放置白兵在 (3, 4)
        board.getBoard()[3][4] = new Pawn(Color.WHITE);

        // 尝试吃掉自己的白兵
        VariantChessMove move = new VariantChessMove(4, 4, 3, 4);
        assertFalse(whiteKing.isValidMove(move, board), "King should not be able to capture its own piece.");
    }

    @Test
    void testKingColorAfterMove() {
        // 移动白王到 (3, 4)
        VariantChessMove move = new VariantChessMove(4, 4, 3, 4);
        assertTrue(whiteKing.isValidMove(move, board), "King should be able to move one square vertically.");
        whiteKing.move(move, board);
        assertEquals(whiteKing, board.getPieceAt(3, 4), "King should be at the new position after move.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");

        // 检查移动后的棋子颜色是否仍然是白色
        VariantChessPiece piece = board.getPieceAt(3, 4);
        assertNotNull(piece, "There should be a piece at the new position.");
        assertTrue(piece instanceof King, "The piece at the new position should be a King.");
        assertEquals(Color.WHITE, piece.getColor(), "The King should remain white after the move.");
    }

    @Test
    void testDiagonalCaptureMove() {
        // 放置黑兵在 (3, 3)
        board.getBoard()[3][3] = new Pawn(Color.BLACK);

        // 尝试斜线吃掉黑兵
        VariantChessMove move = new VariantChessMove(4, 4, 3, 3);
        assertTrue(whiteKing.isValidMove(move, board), "King should be able to capture an enemy piece by moving one square diagonally.");
        assertTrue(move.isCapture(), "Move should be marked as a capture.");
        whiteKing.move(move, board);
        assertEquals(whiteKing, board.getPieceAt(3, 3), "King should be at the new position after capture.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after capture.");
    }
}
