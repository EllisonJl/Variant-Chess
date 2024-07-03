package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

class BishopTest {

    private VariantChessBoard board;
    private Bishop whiteBishop;
    private Bishop blackBishop;

    @BeforeEach
    void setUp() {
        board = new VariantChessBoard();
        whiteBishop = new Bishop(Color.WHITE);
        blackBishop = new Bishop(Color.BLACK);
        // 手动设置测试位置，避免与初始化棋盘冲突
        board.getBoard()[2][2] = whiteBishop;
    }

    @Test
    void testValidMove() {
        // 有效的田字形移动到空位 (4, 4)
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to move in a田字形 move to an empty position.");
        board.movePiece(move);
        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after move.");
        assertNull(board.getPieceAt(2, 2), "Original position should be empty after move.");

        // 有效的田字形移动到空位 (0, 0)
        move = new VariantChessMove(4, 4, 2, 2);
        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to move in a田字形 move to an empty position.");
        board.movePiece(move);
        assertEquals(whiteBishop, board.getPieceAt(2, 2), "Bishop should be at the new position after move.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");
    }

    @Test
    void testInvalidMoveOutOfBoard() {
        // 移动到棋盘外
        VariantChessMove move = new VariantChessMove(2, 2, 8, 8);
        assertFalse(whiteBishop.isValidMove(move, board), "Bishop should not be able to move outside the board.");
    }

    @Test
    void testInvalidMoveNotDiagonal() {
        // 非田字形移动
        VariantChessMove move = new VariantChessMove(2, 2, 2, 4);
        assertFalse(whiteBishop.isValidMove(move, board), "Bishop should not be able to move non-田字形.");
    }

    @Test
    void testCaptureMove() {
        // 放置黑兵在 (4, 4)
        board.getBoard()[4][4] = new Pawn(Color.BLACK);

        // 尝试吃掉黑兵
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to capture an enemy piece by moving in a田字形.");
        assertTrue(move.isCapture(), "Move should be marked as a capture.");
        board.movePiece(move);
        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after capture.");
        assertNull(board.getPieceAt(2, 2), "Original position should be empty after capture.");
    }

    @Test
    void testInvalidCaptureOwnPiece() {
        // 放置白兵在 (4, 4)
        board.getBoard()[4][4] = new Pawn(Color.WHITE);

        // 尝试吃掉自己的白兵
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertFalse(whiteBishop.isValidMove(move, board), "Bishop should not be able to capture its own piece by moving in a田字形.");
    }

    @Test
    void testValidMoveWithPieceInMiddle() {
        // 在 (3, 3) 放置一个棋子
        board.getBoard()[3][3] = new Pawn(Color.WHITE);

        // 有效的田字形移动到空位 (4, 4)
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to move in a田字形 with a piece in the middle.");
        board.movePiece(move);
        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after move.");
        assertNull(board.getPieceAt(2, 2), "Original position should be empty after move.");
    }

    @Test
    void testBishopColorAfterMove() {
        // 移动白象到 (4, 4)
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to move in a田字形 move to an empty position.");
        board.movePiece(move);
        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after move.");
        assertNull(board.getPieceAt(2, 2), "Original position should be empty after move.");

        // 检查移动后的棋子颜色是否仍然是白色
        VariantChessPiece piece = board.getPieceAt(4, 4);
        assertNotNull(piece, "There should be a piece at the new position.");
        assertTrue(piece instanceof Bishop, "The piece at the new position should be a Bishop.");
        assertEquals(Color.WHITE, piece.getColor(), "The Bishop should remain white after the move.");
    }

    @Test
    void testMoveWithPieceBlockingPath() {
        // 在 (3, 3) 放置一个棋子
        board.getBoard()[3][3] = new Pawn(Color.BLACK);

        // 有效的田字形移动到空位 (4, 4)
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to move in a田字形 with a piece blocking the path.");
        board.movePiece(move);
        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after move.");
        assertNull(board.getPieceAt(2, 2), "Original position should be empty after move.");
    }
}
