package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the special rule involving the King and Queen's capture of Pawns in the variant chess game.
 * The tests validate the correct application of the special capture rule and the behavior of the pieces involved.
 */
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
        assertTrue(rule.hasWhiteKingUsedSpecialCapture(), "Special capture for King should be marked as used");
        assertTrue(((Pawn) board.getPieceAt(5, 5)).isFirstMove(), "Captured pawn should retain its isFirstMove state");

        VariantChessMove kingMove = new VariantChessMove(4, 4, 4, 3);
        assertTrue(whiteKing.isValidMove(kingMove, board), "King should be able to move again");

        VariantChessMove capturedPieceMove = new VariantChessMove(5, 5, 4, 5);
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
        assertTrue(rule.hasBlackKingUsedSpecialCapture(), "Special capture for King should be marked as used");
        assertTrue(((Pawn) board.getPieceAt(5, 5)).isFirstMove(), "Captured pawn should retain its isFirstMove state");

        VariantChessMove kingMove = new VariantChessMove(4, 4, 4, 3);
        assertTrue(blackKing.isValidMove(kingMove, board), "King should be able to move again");

        VariantChessMove capturedPieceMove = new VariantChessMove(5, 5, 6, 5);
        VariantChessPiece capturedPiece = board.getPieceAt(5, 5);
        assertTrue(capturedPiece.isValidMove(capturedPieceMove, board), "Captured pawn should be able to move");
    }

    @Test
    void testKingCaptureAdjacentPawn() {
        int[][] directions = {
                {5, 4}, {3, 4}, {4, 5}, {4, 3},
                {5, 5}, {5, 3}, {3, 5}, {3, 3}
        };

        for (int[] dir : directions) {
            setUp();
            board.setPieceAt(4, 4, whiteKing);
            board.setPieceAt(dir[0], dir[1], blackPawn);
            VariantChessMove move = new VariantChessMove(4, 4, dir[0], dir[1]);
            rule.applyRule(move, whiteKing, board);

            assertEquals(Color.WHITE, board.getPieceAt(dir[0], dir[1]).getColor(), "Captured piece should be white");
            assertEquals(whiteKing, board.getPieceAt(4, 4), "King should stay in the original position");
            assertTrue(rule.hasWhiteKingUsedSpecialCapture(), "Special capture for King should be marked as used");
            assertTrue(((Pawn) board.getPieceAt(dir[0], dir[1])).isFirstMove(), "Captured pawn should retain its isFirstMove state");
        }
    }

    @Test
    void testKingCaptureAtBoardEdge() {
        board.setPieceAt(2, 2, whiteKing);
        board.setPieceAt(3, 3, blackPawn);

        VariantChessMove move = new VariantChessMove(2, 2, 3, 3);
        rule.applyRule(move, whiteKing, board);

        assertEquals(Color.WHITE, board.getPieceAt(3, 3).getColor(), "Captured piece should be white");
        assertEquals(whiteKing, board.getPieceAt(2, 2), "King should stay in the original position");
        assertTrue(rule.hasWhiteKingUsedSpecialCapture(), "Special capture for King should be marked as used");
        assertTrue(((Pawn) board.getPieceAt(3, 3)).isFirstMove(), "Captured pawn should retain its isFirstMove state");
    }

    @Test
    void testQueenCaptureAtBoardEdge() {
        board.setPieceAt(2, 2, whiteQueen);
        board.setPieceAt(0, 7, blackPawn);

        VariantChessMove move = new VariantChessMove(2, 2, 0, 7);
        rule.applyRule(move, whiteQueen, board);

        assertEquals(Color.WHITE, board.getPieceAt(0, 7).getColor(), "Captured piece should be white");
        assertEquals(whiteQueen, board.getPieceAt(1, 6), "Queen should move to the previous position");
        assertTrue(rule.hasWhiteQueenUsedSpecialCapture(), "Special capture for Queen should be marked as used");
        assertTrue(((Pawn) board.getPieceAt(0, 7)).isFirstMove(), "Captured pawn should retain its isFirstMove state");
    }

    @Test
    void testQueenCaptureStraightAndDiagonalPawn() {
        int[][] straightDirections = {
                {4, 0}, {4, 7}, {0, 4}, {7, 4}
        };

        for (int[] dir : straightDirections) {
            setUp();
            board.setPieceAt(4, 4, whiteQueen);
            board.setPieceAt(dir[0], dir[1], blackPawn);
            VariantChessMove move = new VariantChessMove(4, 4, dir[0], dir[1]);
            rule.applyRule(move, whiteQueen, board);

            assertEquals(Color.WHITE, board.getPieceAt(dir[0], dir[1]).getColor(), "Captured piece should be white");
            int expectedX = dir[0] == 4 ? 4 : (dir[0] < 4 ? dir[0] + 1 : dir[0] - 1);
            int expectedY = dir[1] == 4 ? 4 : (dir[1] < 4 ? dir[1] + 1 : dir[1] - 1);
            assertEquals(whiteQueen, board.getPieceAt(expectedX, expectedY), "Queen should move to the previous position");
            assertTrue(rule.hasWhiteQueenUsedSpecialCapture(), "Special capture for Queen should be marked as used");
            assertTrue(((Pawn) board.getPieceAt(dir[0], dir[1])).isFirstMove(), "Captured pawn should retain its isFirstMove state");
        }

        int[][] diagonalDirections = {
                {0, 0}, {7, 7}, {0, 7}, {7, 0}
        };

        for (int[] dir : diagonalDirections) {
            setUp();
            board.setPieceAt(4, 4, whiteQueen);
            board.setPieceAt(dir[0], dir[1], blackPawn);
            VariantChessMove move = new VariantChessMove(4, 4, dir[0], dir[1]);
            rule.applyRule(move, whiteQueen, board);

            assertEquals(Color.WHITE, board.getPieceAt(dir[0], dir[1]).getColor(), "Captured piece should be white");
            int expectedX = (dir[0] < 4) ? dir[0] + 1 : dir[0] - 1;
            int expectedY = (dir[1] < 4) ? dir[1] + 1 : dir[1] - 1;
            assertEquals(whiteQueen, board.getPieceAt(expectedX, expectedY), "Queen should move to the previous position");
            assertTrue(rule.hasWhiteQueenUsedSpecialCapture(), "Special capture for Queen should be marked as used");
            assertTrue(((Pawn) board.getPieceAt(dir[0], dir[1])).isFirstMove(), "Captured pawn should retain its isFirstMove state");
        }
    }

    @Test
    void testWhiteQueenSpecialCaptureWithPawn() {
        board.setPieceAt(3, 3, whiteQueen);
        board.setPieceAt(5, 5, blackPawn);

        VariantChessMove move = new VariantChessMove(3, 3, 5, 5);
        rule.applyRule(move, whiteQueen, board);

        assertEquals(Color.WHITE, board.getPieceAt(5, 5).getColor(), "Captured piece should be white");
        assertEquals(whiteQueen, board.getPieceAt(4, 4), "Queen should move to the previous position");
        assertTrue(rule.hasWhiteQueenUsedSpecialCapture(), "Special capture for Queen should be marked as used");
        assertTrue(((Pawn) board.getPieceAt(5, 5)).isFirstMove(), "Captured pawn should retain its isFirstMove state");

        VariantChessMove queenMove = new VariantChessMove(4, 4, 3, 4);
        assertTrue(whiteQueen.isValidMove(queenMove, board), "Queen should be able to move again");

        VariantChessMove capturedPieceMove = new VariantChessMove(5, 5, 4, 5);
        VariantChessPiece capturedPiece = board.getPieceAt(5, 5);
        assertTrue(capturedPiece.isValidMove(capturedPieceMove, board), "Captured pawn should be able to move");
    }

    @Test
    void testBlackQueenSpecialCaptureWithPawn() {
        board.setPieceAt(3, 3, blackQueen);
        board.setPieceAt(5, 5, whitePawn);

        VariantChessMove move = new VariantChessMove(3, 3, 5, 5);
        rule.applyRule(move, blackQueen, board);

        assertEquals(Color.BLACK, board.getPieceAt(5, 5).getColor(), "Captured piece should be black");
        assertEquals(blackQueen, board.getPieceAt(4, 4), "Queen should move to the previous position");
        assertTrue(rule.hasBlackQueenUsedSpecialCapture(), "Special capture for Queen should be marked as used");
        assertTrue(((Pawn) board.getPieceAt(5, 5)).isFirstMove(), "Captured pawn should retain its isFirstMove state");

        VariantChessMove queenMove = new VariantChessMove(4, 4, 3, 4);
        assertTrue(blackQueen.isValidMove(queenMove, board), "Queen should be able to move again");

        VariantChessMove capturedPieceMove = new VariantChessMove(5, 5, 6, 5);
        VariantChessPiece capturedPiece = board.getPieceAt(5, 5);
        assertTrue(capturedPiece.isValidMove(capturedPieceMove, board), "Captured pawn should be able to move");
    }

    @Test
    void testQueenMovesToPositionBeforeCapturedPiece() {
        board.setPieceAt(2, 2, whiteQueen);
        board.setPieceAt(5, 5, blackPawn);

        VariantChessMove move = new VariantChessMove(2, 2, 5, 5);
        rule.applyRule(move, whiteQueen, board);

        int expectedX = 5 - 1;
        int expectedY = 5 - 1;

        assertEquals(whiteQueen, board.getPieceAt(expectedX, expectedY), "Queen should move to the position before captured piece");
        assertEquals(Color.WHITE, board.getPieceAt(5, 5).getColor(), "Captured piece should be white");
    }

    @Test
    void testQueenCapturesAndMovesBackCorrectly() {
        board.setPieceAt(4, 4, whiteQueen);
        board.setPieceAt(6, 6, blackPawn);

        VariantChessMove whiteMove = new VariantChessMove(4, 4, 6, 6);
        rule.applyRule(whiteMove, whiteQueen, board);

        int expectedX = 6 - 1;
        int expectedY = 6 - 1;

        assertEquals(whiteQueen, board.getPieceAt(expectedX, expectedY), "White Queen should move to the previous position");
        assertEquals(Color.WHITE, board.getPieceAt(6, 6).getColor(), "Captured piece should be white");

        board.setPieceAt(4, 4, blackQueen);
        board.setPieceAt(6, 6, whitePawn);

        VariantChessMove blackMove = new VariantChessMove(4, 4, 6, 6);
        rule.applyRule(blackMove, blackQueen, board);

        expectedX = 6 - 1;
        expectedY = 6 - 1;

        assertEquals(blackQueen, board.getPieceAt(expectedX, expectedY), "Black Queen should move to the previous position");
        assertEquals(Color.BLACK, board.getPieceAt(6, 6).getColor(), "Captured piece should be black");
    }

    @Test
    void testKingCaptureChangesColorCorrectly() {
        board.setPieceAt(4, 4, whiteKing);
        board.setPieceAt(5, 5, blackPawn);
        VariantChessMove kingMove = new VariantChessMove(4, 4, 5, 5);
        rule.applyRule(kingMove, whiteKing, board);

        assertEquals(Color.WHITE, board.getPieceAt(5, 5).getColor(), "Captured piece should change to White after White King captures it");

        board.setPieceAt(4, 4, blackKing);
        board.setPieceAt(5, 5, whitePawn);
        kingMove = new VariantChessMove(4, 4, 5, 5);
        rule.applyRule(kingMove, blackKing, board);

        assertEquals(Color.BLACK, board.getPieceAt(5, 5).getColor(), "Captured piece should change to Black after Black King captures it");
    }
}
