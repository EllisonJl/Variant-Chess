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

    /**
     * Sets up the test environment before each test case.
     * Initializes the chess board, the special rule, and the pieces (Kings, Queens, and Pawns).
     */
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
    void testMovingAndTargetPiecesAreDistinct() {
        board.setPieceAt(4, 4, whiteQueen);
        board.setPieceAt(5, 5, blackPawn);

        VariantChessMove move = new VariantChessMove(4, 4, 5, 5);

        // Verify that the moving piece and the target piece are distinct before applying the rule
        VariantChessPiece targetPieceBefore = board.getPieceAt(move.getEndX(), move.getEndY());
        assertNotNull(targetPieceBefore, "Target piece should not be null");
        assertNotSame(whiteQueen, targetPieceBefore, "The moving piece and target piece should not be the same");

        // Apply the special capture rule
        rule.applyRule(move, whiteQueen, board);

        // Calculate expected new position for the Queen
        int deltaX = move.getEndX() - move.getStartX();
        int deltaY = move.getEndY() - move.getStartY();
        int expectedNewX = move.getEndX() - Integer.signum(deltaX);
        int expectedNewY = move.getEndY() - Integer.signum(deltaY);

        // Verify that after applying the rule, the pieces are still correctly positioned
        assertEquals(Color.WHITE, board.getPieceAt(5, 5).getColor(), "Captured piece should be white");
        assertEquals(whiteQueen, board.getPieceAt(expectedNewX, expectedNewY), "Queen should move to the correct position before capture");
        assertNull(board.getPieceAt(move.getStartX(), move.getStartY()), "Original position should be empty");
    }


