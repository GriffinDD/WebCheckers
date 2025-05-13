package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.TemplateEngineTester;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;


import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

@Tag("UI-tier")
public class GetHomeRouteTest {

    private GetHomeRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private PlayerLobby playerlobby;
    private Game game;
    private Player player;
    private Player player2;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        player = mock(Player.class);
        player2 = mock(Player.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        playerlobby = mock(PlayerLobby.class);
        game = mock(Game.class);
        // create a unique CuT for each test
        CuT = new GetHomeRoute(engine, playerlobby);
    }

    /**
     * Test that the route functions if a new session is opened
     */
    @Test
    public void new_session() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("title", Attributes.GetHomeRouteAttributes.WELCOME_TITLE);
        testHelper.assertViewModelAttribute("challenge", Attributes.GetHomeRouteAttributes.CHALLENGE);
        testHelper.assertViewModelAttribute("playerName", "");
        testHelper.assertViewName("home.ftl");
    }

    /**
     * Test that the route functions if a session has been previously opened when going home
     */
    @Test
    public void open_session() {
        CuT.init = true;
        when(session.attribute("currentUser")).thenReturn(null);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("home.ftl");
    }

    /**
     * Test that a previously opened home route will function with no game
     */
    @Test
    public void open_and_nogamestartedsession() {
        CuT.init = true;
        when(session.attribute("currentUser")).thenReturn(player);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("home.ftl");
    }

    /**
     * Test that a previously opened home route will function with a game
     */
    @Test
    public void open_and_gamestartedsession() {
        CuT.init = true;
        when(session.attribute("currentUser")).thenReturn(player);
        when(playerlobby.getGame()).thenReturn(game);
        when(game.getWhitePlayer()).thenReturn(null);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("home.ftl");
    }

    /**
     * Test that a previously opened home route will function with a game and a different WhitePlayer
     */
    @Test
    public void open_and_gamestartedsession_WhitePlayerIsNotSelf() {
        CuT.init = true;
        when(session.attribute("currentUser")).thenReturn(player);
        when(playerlobby.getGame()).thenReturn(game);
        when(game.getWhitePlayer()).thenReturn(player2);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("home.ftl");
    }

    /**
     * Test that a previously opened home route will function with a game and a self WhitePlayer, while no theme has
     * been selected
     */
    @Test
    public void open_and_gamestartedsession_WhitePlayerIsSelf() {
        CuT.init = true;
        when(session.attribute("currentUser")).thenReturn(player);
        when(session.attribute("CurrentTheme")).thenReturn(null);
        when(session.attribute("GameTheme")).thenReturn("game.ftl");
        when(playerlobby.getGame()).thenReturn(game);
        when(game.getWhitePlayer()).thenReturn(player);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("game.ftl");
    }


    /**
     * Test that a previously opened home route will function with a selected theme
     */
    @Test
    public void open_and_gamestartedsessionandTheme() {
        CuT.init = true;
        when(session.attribute("currentUser")).thenReturn(null);
        when(session.attribute("CurrentTheme")).thenReturn("Wattson");
        when(playerlobby.getGame()).thenReturn(game);
        when(game.getWhitePlayer()).thenReturn(player);
        assertNotEquals(session.attribute("GameTheme"), "game.ftl");
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("home.ftl");
    }

    /**
     * Test that a previously opened home route will function with a game and will properly route a player to the game
     * if one is currently happening
     */
    @Test
    public void open_and_gamerefreshed() {
        CuT.init = true;
        when(session.attribute("currentUser")).thenReturn(player);
        when(playerlobby.getGame()).thenReturn(game);
        when(session.attribute("gaming")).thenReturn(true);
        when(game.getWhitePlayer()).thenReturn(null);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            System.out.println("halt causes exception");
        }
        testHelper.assertViewModelDoesNotExist();
    }


}
