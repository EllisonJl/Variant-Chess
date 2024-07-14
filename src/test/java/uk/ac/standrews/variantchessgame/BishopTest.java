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
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to move in a田字形 move to an empty position.");
        board.movePiece(move);
        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after move.");
        assertNull(board.getPieceAt(2, 2), "Original position should be empty after move.");
    }

    @Test
    void testInvalidMoveOutOfBoard() {
        VariantChessMove move = new VariantChessMove(2, 2, 8, 8);
        assertFalse(whiteBishop.isValidMove(move, board), "Bishop should not be able to move outside the board.");
    }

    @Test
    void testInvalidMoveNotDiagonal() {
        VariantChessMove move = new VariantChessMove(2, 2, 2, 4);
        assertFalse(whiteBishop.isValidMove(move, board), "Bishop should not be able to move non-田字形.");
    }

    @Test
    void testCaptureMove() {
        board.getBoard()[4][4] = new Pawn(Color.BLACK);
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to capture an enemy piece by moving in a田字形.");
        assertTrue(move.isCapture(), "Move should be marked as a capture.");
        board.movePiece(move);
        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after capture.");
        assertNull(board.getPieceAt(2, 2), "Original position should be empty after capture.");
    }

    @Test
    void testInvalidCaptureOwnPiece() {
        board.getBoard()[4][4] = new Pawn(Color.WHITE);
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertFalse(whiteBishop.isValidMove(move, board), "Bishop should not be able to capture its own piece by moving in a田字形.");
    }

    @Test
    void testValidMoveWithPieceInMiddle() {
        board.getBoard()[3][3] = new Pawn(Color.WHITE);
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to move in a田字形 with a piece in the middle.");
        board.movePiece(move);
        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after move.");
        assertNull(board.getPieceAt(2, 2), "Original position should be empty after move.");
    }

    @Test
    void testBishopColorAfterMove() {
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to move in a田字形 move to an empty position.");
        board.movePiece(move);
        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after move.");
        assertNull(board.getPieceAt(2, 2), "Original position should be empty after move.");
        VariantChessPiece piece = board.getPieceAt(4, 4);
        assertNotNull(piece, "There should be a piece at the new position.");
        assertTrue(piece instanceof Bishop, "The piece at the new position should be a Bishop.");
        assertEquals(Color.WHITE, piece.getColor(), "The Bishop should remain white after the move.");
    }

    @Test
    void testMoveWithPieceBlockingPath() {
        board.getBoard()[3][3] = new Pawn(Color.BLACK);
        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        assertTrue(whiteBishop.isValidMove(move, board), "Bishop should be able to move in a田字形 with a piece blocking the path.");
        board.movePiece(move);
        assertEquals(whiteBishop, board.getPieceAt(4, 4), "Bishop should be at the new position after move.");
        assertNull(board.getPieceAt(2, 2), "Original position should be empty after move.");
    }
}
