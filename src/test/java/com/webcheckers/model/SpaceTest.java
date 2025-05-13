package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.webcheckers.util.GameColor;
import com.webcheckers.util.PieceType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-Tier")
public class SpaceTest {
    private static final int VALID_CELL = 3;
    private static final int VALID_ROW = 0;
    private static final int INVALID_ROW_HIGH = 8;
    private static final int INVALID_ROW_LOW = -1;
    private static final int INVALID_CELL = -1;
    private static Piece piece;

    /**
     * Create a mock object for the piece
     */
    @BeforeEach
    public void setup(){
        piece = mock(Piece.class);
        when(piece.getColor()).thenReturn(GameColor.WHITE);
        when(piece.getType()).thenReturn(PieceType.SINGLE);
    }

    /**
     * Tests the constructor for the Space with a mock piece
     */
    @Test
    public void test_constructor(){
        Space CuT = new Space(VALID_CELL, VALID_ROW, piece);
        assertNotNull(CuT);
        Piece p = CuT.getPiece();
        assert(p.equals(piece));
        assertEquals(CuT.getTypeOfPiece(), PieceType.SINGLE);
        CuT.setPiece(null);
        assertNull(CuT.getTypeOfPiece());
    }

    /**
     * Tests the space that is in a valid position but with no piece.
     */
    @Test
    public void test_good_space_no_piece(){
        Space CuT = new Space(VALID_CELL, VALID_ROW);
        assert(CuT.isValid()); //has no piece
        assert(CuT.isWithinBounds());
        assert(CuT.getPiece() == null);
        assert(CuT.getCellIdx() == VALID_CELL);
        assert(CuT.getIndex() == VALID_ROW);
        assert(CuT.getIndex() == VALID_ROW);
    }

    /**
     * Tests the space that is an invalid position but with a piece.
     */
    @Test
    public void test_invalid_space_with_piece(){
        Space CuT = new Space(INVALID_CELL, VALID_ROW);
        Space CuT2 = new Space(VALID_CELL, INVALID_ROW_HIGH, piece);
        Space CuT3 = new Space(INVALID_CELL, INVALID_ROW_HIGH, piece);
        Space CuT4 = new Space(VALID_CELL, INVALID_ROW_LOW, piece);
        Space CuT5 = new Space(INVALID_CELL, INVALID_ROW_LOW, piece);
        assertFalse(CuT.isValid());
        assertFalse(CuT2.isValid());
        assertFalse(CuT3.isValid());
        assertFalse(CuT4.isValid());
        assertFalse(CuT5.isValid());
        assertFalse(CuT3.isWithinBounds());
        assertFalse(CuT.isWithinBounds());
        assertFalse(CuT2.isWithinBounds());
        assertFalse(CuT4.isWithinBounds());
        assertFalse(CuT5.isWithinBounds());


        assertEquals(CuT2.getPiece(), piece);
        System.out.println(CuT.getIndex());
        assertEquals(CuT.getIndex(), VALID_ROW);
        assertEquals(CuT.getCellIdx(), INVALID_CELL);

        assertNotEquals(CuT, CuT2);
        assertNotEquals(CuT, CuT3);
    }
    /**
     * Test setting a piece
     */
    @Test
    public void test_set_piece(){
        Space CuT = new Space(VALID_CELL, VALID_ROW);
        assert(CuT.getPiece() == null);
        assert(CuT.isValid());
        CuT.setPiece(piece);
        assertEquals(CuT.getPiece(), piece);
        assertFalse(CuT.isValid());
        assertEquals(CuT.getPiece(), piece);
    }
}
