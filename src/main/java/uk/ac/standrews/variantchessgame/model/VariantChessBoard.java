package uk.ac.standrews.variantchessgame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the chessboard in the variant chess game.
 * This class manages the state of the board, including the placement of pieces and the execution of moves.
 */
public class VariantChessBoard {

    // 8x8 board grid to hold chess pieces
    private VariantChessPiece[][] board;
    // Initial state of the board
    private VariantChessPiece[][] initialBoard;
    // Current state of the game
    private GameState gameState;

    /**
     * Constructs a new VariantChessBoard with an initialized 8x8 grid.
     * The board is populated with the initial setup of pieces.
     */
    public VariantChessBoard() {
        board = new VariantChessPiece[8][8];
        initialBoard = new VariantChessPiece[8][8];
        initializeBoard();
    }

    /**
     * Returns the 2D array representing the board.
     *
     * @return 2D array of VariantChessPiece representing the board.
     */
    public VariantChessPiece[][] getBoard() {
        return board;
    }

    /**
     * Returns the 2D array representing the initial board state.
     *
     * @return 2D array of VariantChessPiece representing the initial board state.
     */
    public VariantChessPiece[][] getInitialBoard() {
        return initialBoard;
    }

    /**
     * Initializes the chessboard by placing major pieces, pawns, and cannons in their starting positions.
     * Also sets up the game state and prints the initial board configuration.
     */
    public void initializeBoard() {
        clearBoard();
        placeMajorPiecesSymmetrically(0, Color.BLACK, 7, Color.WHITE);
        placePawnsAndCannons(1, Color.BLACK);
        placePawnsAndCannons(6, Color.WHITE);
        gameState = new GameState(this);

        saveInitialBoardState();

        System.out.println("Board initialized to initial state:");
        printBoard();
    }

    private void clearBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
    }


    /**
     * Saves the current state of the board as the initial state.
     */
    private void saveInitialBoardState() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                initialBoard[i][j] = board[i][j];
            }
        }
    }

    /**
     * Prints the current state of the board to the console.
     * Empty squares are represented by ".", while occupied squares display the first letter of the piece's class name.
     */
    public void printBoard() {
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

    /**
     * Places major pieces (Rooks, Knights, Bishops, Queen, King) on the board symmetrically for both black and white players.
     * The placement is randomized for Knights, Bishops, and the Queen.
     *
     * @param blackRow The row index for black pieces.
     * @param blackColor The color of the black pieces.
     * @param whiteRow The row index for white pieces.
     * @param whiteColor The color of the white pieces.
     */
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

    /**
     * Places pawns and cannons on the board for a given color.
     * Cannons are placed on columns 1 and 6, and pawns are placed on the remaining columns.
     *
     * @param row The row index for placing pawns and cannons.
     * @param color The color of the pieces being placed.
     */
    private void placePawnsAndCannons(int row, Color color) {
        board[row][1] = new Cannon(color);
        board[row][6] = new Cannon(color);

        for (int col = 0; col < 8; col++) {
            if (col != 1 && col != 6) {
                board[row][col] = new Pawn(color);
            }
        }
    }

    /**
     * Shuffles an array in place using the Fisher-Yates algorithm.
     *
     * @param array The array to be shuffled.
     * @param rand The Random object used for generating random indices.
     */
    private void shuffleArray(int[] array, Random rand) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    /**
     * Retrieves the piece located at the specified coordinates on the board.
     *
     * @param x The x-coordinate (row index) of the board.
     * @param y The y-coordinate (column index) of the board.
     * @return The piece located at the specified coordinates, or {@code null} if out of bounds or no piece is present.
     */
    public VariantChessPiece getPieceAt(int x, int y) {
        if (isInBounds(x, y)) {
            return board[x][y];
        }
        return null;
    }

    /**
     * Places a piece at the specified coordinates on the board.
     *
     * @param x The x-coordinate (row index) of the board.
     * @param y The y-coordinate (column index) of the board.
     * @param piece The piece to be placed at the specified coordinates.
     */
    public void setPieceAt(int x, int y, VariantChessPiece piece) {
        if (isInBounds(x, y)) {
            board[x][y] = piece;
        }
    }

    /**
     * Moves a piece from its start position to its end position on the board.
     * Updates the board state and handles special cases such as Pawn's first move.
     *
     * @param move The move to be executed, including start and end positions.
     */
    public void movePiece(VariantChessMove move) {
        VariantChessPiece piece = getPieceAt(move.getStartX(), move.getStartY());
        System.out.println("Moving piece: " + piece + " from (" + move.getStartX() + ", " + move.getStartY() + ") to (" + move.getEndX() + ", " + move.getEndY() + ")");

        setPieceAt(move.getEndX(), move.getEndY(), piece);
        setPieceAt(move.getStartX(), move.getStartY(), null);

        if (piece instanceof Pawn) {
            ((Pawn) piece).setFirstMove(false); // 移动成功后更新 isFirstMove 状态
        }

        System.out.println("Piece moved successfully.");
        System.out.println("Current Board State:");
        printBoard();
    }


    /**
     * Checks if the specified coordinates are within the bounds of the board.
     *
     * @param x The x-coordinate (row index) of the board.
     * @param y The y-coordinate (column index) of the board.
     * @return {@code true} if the coordinates are within bounds, {@code false} otherwise.
     */
    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public List<VariantChessMove> getAllPossibleMoves(Color color) {
        List<VariantChessMove> possibleMoves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                VariantChessPiece piece = getPieceAt(i, j);
                if (piece != null && piece.getColor() == color) {
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            VariantChessMove move = new VariantChessMove(i, j, x, y);
                            if (piece.isValidMove(move, this)) {
                                possibleMoves.add(move);
                            }
                        }
                    }
                }
            }
        }
        return possibleMoves;
    }
}
