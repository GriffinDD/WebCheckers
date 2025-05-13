package com.webcheckers.util;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Util-Tier")
public class MessageTest {

    private static final String message = "message tester";

    /**
     * Test that a message will be created correctly as an info message
     */
    @Test
    public void MessageInfo(){
        final Message CuT = Message.info(message);
        assertEquals(message, CuT.getText());
        assertEquals(Message.Type.INFO, CuT.getType());
        assertTrue(CuT.isSuccessful());
    }


    /**
     * Test that a message will be created correctly as an error message
     */
    @Test
    public void MessageError(){
        final Message CuT = Message.error(message);
        assertEquals(message, CuT.getText());
        assertEquals(Message.Type.ERROR, CuT.getType());
        assertFalse(CuT.isSuccessful());
    }

    /**
     * Test the equals method
     */
    @Test
    public void MessageEquals(){
        final Message CuT = Message.error(message);
        Object o = new Object();
        final Message CuT2 = Message.error("not the same");
        final Message CuT3 = Message.info(message);
        assertFalse(CuT.equals(o));
        assertFalse(CuT.equals(CuT2));
        assertFalse(CuT.equals(CuT3));
    }
}
