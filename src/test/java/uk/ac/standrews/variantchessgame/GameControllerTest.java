package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.standrews.variantchessgame.controller.GameController;
import uk.ac.standrews.variantchessgame.model.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VariantChessBoard board;

    private Pawn whitePawn;
    private Cannon cannon;
    private King king;
    private Knight knight;
    private Bishop bishop;
    private Queen queen;
    private Rook rook;

    @BeforeEach
    void setUp() {
        whitePawn = new Pawn(Color.WHITE);
        cannon = new Cannon(Color.WHITE);
        king = new King(Color.WHITE);
        knight = new Knight(Color.WHITE);
        bishop = new Bishop(Color.WHITE);
        queen = new Queen(Color.WHITE);
        rook = new Rook(Color.WHITE);

        // Mock board behavior for getting pieces
        when(board.getPieceAt(1, 0)).thenReturn(whitePawn);
        when(board.getPieceAt(1, 1)).thenReturn(cannon);
        when(board.getPieceAt(1, 2)).thenReturn(king);
        when(board.getPieceAt(1, 3)).thenReturn(knight);
        when(board.getPieceAt(1, 4)).thenReturn(bishop);
        when(board.getPieceAt(1, 5)).thenReturn(queen);
        when(board.getPieceAt(1, 6)).thenReturn(rook);
        when(board.getPieceAt(2, 0)).thenReturn(null);
    }

    @Test
    void testMovePawn() throws Exception {
        VariantChessMove move = new VariantChessMove(1, 0, 2, 0);
        String moveJson = "{\"startX\":1,\"startY\":0,\"endX\":2,\"endY\":0}";

        when(whitePawn.isValidMove(any(VariantChessMove.class), any(VariantChessBoard.class))).thenReturn(true);

        mockMvc.perform(post("/api/game/movePawn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(moveJson))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testMoveCannon() throws Exception {
        VariantChessMove move = new VariantChessMove(1, 1, 2, 1);
        String moveJson = "{\"startX\":1,\"startY\":1,\"endX\":2,\"endY\":1}";

        when(cannon.isValidMove(any(VariantChessMove.class), any(VariantChessBoard.class))).thenReturn(true);

        mockMvc.perform(post("/api/game/moveCannon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(moveJson))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testMoveKing() throws Exception {
        VariantChessMove move = new VariantChessMove(1, 2, 2, 2);
        String moveJson = "{\"startX\":1,\"startY\":2,\"endX\":2,\"endY\":2}";

        when(king.isValidMove(any(VariantChessMove.class), any(VariantChessBoard.class))).thenReturn(true);

        mockMvc.perform(post("/api/game/moveKing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(moveJson))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testMoveKnight() throws Exception {
        VariantChessMove move = new VariantChessMove(1, 3, 2, 3);
        String moveJson = "{\"startX\":1,\"startY\":3,\"endX\":2,\"endY\":3}";

        when(knight.isValidMove(any(VariantChessMove.class), any(VariantChessBoard.class))).thenReturn(true);

        mockMvc.perform(post("/api/game/moveKnight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(moveJson))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testMoveBishop() throws Exception {
        VariantChessMove move = new VariantChessMove(1, 4, 2, 4);
        String moveJson = "{\"startX\":1,\"startY\":4,\"endX\":2,\"endY\":4}";

        when(bishop.isValidMove(any(VariantChessMove.class), any(VariantChessBoard.class))).thenReturn(true);

        mockMvc.perform(post("/api/game/moveBishop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(moveJson))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testMoveQueen() throws Exception {
        VariantChessMove move = new VariantChessMove(1, 5, 2, 5);
        String moveJson = "{\"startX\":1,\"startY\":5,\"endX\":2,\"endY\":5}";

        when(queen.isValidMove(any(VariantChessMove.class), any(VariantChessBoard.class))).thenReturn(true);

        mockMvc.perform(post("/api/game/moveQueen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(moveJson))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testMoveRook() throws Exception {
        VariantChessMove move = new VariantChessMove(1, 6, 2, 6);
        String moveJson = "{\"startX\":1,\"startY\":6,\"endX\":2,\"endY\":6}";

        when(rook.isValidMove(any(VariantChessMove.class), any(VariantChessBoard.class))).thenReturn(true);

        mockMvc.perform(post("/api/game/moveRook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(moveJson))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
