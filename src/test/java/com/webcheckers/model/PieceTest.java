package com.webcheckers.model;

import com.webcheckers.util.GameColor;
import com.webcheckers.util.PieceType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-Tier")
public class PieceTest {

    /**
     * Test that a piece will be correctly constructed with its base constructor
     */
    @Test
    public void testBaseConstructor(){
        Piece piece = new Piece(GameColor.RED);
        assertEquals(GameColor.RED, piece.getColor());
        assertEquals(PieceType.SINGLE, piece.getType());
    }

    /**
     * Test that a piece will be correctly constructed with a piece-specified constructor
     */
    @Test public void testKingPiece(){
        Piece piece = new Piece(PieceType.KING, GameColor.WHITE);
        assertEquals(PieceType.KING, piece.getType());
        assertEquals(GameColor.WHITE, piece.getColor());
    }

}
