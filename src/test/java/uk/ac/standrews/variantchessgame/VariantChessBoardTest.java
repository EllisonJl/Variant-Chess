package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

class VariantChessBoardTest {

    private VariantChessBoard board;

    @BeforeEach
    void setUp() {
        board = new VariantChessBoard();
    }

    @Test
    void testInitialBoardSetup() {
        VariantChessPiece[][] initialBoard = board.getBoard();

        // Test fixed positions for Rook, Cannon, and Pawn
        for (int i = 0; i < 8; i++) {
            if (i == 1 || i == 6) {
                assertTrue(initialBoard[1][i] instanceof Cannon, "White Cannon should be at (1,1) and (1,6)");
                assertTrue(initialBoard[6][i] instanceof Cannon, "Black Cannon should be at (6,1) and (6,6)");
            } else {
                assertTrue(initialBoard[1][i] instanceof Pawn, "White Pawn should be at (1," + i + ")");
                assertTrue(initialBoard[6][i] instanceof Pawn, "Black Pawn should be at (6," + i + ")");
            }
        }

        // Test fixed positions for Rook
        assertTrue(initialBoard[0][0] instanceof Rook && initialBoard[0][0].getColor() == Color.WHITE, "White Rook should be at (0,0)");
        assertTrue(initialBoard[0][7] instanceof Rook && initialBoard[0][7].getColor() == Color.WHITE, "White Rook should be at (0,7)");
        assertTrue(initialBoard[7][0] instanceof Rook && initialBoard[7][0].getColor() == Color.BLACK, "Black Rook should be at (7,0)");
        assertTrue(initialBoard[7][7] instanceof Rook && initialBoard[7][7].getColor() == Color.BLACK, "Black Rook should be at (7,7)");

        // Test random positions for major pieces (Knight, Bishop, King, Queen)
        int whiteKnights = 0, blackKnights = 0;
        int whiteBishops = 0, blackBishops = 0;
        int whiteQueens = 0, blackQueens = 0;
        int whiteKings = 0, blackKings = 0;

        for (int i = 1; i < 7; i++) {
            VariantChessPiece piece = initialBoard[0][i];
            if (piece instanceof Knight && piece.getColor() == Color.WHITE) whiteKnights++;
            if (piece instanceof Bishop && piece.getColor() == Color.WHITE) whiteBishops++;
            if (piece instanceof Queen && piece.getColor() == Color.WHITE) whiteQueens++;
            if (piece instanceof King && piece.getColor() == Color.WHITE) whiteKings++;

            piece = initialBoard[7][i];
            if (piece instanceof Knight && piece.getColor() == Color.BLACK) blackKnights++;
            if (piece instanceof Bishop && piece.getColor() == Color.BLACK) blackBishops++;
            if (piece instanceof Queen && piece.getColor() == Color.BLACK) blackQueens++;
            if (piece instanceof King && piece.getColor() == Color.BLACK) blackKings++;
        }

        assertEquals(2, whiteKnights, "There should be 2 White Knights");
        assertEquals(2, blackKnights, "There should be 2 Black Knights");
        assertEquals(2, whiteBishops, "There should be 2 White Bishops");
        assertEquals(2, blackBishops, "There should be 2 Black Bishops");
        assertEquals(1, whiteQueens, "There should be 1 White Queen");
        assertEquals(1, blackQueens, "There should be 1 Black Queen");
        assertEquals(1, whiteKings, "There should be 1 White King");
        assertEquals(1, blackKings, "There should be 1 Black King");
    }

    @Test
    void testMovePiece() {
        VariantChessPiece[][] initialBoard = board.getBoard();
        Pawn whitePawn = (Pawn) initialBoard[1][0];
        VariantChessMove move = new VariantChessMove(1, 0, 2, 0);

        board.movePiece(move);

        assertNull(board.getPieceAt(1, 0), "The original position should be empty after the move.");
        assertEquals(whitePawn, board.getPieceAt(2, 0), "The pawn should be at the new position after the move.");
    }

    @Test
    void testInvalidMoveOutOfBounds() {
        assertFalse(board.isInBounds(-1, 0), "Position (-1, 0) should be out of bounds.");
        assertFalse(board.isInBounds(8, 0), "Position (8, 0) should be out of bounds.");
        assertFalse(board.isInBounds(0, -1), "Position (0, -1) should be out of bounds.");
        assertFalse(board.isInBounds(0, 8), "Position (0, 8) should be out of bounds.");
    }

    @Test
    void testSetPieceAt() {
        VariantChessPiece knight = new Knight(Color.WHITE);
        board.setPieceAt(4, 4, knight);

        assertEquals(knight, board.getPieceAt(4, 4), "The knight should be placed at the specified position.");
    }

    @Test
    void testIsInBounds() {
        assertTrue(board.isInBounds(0, 0), "Position (0, 0) should be in bounds.");
        assertTrue(board.isInBounds(7, 7), "Position (7, 7) should be in bounds.");
        assertFalse(board.isInBounds(8, 8), "Position (8, 8) should be out of bounds.");
        assertFalse(board.isInBounds(-1, -1), "Position (-1, -1) should be out of bounds.");
    }
}
