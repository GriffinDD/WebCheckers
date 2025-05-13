package com.webcheckers.application;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


@Tag("Application-tier")
public class ApplicationTest {

    /**
     * Tests the application main method
     */
    @Test
    public void main_method() {
        Application.main(null);
    }

  /*  *//**
     * Tests the application main method with an exception thrown( figure out how to do later)
     *//*
    @Test
    public void main_method_withException() {
        Application.main(null);
    }*/

    /**
     * Tests the demo mode functionality of the application
     */
    @Test
    public void DemoMode(){
        Application app = new Application();
        assertNotNull(app);
        assert(!Application.isInDemoMode());
    }
}
