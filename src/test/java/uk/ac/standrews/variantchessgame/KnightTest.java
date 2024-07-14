package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

class KnightTest {

    private VariantChessBoard board;
    private Knight whiteKnight;
    private Knight blackKnight;

    @BeforeEach
    void setUp() {
        board = new VariantChessBoard();
        whiteKnight = new Knight(Color.WHITE);
        blackKnight = new Knight(Color.BLACK);
        // 手动设置测试位置，避免与初始化棋盘冲突
        board.getBoard()[4][4] = whiteKnight;
    }

    @Test
    void testValidMove() {
        // 确保目标位置为空，以进行有效的“日”字形移动 (6, 5)
        board.getBoard()[6][5] = null;
        VariantChessMove move = new VariantChessMove(4, 4, 6, 5);
        assertTrue(whiteKnight.isValidMove(move, board), "Knight should be able to move in an 'L' shape.");
        board.movePiece(move);
        assertEquals(whiteKnight, board.getPieceAt(6, 5), "Knight should be at the new position after move.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");

        // 确保目标位置为空，以进行有效的“日”字形移动 (4, 6)
        board.getBoard()[4][6] = null;
        move = new VariantChessMove(6, 5, 4, 6);
        assertTrue(whiteKnight.isValidMove(move, board), "Knight should be able to move in an 'L' shape.");
        board.movePiece(move);
        assertEquals(whiteKnight, board.getPieceAt(4, 6), "Knight should be at the new position after move.");
        assertNull(board.getPieceAt(6, 5), "Original position should be empty after move.");
    }

    @Test
    void testKnightColorAfterMove() {
        // 确保目标位置为空，以进行有效的“日”字形移动 (6, 5)
        board.getBoard()[6][5] = null;
        VariantChessMove move = new VariantChessMove(4, 4, 6, 5);
        assertTrue(whiteKnight.isValidMove(move, board), "Knight should be able to move in an 'L' shape.");
        board.movePiece(move);
        assertEquals(whiteKnight, board.getPieceAt(6, 5), "Knight should be at the new position after move.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");

        // 检查移动后的棋子颜色是否仍然是白色
        VariantChessPiece piece = board.getPieceAt(6, 5);
        assertNotNull(piece, "There should be a piece at the new position.");
        assertTrue(piece instanceof Knight, "The piece at the new position should be a Knight.");
        assertEquals(Color.WHITE, piece.getColor(), "The Knight should remain white after the move.");
    }
        @Test
    void testInvalidMoveOutOfBoard() {
        // 移动到棋盘外
        VariantChessMove move = new VariantChessMove(4, 4, 8, 5);
        assertFalse(whiteKnight.isValidMove(move, board), "Knight should not be able to move outside the board.");
    }

    @Test
    void testInvalidMoveNotLShape() {
        // 非“日”字形移动
        VariantChessMove move = new VariantChessMove(4, 4, 5, 5);
        assertFalse(whiteKnight.isValidMove(move, board), "Knight should not be able to move in a non-'L' shape.");
    }

    @Test
    void testCaptureMove() {
        // 放置黑兵在 (6, 5)
        board.getBoard()[6][5] = new Pawn(Color.BLACK);

        // 尝试吃掉黑兵
        VariantChessMove move = new VariantChessMove(4, 4, 6, 5);
        assertTrue(whiteKnight.isValidMove(move, board), "Knight should be able to capture an enemy piece by moving in an 'L' shape.");
        assertTrue(move.isCapture(), "Move should be marked as a capture.");
        board.movePiece(move);
        assertEquals(whiteKnight, board.getPieceAt(6, 5), "Knight should be at the new position after capture.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after capture.");
    }

    @Test
    void testInvalidCaptureOwnPiece() {
        // 放置白兵在 (6, 5)
        board.getBoard()[6][5] = new Pawn(Color.WHITE);

        // 尝试吃掉自己的白兵
        VariantChessMove move = new VariantChessMove(4, 4, 6, 5);
        assertFalse(whiteKnight.isValidMove(move, board), "Knight should not be able to capture its own piece.");
    }

}
