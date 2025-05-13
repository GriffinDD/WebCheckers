package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.TemplateEngineTester;
import com.webcheckers.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
public class PostSignoutRouteTest {

    private PostSignoutRoute CuT;

    private Request request;
    private Session session;
    private PlayerLobby playerlobby;
    private TemplateEngine engine;
    private Game game;

    @BeforeEach
    public void setup() {
        game = mock(Game.class);
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        engine = mock(TemplateEngine.class);
        playerlobby = mock(PlayerLobby.class);
        // create a unique CuT for each test
        CuT = new PostSignoutRoute(playerlobby);
    }


    /**
     * Test the constructor.
     */
    @Test
    public void create_postsignout_route(){
        PostSignoutRoute p = new PostSignoutRoute(playerlobby);
        assertNotNull(p);
    }

    /**
     * Testing the normal run case from in the home screen
     */
    @Test
    public void good_player_name(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //
        Response response = mock(Response.class);

        // Invocation
        // It should halt since it redirects.
        try {
            CuT.handle(request, response);
            fail("Redirects invoke halt exceptions.");
        } catch (HaltException e) {
            // expected
        }

        //Analyze
        testHelper.assertViewModelDoesNotExist();
    }

    /**
     * Testing the normal run case from in the in a game where the opponent is still playing
     */
    @Test
    public void run_from_game(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("gaming")).thenReturn(true);
        when(playerlobby.getGame()).thenReturn(game);

        //
        Response response = mock(Response.class);

        // Invocation
        // It should halt since it redirects.
        try {
            CuT.handle(request, response);
            fail("Redirects invoke halt exceptions.");
        } catch (HaltException e) {
            // expected
        }

        //Analyze
        testHelper.assertViewModelDoesNotExist();
    }
}
