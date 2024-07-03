package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

class CannonTest {

    private VariantChessBoard board;
    private Cannon whiteCannon;
    private Cannon blackCannon;

    @BeforeEach
    void setUp() {
        board = new VariantChessBoard();
        whiteCannon = new Cannon(Color.WHITE);
        blackCannon = new Cannon(Color.BLACK);

        // 清空棋盘
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.getBoard()[i][j] = null;
            }
        }
        board.getBoard()[2][2] = whiteCannon;
    }

    @Test
    void testValidMove() {
        // 放置白炮在 (2, 2)
        board.getBoard()[2][2] = whiteCannon;

        // 直线移动到空位 (2, 5)
        VariantChessMove move = new VariantChessMove(2, 2, 2, 5);
        assertTrue(whiteCannon.isValidMove(move, board), "Cannon should be able to move in a straight line to an empty position.");

        // 直线移动到空位 (5, 2)
        move = new VariantChessMove(2, 2, 5, 2);
        assertTrue(whiteCannon.isValidMove(move, board), "Cannon should be able to move in a straight line to an empty position.");
    }

    @Test
    void testMoveBlockedByPiece() {
        // 放置白炮在 (2, 2) 和一个白兵在 (2, 4)
        board.getBoard()[2][2] = whiteCannon;
        board.getBoard()[2][4] = new Pawn(Color.WHITE);

        // 尝试移动到 (2, 5)
        VariantChessMove move = new VariantChessMove(2, 2, 2, 5);
        assertFalse(whiteCannon.isValidMove(move, board), "Cannon should not be able to move past a blocking piece.");
    }

    @Test
    void testCaptureMove() {
        // 放置白炮在 (2, 2)、一个黑兵在 (2, 4) 和一个黑兵在 (2, 6)
        board.getBoard()[2][2] = whiteCannon;
        board.getBoard()[2][4] = new Pawn(Color.BLACK);
        board.getBoard()[2][6] = new Pawn(Color.BLACK);

        // 尝试吃掉黑兵
        VariantChessMove move = new VariantChessMove(2, 2, 2, 6);
        assertTrue(whiteCannon.isValidMove(move, board), "Cannon should be able to capture an enemy piece by jumping over exactly one piece.");
        assertTrue(move.isCapture(), "Move should be marked as a capture.");
    }

    @Test
    void testInvalidMove() {
        // 放置白炮在 (2, 2)
        board.getBoard()[2][2] = whiteCannon;

        // 非直线移动
        VariantChessMove move = new VariantChessMove(2, 2, 3, 3);
        assertFalse(whiteCannon.isValidMove(move, board), "Cannon should not be able to move non-linearly.");

        // 移动到自己位置
        move = new VariantChessMove(2, 2, 2, 2);
        assertFalse(whiteCannon.isValidMove(move, board), "Cannon should not be able to move to its own position.");
    }

    @Test
    void testCaptureWithoutJump() {
        // 放置白炮在 (2, 2) 和一个黑兵在 (2, 5)
        board.getBoard()[2][2] = whiteCannon;
        board.getBoard()[2][5] = new Pawn(Color.BLACK);

        // 尝试直接吃掉黑兵
        VariantChessMove move = new VariantChessMove(2, 2, 2, 5);
        assertFalse(whiteCannon.isValidMove(move, board), "Cannon should not be able to capture without jumping over exactly one piece.");
    }

    @Test
    void testMoveAfterCapture() {
        // 放置白炮在 (2, 2)、一个黑兵在 (2, 4) 和一个黑兵在 (2, 6)
        board.getBoard()[2][2] = whiteCannon;
        board.getBoard()[2][4] = new Pawn(Color.BLACK);
        board.getBoard()[2][6] = new Pawn(Color.BLACK);

        // 吃掉黑兵
        VariantChessMove captureMove = new VariantChessMove(2, 2, 2, 6);
        assertTrue(whiteCannon.isValidMove(captureMove, board), "Cannon should be able to capture an enemy piece by jumping over exactly one piece.");
        assertTrue(captureMove.isCapture(), "Move should be marked as a capture.");
        board.movePiece(captureMove);

        // 尝试再次移动到空位 (2, 7)
        VariantChessMove move = new VariantChessMove(2, 6, 2, 7);
        assertTrue(whiteCannon.isValidMove(move, board), "Cannon should be able to move to an empty position after capture.");
    }

    @Test
    void testCaptureOwnPiece() {
        // 放置白炮在 (2, 2)、一个白兵在 (2, 4) 和一个白兵在 (2, 6)
        board.getBoard()[2][2] = whiteCannon;
        board.getBoard()[2][4] = new Pawn(Color.WHITE);
        board.getBoard()[2][6] = new Pawn(Color.WHITE);

        // 尝试吃掉自己的白兵
        VariantChessMove move = new VariantChessMove(2, 2, 2, 6);
        assertFalse(whiteCannon.isValidMove(move, board), "Cannon should not be able to capture its own piece by jumping over exactly one piece.");
    }
    @Test
    void testCannonColorAfterMove() {
        // 移动白炮到 (2, 5)
        VariantChessMove move = new VariantChessMove(2, 2, 2, 5);
        assertTrue(whiteCannon.isValidMove(move, board), "Cannon should be able to move in a straight line to an empty position.");
        board.movePiece(move);

        // 检查 (2, 2) 位置是否为空
        assertNull(board.getPieceAt(2, 2), "The original position should be empty after the move.");

        // 检查移动后的棋子颜色是否仍然是白色
        VariantChessPiece piece = board.getPieceAt(2, 5);
//        assertNotNull(piece, "There should be a piece at the new position.");
        assertTrue(piece instanceof Cannon, "The piece at the new position should be a Cannon.");
        assertEquals(Color.WHITE, piece.getColor(), "The Cannon should remain white after the move.");
    }

    @Test
    void testCaptureEnemyPiece() {
        // 放置白炮在 (2, 2)、一个白兵在 (2, 4) 和一个黑兵在 (2, 6)
        board.getBoard()[2][2] = whiteCannon;
        board.getBoard()[2][4] = new Pawn(Color.WHITE);
        board.getBoard()[2][6] = new Pawn(Color.BLACK);

        // 尝试吃掉黑兵
        VariantChessMove move = new VariantChessMove(2, 2, 2, 6);
        assertTrue(whiteCannon.isValidMove(move, board), "Cannon should be able to capture an enemy piece by jumping over exactly one piece.");
        assertTrue(move.isCapture(), "Move should be marked as a capture.");
    }
}
