package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.MoveVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@Tag("UI-tier")
public class WebServerTest {

    private WebServer CuT;


    private Gson gson;
    private PlayerLobby playerlobby;
    private TemplateEngine engine;
    private MoveVerifier moveVerifier = new MoveVerifier();



    /**
     * Test that the webserver construction works, initialization is tested in ApplicationTest.Java
     */
    @Test
    public void full_run() {
        engine = mock(TemplateEngine.class);
        gson = new Gson();
        playerlobby = mock(PlayerLobby.class);
        CuT = new WebServer(engine, gson, playerlobby, moveVerifier);
        assertNotNull(CuT);
    }



}
