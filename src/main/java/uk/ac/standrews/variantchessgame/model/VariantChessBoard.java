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
        placeMajorPieces(0, Color.BLACK);
        placePawnsAndCannons(1, Color.BLACK);
        placePawnsAndCannons(6, Color.WHITE);
        placeMajorPieces(7, Color.WHITE);
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
        return board[x][y];
    }

    public void setPieceAt(int x, int y, VariantChessPiece piece) {
        board[x][y] = piece;
    }

    public void movePiece(VariantChessMove move) {
        VariantChessPiece piece = board[move.getStartX()][move.getStartY()];
        System.out.println("Moving piece: " + piece + " from (" + move.getStartX() + ", " + move.getStartY() + ") to (" + move.getEndX() + ", " + move.getEndY() + ")");

        // 直接执行移动操作，而不再次调用 isValidMove
        board[move.getStartX()][move.getStartY()] = null; // 清空原始位置
        board[move.getEndX()][move.getEndY()] = piece; // 移动到新位置

        // 如果是 Pawn，需要更新 isFirstMove 状态
        if (piece instanceof Pawn) {
            ((Pawn) piece).setFirstMove(false);
        }

        System.out.println("Piece moved successfully.");
        System.out.println("Current Board State:");
        printBoard(); // 打印当前棋盘状态
    }

    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
}