//    @Test
//    void testQueenMovesToPositionBeforeCapturedPiece() {
//        board.setPieceAt(2, 2, whiteQueen);
//        board.setPieceAt(5, 5, blackPawn);
//
//        VariantChessMove move = new VariantChessMove(2, 2, 5, 5);
//        rule.applyRule(move, whiteQueen, board);
//
//        // Calculate expected position
//        int expectedX = 5 - 1; // Moving one step back in X direction
//        int expectedY = 5 - 1; // Moving one step back in Y direction
//
//        assertEquals(whiteQueen, board.getPieceAt(expectedX, expectedY), "Queen should move to the position before captured piece");
//        assertEquals(Color.WHITE, board.getPieceAt(5, 5).getColor(), "Captured piece should be white");
//    }
//    @Test
//    void testQueenCapturesAndMovesBackCorrectly() {
//        board.setPieceAt(4, 4, whiteQueen);
//        board.setPieceAt(6, 6, blackPawn);
//
//        VariantChessMove move = new VariantChessMove(4, 4, 6, 6);
//
//        rule.applyRule(move, whiteQueen, board);
//
//        // Calculate expected position
//        int expectedX = 6 - 1; // Moving one step back in X direction
//        int expectedY = 6 - 1; // Moving one step back in Y direction
//
//        assertEquals(whiteQueen, board.getPieceAt(expectedX, expectedY), "Queen should move to the previous position");
//        assertEquals(Color.WHITE, board.getPieceAt(6, 6).getColor(), "Captured piece should be white");
//    }
//
//    /**
//     * Tests that a White King correctly performs a special capture on a Black Pawn.
//     * Validates that the capture rule has been applied, the pawn's first move state is retained,
//     * and both the King and the captured Pawn can still move correctly.
//     */
//    @Test
//    void testWhiteKingSpecialCaptureWithPawn() {
//        board.setPieceAt(4, 4, whiteKing);
//        board.setPieceAt(5, 5, blackPawn);
//
//        VariantChessMove move = new VariantChessMove(4, 4, 5, 5);
//        rule.applyRule(move, whiteKing, board);
//
//        assertEquals(Color.WHITE, board.getPieceAt(5, 5).getColor(), "Captured piece should be white");
//        assertEquals(whiteKing, board.getPieceAt(4, 4), "King should stay in the original position");
//        assertTrue(rule.hasUsedSpecialCapture(), "Special capture should be marked as used");
//        assertTrue(((Pawn) board.getPieceAt(5, 5)).isFirstMove(), "Captured pawn should retain its isFirstMove state");
//        assertEquals(Color.WHITE, ((Pawn) board.getPieceAt(5, 5)).getColor(), "Captured pawn should have white color");
//
//        // Try to move the King again
//        VariantChessMove kingMove = new VariantChessMove(4, 4, 4, 3);
//        assertTrue(whiteKing.isValidMove(kingMove, board), "King should be able to move again");
//
//        // Try to move the captured pawn
//        VariantChessMove capturedPieceMove = new VariantChessMove(5, 5, 4, 5); // White pawn moves upward
//        VariantChessPiece capturedPiece = board.getPieceAt(5, 5);
//        assertTrue(capturedPiece.isValidMove(capturedPieceMove, board), "Captured pawn should be able to move");
//    }
//
//    /**
//     * Tests that a Black King correctly performs a special capture on a White Pawn.
//     * Validates that the capture rule has been applied, the pawn's first move state is retained,
//     * and both the King and the captured Pawn can still move correctly.
//     */
//    @Test
//    void testBlackKingSpecialCaptureWithPawn() {
//        board.setPieceAt(4, 4, blackKing);
//        board.setPieceAt(5, 5, whitePawn);
//
//        VariantChessMove move = new VariantChessMove(4, 4, 5, 5);
//        rule.applyRule(move, blackKing, board);
//
//        assertEquals(Color.BLACK, board.getPieceAt(5, 5).getColor(), "Captured piece should be black");
//        assertEquals(blackKing, board.getPieceAt(4, 4), "King should stay in the original position");
//        assertTrue(rule.hasUsedSpecialCapture(), "Special capture should be marked as used");
//        assertTrue(((Pawn) board.getPieceAt(5, 5)).isFirstMove(), "Captured pawn should retain its isFirstMove state");
//        assertEquals(Color.BLACK, ((Pawn) board.getPieceAt(5, 5)).getColor(), "Captured pawn should have black color");
//
//        // Try to move the King again
//        VariantChessMove kingMove = new VariantChessMove(4, 4, 4, 3);
//        assertTrue(blackKing.isValidMove(kingMove, board), "King should be able to move again");
//
//        // Try to move the captured pawn
//        VariantChessMove capturedPieceMove = new VariantChessMove(5, 5, 6, 5); // Black pawn moves downward
//        VariantChessPiece capturedPiece = board.getPieceAt(5, 5);
//        assertTrue(capturedPiece.isValidMove(capturedPieceMove, board), "Captured pawn should be able to move");
//    }
//
//    /**
//     * Tests that a White Queen correctly performs a special capture on a Black Pawn.
//     * Validates that the capture rule has been applied, the pawn's first move state is retained,
//     * and both the Queen and the captured Pawn can still move correctly.
//     */
//    @Test
//    void testWhiteQueenSpecialCaptureWithPawn() {
//        board.setPieceAt(3, 3, whiteQueen);
//        board.setPieceAt(5, 5, blackPawn);
//
//        VariantChessMove move = new VariantChessMove(3, 3, 5, 5);
//        rule.applyRule(move, whiteQueen, board);
//
//        assertEquals(Color.WHITE, board.getPieceAt(5, 5).getColor(), "Captured piece should be white");
//        assertEquals(whiteQueen, board.getPieceAt(4, 4), "Queen should move to the previous position");
//        assertTrue(rule.hasUsedSpecialCapture(), "Special capture should be marked as used");
//        assertTrue(((Pawn) board.getPieceAt(5, 5)).isFirstMove(), "Captured pawn should retain its isFirstMove state");
//        assertEquals(Color.WHITE, ((Pawn) board.getPieceAt(5, 5)).getColor(), "Captured pawn should have white color");
//
//        // Try to move the Queen again
//        VariantChessMove queenMove = new VariantChessMove(4, 4, 3, 4);
//        assertTrue(whiteQueen.isValidMove(queenMove, board), "Queen should be able to move again");
//
//        // Try to move the captured pawn
//        VariantChessMove capturedPieceMove = new VariantChessMove(5, 5, 4, 5); // White pawn moves upward
//        VariantChessPiece capturedPiece = board.getPieceAt(5, 5);
//        assertTrue(capturedPiece.isValidMove(capturedPieceMove, board), "Captured pawn should be able to move");
//    }
//
//    /**
//     * Tests that a Black Queen correctly performs a special capture on a White Pawn.
//     * Validates that the capture rule has been applied, the pawn's first move state is retained,
//     * and both the Queen and the captured Pawn can still move correctly.
//     */
//    @Test
//    void testBlackQueenSpecialCaptureWithPawn() {
//        board.setPieceAt(3, 3, blackQueen);
//        board.setPieceAt(5, 5, whitePawn);
//
//        VariantChessMove move = new VariantChessMove(3, 3, 5, 5);
//        rule.applyRule(move, blackQueen, board);
//
//        assertEquals(Color.BLACK, board.getPieceAt(5, 5).getColor(), "Captured piece should be black");
//        assertEquals(blackQueen, board.getPieceAt(4, 4), "Queen should move to the previous position");
//        assertTrue(rule.hasUsedSpecialCapture(), "Special capture should be marked as used");
//        assertTrue(((Pawn) board.getPieceAt(5, 5)).isFirstMove(), "Captured pawn should retain its isFirstMove state");
//        assertEquals(Color.BLACK, ((Pawn) board.getPieceAt(5, 5)).getColor(), "Captured pawn should have black color");
//
//        // Try to move the Queen again
//        VariantChessMove queenMove = new VariantChessMove(4, 4, 3, 4);
//        assertTrue(blackQueen.isValidMove(queenMove, board), "Queen should be able to move again");
//
//        // Try to move the captured pawn
//        VariantChessMove capturedPieceMove = new VariantChessMove(5, 5, 6, 5); // Black pawn moves downward
//        VariantChessPiece capturedPiece = board.getPieceAt(5, 5);
//        assertTrue(capturedPiece.isValidMove(capturedPieceMove, board), "Captured pawn should be able to move");
//    }
//
//    /**
//     * Tests that a White King can capture a Black Pawn from all possible adjacent directions.
//     * Validates that the capture rule has been applied correctly in each direction and the King retains its position.
//     */
//    @Test
//    void testKingCaptureAdjacentPawn() {
//        int[][] directions = {
//                {5, 4}, {3, 4}, {4, 5}, {4, 3}, // Up, Down, Right, Left
//                {5, 5}, {5, 3}, {3, 5}, {3, 3}  // Diagonals
//        };
//
//        for (int[] dir : directions) {
//            setUp();  // Reset the board and pieces for each iteration
//            board.setPieceAt(4, 4, whiteKing);
//            board.setPieceAt(dir[0], dir[1], blackPawn);
//            VariantChessMove move = new VariantChessMove(4, 4, dir[0], dir[1]);
//            rule.applyRule(move, whiteKing, board);
//
//            assertEquals(Color.WHITE, board.getPieceAt(dir[0], dir[1]).getColor(), "Captured piece should be white");
//            assertEquals(whiteKing, board.getPieceAt(4, 4), "King should stay in the original position");
//            assertTrue(rule.hasUsedSpecialCapture(), "Special capture should be marked as used");
//            assertTrue(((Pawn) board.getPieceAt(dir[0], dir[1])).isFirstMove(), "Captured pawn should retain its isFirstMove state");
//            assertEquals(Color.WHITE, ((Pawn) board.getPieceAt(dir[0], dir[1])).getColor(), "Captured pawn should have white color");
//        }
//    }
//
//    /**
//     * Tests that a White Queen can capture a Black Pawn in both straight and diagonal directions.
//     * Validates that the capture rule is correctly applied in each direction and the Queen moves to its previous position.
//     */
//    @Test
//    void testQueenCaptureStraightAndDiagonalPawn() {
//        int[][] straightDirections = {
//                {4, 0}, {4, 7}, {0, 4}, {7, 4} // Left, Right, Up, Down
//        };
//
//        for (int[] dir : straightDirections) {
//            setUp();  // Reset the board and pieces for each iteration
//            board.setPieceAt(4, 4, whiteQueen);
//            board.setPieceAt(dir[0], dir[1], blackPawn);
//            VariantChessMove move = new VariantChessMove(4, 4, dir[0], dir[1]);
//            rule.applyRule(move, whiteQueen, board);
//
//            assertEquals(Color.WHITE, board.getPieceAt(dir[0], dir[1]).getColor(), "Captured piece should be white");
//            int expectedX = dir[0] == 4 ? 4 : (dir[0] < 4 ? dir[0] + 1 : dir[0] - 1);
//            int expectedY = dir[1] == 4 ? 4 : (dir[1] < 4 ? dir[1] + 1 : dir[1] - 1);
//            assertEquals(whiteQueen, board.getPieceAt(expectedX, expectedY), "Queen should move to the previous position");
//            assertTrue(rule.hasUsedSpecialCapture(), "Special capture should be marked as used");
//            assertTrue(((Pawn) board.getPieceAt(dir[0], dir[1])).isFirstMove(), "Captured pawn should retain its isFirstMove state");
//            assertEquals(Color.WHITE, ((Pawn) board.getPieceAt(dir[0], dir[1])).getColor(), "Captured pawn should have white color");
//        }
//
//        int[][] diagonalDirections = {
//                {0, 0}, {7, 7}, {0, 7}, {7, 0} // Diagonals
//        };
//
//        for (int[] dir : diagonalDirections) {
//            setUp();  // Reset the board and pieces for each iteration
//            board.setPieceAt(4, 4, whiteQueen);
//            board.setPieceAt(dir[0], dir[1], blackPawn);
//            VariantChessMove move = new VariantChessMove(4, 4, dir[0], dir[1]);
//            rule.applyRule(move, whiteQueen, board);
//
//            assertEquals(Color.WHITE, board.getPieceAt(dir[0], dir[1]).getColor(), "Captured piece should be white");
//            int expectedX = (dir[0] < 4) ? dir[0] + 1 : dir[0] - 1;
//            int expectedY = (dir[1] < 4) ? dir[1] + 1 : dir[1] - 1;
//            assertEquals(whiteQueen, board.getPieceAt(expectedX, expectedY), "Queen should move to the previous position");
//            assertTrue(rule.hasUsedSpecialCapture(), "Special capture should be marked as used");
//            assertTrue(((Pawn) board.getPieceAt(dir[0], dir[1])).isFirstMove(), "Captured pawn should retain its isFirstMove state");
//            assertEquals(Color.WHITE, ((Pawn) board.getPieceAt(dir[0], dir[1])).getColor(), "Captured pawn should have white color");
//        }
//    }
//
//    /**
//     * Tests that a White King can capture a Black Pawn when they are positioned near the edge of the board.
//     * Validates that the capture rule is applied correctly and the King retains its position.
//     */
//    @Test
//    void testKingCaptureAtBoardEdge() {
//        board.setPieceAt(2, 2, whiteKing);
//        board.setPieceAt(3, 3, blackPawn);
//
//        VariantChessMove move = new VariantChessMove(2, 2, 3, 3);
//        rule.applyRule(move, whiteKing, board);
//
//        assertEquals(Color.WHITE, board.getPieceAt(3, 3).getColor(), "Captured piece should be white");
//        assertEquals(whiteKing, board.getPieceAt(2, 2), "King should stay in the original position");
//        assertTrue(rule.hasUsedSpecialCapture(), "Special capture should be marked as used");
//        assertTrue(((Pawn) board.getPieceAt(3, 3)).isFirstMove(), "Captured pawn should retain its isFirstMove state");
//        assertEquals(Color.WHITE, ((Pawn) board.getPieceAt(3, 3)).getColor(), "Captured pawn should have white color");
//    }
//
//    /**
//     * Tests that a White Queen can capture a Black Pawn when they are positioned near the edge of the board.
//     * Validates that the capture rule is applied correctly and the Queen moves to its previous position.
//     */
//    @Test
//    void testQueenCaptureAtBoardEdge() {
//        board.setPieceAt(2, 2, whiteQueen);
//        board.setPieceAt(0, 7, blackPawn);
//
//        VariantChessMove move = new VariantChessMove(2, 2, 0, 7);
//        rule.applyRule(move, whiteQueen, board);
//
//        assertEquals(Color.WHITE, board.getPieceAt(0, 7).getColor(), "Captured piece should be white");
//        assertEquals(whiteQueen, board.getPieceAt(1, 6), "Queen should move to the previous position");
//        assertTrue(rule.hasUsedSpecialCapture(), "Special capture should be marked as used");
//        assertTrue(((Pawn) board.getPieceAt(0, 7)).isFirstMove(), "Captured pawn should retain its isFirstMove state");
//        assertEquals(Color.WHITE, ((Pawn) board.getPieceAt(0, 7)).getColor(), "Captured pawn should have white color");
//    }
//    @Test
//    void testKingMovesToPositionBeforeCapturedPiece() {
//        board.setPieceAt(3, 3, whiteKing);
//        board.setPieceAt(4, 4, blackPawn);
//
//        VariantChessMove move = new VariantChessMove(3, 3, 4, 4);
//        rule.applyRule(move, whiteKing, board);
//
//        // King should stay in the same position
//        assertEquals(whiteKing, board.getPieceAt(3, 3), "King should stay in the original position");
//        assertEquals(Color.WHITE, board.getPieceAt(4, 4).getColor(), "Captured piece should be white");
//    }
}
