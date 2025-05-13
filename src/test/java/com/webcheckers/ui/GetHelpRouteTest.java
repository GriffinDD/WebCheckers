package com.webcheckers.ui;

import com.webcheckers.TemplateEngineTester;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.GameColor;
import com.webcheckers.util.MenuState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
public class GetHelpRouteTest {

    private GetHelpRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private Player player;
    private PlayerLobby playerLobby;
    private Game game;


    @BeforeEach
    public void setup() {
        game = new Game();
        playerLobby = mock(PlayerLobby.class);
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        player = mock(Player.class);
        // create a unique CuT for each test
        CuT = new GetHelpRoute(engine, playerLobby);

        when(playerLobby.getGame()).thenReturn(game);
        when(session.attribute("currentUser")).thenReturn(player);
    }

    /**
     * Test that the route functions on a red player
     */
    @Test
    public void full_run_red() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("currentUser")).thenReturn(player);
        when(session.attribute("myColor")).thenReturn(GameColor.RED);
        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        assertEquals(MenuState.INHELP, game.getRedState());
        testHelper.assertViewModelAttribute("title", "Welcome to the help page");
        testHelper.assertViewName("help.ftl");
    }

    /**
     * Test that the route functions on a white player
     */
    @Test
    public void full_run() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("currentUser")).thenReturn(player);
        when(session.attribute("myColor")).thenReturn(GameColor.WHITE);
        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        assertEquals(MenuState.INHELP, game.getWhiteState());
        testHelper.assertViewModelAttribute("title", "Welcome to the help page");
        testHelper.assertViewName("help.ftl");
    }
    /**
     * Test that the route functions on a null player
     */
    @Test
    public void full_run_null_user() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("currentUser")).thenReturn(null);
        when(session.attribute("myColor")).thenReturn(GameColor.WHITE);
        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        assertEquals(MenuState.INHELP, game.getWhiteState());
        testHelper.assertViewModelAttribute("title", "Welcome to the help page");
        testHelper.assertViewName("help.ftl");
    }

    /**
     * Test that the route functions on a null game
     */
    @Test
    public void full_run_null_game() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(playerLobby.getGame()).thenReturn(null);
        when(session.attribute("currentUser")).thenReturn(player);
        when(session.attribute("myColor")).thenReturn(GameColor.WHITE);
        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        assertEquals(MenuState.HOME, game.getWhiteState());
        testHelper.assertViewModelAttribute("title", "Welcome to the help page");
        testHelper.assertViewName("help.ftl");
    }


}
