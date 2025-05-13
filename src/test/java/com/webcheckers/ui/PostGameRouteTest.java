package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.TemplateEngineTester;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.GameColor;
import com.webcheckers.util.MenuState;
import com.webcheckers.util.ViewMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
public class PostGameRouteTest {

    private PostGameRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private PlayerLobby playerlobby;
    private Game game;
    private Player player;
    private Player player2;
    private Player player3;
    private String name1 = "bob";
    private String name2 = "john";

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        player = mock(Player.class);
        player2 = mock(Player.class);
        player3 = mock(Player.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        playerlobby = mock(PlayerLobby.class);
        game = mock(Game.class);
        // create a unique CuT for each test
        CuT = new PostGameRoute(engine, playerlobby);

        when(playerlobby.getGame()).thenReturn(null, game);
        when(session.attribute("currentUser")).thenReturn(player);
        when(request.queryParams("OpposingPlayerName")).thenReturn(name1);
        when(playerlobby.getPlayer(name1)).thenReturn(new Player(name1));
        when((player.getName())).thenReturn(name2);
    }

    /**
     * Test that the route functions on normal call with successful context
     */
    @Test
    public void successful_call() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(playerlobby.hasPlayer(name1)).thenReturn(true);
        when(session.attribute("GameTheme")).thenReturn("game.ftl");
        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            System.out.println("runtime error occurred");
        }
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("title", "Game");
        testHelper.assertViewModelAttribute("viewMode", ViewMode.PLAY);
        testHelper.assertViewModelAttribute("activeColor", GameColor.RED);
        testHelper.assertViewName("game.ftl");
    }

    /**
     * Test that the route functions if called with an opponent already in a game
     */
    @Test
    public void enemy_in_game() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(playerlobby.hasPlayer(name1)).thenReturn(true);
        when(playerlobby.inGame(new Player(name1))).thenReturn(true);
        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            System.out.println("halt causes exception");
        }
        testHelper.assertViewModelDoesNotExist();
    }


    /**
     * Test that the route functions if called with an opponent who does not exist
     */
    @Test
    public void enemy_not_exists() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(playerlobby.hasPlayer(name1)).thenReturn(false);
        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            System.out.println("halt causes exception");
        }
        testHelper.assertViewModelDoesNotExist();
    }


    /**
     * Test that the route functions if called with an opponent who is in a menu
     */
    @Test
    public void enemy_in_menu() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(playerlobby.hasPlayer(name1)).thenReturn(true);
        when(playerlobby.getPlayer(name1)).thenReturn(player3);
        when(player3.getMenuState()).thenReturn(MenuState.INHELP);
        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            System.out.println("halt causes exception");
        }
        testHelper.assertViewModelDoesNotExist();
    }

    /**
     * Test that the route functions if called with an opponent who is in a menu
     */
    @Test
    public void players_have_same_name() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(playerlobby.hasPlayer(name1)).thenReturn(true);
        when(playerlobby.getPlayer(name1)).thenReturn(player3);
        when(request.queryParams("OpposingPlayerName")).thenReturn(name2);

        when(player3.getMenuState()).thenReturn(MenuState.INHELP);
        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            System.out.println("halt causes exception");
        }
        testHelper.assertViewModelDoesNotExist();
    }

    /**
     * Test that the route functions if called with an opponent who is in a menu
     */
    @Test
    public void players_in_theme() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(playerlobby.hasPlayer(name1)).thenReturn(true);
        when(playerlobby.getPlayer(name1)).thenReturn(player3);
        when(request.queryParams("OpposingPlayerName")).thenReturn(name2);

        when(player3.getMenuState()).thenReturn(MenuState.INTHEME);
        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            System.out.println("halt causes exception");
        }
        testHelper.assertViewModelDoesNotExist();
    }
    /**
     * Test that the route functions if called by a null player
     */
    @Test
    public void test_for_current_player_null(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(playerlobby.hasPlayer(name1)).thenReturn(true);
        when(session.attribute("currentUser")).thenReturn(null);
        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            System.out.println("halt causes exception");
        }
        testHelper.assertViewModelDoesNotExist();
    }

    /**
     * Not sure but I'm trying to get more branch coverage on tests.
     */
    @Test
    public void test_for_not_null_game_branch(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(session.attribute("currentUser")).thenReturn(player);
        when(session.attribute("OpposingPlayerName")).thenReturn("test");
        when(request.queryParams("test")).thenReturn("opp");
        when(playerlobby.getPlayer("opp")).thenReturn(player2);
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(playerlobby.hasPlayer(name1)).thenReturn(true);
        when(playerlobby.inGame(player2)).thenReturn(false);
        when(player2.getMenuState()).thenReturn(MenuState.HOME);

        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            assert(false);
            System.out.println("halt causes exception");
        }
        testHelper.assertViewModelExists();

    }
}
