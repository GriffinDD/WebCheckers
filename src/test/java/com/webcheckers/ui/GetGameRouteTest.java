package com.webcheckers.ui;

import com.webcheckers.TemplateEngineTester;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.*;
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

@Tag("UI-Tier")
public class GetGameRouteTest {
    private GetGameRoute CuT;

    private Request request;
    private Session session;
    private TemplateEngine engine;
    private Response response;
    private Player player;
    private Player player2;
    private Player player3;
    private PlayerLobby playerlobby;
    private Game game;
    private BoardView board;
    private MoveVerifier moveVerifier;

    @BeforeEach
    public void setup(){
        moveVerifier = mock(MoveVerifier.class);
        request = mock(Request.class);
        session = mock(Session.class);
        player = mock(Player.class);
        player2 = mock(Player.class);
        player3 = mock(Player.class);
        board = mock(BoardView.class);
        game = mock(Game.class);
        playerlobby = mock(PlayerLobby.class);
        when(request.session()).thenReturn(session);
        engine =mock(TemplateEngine.class);
        response = mock(Response.class);
        CuT = new GetGameRoute(engine, playerlobby, moveVerifier);



        when(session.attribute("currentUser")).thenReturn(player);
        when(session.attribute("playerlobby")).thenReturn(playerlobby);
        when(playerlobby.getGame()).thenReturn(game);
        when(game.getRedPlayer()).thenReturn(player2);
        when(game.getWhitePlayer()).thenReturn(player3);
        when(game.getOtherPlayer(player)).thenReturn(player3);
        when(game.getRedBoard()).thenReturn(board);
        when(game.getWhiteBoard()).thenReturn(board);
        when(player3.getMenuState()).thenReturn(MenuState.INHELP);
        when(session.attribute("activeColor")).thenReturn(GameColor.RED);
        when(session.attribute("GameTheme")).thenReturn("Wattson.ftl");
    }

