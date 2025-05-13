package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.*;
import com.webcheckers.util.GameColor;
import com.webcheckers.util.MenuState;
import com.webcheckers.util.Message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.HaltException;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.ArrayList;
@Tag("UI-tier")
public class PostCheckTurnRouteTest {

    private PostCheckTurnRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private Gson gson;
    private BoardView board;
    private MoveVerifier moveVerifier;
    private List<Move> moves;
    private PlayerLobby playerLobby;
    private Game game;

    @BeforeEach
    public void setup() {
        game = mock(Game.class);
        playerLobby = mock(PlayerLobby.class);
        request = mock(Request.class);
        session = mock(Session.class);
        gson = new Gson();
        when(request.session()).thenReturn(session);
        moveVerifier = mock(MoveVerifier.class);
        response = mock(Response.class);
        board = mock(BoardView.class);
        moves = new ArrayList<>();
        // create a unique CuT for each test
        CuT = new PostCheckTurnRoute(gson, moveVerifier, playerLobby);
        when(session.attribute("board")).thenReturn(board);
        when(playerLobby.getGame()).thenReturn(game);
    }

    /**
     * Test that the route functions when the white player is in the help menu.
     */
    @Test
    public void white_player_in_help_menu_reds_turn(){
        when(game.getWhiteState()).thenReturn(MenuState.INHELP);
        when(session.attribute("myColor")).thenReturn(GameColor.RED);
        try{
            String result = (String)CuT.handle(request, response);
            assertEquals(gson.toJson(Message.info("true")), result);
        }catch(HaltException h){
            System.out.println("unexpected halt occurred");
            assert(false);
        }catch(Exception e){
            System.out.println("runtime error occurred");
            assert(false);
        }
    }

    /**
     * Test that the route functions when the red player is in the help menu.
     */
    @Test
    public void red_player_in_help_menu_whites_turn(){
        when(game.getRedState()).thenReturn(MenuState.INHELP);
        when(session.attribute("myColor")).thenReturn(GameColor.WHITE);
        try{
            String result = (String)CuT.handle(request, response);
            assertEquals(gson.toJson(Message.info("true")), result);
        }catch(HaltException h){
            System.out.println("unexpected halt occurred");
            assert(false);
        }catch(Exception e){
            assert(false);
            System.out.println("runtime error occurred");
        }
    }

        /**
     * Test that the route functions when the red player is in the transition state
     */
    @Test
    public void red_player_in_transition_whites_turn(){
        when(game.getRedState()).thenReturn(MenuState.TRANSITION);
        when(session.attribute("myColor")).thenReturn(GameColor.WHITE);
        try{
            String result = (String)CuT.handle(request, response);
            assertEquals(gson.toJson(Message.info("true")), result);
        }catch(HaltException h){
            System.out.println("unexpected halt occurred");
            assert(false);
        }catch(Exception e){
            assert(false);
            System.out.println("runtime error occurred");
        }
    }

    /**
     * Test that the route functions when the white player is in the transition state
     */
    @Test
    public void white_player_in_transition_reds_turn(){
        when(game.getWhiteState()).thenReturn(MenuState.TRANSITION);
        when(session.attribute("myColor")).thenReturn(GameColor.RED);
        try{
            String result = (String)CuT.handle(request, response);
            assertEquals(gson.toJson(Message.info("true")), result);
        }catch(HaltException h){
            System.out.println("unexpected halt occurred");
            assert(false);
        }catch(Exception e){
            assert(false);
            System.out.println("runtime error occurred");
        }
    }

    /**
     * Test that the route functions on normal call with a null move
     */
    @Test
    public void nullmove_call() {
        try {
            CuT.handle(request, response);
        } catch (HaltException h) {
            assert(false);
            System.out.println("halt causes exception");
        } catch (Exception e) {
            assert(true);
            System.out.println("runtime error occurred (which it should)");
        }
    }


    /**
     * Test that the route functions on normal call with a non-null move with red as the active player
     */
    @Test
    public void successful_call_red() {
        when(moveVerifier.getLastSubmittedMoves()).thenReturn(moves);
        when(session.attribute("activeColor")).thenReturn(GameColor.RED);
        when(session.attribute("myColor")).thenReturn(GameColor.RED);
        try {
            String result = (String)CuT.handle(request, response);
            assertEquals(gson.toJson(Message.info("true")), result);
        } catch (HaltException h) {
            assert(false);
            System.out.println("halt causes exception");
        } catch (Exception e) {
            assert(false);
            System.out.println("runtime error occurred");
        }
    }

    /**
     * Test that the route functions on normal call with a non-null move with white as the active player
     */
    @Test
    public void successful_call_white() {
        when(moveVerifier.getLastSubmittedMoves()).thenReturn(moves);
        when(session.attribute("activeColor")).thenReturn(GameColor.WHITE);
        when(session.attribute("myColor")).thenReturn(GameColor.WHITE);
        try {
            String result = (String)CuT.handle(request, response);
            assertEquals(gson.toJson(Message.info("true")), result);
        } catch (HaltException h) {
            assert(false);
            System.out.println("halt causes exception");
        } catch (Exception e) {
            assert(false);
            System.out.println("runtime error occurred");
        }
    }

    /**
     * Test that the white player will properly empty the array list when given viable double jumps
     */
    @Test
    public void successful_call_white_doublejump() {
        /// overriding the default beforeeach
        moves = new ArrayList<>();
        moves.add(new Move(new Position(2, 2), new Position(4, 4)));
        moves.add(new Move(new Position(4, 4), new Position(6, 6)));

        when(moveVerifier.getLastSubmittedMoves()).thenReturn(moves);
        when(session.attribute("activeColor")).thenReturn(GameColor.WHITE);
        when(session.attribute("myColor")).thenReturn(GameColor.WHITE);
        when(game.getWhiteBoard()).thenReturn(board);
        try {
            String result =(String)CuT.handle(request, response);
            assertEquals(gson.toJson(Message.info("true")), result);
        } catch (HaltException h) {
            assert(true);
            System.out.println("halt causes exception");
        } catch (Exception e) {
            assert(false);
            System.out.println("runtime error occurred");
        }
    }

    /**
     * Test that the red player will properly empty the array list when given viable double jumps
     */
    @Test
    public void successful_call_red_doublejump() {
        /// overriding the default beforeeach
        moves = new ArrayList<>();
        moves.add(new Move(new Position(2, 2), new Position(4, 4)));
        moves.add(new Move(new Position(4, 4), new Position(6, 6)));

        when(moveVerifier.getLastSubmittedMoves()).thenReturn(moves);
        when(session.attribute("activeColor")).thenReturn(GameColor.RED);
        when(session.attribute("myColor")).thenReturn(GameColor.RED);
        when(game.getRedBoard()).thenReturn(board);
        try {
            String result =(String)CuT.handle(request, response);
            assertEquals(gson.toJson(Message.info("true")), result);
        } catch (HaltException h) {
            assert(true);
            System.out.println("halt causes exception");
        } catch (Exception e) {
            assert(false);
            System.out.println("runtime error occurred");
        }
    }
}
