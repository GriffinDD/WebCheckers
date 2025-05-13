package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-Tier")
public class MoveTest {

    private static final Position GOOD_START = new Position(0, 0);
    private static final Position GOOD_END = new Position(1, 1);
    private static final Position JUMP_END = new Position(2, 2);

    /**
     * Tests the constructor for a non-jump move
     */
    @Test
    public void test_constructor_non_jump(){
        Move CuT = new Move(GOOD_START, GOOD_END);
        assertNotNull(CuT);
        assert(CuT.getStart().equals(GOOD_START));
        assert(CuT.getEnd().equals(GOOD_END));
        assertNotNull(CuT.toString());
        assertFalse(CuT.isJump());
    }

    /**
     * Test that a move that would be a jump is known to be a jump
     */
    @Test
    public void test_jump_move(){
        Move CuT = new Move(GOOD_START, JUMP_END);
        assertNotNull(CuT);
        assertEquals(CuT.getStart(), GOOD_START);
        assertEquals(CuT.getEnd(), JUMP_END);
        assert(CuT.isJump());
        assertNotNull(CuT.toString());
    }

    /**
     * Testing the equals method and hashcode generation
     */
    @Test
    public void test_equals(){
        Move CuT1 = new Move(GOOD_START, GOOD_END);
        Move CuT2 = new Move(GOOD_START, GOOD_END);
        Move CuT11 = new Move(GOOD_START, JUMP_END);
        Move CuT12 = new Move(GOOD_START, JUMP_END);
        Move CuTbreaker = new Move( new Position(-1, -1), GOOD_END);
        assertEquals(true, CuT1.equals(CuT2));
        assertEquals(false, CuT1.equals(CuTbreaker));
        assertEquals(false, CuT1.equals(CuT11));
        assertEquals(true, CuT11.equals(CuT12));
        assertEquals(false, CuT11.equals(null));
        assertNotEquals(CuT1.hashCode(), CuT11.hashCode());
        assertEquals(CuT1.hashCode(), CuT2.hashCode());

    }

}
