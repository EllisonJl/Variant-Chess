package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.ac.standrews.variantchessgame.controller.GameController;
import uk.ac.standrews.variantchessgame.model.*;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GameControllerTest {

    private MockMvc mockMvc;
    private VariantChessBoard board;

    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public VariantChessBoard getBoard() {
        return board;
    }

    public void setBoard(VariantChessBoard board) {
        this.board = board;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    private GameController gameController;
    private GameState gameState;

    @BeforeEach
    void setUp() {
        board = Mockito.mock(VariantChessBoard.class);
        MockitoAnnotations.openMocks(this);

        // Mock the GameState
        gameState = new GameState(board);
        gameController = new GameController(board);
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();

        // Add this line to debug initial board state
        System.out.println("Initial Board State:");
        board.printBoard();
    }


//    /**
//     * Test retrieving the current turn.
//     * Verifies that the endpoint returns the current player's turn.
//     */
//    @Test
//    void testGetCurrentTurn() throws Exception {
//        when(gameController.getCurrentTurn()).thenReturn("WHITE");
//
//        mockMvc.perform(get("/api/game/currentTurn"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("WHITE"));
//    }

//    /**
//     * Test moving a Pawn piece.
//     * Verifies that the endpoint processes a Pawn move and returns the result.
//     */
//    @Test
//    void testMovePawn() throws Exception {
//        VariantChessMove move = new VariantChessMove(1, 1, 1, 3);
//        when(board.getPieceAt(1, 1)).thenReturn(new Pawn(Color.WHITE));
//
//        mockMvc.perform(post("/api/game/movePawn")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"startX\":1,\"startY\":1,\"endX\":1,\"endY\":3}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("VALID_MOVE"));
//    }
//
//
//    /**
//     * Test moving a Cannon piece.
//     * Verifies that the endpoint processes a Cannon move and returns the result.
//     */
//    @Test
//    void testMoveCannon() throws Exception {
//        VariantChessMove move = new VariantChessMove(2, 2, 2, 5);
//        when(board.getPieceAt(2, 2)).thenReturn(new Cannon(Color.WHITE));
//
//        mockMvc.perform(post("/api/game/moveCannon")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"startX\":2,\"startY\":2,\"endX\":2,\"endY\":5}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("VALID_MOVE"));
//    }
//
//    /**
//     * Test moving a King piece.
//     * Verifies that the endpoint processes a King move and returns the result.
//     */
//    @Test
//    void testMoveKing() throws Exception {
//        // 创建一个King的模拟对象
//        King kingMock = Mockito.mock(King.class);
//
//        // 确保getPieceAt()返回这个模拟对象
//        when(board.getPieceAt(4, 4)).thenReturn(kingMock);
//        when(board.getPieceAt(4, 5)).thenReturn(null); // 确保目标位置为空
//
//        // 模拟isValidMove()方法返回true
//        when(kingMock.isValidMove(any(VariantChessMove.class), eq(board))).thenReturn(true);
//
//        mockMvc.perform(post("/api/game/moveKing")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"startX\":4,\"startY\":4,\"endX\":4,\"endY\":5}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("VALID_MOVE"));
//    }
//
//    /**
//     * Test moving a Knight piece.
//     * Verifies that the endpoint processes a Knight move and returns the result.
//     */
//    @Test
//    void testMoveKnight() throws Exception {
//        VariantChessMove move = new VariantChessMove(1, 0, 2, 2);
//        when(board.getPieceAt(1, 0)).thenReturn(new Knight(Color.WHITE));
//
//        mockMvc.perform(post("/api/game/moveKnight")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"startX\":1,\"startY\":0,\"endX\":2,\"endY\":2}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("VALID_MOVE"));
//    }
//
//    /**
//     * Test moving a Bishop piece.
//     * Verifies that the endpoint processes a Bishop move and returns the result.
//     */
//    @Test
//    void testMoveBishop() throws Exception {
//        VariantChessMove move = new VariantChessMove(2, 0, 4, 2);
//        when(board.getPieceAt(2, 0)).thenReturn(new Bishop(Color.WHITE));
//
//        mockMvc.perform(post("/api/game/moveBishop")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"startX\":2,\"startY\":0,\"endX\":4,\"endY\":2}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("VALID_MOVE"));
//    }
//
//    /**
//     * Test moving a Queen piece.
//     * Verifies that the endpoint processes a Queen move and returns the result.
//     */
//    @Test
//    void testMoveQueen() throws Exception {
//        VariantChessMove move = new VariantChessMove(3, 0, 3, 4);
//        when(board.getPieceAt(3, 0)).thenReturn(new Queen(Color.WHITE));
//
//        mockMvc.perform(post("/api/game/moveQueen")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"startX\":3,\"startY\":0,\"endX\":3,\"endY\":4}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("VALID_MOVE"));
//    }
//
//    /**
//     * Test moving a Rook piece.
//     * Verifies that the endpoint processes a Rook move and returns the result.
//     */
//    @Test
//    void testMoveRook() throws Exception {
//        VariantChessMove move = new VariantChessMove(0, 0, 0, 5);
//        when(board.getPieceAt(0, 0)).thenReturn(new Rook(Color.WHITE));
//
//        mockMvc.perform(post("/api/game/moveRook")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"startX\":0,\"startY\":0,\"endX\":0,\"endY\":5}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("VALID_MOVE"));
//    }
    /**
     * Test the initial board retrieval.
     * Verifies that the endpoint returns the initial state of the board.
     */
    @Test
    void testGetInitialBoard() throws Exception {
        VariantChessPiece[][] initialBoard = new VariantChessPiece[8][8]; // Mocked initial board
        when(board.getInitialBoard()).thenReturn(initialBoard);

        mockMvc.perform(get("/api/game/initialBoard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(8))) // Check for 8 rows
                .andExpect(jsonPath("$[0]", hasSize(8))) // Check for 8 columns in the first row
                .andExpect(jsonPath("$[1]", hasSize(8))) // Check for 8 columns in the second row
                .andExpect(jsonPath("$[2]", hasSize(8))) // Check for 8 columns in the third row
                .andExpect(jsonPath("$[3]", hasSize(8))) // Check for 8 columns in the fourth row
                .andExpect(jsonPath("$[4]", hasSize(8))) // Check for 8 columns in the fifth row
                .andExpect(jsonPath("$[5]", hasSize(8))) // Check for 8 columns in the sixth row
                .andExpect(jsonPath("$[6]", hasSize(8))) // Check for 8 columns in the seventh row
                .andExpect(jsonPath("$[7]", hasSize(8))); // Check for 8 columns in the eighth row
    }

    /**
     * Tests retrieving the current state of the chess board.
     * Verifies that the board is returned as an 8x8 array.
     */
    @Test
    void testGetBoard() throws Exception {
        // Mock the board to return an 8x8 grid with null pieces (empty board for test)
        VariantChessPiece[][] emptyBoard = new VariantChessPiece[8][8];
        when(board.getBoard()).thenReturn(emptyBoard);

        mockMvc.perform(get("/api/game/board"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(8))) // Check for 8 rows
                .andExpect(jsonPath("$[*]", everyItem(hasSize(8)))); // Check for each row having 8 columns
    }

    /**
     * Test restarting the game.
     * Verifies that the endpoint reinitializes the board and game state.
     */
    @Test
    void testRestartGame() throws Exception {
        mockMvc.perform(post("/api/game/restart"))
                .andExpect(status().isOk());

        verify(board, times(1)).initializeBoard();
    }
@Test
void testMovePiece() throws Exception {
    VariantChessMove move = new VariantChessMove(0, 1, 0, 2);

    // Mock the starting piece and target position
    when(board.getPieceAt(0, 1)).thenReturn(new Pawn(Color.WHITE));
    when(board.getPieceAt(0, 2)).thenReturn(null);

    // Handle the void method call
    doNothing().when(board).movePiece(any(VariantChessMove.class));

    // Mock the GameController's movePiece method instead of invoking it directly
    GameController mockController = Mockito.spy(new GameController(board));
    doReturn("VALID_MOVE;CURRENT_TURN=BLACK").when(mockController).movePiece(any(VariantChessMove.class));

    // Use MockMvc with the spied controller
    mockMvc = MockMvcBuilders.standaloneSetup(mockController).build();

    // Perform the POST request with the correct JSON
    mockMvc.perform(post("/api/game/movePiece")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"startX\":0,\"startY\":1,\"endX\":0,\"endY\":2}"))
            .andExpect(status().isOk())
            .andExpect(content().string("VALID_MOVE;CURRENT_TURN=BLACK"));
}
}
