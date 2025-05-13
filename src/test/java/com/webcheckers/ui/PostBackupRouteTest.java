package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.MoveVerifier;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-Tier")
public class PostBackupRouteTest {
    private PostBackupRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private Gson gson;
    private MoveVerifier moveVerifier;

    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        gson = new Gson();
        moveVerifier = mock(MoveVerifier.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);

    }

    /**
     * Test that the route functions on its only use case
     */
    @Test
    public void full_run() {
        CuT = new PostBackupRoute(gson, moveVerifier);
        try {
            String result = (String)CuT.handle(request, response);
            assertEquals(gson.toJson(Message.info("Move undone")), result);
        } catch (HaltException h) {
            System.out.println("halt causes exception");
        } catch (Exception e) {
            System.out.println("runtime error occurred");
        }
    }

}
