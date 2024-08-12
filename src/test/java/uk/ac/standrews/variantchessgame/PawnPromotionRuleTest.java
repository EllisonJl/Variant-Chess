//package uk.ac.standrews.variantchessgame;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import uk.ac.standrews.variantchessgame.model.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests for the Pawn promotion rule in the variant chess game.
// * These tests verify the correct behavior of Pawn promotion based on the number of captures made by the Pawn.
// */
//class PawnPromotionRuleTest {
//
//    private VariantChessBoard board;
//    private Pawn whitePawn;
//    private PawnPromotionRule rule;
//
//    /**
//     * Sets up the test environment before each test is run.
//     * Initializes the VariantChessBoard and creates a white Pawn piece.
//     * Sets up the PawnPromotionRule for testing promotion scenarios.
//     */
//    @BeforeEach
//    void setUp() {
//        board = new VariantChessBoard();
//        whitePawn = new Pawn(Color.WHITE);
//        rule = new PawnPromotionRule();
//        // Place the white Pawn at position (6, 0) to test promotion from the sixth row
//        board.getBoard()[6][0] = whitePawn;
//    }
//
//    /**
//     * Tests Pawn promotion to a Knight or Bishop after the first capture.
//     * The Pawn should be promoted to a Knight or a Bishop after the first capture.
//     * Verifies that the new piece is correctly promoted and can move according to its rules.
//     */
//    @Test
//    void testPawnPromotionFirstCapture() {
//        // Place a black Rook at (5, 0) for the first capture
//        board.setPieceAt(5, 0, new Rook(Color.BLACK));
//        VariantChessMove move = new VariantChessMove(6, 0, 5, 0);
//        board.movePiece(move);
//        rule.applyRule(move, board.getPieceAt(5, 0), board);
//
//        VariantChessPiece newPiece = board.getPieceAt(5, 0);
//        assertTrue(newPiece instanceof Knight || newPiece instanceof Bishop,
//                "Pawn should be promoted to Knight or Bishop after the first capture.");
//        assertEquals(Color.WHITE, newPiece.getColor(), "New piece should be white.");
//
//        // Verify the new piece's movement rules
//        VariantChessMove validMove;
//        if (newPiece instanceof Knight) {
//            validMove = new VariantChessMove(5, 0, 3, 1); // Knight L-shape move
//        } else {
//            validMove = new VariantChessMove(5, 0, 3, 2); // Bishop diagonal move
//        }
//        assertTrue(newPiece.isValidMove(validMove, board), "New piece should be able to move according to its rules.");
//    }
//
//    /**
//     * Tests Pawn promotion after the Pawn has made two captures.
//     * The Pawn should be promoted to either a Cannon or a Rook after the second capture.
//     * Verifies that the new piece is correctly promoted and can move according to its rules.
//     */
//    @Test
//    void testPawnPromotionSecondCapture() {
//        // Place a black Rook at (5, 0) for the first capture
//        board.setPieceAt(5, 0, new Rook(Color.BLACK));
//        VariantChessMove move = new VariantChessMove(6, 0, 5, 0);
//        board.movePiece(move);
//        rule.applyRule(move, board.getPieceAt(5, 0), board);
//
//        // Place another black Rook at (4, 0) for the second capture
//        board.setPieceAt(4, 0, new Rook(Color.BLACK));
//        move = new VariantChessMove(5, 0, 4, 0);
//        board.movePiece(move);
//        rule.applyRule(move, board.getPieceAt(4, 0), board);
//
//        VariantChessPiece newPiece = board.getPieceAt(4, 0);
//        assertTrue(newPiece instanceof Cannon || newPiece instanceof Rook,
//                "Pawn should be promoted to Cannon or Rook after the second capture.");
//        assertEquals(Color.WHITE, newPiece.getColor(), "New piece should be white.");
//
//        // Verify the new piece's movement rules
//        VariantChessMove validMove;
//        if (newPiece instanceof Cannon) {
//            validMove = new VariantChessMove(4, 0, 4, 7); // Cannon horizontal move
//        } else {
//            validMove = new VariantChessMove(4, 0, 4, 7); // Rook horizontal move
//        }
//        assertTrue(newPiece.isValidMove(validMove, board), "New piece should be able to move according to its rules.");
//    }
//
//    /**
//     * Tests Pawn promotion after the Pawn has made three captures.
//     * The Pawn should be promoted to a Queen after the third capture.
//     * Verifies that the new piece is correctly promoted and can move according to its rules.
//     */
//    @Test
//    void testPawnPromotionThirdCapture() {
//        // Place a black Rook at (5, 0) for the first capture
//        board.getBoard()[5][0] = new Rook(Color.BLACK);
//        VariantChessMove move = new VariantChessMove(6, 0, 5, 0);
//        board.movePiece(move);
//        rule.applyRule(move, board.getPieceAt(5, 0), board);
//
//        // Place another black Rook at (4, 0) for the second capture
//        board.getBoard()[4][0] = new Rook(Color.BLACK);
//        move = new VariantChessMove(5, 0, 4, 0);
//        board.movePiece(move);
//        rule.applyRule(move, board.getPieceAt(4, 0), board);
//
//        // Place a third black Rook at (3, 0) for the third capture
//        board.getBoard()[3][0] = new Rook(Color.BLACK);
//        move = new VariantChessMove(4, 0, 3, 0);
//        board.movePiece(move);
//        rule.applyRule(move, board.getPieceAt(3, 0), board);
//
//        VariantChessPiece newPiece = board.getPieceAt(3, 0);
//        assertTrue(newPiece instanceof Queen, "Pawn should be promoted to Queen after the third capture.");
//        assertEquals(Color.WHITE, newPiece.getColor(), "New piece should be white.");
//
//        // Verify the new piece's movement rules
//        VariantChessMove queenMove = new VariantChessMove(3, 0, 3, 7); // Queen horizontal move
//        assertTrue(newPiece.isValidMove(queenMove, board), "Queen should be able to move according to its rules.");
//    }
//
//    /**
//     * Tests that the Pawn remains a Queen after capturing a fourth time.
//     * The Pawn should not be promoted further beyond the Queen after the fourth capture.
//     * Verifies that the Pawn remains a Queen and does not promote to any other piece.
//     */
//    @Test
//    void testPawnNoPromotionAfterFourthCapture() {
//        // Place a black Rook at (5, 0) for the first capture
//        board.getBoard()[5][0] = new Rook(Color.BLACK);
//        VariantChessMove move = new VariantChessMove(6, 0, 5, 0);
//        board.movePiece(move);
//        rule.applyRule(move, board.getPieceAt(5, 0), board);
//
//        // Place another black Rook at (4, 0) for the second capture
//        board.getBoard()[4][0] = new Rook(Color.BLACK);
//        move = new VariantChessMove(5, 0, 4, 0);
//        board.movePiece(move);
//        rule.applyRule(move, board.getPieceAt(4, 0), board);
//
//        // Place a third black Rook at (3, 0) for the third capture
//        board.getBoard()[3][0] = new Rook(Color.BLACK);
//        move = new VariantChessMove(4, 0, 3, 0);
//        board.movePiece(move);
//        rule.applyRule(move, board.getPieceAt(3, 0), board);
//
//        // Place a fourth black Rook at (2, 0) for the fourth capture
//        board.getBoard()[2][0] = new Rook(Color.BLACK);
//        move = new VariantChessMove(3, 0, 2, 0);
//        board.movePiece(move);
//        rule.applyRule(move, board.getPieceAt(2, 0), board);
//
//        VariantChessPiece newPiece = board.getPieceAt(2, 0);
//        assertTrue(newPiece instanceof Queen, "Pawn should remain a Queen after the fourth capture.");
//        assertEquals(Color.WHITE, newPiece.getColor(), "New piece should be white.");
//    }
//
//    /**
//     * Tests that the Pawn can move correctly after promotion.
//     * Verifies that the promoted piece moves according to its new rules, based on the type of piece it was promoted to.
//     */
//    @Test
//    void testPawnMovesAfterPromotion() {
//        // Place a black Rook at (5, 0) for the first capture
//        board.getBoard()[5][0] = new Rook(Color.BLACK);
//        VariantChessMove move = new VariantChessMove(6, 0, 5, 0);
//        board.movePiece(move);
//        rule.applyRule(move, board.getPieceAt(5, 0), board);
//
//        VariantChessPiece promotedPiece = board.getPieceAt(5, 0);
//        assertNotNull(promotedPiece, "Promoted piece should be present.");
//
//        // Check that the promoted piece can move according to its rules
//        VariantChessMove moveToTest;
//        if (promotedPiece instanceof Knight) {
//            moveToTest = new VariantChessMove(5, 0, 3, 1); // Knight L-shape move
//        } else if (promotedPiece instanceof Bishop) {
//            moveToTest = new VariantChessMove(5, 0, 3, 2); // Bishop diagonal move
//        } else if (promotedPiece instanceof Cannon) {
//            moveToTest = new VariantChessMove(5, 0, 5, 7); // Cannon horizontal move
//        } else if (promotedPiece instanceof Rook) {
//            moveToTest = new VariantChessMove(5, 0, 5, 7); // Rook horizontal move
//        } else if (promotedPiece instanceof Queen) {
//            moveToTest = new VariantChessMove(5, 0, 5, 7); // Queen horizontal move
//        } else {
//            fail("Unexpected piece type after promotion.");
//            return;
//        }
//        assertTrue(promotedPiece.isValidMove(moveToTest, board), "Promoted piece should be able to move according to its new rules.");
//    }
//}
