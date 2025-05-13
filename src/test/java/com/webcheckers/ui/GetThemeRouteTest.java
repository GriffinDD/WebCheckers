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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("UI-Tier")
public class GetThemeRouteTest {
    private GetThemeRoute CuT;

    private Request request;
    private Session session;
    private TemplateEngine engine;
    private Response response;
    private PlayerLobby playerLobby;
    private Player player;
    private Game game;

    @BeforeEach
    public void setup(){
        player = mock(Player.class);
        playerLobby = mock(PlayerLobby.class);
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        engine = mock(TemplateEngine.class);
        response = mock(Response.class);
        game = new Game();
        CuT = new GetThemeRoute(engine, playerLobby);
        when(session.attribute("currentUser")).thenReturn(player);
        when(playerLobby.getGame()).thenReturn(game);
    }

    /**
     * Test that the route functions in normal use case for red
     */
    @Test
    public void full_run_red() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("myColor")).thenReturn(GameColor.RED);
        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        assertEquals(MenuState.INTHEME, game.getRedState());
        testHelper.assertViewModelAttribute("title", "Welcome to the theme selection page");
        testHelper.assertViewModelAttribute("message", "Please choose a theme for your game");
        testHelper.assertViewName("theme.ftl");
    }

    /**
     * Test that the route functions in normal use case for white
     */
    @Test
    public void full_run_white() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("myColor")).thenReturn(GameColor.WHITE);
        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        assertEquals(MenuState.INTHEME, game.getWhiteState());
        testHelper.assertViewModelAttribute("title", "Welcome to the theme selection page");
        testHelper.assertViewModelAttribute("message", "Please choose a theme for your game");
        testHelper.assertViewName("theme.ftl");
    }

}
