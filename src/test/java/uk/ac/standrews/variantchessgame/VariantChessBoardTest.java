package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.*;

import static org.junit.jupiter.api.Assertions.*;

class VariantChessBoardTest {

    private VariantChessBoard board;

    @BeforeEach
    void setUp() {
        // Initialize a new VariantChessBoard before each test case.
        board = new VariantChessBoard();
    }

    @Test
    void testInitialBoardSetup() {
        // Test the initial setup of the chessboard to ensure pieces are placed correctly.

        VariantChessPiece[][] initialBoard = board.getBoard();

        // Validate positions for Cannon and Pawn on the board.
        for (int i = 0; i < 8; i++) {
            if (i == 1 || i == 6) {
                assertTrue(initialBoard[1][i] instanceof Cannon && initialBoard[1][i].getColor() == Color.BLACK, "Black Cannon should be at (1," + i + ")");
                assertTrue(initialBoard[6][i] instanceof Cannon && initialBoard[6][i].getColor() == Color.WHITE, "White Cannon should be at (6," + i + ")");
            } else {
                assertTrue(initialBoard[1][i] instanceof Pawn && initialBoard[1][i].getColor() == Color.BLACK, "Black Pawn should be at (1," + i + ")");
                assertTrue(initialBoard[6][i] instanceof Pawn && initialBoard[6][i].getColor() == Color.WHITE, "White Pawn should be at (6," + i + ")");
            }
        }

        // Validate fixed positions for Rooks on the board.
        assertTrue(initialBoard[0][0] instanceof Rook && initialBoard[0][0].getColor() == Color.BLACK, "Black Rook should be at (0,0)");
        assertTrue(initialBoard[0][7] instanceof Rook && initialBoard[0][7].getColor() == Color.BLACK, "Black Rook should be at (0,7)");
        assertTrue(initialBoard[7][0] instanceof Rook && initialBoard[7][0].getColor() == Color.WHITE, "White Rook should be at (7,0)");
        assertTrue(initialBoard[7][7] instanceof Rook && initialBoard[7][7].getColor() == Color.WHITE, "White Rook should be at (7,7)");

        // Count and validate the number of major pieces (Knight, Bishop, King, Queen) for both colors.
        int whiteKnights = 0, blackKnights = 0;
        int whiteBishops = 0, blackBishops = 0;
        int whiteQueens = 0, blackQueens = 0;
        int whiteKings = 0, blackKings = 0;

        for (int i = 1; i < 7; i++) {
            VariantChessPiece piece = initialBoard[0][i];
            if (piece instanceof Knight && piece.getColor() == Color.BLACK) blackKnights++;
            if (piece instanceof Bishop && piece.getColor() == Color.BLACK) blackBishops++;
            if (piece instanceof Queen && piece.getColor() == Color.BLACK) blackQueens++;
            if (piece instanceof King && piece.getColor() == Color.BLACK) blackKings++;

            piece = initialBoard[7][i];
            if (piece instanceof Knight && piece.getColor() == Color.WHITE) whiteKnights++;
            if (piece instanceof Bishop && piece.getColor() == Color.WHITE) whiteBishops++;
            if (piece instanceof Queen && piece.getColor() == Color.WHITE) whiteQueens++;
            if (piece instanceof King && piece.getColor() == Color.WHITE) whiteKings++;
        }

        // Assert the expected counts for each type of piece.
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
        // Test the functionality of moving a piece on the board.
        VariantChessPiece[][] initialBoard = board.getBoard();
        Pawn whitePawn = (Pawn) initialBoard[6][0]; // White Pawn initially at (6, 0)
        VariantChessMove move = new VariantChessMove(6, 0, 5, 0); // Move the Pawn from (6, 0) to (5, 0)

        board.movePiece(move);

        // Verify that the original position is now empty and the Pawn is at the new position.
        assertNull(board.getPieceAt(6, 0), "The original position should be empty after the move.");
        assertEquals(whitePawn, board.getPieceAt(5, 0), "The pawn should be at the new position after the move.");
    }

    @Test
    void testInvalidMoveOutOfBounds() {
        // Test whether the board correctly identifies positions that are out of bounds.

        assertFalse(board.isInBounds(-1, 0), "Position (-1, 0) should be out of bounds.");
        assertFalse(board.isInBounds(8, 0), "Position (8, 0) should be out of bounds.");
        assertFalse(board.isInBounds(0, -1), "Position (0, -1) should be out of bounds.");
        assertFalse(board.isInBounds(0, 8), "Position (0, 8) should be out of bounds.");
    }

    @Test
    void testSetPieceAt() {
        // Test the functionality of placing a piece at a specified position on the board.
        VariantChessPiece knight = new Knight(Color.WHITE);
        board.setPieceAt(4, 4, knight);

        // Verify that the piece has been correctly placed at the specified position.
        assertEquals(knight, board.getPieceAt(4, 4), "The knight should be placed at the specified position.");
    }

    @Test
    void testIsInBounds() {
        // Test the functionality of checking whether a position is within the board's boundaries.

        assertTrue(board.isInBounds(0, 0), "Position (0, 0) should be in bounds.");
        assertTrue(board.isInBounds(7, 7), "Position (7, 7) should be in bounds.");
        assertFalse(board.isInBounds(8, 8), "Position (8, 8) should be out of bounds.");
        assertFalse(board.isInBounds(-1, -1), "Position (-1, -1) should be out of bounds.");
    }
}
