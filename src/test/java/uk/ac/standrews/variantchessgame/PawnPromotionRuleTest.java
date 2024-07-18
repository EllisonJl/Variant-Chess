package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

class PawnPromotionRuleTest {

    private VariantChessBoard board;
    private Pawn whitePawn;
    private PawnPromotionRule rule;

    @BeforeEach
    void setUp() {
        board = new VariantChessBoard();
        whitePawn = new Pawn(Color.WHITE);
        rule = new PawnPromotionRule();
        board.getBoard()[6][0] = whitePawn;  // 白棋兵从第六行开始
    }

    @Test
    void testPawnPromotionSecondCapture() {
        // 捕获一次
        board.setPieceAt(5, 0, new Rook(Color.BLACK));
        VariantChessMove move = new VariantChessMove(6, 0, 5, 0);
        board.movePiece(move);
        rule.applyRule(move, board.getPieceAt(5, 0), board);

        // 捕获第二次
        board.setPieceAt(4, 0, new Rook(Color.BLACK));
        move = new VariantChessMove(5, 0, 4, 0);
        board.movePiece(move);
        rule.applyRule(move, board.getPieceAt(4, 0), board);

        VariantChessPiece newPiece = board.getPieceAt(4, 0);
        assertTrue(newPiece instanceof Cannon || newPiece instanceof Rook, "Pawn should be promoted to Cannon or Rook after second capture.");
        assertEquals(Color.WHITE, newPiece.getColor(), "New piece should be white.");

        // 验证新棋子的移动规则
        VariantChessMove cannonMove = new VariantChessMove(4, 0, 4, 7); // Cannon 横向移动
        assertTrue(newPiece.isValidMove(cannonMove, board), "Cannon/Rook should be able to move according to its rules.");
    }

    @Test
    void testPawnPromotionThirdCapture() {
        // 捕获一次
        board.getBoard()[5][0] = new Rook(Color.BLACK);
        VariantChessMove move = new VariantChessMove(6, 0, 5, 0);
        board.movePiece(move);
        rule.applyRule(move, board.getPieceAt(5, 0), board);

        // 捕获第二次
        board.getBoard()[4][0] = new Rook(Color.BLACK);
        move = new VariantChessMove(5, 0, 4, 0);
        board.movePiece(move);
        rule.applyRule(move, board.getPieceAt(4, 0), board);

        // 捕获第三次
        board.getBoard()[3][0] = new Rook(Color.BLACK);
        move = new VariantChessMove(4, 0, 3, 0);
        board.movePiece(move);
        rule.applyRule(move, board.getPieceAt(3, 0), board);

        VariantChessPiece newPiece = board.getPieceAt(3, 0);
        assertTrue(newPiece instanceof Queen, "Pawn should be promoted to Queen after third capture.");
        assertEquals(Color.WHITE, newPiece.getColor(), "New piece should be white.");

        // 验证新棋子的移动规则
        VariantChessMove queenMove = new VariantChessMove(3, 0, 3, 7); // Queen 横向移动
        assertTrue(newPiece.isValidMove(queenMove, board), "Queen should be able to move according to its rules.");
    }

    @Test
    void testPawnNoPromotionAfterThirdCapture() {
        // 捕获一次
        board.getBoard()[5][0] = new Rook(Color.BLACK);
        VariantChessMove move = new VariantChessMove(6, 0, 5, 0);
        board.movePiece(move);
        rule.applyRule(move, board.getPieceAt(5, 0), board);

        // 捕获第二次
        board.getBoard()[4][0] = new Rook(Color.BLACK);
        move = new VariantChessMove(5, 0, 4, 0);
        board.movePiece(move);
        rule.applyRule(move, board.getPieceAt(4, 0), board);

        // 捕获第三次
        board.getBoard()[3][0] = new Rook(Color.BLACK);
        move = new VariantChessMove(4, 0, 3, 0);
        board.movePiece(move);
        rule.applyRule(move, board.getPieceAt(3, 0), board);

        // 捕获第四次
        board.getBoard()[2][0] = new Rook(Color.BLACK);
        move = new VariantChessMove(3, 0, 2, 0);
        board.movePiece(move);
        rule.applyRule(move, board.getPieceAt(2, 0), board);

        VariantChessPiece newPiece = board.getPieceAt(2, 0);
        assertTrue(newPiece instanceof Queen, "Pawn should remain a Queen after fourth capture.");
        assertEquals(Color.WHITE, newPiece.getColor(), "New piece should be white.");
    }

    @Test
    void testPawnMovesAfterPromotion() {
        // 捕获一次，升级到骑士或主教
        board.getBoard()[5][0] = new Rook(Color.BLACK);
        VariantChessMove move = new VariantChessMove(6, 0, 5, 0);
        board.movePiece(move);
        rule.applyRule(move, board.getPieceAt(5, 0), board);

        VariantChessPiece promotedPiece = board.getPieceAt(5, 0);
        assertNotNull(promotedPiece, "Promoted piece should be present.");

        // 检查升级后的棋子是否能正常移动
        if (promotedPiece instanceof Knight) {
            move = new VariantChessMove(5, 0, 3, 1); // 骑士 L形移动
        } else if (promotedPiece instanceof Bishop) {
            move = new VariantChessMove(5, 0, 3, 2); // 主教 斜向移动
        } else if (promotedPiece instanceof Cannon) {
            move = new VariantChessMove(5, 0, 5, 7); // 大炮 横向移动
        } else if (promotedPiece instanceof Rook) {
            move = new VariantChessMove(5, 0, 5, 7); // 车 横向移动
        } else if (promotedPiece instanceof Queen) {
            move = new VariantChessMove(5, 0, 5, 7); // 后 横向移动
        }
        assertTrue(promotedPiece.isValidMove(move, board), "Promoted piece should be able to move according to its new rules.");
    }
}
