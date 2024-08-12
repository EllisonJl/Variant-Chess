package uk.ac.standrews.variantchessgame.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
public class MoveHistory {

    private Stack<List<VariantChessMove>> moveStack; // Stack for storing completed moves (both player and AI)
    private Stack<List<VariantChessMove>> redoStack; // Stack for storing moves that have been undone and can be redone

    /**
     * Gets the stack of moves.
     *
     * @return The stack containing the history of moves.
     */
    public Stack<List<VariantChessMove>> getMoveStack() {
        return moveStack;
    }

    /**
     * Sets the stack of moves.
     *
     * @param moveStack The stack to set for storing the history of moves.
     */
    public void setMoveStack(Stack<List<VariantChessMove>> moveStack) {
        this.moveStack = moveStack;
    }

    /**
     * Gets the stack of undone moves.
     *
     * @return The stack containing moves that can be redone.
     */
    public Stack<List<VariantChessMove>> getRedoStack() {
        return redoStack;
    }

    /**
     * Sets the stack of undone moves.
     *
     * @param redoStack The stack to set for storing moves that can be redone.
     */
    public void setRedoStack(Stack<List<VariantChessMove>> redoStack) {
        this.redoStack = redoStack;
    }

    /**
     * Constructor for initializing the move history stacks.
     */
    public MoveHistory() {
        this.moveStack = new Stack<>(); // Initialize the stack for moves
        this.redoStack = new Stack<>(); // Initialize the stack for undone moves
    }

    /**
     * Adds a full move (both player and AI) to the history, clearing the redo stack.
     *
     * @param fullMove The list of moves to add to the history.
     */
    public void addFullMove(List<VariantChessMove> fullMove) {
        moveStack.push(fullMove); // Add the move to the stack of moves
        redoStack.clear(); // Clear the redo stack since a new move makes future redo invalid
    }

    /**
     * Undoes the last full move and stores it in the redo stack.
     *
     * @return The last undone full move, or null if no moves are available to undo.
     */
    public List<VariantChessMove> undo() {
        if (!moveStack.isEmpty()) {
            List<VariantChessMove> lastFullMove = moveStack.pop(); // Remove the last move from the stack
            redoStack.push(lastFullMove); // Add the removed move to the redo stack
            return lastFullMove; // Return the undone move
        }
        return null; // Return null if there are no moves to undo
    }

    /**
     * Redoes the last undone full move.
     *
     * @return The last redone full move, or null if no moves are available to redo.
     */
    public List<VariantChessMove> redo() {
        if (!redoStack.isEmpty()) {
            List<VariantChessMove> fullMove = redoStack.pop(); // Remove the last move from the redo stack
            moveStack.push(fullMove); // Add the removed move back to the stack of moves
            return fullMove; // Return the redone move
        }
        return null; // Return null if there are no moves to redo
    }

}
