package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.ac.standrews.variantchessgame.controller.GameController;
import uk.ac.standrews.variantchessgame.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GameControllerTest {

    private MockMvc mockMvc;
    private VariantChessBoard board;
    private MoveHistory moveHistory;
    private GameController gameController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the VariantChessBoard and MoveHistory
        board = Mockito.mock(VariantChessBoard.class);
        moveHistory = Mockito.mock(MoveHistory.class);

        // Initialize the GameController with the mocked board and moveHistory
        gameController = new GameController(board);

        // Set up MockMvc with the GameController
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();

        // Add this line to debug initial board state
        System.out.println("Initial Board State:");
        board.printBoard();
    }

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
     * Test setting a game rule and restarting.
     * Verifies that the correct game rule is set and the board is reinitialized.
     */
    @Test
    void testSetGameRule() throws Exception {
        mockMvc.perform(post("/api/game/setRule/CannonSpecialRule"))
                .andExpect(status().isOk());

        verify(board, times(1)).initializeBoard();
        assertEquals("CannonSpecialRule", gameController.getCurrentRule());
    }
}
