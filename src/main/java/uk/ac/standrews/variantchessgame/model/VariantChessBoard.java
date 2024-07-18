package uk.ac.standrews.variantchessgame.model;

import java.util.Random;

public class VariantChessBoard {
    private VariantChessPiece[][] board;
    private GameState gameState;

    public VariantChessPiece[][] getBoard() {
        return board;
    }

    public VariantChessBoard() {
        board = new VariantChessPiece[8][8];
        initializeBoard();
    }

    public void initializeBoard() {
        placeMajorPiecesSymmetrically(0, Color.BLACK, 7, Color.WHITE);
        placePawnsAndCannons(1, Color.BLACK);
        placePawnsAndCannons(6, Color.WHITE);
        gameState = new GameState(this); // Reinitialize game state and select new rules
        printBoard();
    }

    private void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                VariantChessPiece piece = board[i][j];
                if (piece == null) {
                    System.out.print(". ");
                } else {
                    System.out.print(piece.getClass().getSimpleName().charAt(0) + " ");
                }
            }
            System.out.println();
        }
    }

    private void placeMajorPiecesSymmetrically(int blackRow, Color blackColor, int whiteRow, Color whiteColor) {
        board[blackRow][0] = new Rook(blackColor);
        board[blackRow][7] = new Rook(blackColor);
        board[whiteRow][0] = new Rook(whiteColor);
        board[whiteRow][7] = new Rook(whiteColor);

        Random rand = new Random();
        int[] pieces = {1, 2, 3, 4, 5, 6};
        shuffleArray(pieces, rand);

        board[blackRow][pieces[0]] = new Knight(blackColor);
        board[blackRow][pieces[1]] = new Knight(blackColor);
        board[blackRow][pieces[2]] = new Bishop(blackColor);
        board[blackRow][pieces[3]] = new Bishop(blackColor);
        board[blackRow][pieces[4]] = new Queen(blackColor);
        board[blackRow][pieces[5]] = new King(blackColor);

        board[whiteRow][pieces[0]] = new Knight(whiteColor);
        board[whiteRow][pieces[1]] = new Knight(whiteColor);
        board[whiteRow][pieces[2]] = new Bishop(whiteColor);
        board[whiteRow][pieces[3]] = new Bishop(whiteColor);
        board[whiteRow][pieces[4]] = new Queen(whiteColor);
        board[whiteRow][pieces[5]] = new King(whiteColor);
    }

    private void placePawnsAndCannons(int row, Color color) {
        board[row][1] = new Cannon(color);
        board[row][6] = new Cannon(color);

        for (int col = 0; col < 8; col++) {
            if (col != 1 && col != 6) {
                board[row][col] = new Pawn(color);
            }
        }
    }

    private void shuffleArray(int[] array, Random rand) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public VariantChessPiece getPieceAt(int x, int y) {
        if (isInBounds(x, y)) {
            return board[x][y];
        }
        return null;
    }

    public void setPieceAt(int x, int y, VariantChessPiece piece) {
        if (isInBounds(x, y)) {
            board[x][y] = piece;
        }
    }

    public void movePiece(VariantChessMove move) {
        VariantChessPiece piece = getPieceAt(move.getStartX(), move.getStartY());
        System.out.println("Moving piece: " + piece + " from (" + move.getStartX() + ", " + move.getStartY() + ") to (" + move.getEndX() + ", " + move.getEndY() + ")");

        // Directly perform the move operation without re-validating
        setPieceAt(move.getEndX(), move.getEndY(), piece); // Move to the new position
        setPieceAt(move.getStartX(), move.getStartY(), null); // Clear the original position

        // If it's a Pawn, update the isFirstMove state
        if (piece instanceof Pawn) {
            ((Pawn) piece).setFirstMove(false);
        }

        System.out.println("Piece moved successfully.");
        System.out.println("Current Board State:");
        printBoard(); // Print current board state
    }

    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
}
