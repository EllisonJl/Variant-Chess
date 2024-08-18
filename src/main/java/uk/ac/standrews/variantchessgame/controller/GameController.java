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
    private final MoveHistory moveHistory; // MoveHistory instance

    private final ChessAI chessAI;

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
        this.chessAI = new ChessAI();
        this.moveHistory = new MoveHistory();
    }

    /**
     * Endpoint to set a specific game rule and restart the game.
     *
     * @param rule The name of the rule to set ("CannonSpecialRule", "KingQueenSpecialRule", "PawnPromotionRule", "RandomRule").
     */
    @PostMapping("/setRule/{rule}")
    public void setGameRule(@PathVariable String rule) {
        int ruleIndex;
        switch (rule) {
            case "CannonSpecialRule":
                ruleIndex = 0;
                break;
            case "KingQueenSpecialRule":
                ruleIndex = 1;
                break;
            case "PawnPromotionRule":
                ruleIndex = 2;
                break;
            default:
                throw new IllegalArgumentException("Invalid rule: " + rule);
        }
        board.initializeBoard(); // Restart the game with the new rule
        gameState.selectRuleByIndex(ruleIndex);
        System.out.println("Game rule set and board reinitialized."); // Debugging line
    }

    /**
     * Handles the undo operation for the last move in the game.
     * This method is mapped to the "/undo" POST request and will attempt to undo the last move(s) made.
     * If the undo operation is successful, it returns "UNDO_SUCCESS". Otherwise, it returns "UNDO_FAIL".
     *
     * @return A string indicating the success or failure of the undo operation.
     */
    @PostMapping("/undo")
    public String undoLastMove() {
        List<VariantChessMove> lastFullMove = moveHistory.undo();
        if (lastFullMove != null) {
            // Undo moves in reverse order to properly restore the game state
            for (int i = lastFullMove.size() - 1; i >= 0; i--) {
                VariantChessMove move = lastFullMove.get(i);
                // Undo the move by moving the piece back to its original position
                board.movePiece(new VariantChessMove(move.getEndX(), move.getEndY(), move.getStartX(), move.getStartY()));
                if (move.isCapture() && move.getCapturedPiece() != null) {
                    // Restore the captured piece to its original position
                    board.setPieceAt(move.getEndX(), move.getEndY(), move.getCapturedPiece());
                }

                // Restore the first move status for pawns
                VariantChessPiece piece = board.getPieceAt(move.getStartX(), move.getStartY());
                if (piece instanceof Pawn) {
                    ((Pawn) piece).setFirstMove(move.wasFirstMove());
                }

                gameState.switchTurn(); // Switch back the turn to the previous player
            }
            return "UNDO_SUCCESS"; // Return success message
        }
        return "UNDO_FAIL"; // Return failure message if undo is not possible
    }

    /**
     * Handles the redo operation for the last undone move in the game.
     * This method is mapped to the "/redo" POST request and will attempt to redo the last move(s) that were undone.
     * If the redo operation is successful, it returns "REDO_SUCCESS". Otherwise, it returns "REDO_FAIL".
     *
     * @return A string indicating the success or failure of the redo operation.
     */
    @PostMapping("/redo")
    public String redoLastMove() {
        List<VariantChessMove> nextFullMove = moveHistory.redo();
        if (nextFullMove != null) {
            for (VariantChessMove move : nextFullMove) {
                board.movePiece(move); // Redo the move by moving the piece to its previous position
                if (move.isCapture() && move.getCapturedPiece() != null) {
                    // Re-capture the piece by removing it from the board
                    board.setPieceAt(move.getEndX(), move.getEndY(), null);
                }
                gameState.switchTurn(); // Switch the turn to the next player
            }
            return "REDO_SUCCESS"; // Return success message
        }
        return "REDO_FAIL"; // Return failure message if redo is not possible
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
        VariantChessPiece piece = board.getPieceAt(move.getStartX(), move.getStartY());
        if (piece == null || !pieceClass.isInstance(piece)) {
            System.out.println("Invalid move: No piece at the start position or piece type mismatch.");
            return "INVALID_MOVE";
        }

        // Save the first move status for pawns
        if (piece instanceof Pawn) {
            move.setWasFirstMove(((Pawn) piece).isFirstMove());
        }

        if (piece.isValidMove(move, board)) {
            VariantChessPiece targetPiece = board.getPieceAt(move.getEndX(), move.getEndY());
            boolean isCapture = targetPiece != null;

            if (isCapture) {
                System.out.println("Capture occurred.");
                move.setCapture(true); // Mark the move as a capture
                move.setCapturedPiece(targetPiece); // Track the captured piece
                board.setPieceAt(move.getEndX(), move.getEndY(), null);
                gameState.resetMoveWithoutCapture();
            } else {
                gameState.incrementMoveWithoutCapture();
            }

            board.movePiece(move);
            gameState.incrementMoveCount();

            // Check the current rule and apply the Cannon explosion logic only for CannonSpecialRule
            if (piece instanceof Cannon && isCapture) {
                if (gameState.getSelectedRule() instanceof CannonSpecialRule) {
                    ((Cannon) piece).incrementCaptureCount();
                    if (((Cannon) piece).getCaptureCount() >= 3) {
                        ((Cannon) piece).detonate(board, move.getEndX(), move.getEndY());
                    }
                }
            }

            // Apply other special rules
            if ((piece instanceof Pawn || piece.isPromotedFromPawn()) && isCapture) {
                gameState.getSelectedRule().applyRule(move, piece, board);
                VariantChessPiece newPiece = board.getPieceAt(move.getEndX(), move.getEndY());
                System.out.println("New piece type after promotion: " + newPiece.getClass().getSimpleName());
            } else if (piece instanceof King || piece instanceof Queen) {
                if (isCapture) {
                    gameState.getSelectedRule().applyRule(move, piece, board);
                }
            }

            // Update the first move status for the pawn
            if (piece instanceof Pawn) {
                ((Pawn) piece).setFirstMove(false); // Mark the pawn's first move as done
            }

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
            System.out.println("Invalid move according to isValidMove method.");
            return "INVALID_MOVE";
        }
    }

    /**
     * Endpoint to move any piece, processing the move and returning the result.
     * Handles AI move for black pieces automatically.
     *
     * @param move The move request for the piece.
     * @return The result of the move ("VALID_MOVE", "INVALID_MOVE", "WHITE_WINS", "BLACK_WINS", or "STALEMATE").
     */
    @PostMapping("/movePiece")
    public String movePiece(@RequestBody VariantChessMove move) {
        String moveResult = processMove(move, board.getPieceAt(move.getStartX(), move.getStartY()).getClass());

        List<VariantChessMove> fullMove = new ArrayList<>(); // Track both player and AI moves

        if ("VALID_MOVE".equals(moveResult)) {
            fullMove.add(move); // Add player move to the full move list

            if (gameState.getCurrentTurn() == Color.BLACK) {
                System.out.println("AI's turn.");
                GameRule currentRule = gameState.getSelectedRule(); // Get the current rule
                VariantChessMove aiMove = chessAI.calculateBestMove(board, Color.BLACK, currentRule); // Pass rule to AI
                if (aiMove != null) {
                    System.out.println("AI moves from (" + aiMove.getStartX() + ", " + aiMove.getStartY() + ") to (" + aiMove.getEndX() + ", " + aiMove.getEndY() + ")");
                    String aiMoveResult = processMove(aiMove, board.getPieceAt(aiMove.getStartX(), aiMove.getStartY()).getClass());

                    if ("VALID_MOVE".equals(aiMoveResult)) {
                        System.out.println("AI move complete, switching back to white.");
                        fullMove.add(aiMove); // Add AI move to the full move list
                        moveResult = aiMoveResult; // Update move result to AI's move result
                    }
                } else {
                    System.out.println("AI has no valid moves.");
                }
            }

            moveHistory.addFullMove(fullMove); // Record the full move in history
        }

        return moveResult + ";CURRENT_TURN=" + gameState.getCurrentTurn().toString();
    }

    @GetMapping("/currentTurn")
    public String getCurrentTurn() {
        return gameState.getCurrentTurn().toString();
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
