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

    @Autowired
    public GameController(VariantChessBoard board) {
        this.board = board;
    }

    @PostMapping("/move")
    public boolean makeMove(@RequestBody VariantChessMove move) {
        logger.info("Received move request: startX={}, startY={}, endX={}, endY={}", move.getStartX(), move.getStartY(), move.getEndX(), move.getEndY());
        VariantChessPiece piece = board.getPieceAt(move.getStartX(), move.getStartY());

        if (piece != null && piece instanceof Pawn) {
            logger.info("Piece at start position is a Pawn.");
            if (piece.isValidMove(move, board)) {
                board.movePiece(move);
                logger.info("Move is valid, piece moved.");
                return true;
            } else {
                logger.warn("Move is invalid according to isValidMove method.");
            }
        } else {
            logger.warn("No piece at start position or piece is not a Pawn.");
        }

        return false;
    }

    @PostMapping("/moveCannon")
    public boolean moveCannon(@RequestBody VariantChessMove move) {
        logger.info("Received Cannon move request: startX={}, startY={}, endX={}, endY={}", move.getStartX(), move.getStartY(), move.getEndX(), move.getEndY());
        VariantChessPiece piece = board.getPieceAt(move.getStartX(), move.getStartY());

        if (piece != null && piece instanceof Cannon) {
            logger.info("Piece at start position is a Cannon.");
            if (piece.isValidMove(move, board)) {
                board.movePiece(move);
                logger.info("Move is valid, piece moved.");
                return true;
            } else {
                logger.warn("Move is invalid according to isValidMove method.");
            }
        } else {
            logger.warn("No piece at start position or piece is not a Cannon.");
        }

        return false;
    }

    @PostMapping("/moveKing")
    public boolean moveKing(@RequestBody VariantChessMove move) {
        logger.info("Received King move request: startX={}, startY={}, endX={}, endY={}", move.getStartX(), move.getStartY(), move.getEndX(), move.getEndY());
        VariantChessPiece piece = board.getPieceAt(move.getStartX(), move.getStartY());

        if (piece != null && piece instanceof King) {
            logger.info("Piece at start position is a King.");
            if (piece.isValidMove(move, board)) {
                board.movePiece(move);
                logger.info("Move is valid, piece moved.");
                return true;
            } else {
                logger.warn("Move is invalid according to isValidMove method.");
            }
        } else {
            logger.warn("No piece at start position or piece is not a King.");
        }

        return false;
    }
    @PostMapping("/moveKnight")
    public boolean moveKnight(@RequestBody VariantChessMove move) {
        logger.info("Received Knight move request: startX={}, startY={}, endX={}, endY={}", move.getStartX(), move.getStartY(), move.getEndX(), move.getEndY());
        VariantChessPiece piece = board.getPieceAt(move.getStartX(), move.getStartY());

        if (piece != null && piece instanceof Knight) {
            logger.info("Piece at start position is a Knight.");
            if (piece.isValidMove(move, board)) {
                board.movePiece(move);
                logger.info("Move is valid, piece moved.");
                return true;
            } else {
                logger.warn("Move is invalid according to isValidMove method.");
            }
        } else {
            logger.warn("No piece at start position or piece is not a Knight.");
        }

        return false;
    }
    @PostMapping("/moveBishop")
    public boolean moveBishop(@RequestBody VariantChessMove move) {
        logger.info("Received Bishop move request: startX={}, startY={}, endX={}, endY={}", move.getStartX(), move.getStartY(), move.getEndX(), move.getEndY());
        VariantChessPiece piece = board.getPieceAt(move.getStartX(), move.getStartY());

        if (piece != null && piece instanceof Bishop) {
            logger.info("Piece at start position is a Bishop.");
            if (piece.isValidMove(move, board)) {
                board.movePiece(move);
                logger.info("Move is valid, piece moved.");
                return true;
            } else {
                logger.warn("Move is invalid according to isValidMove method.");
            }
        } else {
            logger.warn("No piece at start position or piece is not a Bishop.");
        }

        return false;
    }

    @PostMapping("/moveQueen")
    public boolean moveQueen(@RequestBody VariantChessMove move) {
        logger.info("Received Queen move request: startX={}, startY={}, endX={}, endY={}", move.getStartX(), move.getStartY(), move.getEndX(), move.getEndY());
        VariantChessPiece piece = board.getPieceAt(move.getStartX(), move.getStartY());

        if (piece != null && piece instanceof Queen) {
            logger.info("Piece at start position is a Queen.");
            if (piece.isValidMove(move, board)) {
                board.movePiece(move);
                logger.info("Move is valid, piece moved.");
                return true;
            } else {
                logger.warn("Move is invalid according to isValidMove method.");
            }
        } else {
            logger.warn("No piece at start position or piece is not a Queen.");
        }

        return false;
    }

    @PostMapping("/moveRook")
    public boolean moveRook(@RequestBody VariantChessMove move) {
        logger.info("Received Rook move request: startX={}, startY={}, endX={}, endY={}", move.getStartX(), move.getStartY(), move.getEndX(), move.getEndY());
        VariantChessPiece piece = board.getPieceAt(move.getStartX(), move.getStartY());

        if (piece != null && piece instanceof Rook) {
            logger.info("Piece at start position is a Rook.");
            if (piece.isValidMove(move, board)) {
                board.movePiece(move);
                logger.info("Move is valid, piece moved.");
                return true;
            } else {
                logger.warn("Move is invalid according to isValidMove method.");
            }
        } else {
            logger.warn("No piece at start position or piece is not a Rook.");
        }

        return false;
    }

}
