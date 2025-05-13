package com.webcheckers.model;

/**
 * A piece represents a checker. It has a color (either red or white, according to the player which controls it),
 * and a piece type, which represents either a standard single piece or a king piece.
 *
 * @author Benjamin LaGreca
 * @author Joseph Casale
 * @author Evan Wologodzew
 */

import com.webcheckers.util.GameColor;
import com.webcheckers.util.PieceType;

public class Piece {

    private final PieceType type;

    private final GameColor color;

    /**
     * initializes a piece with a specific color and type, mainly used for testing
     * @param type, the type of the new piece
     * @param color, the color of the new piece
     */
    public Piece(PieceType type, GameColor color) {
        this.type = type;
        this.color = color;
    }

    /**
     * initializes a piece with a specific color and default type of single piece
     * @param color, the color of the new piece
     */
    public Piece(GameColor color) {
        this.type = PieceType.SINGLE;
        this.color = color;
    }
    /**
     * gets the type of this piece
     * @return type, this piece's PieceType
     */
    public PieceType getType() {
        return type;
    }

    /**
     * gets the color of this piece
     * @return color, this piece's GameColor
     */
    public GameColor getColor() {
        return color;
    }

    /**
     * gets the string representation of this piece
     * @return a string with the color and type of this piece
     */
    @Override
    public String toString() {
        return String.format("%s %s", color, type);
    }
}
