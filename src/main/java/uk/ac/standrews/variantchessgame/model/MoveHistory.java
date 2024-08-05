package uk.ac.standrews.variantchessgame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MoveHistory {

    private Stack<List<VariantChessMove>> moveStack; // Stack for full moves
    private Stack<List<VariantChessMove>> redoStack; // Stack for undone full moves

    public MoveHistory() {
        this.moveStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    // Adds a full move (both player and AI) to the history, clearing the redo stack
    public void addFullMove(List<VariantChessMove> fullMove) {
        moveStack.push(fullMove);
        redoStack.clear(); // Clear redo stack as new move invalidates future redo
    }

    // Undoes the last full move and stores it in redo stack
    public List<VariantChessMove> undo() {
        if (!moveStack.isEmpty()) {
            List<VariantChessMove> lastFullMove = moveStack.pop();
            redoStack.push(lastFullMove);
            return lastFullMove;
        }
        return null;
    }

    // Redoes the last undone full move
    public List<VariantChessMove> redo() {
        if (!redoStack.isEmpty()) {
            List<VariantChessMove> fullMove = redoStack.pop();
            moveStack.push(fullMove);
            return fullMove;
        }
        return null;
    }

    // Checks if undo operation is possible
    public boolean canUndo() {
        return !moveStack.isEmpty();
    }

    // Checks if redo operation is possible
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}
