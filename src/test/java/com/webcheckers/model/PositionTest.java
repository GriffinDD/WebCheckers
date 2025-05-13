package com.webcheckers.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Tag("Model-Tier")
public class PositionTest {
    private static final int x1 = 1;
    private static final int x2 = 2;
    private static final int x3 = 3;
    private static final int y1 = 4;
    private static final int y2 = 5;
    private static final int y3 = 6;

    /**
     * Test the position constructor
     */
    @Test
    public void constructor(){
        final Position posTest = new Position(x1,y1);
        assertEquals(x1, posTest.getRow());
        assertEquals(y1, posTest.getCell());
    }

    /**
     * Test a row can be correctly accessed
     */
    @Test
    public void getRow(){
        final Position pos = new Position(x2, y2);
        assertEquals(pos.getRow(), x2);
    }

    /**
     * Test that a cell can be correctly accessed
     */
    @Test
    public void getCell(){
        final Position pos = new Position(x3, y1);
        assertEquals(pos.getCell(), y1);
    }

    /**
     * Verify the construction of the toString representation
     */
    @Test
    public void toStringTest(){
        final Position pos = new Position(x2, y3);
        assertEquals(String.format("(Row: %d, Cell: %d)", x2, y3), pos.toString());
    }

    /**
     * test that position equivalency and creation works as expected
     */
    @Test
    public void equalsTest(){
        Integer dummyInt = 4;
        Position pos = new Position(x1, y1);
        Position pos2electricBoogaloo = new Position(x1, y1);
        Position pos3= new Position(x2, y1);
        Position pos4= new Position(x1, y2);
        assert !pos.equals(dummyInt);
        assertEquals(pos, pos2electricBoogaloo);
        assertNotEquals(pos, pos3);
        assertNotEquals(pos, pos4);

    }

}
