package com.webcheckers.model;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("Model-Tier")
public class RowTest {

    private static int index = 5;

    /**
     * Test that a row will be constructed properly and that initialization functions work
     */
    @Test
    public void constructor(){
        Row CuT = new Row(index);
        assertEquals(index,  CuT.getIndex());
    }

    /**
     * Test that the iterator function works, though the outcome cannot be tested due to the individual nature of the streams
     */
    @Test
    public void iteratortester(){
        Row CuT = new Row(index);
        CuT.iterator();

    }
}
