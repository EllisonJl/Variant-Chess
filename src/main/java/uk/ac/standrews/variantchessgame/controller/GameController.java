package uk.ac.standrews.variantchessgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.ac.standrews.variantchessgame.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for handling HTTP requests related to the variant chess game.
 * Provides endpoints to interact with the game board, make moves, restart the game, and fetch valid moves.
 */
@RestController
@RequestMapping("/api/game")
public class GameController {

    private final VariantChessBoard board; // The chess board instance
    private GameState gameState; // The current state of the game

    /**
     * Constructor to initialize the GameController with the chess board.
     * Initializes the game state with the given board.
     *
     * @param board The VariantChessBoard instance to be used.
     */
    @Autowired
    public GameController(VariantChessBoard board) {
        this.board = board;
        this.gameState = new GameState(board);
    }

    /**
     * Endpoint to retrieve the initial state of the chess board.
     *
     * @return The initial configuration of the board as a 2D array of VariantChessPiece.
     */
    @GetMapping("/initialBoard")
    public VariantChessPiece[][] getInitialBoard() {
        return board.getInitialBoard();
    }

    /**
     * Endpoint to retrieve the current state of the chess board.
     *
     * @return The current configuration of the board as a 2D array of VariantChessPiece.
     */
    @GetMapping("/board")
    public VariantChessPiece[][] getBoard() {
        System.out.println("Returning current board state:");
        board.printBoard(); // Add a method in VariantChessBoard to print the board state
        return board.getBoard();
    }


    /**
     * Endpoint to retrieve the current selected rule for the game.
     *
     * @return The class name of the currently selected rule.
     */
    @GetMapping("/currentRule")
    public String getCurrentRule() {
        return gameState.getSelectedRule().getClass().getSimpleName();
    }

    /**
     * Endpoint to restart the game by reinitializing the board and game state.
     */
    @PostMapping("/restart")
    public void restartGame() {
        board.initializeBoard();
        this.gameState = new GameState(board);
        System.out.println("Game restarted and board reinitialized.");
    }

    /**
     * Processes a move request and validates it based on the piece type.
     * Updates the board and game state accordingly.
     *
     * @param move      The move to be processed.
     * @param pieceClass The class of the piece that is moving.
     * @return A string indicating the result of the move ("VALID_MOVE", "INVALID_MOVE", "WHITE_WINS", "BLACK_WINS", or "STALEMATE").
     */
    private String processMove(VariantChessMove move, Class<? extends VariantChessPiece> pieceClass) {
        System.out.println(String.format("Received move request: startX=%d, startY=%d, endX=%d, endY=%d",
                move.getStartX(), move.getStartY(), move.getEndX(), move.getEndY()));

        // Retrieve the piece at the starting position
        VariantChessPiece piece = board.getPieceAt(move.getStartX(), move.getStartY());
        if (piece == null) {
            System.out.println(String.format("No piece found at position (%d, %d)", move.getStartX(), move.getStartY()));
            return "INVALID_MOVE";
        }

        // Check if the piece is of the correct type and color
        System.out.println("Current piece is: " + piece.getClass().getSimpleName());
        System.out.println("Current piece color is: " + piece.getColor());
        System.out.println("Current turn is: " + gameState.getCurrentTurn());

        if (pieceClass.isInstance(piece) && piece.getColor() == gameState.getCurrentTurn()) {
            // Validate the move
            if (piece.isValidMove(move, board)) {
                VariantChessPiece targetPiece = board.getPieceAt(move.getEndX(), move.getEndY());
                boolean isCapture = targetPiece != null;

                // Handle piece capture if applicable
                if (isCapture) {
                    board.setPieceAt(move.getEndX(), move.getEndY(), null);
                    gameState.resetMoveWithoutCapture();
                } else {
                    gameState.incrementMoveWithoutCapture();
                }

                // Move the piece
                board.movePiece(move);
                gameState.incrementMoveCount();

                // Apply promotion or special rules
                if (piece instanceof Pawn && isCapture) {
                    gameState.getSelectedRule().applyRule(move, piece, board);
                } else if (piece instanceof Cannon && isCapture) {
                    ((Cannon) piece).incrementCaptureCount();
                    if (((Cannon) piece).getCaptureCount() >= 3) {
                        ((Cannon) piece).detonate(board, move.getEndX(), move.getEndY());
                    }
                } else if (piece instanceof King || piece instanceof Queen) {
                    if (isCapture) {
                        gameState.getSelectedRule().applyRule(move, piece, board);
                    }
                }

                // Switch turn and check game status
                gameState.switchTurn();
                System.out.println("Move is valid, piece moved.");

                if (gameState.isWin()) {
                    return gameState.getCurrentTurn() == Color.WHITE ? "BLACK_WINS" : "WHITE_WINS";
                }

                if (gameState.isDraw()) {
                    return "STALEMATE";
                }

                return "VALID_MOVE";
            } else {
                System.out.println("Move is invalid according to isValidMove method.");
                return "INVALID_MOVE";
            }
        } else {
            System.out.println("Piece at start position is not a " + pieceClass.getSimpleName() + " or it's not the piece's turn.");
            return "INVALID_MOVE";
        }
    }


