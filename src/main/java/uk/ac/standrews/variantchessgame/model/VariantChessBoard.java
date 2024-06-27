package uk.ac.standrews.variantchessgame.model;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class VariantChessBoard {
    private VariantChessPiece[][] board;

    public VariantChessPiece[][] getBoard() {
        return board;
    }

    public VariantChessBoard() {
        board = new VariantChessPiece[8][8];
        initializeBoard();
    }

    public void initializeBoard() {
        placeMajorPieces(0, Color.WHITE);
        placePawnsAndCannons(1, Color.WHITE);
        placePawnsAndCannons(6, Color.BLACK);
        placeMajorPieces(7, Color.BLACK);
    }

    private void placeMajorPieces(int row, Color color) {
        board[row][0] = new Rook(color);
        board[row][7] = new Rook(color);

        Random rand = new Random();
        int[] pieces = {1, 2, 3, 4, 5, 6};
        shuffleArray(pieces, rand);

        board[row][pieces[0]] = new Knight(color);
        board[row][pieces[1]] = new Knight(color);
        board[row][pieces[2]] = new Bishop(color);
        board[row][pieces[3]] = new Bishop(color);
        board[row][pieces[4]] = new Queen(color);
        board[row][pieces[5]] = new King(color);
    }

    private void placePawnsAndCannons(int row, Color color) {
        Random rand = new Random();
        int[] positions = {0, 2, 3, 4, 5, 6};  // 固定炮的位置
        shuffleArray(positions, rand);

        board[row][1] = new Cannon(color);  // 固定炮的位置
        board[row][6] = new Cannon(color);  // 固定炮的位置
        for (int i = 0; i < positions.length; i++) {
            board[row][positions[i]] = new Pawn(color);
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
        return board[x][y];
    }

    public void movePiece(VariantChessMove move) {
        VariantChessPiece piece = board[move.getStartX()][move.getStartY()];
        if (piece != null) {
            board[move.getStartX()][move.getStartY()] = null;
            board[move.getEndX()][move.getEndY()] = piece;
        }
    }

}
