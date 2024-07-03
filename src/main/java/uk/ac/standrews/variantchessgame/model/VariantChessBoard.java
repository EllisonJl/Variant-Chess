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
        // 固定放置炮的位置
        board[row][1] = new Cannon(color);  // 固定炮的位置
        board[row][6] = new Cannon(color);  // 固定炮的位置

        // 按固定位置放置兵
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
