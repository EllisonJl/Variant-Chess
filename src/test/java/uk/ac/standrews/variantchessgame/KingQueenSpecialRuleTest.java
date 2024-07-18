package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

class KingQueenSpecialRuleTest {

    private VariantChessBoard board;
    private KingQueenSpecialRule rule;
    private King whiteKing;
    private King blackKing;
    private Queen whiteQueen;
    private Queen blackQueen;
    private Pawn blackPawn;
    private Pawn whitePawn;

    @BeforeEach
    void setUp() {
        board = new VariantChessBoard();
        rule = new KingQueenSpecialRule();
        whiteKing = new King(Color.WHITE);
        blackKing = new King(Color.BLACK);
        whiteQueen = new Queen(Color.WHITE);
        blackQueen = new Queen(Color.BLACK);
        blackPawn = new Pawn(Color.BLACK);
        whitePawn = new Pawn(Color.WHITE);
    }

    @Test
    void testWhiteKingSpecialCaptureWithPawn() {
        board.setPieceAt(4, 4, whiteKing);
        board.setPieceAt(5, 5, blackPawn);

        VariantChessMove move = new VariantChessMove(4, 4, 5, 5);
        rule.applyRule(move, whiteKing, board);

        assertEquals(Color.WHITE, board.getPieceAt(5, 5).getColor(), "Captured piece should be white");
        assertEquals(whiteKing, board.getPieceAt(4, 4), "King should stay in the original position");
        assertTrue(rule.hasUsedSpecialCapture(), "Special capture should be marked as used");
        assertTrue(((Pawn) board.getPieceAt(5, 5)).isFirstMove(), "Captured pawn should retain its isFirstMove state");
        assertEquals(Color.WHITE, ((Pawn) board.getPieceAt(5, 5)).getColor(), "Captured pawn should have white color");

        // Try to move the King again
        VariantChessMove kingMove = new VariantChessMove(4, 4, 4, 3);
        assertTrue(whiteKing.isValidMove(kingMove, board), "King should be able to move again");

        // Try to move the captured pawn
        VariantChessMove capturedPieceMove = new VariantChessMove(5, 5, 4, 5); // White pawn moves upward
        VariantChessPiece capturedPiece = board.getPieceAt(5, 5);
        assertTrue(capturedPiece.isValidMove(capturedPieceMove, board), "Captured pawn should be able to move");
    }

    @Test
    void testBlackKingSpecialCaptureWithPawn() {
        board.setPieceAt(4, 4, blackKing);
        board.setPieceAt(5, 5, whitePawn);

        VariantChessMove move = new VariantChessMove(4, 4, 5, 5);
        rule.applyRule(move, blackKing, board);

        assertEquals(Color.BLACK, board.getPieceAt(5, 5).getColor(), "Captured piece should be black");
        assertEquals(blackKing, board.getPieceAt(4, 4), "King should stay in the original position");
        assertTrue(rule.hasUsedSpecialCapture(), "Special capture should be marked as used");
        assertTrue(((Pawn) board.getPieceAt(5, 5)).isFirstMove(), "Captured pawn should retain its isFirstMove state");
        assertEquals(Color.BLACK, ((Pawn) board.getPieceAt(5, 5)).getColor(), "Captured pawn should have black color");

        // Try to move the King again
        VariantChessMove kingMove = new VariantChessMove(4, 4, 4, 3);
        assertTrue(blackKing.isValidMove(kingMove, board), "King should be able to move again");

        // Try to move the captured pawn
        VariantChessMove capturedPieceMove = new VariantChessMove(5, 5, 6, 5); // Black pawn moves downward
        VariantChessPiece capturedPiece = board.getPieceAt(5, 5);
        assertTrue(capturedPiece.isValidMove(capturedPieceMove, board), "Captured pawn should be able to move");
    }

    @Test
    void testWhiteQueenSpecialCaptureWithPawn() {
        board.setPieceAt(3, 1, whiteQueen);
        board.setPieceAt(3, 7, blackPawn);

        VariantChessMove move = new VariantChessMove(3, 1, 3, 7);
        rule.applyRule(move, whiteQueen, board);

        assertEquals(Color.WHITE, board.getPieceAt(3, 7).getColor(), "Captured piece should be white");
        assertEquals(whiteQueen, board.getPieceAt(3, 6), "Queen should move to the previous position");
        assertTrue(rule.hasUsedSpecialCapture(), "Special capture should be marked as used");
        assertTrue(((Pawn) board.getPieceAt(3, 7)).isFirstMove(), "Captured pawn should retain its isFirstMove state");
        assertEquals(Color.WHITE, ((Pawn) board.getPieceAt(3, 7)).getColor(), "Captured pawn should have white color");

        // Try to move the Queen again
        VariantChessMove queenMove = new VariantChessMove(3, 6, 3, 5);
        assertTrue(whiteQueen.isValidMove(queenMove, board), "Queen should be able to move again");

        // Try to move the captured pawn
        VariantChessMove capturedPieceMove = new VariantChessMove(3, 7, 2, 7); // White pawn moves upward
        VariantChessPiece capturedPiece = board.getPieceAt(3, 7);
        assertTrue(capturedPiece.isValidMove(capturedPieceMove, board), "Captured pawn should be able to move");
    }

    @Test
    void testBlackQueenSpecialCaptureWithPawn() {
        board.setPieceAt(3, 1, blackQueen);
        board.setPieceAt(3, 7, whitePawn);

        VariantChessMove move = new VariantChessMove(3, 1, 3, 7);
        rule.applyRule(move, blackQueen, board);

        assertEquals(Color.BLACK, board.getPieceAt(3, 7).getColor(), "Captured piece should be black");
        assertEquals(blackQueen, board.getPieceAt(3, 6), "Queen should move to the previous position");
        assertTrue(rule.hasUsedSpecialCapture(), "Special capture should be marked as used");
        assertTrue(((Pawn) board.getPieceAt(3, 7)).isFirstMove(), "Captured pawn should retain its isFirstMove state");
        assertEquals(Color.BLACK, ((Pawn) board.getPieceAt(3, 7)).getColor(), "Captured pawn should have black color");

        // Try to move the Queen again
        VariantChessMove queenMove = new VariantChessMove(3, 6, 3, 5);
        assertTrue(blackQueen.isValidMove(queenMove, board), "Queen should be able to move again");

        // Try to move the captured pawn
        VariantChessMove capturedPieceMove = new VariantChessMove(3, 7, 4, 7); // Black pawn moves downward
        VariantChessPiece capturedPiece = board.getPieceAt(3, 7);
        assertTrue(capturedPiece.isValidMove(capturedPieceMove, board), "Captured pawn should be able to move");
    }
}
