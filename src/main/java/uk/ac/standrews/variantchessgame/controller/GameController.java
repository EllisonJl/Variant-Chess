package uk.ac.standrews.variantchessgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.ac.standrews.variantchessgame.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final VariantChessBoard board;
    private final GameState gameState;

    @Autowired
    public GameController(VariantChessBoard board) {
        this.board = board;
        this.gameState = new GameState(board); // 初始化 GameState
    }

    @GetMapping("/initialBoard")
    public VariantChessPiece[][] getInitialBoard() {
        System.out.println("Returning current board state.");
        return board.getBoard();
    }


    private boolean processMove(VariantChessMove move, Class<? extends VariantChessPiece> pieceClass) {
        System.out.println(String.format("Received move request: startX=%d, startY=%d, endX=%d, endY=%d", move.getStartX(), move.getStartY(), move.getEndX(), move.getEndY()));
        VariantChessPiece piece = board.getPieceAt(move.getStartX(), move.getStartY());
        if (piece == null) {
            System.out.println(String.format("No piece found at position (%d, %d)", move.getStartX(), move.getStartY()));
            return false;
        }
        System.out.println("当前棋子的颜色是：" + piece.getColor());
        System.out.println("当前回合棋子的颜色是：" + gameState.getCurrentTurn());
        if (pieceClass.isInstance(piece) && piece.getColor() == gameState.getCurrentTurn()) {
            if (piece.isValidMove(move, board)) {
                boolean isCapture = board.getPieceAt(move.getEndX(), move.getEndY()) != null;
                board.movePiece(move);
                gameState.incrementMoveCount();

                if (isCapture) {
                    gameState.resetMoveWithoutCapture();
                } else {
                    gameState.incrementMoveWithoutCapture();
                }

                gameState.switchTurn();
                System.out.println("Move is valid, piece moved.");

                if (gameState.isWin()) {
                    return gameState.getCurrentTurn() == Color.WHITE;
                }

                if (gameState.isDraw()) {
                    return false;
                }

                return true;
            } else {
                System.out.println("Move is invalid according to isValidMove method.");
            }
        } else {
            System.out.println("No piece at start position, piece is not a " + pieceClass.getSimpleName() + ", or it's not the piece's turn.");
        }

        return false;
    }

    @PostMapping("/movePawn")
    public boolean movePawn(@RequestBody VariantChessMove move) {
        return processMove(move, Pawn.class);
    }

    @PostMapping("/moveCannon")
    public boolean moveCannon(@RequestBody VariantChessMove move) {
        return processMove(move, Cannon.class);
    }

    @PostMapping("/moveKing")
    public boolean moveKing(@RequestBody VariantChessMove move) {
        return processMove(move, King.class);
    }

    @PostMapping("/moveKnight")
    public boolean moveKnight(@RequestBody VariantChessMove move) {
        return processMove(move, Knight.class);
    }

    @PostMapping("/moveBishop")
    public boolean moveBishop(@RequestBody VariantChessMove move) {
        return processMove(move, Bishop.class);
    }

    @PostMapping("/moveQueen")
    public boolean moveQueen(@RequestBody VariantChessMove move) {
        return processMove(move, Queen.class);
    }

    @PostMapping("/moveRook")
    public boolean moveRook(@RequestBody VariantChessMove move) {
        return processMove(move, Rook.class);
    }

    @PostMapping("/validMoves")
    public List<VariantChessMove> getValidMoves(@RequestBody ValidMovesRequest request) {
        int startX = request.getStartX();
        int startY = request.getStartY();
        VariantChessPiece piece = board.getPieceAt(startX, startY);

        List<VariantChessMove> validMoves = new ArrayList<>();
        if (piece != null && piece.getColor() == request.getColor()) {
            for (int endX = 0; endX < 8; endX++) {
                for (int endY = 0; endY < 8; endY++) {
                    VariantChessMove move = new VariantChessMove(startX, startY, endX, endY);
                    if (piece.isValidMove(move, board)) {
                        validMoves.add(move);
                    }
                }
            }
        } else {
            System.out.println(String.format("No piece at position (%d, %d) or piece color does not match request color.", startX, startY));
        }
        return validMoves;
    }

    public static class ValidMovesRequest {
        private int startX;
        private int startY;
        private String piece;
        private Color color;

        public int getStartX() {
            return startX;
        }

        public void setStartX(int startX) {
            this.startX = startX;
        }

        public int getStartY() {
            return startY;
        }

        public void setStartY(int startY) {
            this.startY = startY;
        }

        public String getPiece() {
            return piece;
        }

        public void setPiece(String piece) {
            this.piece = piece;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }
    }
}
