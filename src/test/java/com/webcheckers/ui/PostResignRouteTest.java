package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.GameColor;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
public class PostResignRouteTest {

    private PostResignRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private PlayerLobby playerlobby;
    private Game game;
    private Gson gson;
    private Player player;
    private Player player2;
    private BoardView board;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        board = mock(BoardView.class);
        gson = new Gson();
        player = mock(Player.class);
        player2 = mock(Player.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        playerlobby = mock(PlayerLobby.class);
        game = mock(Game.class);
        // create a unique CuT for each test
        CuT = new PostResignRoute(gson, playerlobby);
        when(playerlobby.getGame()).thenReturn(game);
        when(session.attribute("currentUser")).thenReturn(player);
        when(game.getOtherPlayer(player)).thenReturn(player2);
        when(game.getRedBoard()).thenReturn(board);
        when(game.getWhiteBoard()).thenReturn(board);

    }

    /**
     * Test that the route functions on normal call with successful context on a white game
     */
    @Test
    public void successful_call_white() {
        when(session.attribute("activeColor")).thenReturn(GameColor.WHITE);
        try {
            CuT.handle(request, response);
        } catch (HaltException h) {
            System.out.println("halt causes exception");
        } catch (Exception e) {
            assert(false);
            System.out.println("runtime error occurred");
        }
    }

    /**
     * Test that the output functions on normal call with successful context on a white game
     */
    @Test
    public void successful_output_call_white() {
        when(session.attribute("activeColor")).thenReturn(GameColor.WHITE);
        String message = null;
        try {
            message = (String)CuT.handle(request, response);
            assert true;
        } catch (HaltException h) {
            System.out.println("halt causes exception");
        } catch (Exception e) {
            assert(false);
            System.out.println("runtime error occurred");
        }
        assert(message.equals(gson.toJson(Message.info("success"))));
    }

    /**
     * Test that the route functions on normal call with successful context on a red game
     */
    @Test
    public void successful_call_red() {
        when(session.attribute("activeColor")).thenReturn(GameColor.RED);
        try {
            CuT.handle(request, response);
        } catch (HaltException h) {
            System.out.println("halt causes exception");
        } catch (Exception e) {
            assert(false);
            System.out.println("runtime error occurred");
        }
    }

    /**
     * Test that the output functions on normal call with successful context on a red game
     */
    @Test
    public void successful_output_call_red() {
        when(session.attribute("activeColor")).thenReturn(GameColor.RED);
        String message = null;
        try {
            message = (String)CuT.handle(request, response);
            assert true;
        } catch (HaltException h) {
            System.out.println("halt causes exception");
        } catch (Exception e) {
            assert(false);
            System.out.println("runtime error occurred");
        }
        assert(message.equals(gson.toJson(Message.info("success"))));
    }

        /**
     * Test that the output functions on normal call with successful context on a red game
     */
    @Test
    public void call_when_game_null() {
        when(playerlobby.getGame()).thenReturn(null);
        String message = null;
        try {
            message = (String)CuT.handle(request, response);
            assert true;
        } catch (HaltException h) {
            System.out.println("halt causes exception");
        } catch (Exception e) {
            assert(false);
            System.out.println("runtime error occurred");
        }
        assert(message.equals(gson.toJson(Message.info("success"))));
    }



}
