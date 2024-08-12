package uk.ac.standrews.variantchessgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.standrews.variantchessgame.model.MoveHistory;
import uk.ac.standrews.variantchessgame.model.VariantChessMove;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class MoveHistoryTest {

    private MoveHistory moveHistory;
    private List<VariantChessMove> move1;
    private List<VariantChessMove> move2;

    @BeforeEach
    void setUp() {
        // Initialize a new MoveHistory instance before each test.
        moveHistory = new MoveHistory();
        // Initialize lists to hold test moves.
        move1 = new ArrayList<>();
        move2 = new ArrayList<>();
        // Create dummy moves for testing purposes.
        move1.add(new VariantChessMove(0, 0, 1, 1));
        move2.add(new VariantChessMove(1, 1, 2, 2));
    }

    /**
     * Test the initial state of the MoveHistory.
     * Verifies that both moveStack and redoStack are empty when a new MoveHistory is created.
     */
    @Test
    void testInitialState() {
        assertTrue(moveHistory.getMoveStack().isEmpty(), "moveStack should be empty initially");
        assertTrue(moveHistory.getRedoStack().isEmpty(), "redoStack should be empty initially");
    }

    /**
     * Test the getter and setter methods for moveStack.
     * Verifies that moveStack can be set and retrieved correctly.
     */
    @Test
    void testGetSetMoveStack() {
        Stack<List<VariantChessMove>> newMoveStack = new Stack<>();
        newMoveStack.push(move1);
        moveHistory.setMoveStack(newMoveStack);
        assertEquals(newMoveStack, moveHistory.getMoveStack(), "getMoveStack should return the set moveStack");
    }

    /**
     * Test the getter and setter methods for redoStack.
     * Verifies that redoStack can be set and retrieved correctly.
     */
    @Test
    void testGetSetRedoStack() {
        Stack<List<VariantChessMove>> newRedoStack = new Stack<>();
        newRedoStack.push(move1);
        moveHistory.setRedoStack(newRedoStack);
        assertEquals(newRedoStack, moveHistory.getRedoStack(), "getRedoStack should return the set redoStack");
    }

    /**
     * Test adding full moves to the MoveHistory.
     * Adds moves to the moveStack and verifies that the redoStack is cleared.
     */
    @Test
    void testAddFullMove() {
        // Add the first move and check the state of stacks.
        moveHistory.addFullMove(move1);
        assertFalse(moveHistory.getMoveStack().isEmpty(), "moveStack should contain the added move");
        assertTrue(moveHistory.getRedoStack().isEmpty(), "redoStack should be empty after adding a new move");

        // Add a second move and verify the state of stacks.
        moveHistory.addFullMove(move2);
        assertEquals(2, moveHistory.getMoveStack().size(), "moveStack should contain two moves");
        assertTrue(moveHistory.getRedoStack().isEmpty(), "redoStack should be empty after adding another move");
    }

    /**
     * Test the undo functionality of the MoveHistory.
     * Verifies that the most recent move is removed from moveStack and added to redoStack.
     */
    @Test
    void testUndo() {
        // Add moves to the history.
        moveHistory.addFullMove(move1);
        moveHistory.addFullMove(move2);

        // Undo the last move and check the returned move and stack sizes.
        List<VariantChessMove> undoneMove = moveHistory.undo();
        assertEquals(move2, undoneMove, "Undo should return the last added move");
        assertEquals(1, moveHistory.getMoveStack().size(), "moveStack should have one move left");
        assertEquals(1, moveHistory.getRedoStack().size(), "redoStack should contain one move");

        // Undo the remaining move and verify the state of stacks.
        undoneMove = moveHistory.undo();
        assertEquals(move1, undoneMove, "Undo should return the first added move");
        assertTrue(moveHistory.getMoveStack().isEmpty(), "moveStack should be empty after undoing all moves");
        assertEquals(2, moveHistory.getRedoStack().size(), "redoStack should contain two moves");
    }

    /**
     * Test the redo functionality of the MoveHistory.
     * Verifies that the most recent undone move is restored to moveStack and removed from redoStack.
     */
    @Test
    void testRedo() {
        // Add moves and then undo them.
        moveHistory.addFullMove(move1);
        moveHistory.addFullMove(move2);
        moveHistory.undo(); // Undo move2
        moveHistory.undo(); // Undo move1

        // Redo the last undone move and check the returned move and stack sizes.
        List<VariantChessMove> redoneMove = moveHistory.redo();
        assertEquals(move1, redoneMove, "Redo should return the first undone move");
        assertEquals(1, moveHistory.getMoveStack().size(), "moveStack should have one move after redo");
        assertEquals(1, moveHistory.getRedoStack().size(), "redoStack should contain one move after redo");

        // Redo the remaining move and verify the state of stacks.
        redoneMove = moveHistory.redo();
        assertEquals(move2, redoneMove, "Redo should return the second undone move");
        assertEquals(2, moveHistory.getMoveStack().size(), "moveStack should contain two moves after redo");
        assertTrue(moveHistory.getRedoStack().isEmpty(), "redoStack should be empty after redoing all moves");
    }

    /**
     * Test the undo functionality when the moveStack is empty.
     * Verifies that undoing on an empty moveStack returns null.
     */
    @Test
    void testUndoEmptyStack() {
        List<VariantChessMove> undoneMove = moveHistory.undo();
        assertNull(undoneMove, "Undo on empty moveStack should return null");
    }

    /**
     * Test the redo functionality when the redoStack is empty.
     * Verifies that redoing on an empty redoStack returns null.
     */
    @Test
    void testRedoEmptyStack() {
        List<VariantChessMove> redoneMove = moveHistory.redo();
        assertNull(redoneMove, "Redo on empty redoStack should return null");
    }
}
