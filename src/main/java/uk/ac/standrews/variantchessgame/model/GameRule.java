package uk.ac.standrews.variantchessgame.model;

/**
 * Represents a game rule that can be applied to chess moves in the variant chess game.
 * Implementations of this interface define specific rules that affect the behavior of chess pieces
 * when certain conditions are met.
 */
public interface GameRule {

    /**
     * Applies the rule to the given move.
     * This method is called whenever a move is made to determine if any special rules need to be enforced.
     *
     * @param move The move that is being evaluated.
     * @param piece The piece that is being moved.
     * @param board The current state of the chess board.
     */
    void applyRule(VariantChessMove move, VariantChessPiece piece, VariantChessBoard board);
}
