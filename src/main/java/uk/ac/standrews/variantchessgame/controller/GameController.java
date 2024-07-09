package uk.ac.standrews.variantchessgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.ac.standrews.variantchessgame.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @GetMapping("/board")
    public VariantChessPiece[][] getBoard() {
        logger.info("Returning current board state.");
        return board.getBoard();
    }

    private boolean processMove(VariantChessMove move, Class<? extends VariantChessPiece> pieceClass) {
        logger.info("Received move request: startX={}, startY={}, endX={}, endY={}", move.getStartX(), move.getStartY(), move.getEndX(), move.getEndY());
        VariantChessPiece piece = board.getPieceAt(move.getStartX(), move.getStartY());
        System.out.println("当前棋子的颜色是："+piece.getColor());
        System.out.println("当前回合棋子的颜色是："+ gameState.getCurrentTurn());
        if (piece != null && pieceClass.isInstance(piece) && piece.getColor() == gameState.getCurrentTurn()) {
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
                logger.info("Move is valid, piece moved.");

                if (gameState.isWin()) {
                    return gameState.getCurrentTurn() == Color.WHITE;
                }

                if (gameState.isDraw()) {
                    return false;
                }

                return true;
            } else {
                logger.warn("Move is invalid according to isValidMove method.");
            }
        } else {
            logger.warn("No piece at start position, piece is not a " + pieceClass.getSimpleName() + ", or it's not the piece's turn.");
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
}
