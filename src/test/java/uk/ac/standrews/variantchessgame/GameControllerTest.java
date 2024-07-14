//package uk.ac.standrews.variantchessgame;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import uk.ac.standrews.variantchessgame.controller.GameController;
//import uk.ac.standrews.variantchessgame.model.*;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//class GameControllerTest {
//
//    @Mock
//    private VariantChessBoard board;
//
//    @Mock
//    private GameState gameState;
//
//    @InjectMocks
//    private GameController gameController;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
//    }
//
//    @Test
//    void testGetBoard() throws Exception {
//        VariantChessPiece[][] boardArray = new VariantChessPiece[8][8];
//        when(board.getBoard()).thenReturn(boardArray);
//
//        mockMvc.perform(get("/api/game/board"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json("[[null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null]]"));
//    }
//
//    @Test
//    void testMovePawn() throws Exception {
//        VariantChessMove move = new VariantChessMove(1, 0, 3, 0);
//        when(board.getPieceAt(anyInt(), anyInt())).thenReturn(new Pawn(Color.WHITE));
//        when(gameState.getCurrentTurn()).thenReturn(Color.WHITE);
//        when(board.getPieceAt(3, 0)).thenReturn(null);
//        doNothing().when(board).movePiece(any(VariantChessMove.class));
//        when(gameState.isWin()).thenReturn(false);
//        when(gameState.isDraw()).thenReturn(false);
//
//        mockMvc.perform(post("/api/game/movePawn")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"startX\":1,\"startY\":0,\"endX\":3,\"endY\":0}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("true"));
//    }
//
//    @Test
//    void testMoveCannon() throws Exception {
//        VariantChessMove move = new VariantChessMove(1, 1, 3, 1);
//        when(board.getPieceAt(anyInt(), anyInt())).thenReturn(new Cannon(Color.WHITE));
//        when(gameState.getCurrentTurn()).thenReturn(Color.WHITE);
//        when(board.getPieceAt(3, 1)).thenReturn(null);
//        doNothing().when(board).movePiece(any(VariantChessMove.class));
//        when(gameState.isWin()).thenReturn(false);
//        when(gameState.isDraw()).thenReturn(false);
//
//        mockMvc.perform(post("/api/game/moveCannon")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"startX\":1,\"startY\":1,\"endX\":3,\"endY\":1}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("true"));
//    }
//
//    @Test
//    void testMoveKing() throws Exception {
//        VariantChessMove move = new VariantChessMove(0, 4, 1, 4);
//        when(board.getPieceAt(anyInt(), anyInt())).thenReturn(new King(Color.WHITE));
//        when(gameState.getCurrentTurn()).thenReturn(Color.WHITE);
//        when(board.getPieceAt(1, 4)).thenReturn(null);
//        doNothing().when(board).movePiece(any(VariantChessMove.class));
//        when(gameState.isWin()).thenReturn(false);
//        when(gameState.isDraw()).thenReturn(false);
//
//        mockMvc.perform(post("/api/game/moveKing")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"startX\":0,\"startY\":4,\"endX\":1,\"endY\":4}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("true"));
//    }
//
//    @Test
//    void testMoveKnight() throws Exception {
//        VariantChessMove move = new VariantChessMove(0, 1, 2, 2);
//        when(board.getPieceAt(anyInt(), anyInt())).thenReturn(new Knight(Color.WHITE));
//        when(gameState.getCurrentTurn()).thenReturn(Color.WHITE);
//        when(board.getPieceAt(2, 2)).thenReturn(null);
//        doNothing().when(board).movePiece(any(VariantChessMove.class));
//        when(gameState.isWin()).thenReturn(false);
//        when(gameState.isDraw()).thenReturn(false);
//
//        mockMvc.perform(post("/api/game/moveKnight")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"startX\":0,\"startY\":1,\"endX\":2,\"endY\":2}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("true"));
//    }
//
//    @Test
//    void testMoveBishop() throws Exception {
//        VariantChessMove move = new VariantChessMove(0, 2, 2, 4);
//        when(board.getPieceAt(anyInt(), anyInt())).thenReturn(new Bishop(Color.WHITE));
//        when(gameState.getCurrentTurn()).thenReturn(Color.WHITE);
//        when(board.getPieceAt(2, 4)).thenReturn(null);
//        doNothing().when(board).movePiece(any(VariantChessMove.class));
//        when(gameState.isWin()).thenReturn(false);
//        when(gameState.isDraw()).thenReturn(false);
//
//        mockMvc.perform(post("/api/game/moveBishop")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"startX\":0,\"startY\":2,\"endX\":2,\"endY\":4}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("true"));
//    }
//
//    @Test
//    void testMoveQueen() throws Exception {
//        VariantChessMove move = new VariantChessMove(0, 3, 4, 3);
//        when(board.getPieceAt(anyInt(), anyInt())).thenReturn(new Queen(Color.WHITE));
//        when(gameState.getCurrentTurn()).thenReturn(Color.WHITE);
//        when(board.getPieceAt(4, 3)).thenReturn(null);
//        doNothing().when(board).movePiece(any(VariantChessMove.class));
//        when(gameState.isWin()).thenReturn(false);
//        when(gameState.isDraw()).thenReturn(false);
//
//        mockMvc.perform(post("/api/game/moveQueen")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"startX\":0,\"startY\":3,\"endX\":4,\"endY\":3}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("true"));
//    }
//
//    @Test
//    void testMoveRook() throws Exception {
//        VariantChessMove move = new VariantChessMove(0, 0, 0, 3);
//        when(board.getPieceAt(anyInt(), anyInt())).thenReturn(new Rook(Color.WHITE));
//        when(gameState.getCurrentTurn()).thenReturn(Color.WHITE);
//        when(board.getPieceAt(0, 3)).thenReturn(null);
//        doNothing().when(board).movePiece(any(VariantChessMove.class));
//        when(gameState.isWin()).thenReturn(false);
//        when(gameState.isDraw()).thenReturn(false);
//
//        mockMvc.perform(post("/api/game/moveRook")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"startX\":0,\"startY\":0,\"endX\":0,\"endY\":3}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("true"));
//    }
//}