    /**
     * Endpoint to move a Pawn piece.
     *
     * @param move The move request for the Pawn.
     * @return The result of the move ("VALID_MOVE", "INVALID_MOVE", "WHITE_WINS", "BLACK_WINS", or "STALEMATE").
     */
    @PostMapping("/movePawn")
    public String movePawn(@RequestBody VariantChessMove move) {
        return processMove(move, Pawn.class);
    }

    /**
     * Endpoint to move a Cannon piece.
     *
     * @param move The move request for the Cannon.
     * @return The result of the move ("VALID_MOVE", "INVALID_MOVE", "WHITE_WINS", "BLACK_WINS", or "STALEMATE").
     */
    @PostMapping("/moveCannon")
    public String moveCannon(@RequestBody VariantChessMove move) {
        return processMove(move, Cannon.class);
    }

    /**
     * Endpoint to move a King piece.
     *
     * @param move The move request for the King.
     * @return The result of the move ("VALID_MOVE", "INVALID_MOVE", "WHITE_WINS", "BLACK_WINS", or "STALEMATE").
     */
    @PostMapping("/moveKing")
    public String moveKing(@RequestBody VariantChessMove move) {
        return processMove(move, King.class);
    }

    /**
     * Endpoint to move a Knight piece.
     *
     * @param move The move request for the Knight.
     * @return The result of the move ("VALID_MOVE", "INVALID_MOVE", "WHITE_WINS", "BLACK_WINS", or "STALEMATE").
     */
    @PostMapping("/moveKnight")
    public String moveKnight(@RequestBody VariantChessMove move) {
        return processMove(move, Knight.class);
    }

    /**
     * Endpoint to move a Bishop piece.
     *
     * @param move The move request for the Bishop.
     * @return The result of the move ("VALID_MOVE", "INVALID_MOVE", "WHITE_WINS", "BLACK_WINS", or "STALEMATE").
     */
    @PostMapping("/moveBishop")
    public String moveBishop(@RequestBody VariantChessMove move) {
        return processMove(move, Bishop.class);
    }

    /**
     * Endpoint to move a Queen piece.
     *
     * @param move The move request for the Queen.
     * @return The result of the move ("VALID_MOVE", "INVALID_MOVE", "WHITE_WINS", "BLACK_WINS", or "STALEMATE").
     */
    @PostMapping("/moveQueen")
    public String moveQueen(@RequestBody VariantChessMove move) {
        return processMove(move, Queen.class);
    }

    /**
     * Endpoint to move a Rook piece.
     *
     * @param move The move request for the Rook.
     * @return The result of the move ("VALID_MOVE", "INVALID_MOVE", "WHITE_WINS", "BLACK_WINS", or "STALEMATE").
     */
    @PostMapping("/moveRook")
    public String moveRook(@RequestBody VariantChessMove move) {
        return processMove(move, Rook.class);
    }

    /**
     * Endpoint to retrieve a list of valid moves for a piece.
     *
     * @param request The request containing the piece's starting position, type, and color.
     * @return A list of valid moves for the specified piece.
     */
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

    /**
     * Inner class representing the request for retrieving valid moves.
     */
    public static class ValidMovesRequest {
        private int startX; // X coordinate of the piece's starting position
        private int startY; // Y coordinate of the piece's starting position
        private String piece; // Type of the piece (not used in current implementation but may be used in future enhancements)
        private Color color; // Color of the piece

        // Getters and setters for the ValidMovesRequest properties

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
