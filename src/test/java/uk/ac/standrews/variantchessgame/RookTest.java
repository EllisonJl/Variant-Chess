package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {

    private VariantChessBoard board;
    private Rook whiteRook;
    private Rook blackRook;

    @BeforeEach
    void setUp() {
        board = new VariantChessBoard();
        whiteRook = new Rook(Color.WHITE);
        blackRook = new Rook(Color.BLACK);
        // 手动设置测试位置，避免与初始化棋盘冲突
        board.getBoard()[4][4] = whiteRook;
        board.getBoard()[7][7] = blackRook; // 初始黑车位置
    }

    @Test
    void testValidMove() {
        // 有效的直线移动到空位 (4, 7)
        board.getBoard()[4][7] = null; // 确保目标位置为空
        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertTrue(whiteRook.isValidMove(move, board), "Rook should be able to move in a straight line.");
        board.movePiece(move);
        assertEquals(whiteRook, board.getPieceAt(4, 7), "Rook should be at the new position after move.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");

        // 尝试移动到被自己棋子占据的位置
        move = new VariantChessMove(4, 7, 7, 7);
        assertFalse(whiteRook.isValidMove(move, board), "Rook should not be able to move in a straight line to a position occupied by its own piece.");
        if (whiteRook.isValidMove(move, board)) {
            board.movePiece(move);
        }
        assertNotEquals(whiteRook, board.getPieceAt(7, 7), "Rook should not be at the new position after invalid move.");
        assertEquals(whiteRook, board.getPieceAt(4, 7), "Original position should be the same after invalid move.");
    }


    @Test
    void testInvalidMoveOutOfBoard() {
        // 移动到棋盘外
        VariantChessMove move = new VariantChessMove(4, 4, 8, 8);
        assertFalse(whiteRook.isValidMove(move, board), "Rook should not be able to move outside the board.");
    }

    @Test
    void testInvalidMoveNotStraight() {
        // 非直线移动
        VariantChessMove move = new VariantChessMove(4, 4, 5, 6);
        assertFalse(whiteRook.isValidMove(move, board), "Rook should not be able to move non-straight.");
    }

    @Test
    void testCaptureMove() {
        // 放置黑兵在 (4, 7)
        board.getBoard()[4][7] = new Pawn(Color.BLACK);

        // 尝试吃掉黑兵
        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertTrue(whiteRook.isValidMove(move, board), "Rook should be able to capture an enemy piece by moving in a straight line.");
        assertTrue(move.isCapture(), "Move should be marked as a capture.");
        board.movePiece(move);
        assertEquals(whiteRook, board.getPieceAt(4, 7), "Rook should be at the new position after capture.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after capture.");
    }

    @Test
    void testInvalidCaptureOwnPiece() {
        // 放置白兵在 (4, 7)
        board.getBoard()[4][7] = new Pawn(Color.WHITE);

        // 尝试吃掉自己的白兵
        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertFalse(whiteRook.isValidMove(move, board), "Rook should not be able to capture its own piece.");
    }

    @Test
    void testRookColorAfterMove() {
        // 移动白车到 (4, 7)
        board.getBoard()[4][7] = null; // 确保目标位置为空
        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertTrue(whiteRook.isValidMove(move, board), "Rook should be able to move in a straight line.");
        board.movePiece(move);
        assertEquals(whiteRook, board.getPieceAt(4, 7), "Rook should be at the new position after move.");
        assertNull(board.getPieceAt(4, 4), "Original position should be empty after move.");

        // 检查移动后的棋子颜色是否仍然是白色
        VariantChessPiece piece = board.getPieceAt(4, 7);
        assertNotNull(piece, "There should be a piece at the new position.");
        assertTrue(piece instanceof Rook, "The piece at the new position should be a Rook.");
        assertEquals(Color.WHITE, piece.getColor(), "The Rook should remain white after the move.");
    }

    @Test
    void testMoveBlockedByPiece() {
        // 放置白车在 (4, 4) 和一个白兵在 (4, 5)
        board.getBoard()[4][5] = new Pawn(Color.WHITE);

        // 尝试移动到 (4, 7)
        VariantChessMove move = new VariantChessMove(4, 4, 4, 7);
        assertFalse(whiteRook.isValidMove(move, board), "Rook should not be able to move past a blocking piece.");
    }

    @Test
    void testMoveAfterCapture() {
        // 放置白车在 (4, 4) 和一个黑兵在 (4, 7)
        board.getBoard()[4][7] = new Pawn(Color.BLACK);

        // 吃掉黑兵
        VariantChessMove captureMove = new VariantChessMove(4, 4, 4, 7);
        assertTrue(whiteRook.isValidMove(captureMove, board), "Rook should be able to capture an enemy piece by moving in a straight line.");
        assertTrue(captureMove.isCapture(), "Move should be marked as a capture.");
        board.movePiece(captureMove);

        // 尝试再次移动到空位 (4, 6)
        VariantChessMove move = new VariantChessMove(4, 7, 4, 6);
        assertTrue(whiteRook.isValidMove(move, board), "Rook should be able to move to an empty position after capture.");
    }
}
