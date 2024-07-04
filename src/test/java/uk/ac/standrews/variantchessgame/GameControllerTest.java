package uk.ac.standrews.variantchessgame;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.standrews.variantchessgame.controller.GameController;
import uk.ac.standrews.variantchessgame.model.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VariantChessBoard board;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        board = new VariantChessBoard();
    }

    @Test
    void testMovePawn() throws Exception {
        // 将兵放在初始位置 (1, 0)
        board.getBoard()[1][0] = new Pawn(Color.WHITE);

        VariantChessMove move = new VariantChessMove(1, 0, 2, 0);
        mockMvc.perform(post("/api/game/movePawn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(move)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testMoveCannon() throws Exception {
        // 将炮放在初始位置 (1, 1)
        board.getBoard()[1][1] = new Cannon(Color.WHITE);

        VariantChessMove move = new VariantChessMove(1, 1, 3, 1);
        mockMvc.perform(post("/api/game/moveCannon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(move)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testMoveKnight() throws Exception {
        // 将骑士放在位置 (2, 2)
        board.getBoard()[2][2] = new Knight(Color.WHITE);

        VariantChessMove move = new VariantChessMove(2, 2, 4, 3);
        mockMvc.perform(post("/api/game/moveKnight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(move)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testMoveBishop() throws Exception {
        // 将主教放在位置 (2, 2)
        board.getBoard()[2][2] = new Bishop(Color.WHITE);

        VariantChessMove move = new VariantChessMove(2, 2, 4, 4);
        mockMvc.perform(post("/api/game/moveBishop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(move)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testMoveQueen() throws Exception {
        // 将皇后放在位置 (2, 2)
        board.getBoard()[2][2] = new Queen(Color.WHITE);

        VariantChessMove move = new VariantChessMove(2, 2, 4, 2);
        mockMvc.perform(post("/api/game/moveQueen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(move)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testMoveKing() throws Exception {
        // 将国王放在位置 (4, 4)
        board.getBoard()[4][4] = new King(Color.WHITE);

        VariantChessMove move = new VariantChessMove(4, 4, 5, 5);
        mockMvc.perform(post("/api/game/moveKing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(move)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testMoveRook() throws Exception {
        // 将车放在位置 (2, 0)
        board.getBoard()[2][0] = new Rook(Color.WHITE);

        VariantChessMove move = new VariantChessMove(2, 0, 2, 3);
        mockMvc.perform(post("/api/game/moveRook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(move)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