    /**
     * Test that the route functions upon on a normal game view interaction with no additional notifications
     */
    @Test
    public void full_run() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("myColor")).thenReturn(GameColor.RED);
        when(playerlobby.getGame()).thenReturn(game);
        when(game.getActiveColor()).thenReturn(GameColor.RED);
        try {
            CuT.handle(request, response);
        }catch(HaltException h){
            assert(true);
            System.out.println("Expected halt exception occured");
        }
        catch (Exception e) {
            System.out.println("runtime error occurred");
            assert(false);
        }
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("title", "Game");
        testHelper.assertViewModelAttribute("currentUser", player);
        testHelper.assertViewModelAttribute("viewMode", ViewMode.PLAY);
        testHelper.assertViewModelAttribute("redPlayer", player2);
        testHelper.assertViewModelAttribute("whitePlayer", player3);
        testHelper.assertViewModelAttribute("board", board);
        testHelper.assertViewName("Wattson.ftl");
    }

    /**
     * Test that the route functions upon when the player should win
     */
    @Test
    public void win_condition() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("myColor")).thenReturn(GameColor.RED);
        when(playerlobby.getGame()).thenReturn(null);
        try {
            CuT.handle(request, response);
        } catch (HaltException h){
            assert(true);
            System.out.println("Expected halt exception occurred.");
        }catch (Exception e) {
            System.out.println("runtime error occurred");
            assert(false);
        }
        testHelper.assertViewModelDoesNotExist();
    }

    /**
     * Test that the route functions upon when the player should lose
     */
    @Test
    public void lose_condition() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("myColor")).thenReturn(GameColor.RED);
        when(playerlobby.getGame()).thenReturn(game);
        when(board.outOfMoves()).thenReturn(true);
        try {
            CuT.handle(request, response);
        }catch(HaltException h){
            assert(true);
            System.out.println("Expected halt exception occurred");
        } catch (Exception e) {
            System.out.println("runtime error occurred");
            assert(false);
        }
        testHelper.assertViewModelDoesNotExist();
    }

    /**
     * Test that the route functions when opponent is in help menu for a red player
     */
    @Test
    public void full_run_inhelp_red() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("myColor")).thenReturn(GameColor.RED);
        when(playerlobby.getGame()).thenReturn(game);
        when(game.getWhiteState()).thenReturn(MenuState.INHELP);
        when(game.getActiveColor()).thenReturn(GameColor.RED);
        try {
            CuT.handle(request, response);
        }catch(HaltException h){
            assert(true);
            System.out.println("Expected halt exception occurred");
        }catch (Exception e) {
            System.out.println("runtime error occurred");
            assert(false);
        }
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("title", "Game");
        testHelper.assertViewModelAttribute("currentUser", player);
        testHelper.assertViewModelAttribute("viewMode", ViewMode.PLAY);
        testHelper.assertViewModelAttribute("redPlayer", player2);
        testHelper.assertViewModelAttribute("whitePlayer", player3);
        testHelper.assertViewModelAttribute("board", board);
        testHelper.assertViewName("Wattson.ftl");
    }

    /**
     * Test that the route functions when opponent is in the theme menu for a red player
     */
    @Test
    public void full_run_intheme_red() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("myColor")).thenReturn(GameColor.RED);
        when(playerlobby.getGame()).thenReturn(game);
        when(game.getWhiteState()).thenReturn(MenuState.INTHEME);
        when(game.getActiveColor()).thenReturn(GameColor.RED);
        try {
            CuT.handle(request, response);
        }catch(HaltException h){
            assert(true);
            System.out.println("Expected halt exception occurred");
        } catch (Exception e) {
            assert(false);
            System.out.println("runtime error occurred");
        }
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("title", "Game");
        testHelper.assertViewModelAttribute("currentUser", player);
        testHelper.assertViewModelAttribute("viewMode", ViewMode.PLAY);
        testHelper.assertViewModelAttribute("redPlayer", player2);
        testHelper.assertViewModelAttribute("whitePlayer", player3);
        testHelper.assertViewModelAttribute("board", board);
        testHelper.assertViewName("Wattson.ftl");
    }

    /**
     * Test that the route functions when opponent is in the theme menu for a white player
     */
    @Test
    public void full_run_intheme_white() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("myColor")).thenReturn(GameColor.WHITE);
        when(playerlobby.getGame()).thenReturn(game);
        when(game.getRedState()).thenReturn(MenuState.INTHEME);
        when(game.getActiveColor()).thenReturn(GameColor.WHITE);
        try {
            CuT.handle(request, response);
        }catch(HaltException h){
            assert(true);
            System.out.println("Expected halt exception occurred");
        } catch (Exception e) {
            assert(false);
            System.out.println("runtime error occurred");
        }
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("title", "Game");
        testHelper.assertViewModelAttribute("currentUser", player);
        testHelper.assertViewModelAttribute("viewMode", ViewMode.PLAY);
        testHelper.assertViewModelAttribute("redPlayer", player2);
        testHelper.assertViewModelAttribute("whitePlayer", player3);
        testHelper.assertViewModelAttribute("board", board);
        testHelper.assertViewName("Wattson.ftl");
    }

    /**
     * Test that the route functions when opponent is in the help menu for a white player
     */
    @Test
    public void full_run_inhelp_white() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("myColor")).thenReturn(GameColor.WHITE);
        when(playerlobby.getGame()).thenReturn(game);
        when(game.getRedState()).thenReturn(MenuState.INHELP);
        when(game.getActiveColor()).thenReturn(GameColor.WHITE);
        try {
            CuT.handle(request, response);
        }catch(HaltException h){
            assert(true);
            System.out.println("Expected halt exception occurred");
        } catch (Exception e) {
            assert(false);
            System.out.println("runtime error occurred");
        }
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("title", "Game");
        testHelper.assertViewModelAttribute("currentUser", player);
        testHelper.assertViewModelAttribute("viewMode", ViewMode.PLAY);
        testHelper.assertViewModelAttribute("redPlayer", player2);
        testHelper.assertViewModelAttribute("whitePlayer", player3);
        testHelper.assertViewModelAttribute("board", board);
        testHelper.assertViewName("Wattson.ftl");
    }

}
