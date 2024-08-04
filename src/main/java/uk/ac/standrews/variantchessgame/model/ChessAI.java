package uk.ac.standrews.variantchessgame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChessAI {

    private final Random random = new Random();

    // 简单的评分函数，给棋子类型赋值
    private int evaluatePieceValue(VariantChessPiece piece) {
        if (piece == null) return 0;
        if (piece instanceof Pawn) return 1;
        if (piece instanceof Knight || piece instanceof Bishop) return 3;
        if (piece instanceof Rook) return 5;
        if (piece instanceof Queen) return 9;
        if (piece instanceof King) return 100; // 王的价值
        return 0;
    }

    // 评估棋盘状态
    private int evaluateBoard(VariantChessBoard board, Color aiColor) {
        int score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                VariantChessPiece piece = board.getPieceAt(i, j);
                if (piece != null) {
                    int pieceValue = evaluatePieceValue(piece);
                    score += (piece.getColor() == aiColor) ? pieceValue : -pieceValue;
                }
            }
        }
        return score;
    }

    // 找到最佳移动
    public VariantChessMove calculateBestMove(VariantChessBoard board, Color color) {
        List<VariantChessMove> allPossibleMoves = new ArrayList<>();
        List<VariantChessMove> bestMoves = new ArrayList<>();
        int bestScore = Integer.MIN_VALUE;

        // 遍历所有可能的移动
        for (int startX = 0; startX < 8; startX++) {
            for (int startY = 0; startY < 8; startY++) {
                VariantChessPiece piece = board.getPieceAt(startX, startY);
                if (piece != null && piece.getColor() == color) {
                    for (int endX = 0; endX < 8; endX++) {
                        for (int endY = 0; endY < 8; endY++) {
                            VariantChessMove move = new VariantChessMove(startX, startY, endX, endY);
                            if (piece.isValidMove(move, board)) {
                                allPossibleMoves.add(move);

                                // 进行移动并评估分数
                                VariantChessPiece originalEndPiece = board.getPieceAt(endX, endY);
                                board.movePiece(move);
                                int score = evaluateBoard(board, color);
                                board.movePiece(new VariantChessMove(endX, endY, startX, startY)); // 撤销移动
                                board.setPieceAt(endX, endY, originalEndPiece);

                                // 选择分数最高的移动
                                if (score > bestScore) {
                                    bestScore = score;
                                    bestMoves.clear();
                                    bestMoves.add(move);
                                } else if (score == bestScore) {
                                    bestMoves.add(move);
                                }
                            }
                        }
                    }
                }
            }
        }

        // 如果没有找到任何合法移动
        if (allPossibleMoves.isEmpty()) {
            return null;
        }

        // 随机选择一个评分最高的移动
        return bestMoves.get(random.nextInt(bestMoves.size()));
    }
}
