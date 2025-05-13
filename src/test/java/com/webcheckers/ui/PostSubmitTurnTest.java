package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.*;
import com.webcheckers.util.GameColor;
import com.webcheckers.util.Message;
import com.webcheckers.util.PieceType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.HaltException;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Tag("UI-tier")
public class PostSubmitTurnTest {

    private PostSubmitTurn CuT;

    private Request request;
    private Session session;
    private Response response;
    private PlayerLobby playerlobby;
    private Game game;
    private Gson gson;
    private Player player;
    private Player player2;
    private BoardView board;
    private MoveVerifier moveVerifier;
    private List<Move> moves;
    private Move move;
    private Position start;
    private Piece piece;
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        piece = mock(Piece.class);
        gson = new Gson();
        when(request.session()).thenReturn(session);
        moveVerifier = mock(MoveVerifier.class);
        response = mock(Response.class);
        playerlobby = mock(PlayerLobby.class);
        game = mock(Game.class);
        board = mock(BoardView.class);
        moves = new ArrayList<>();
        player = mock(Player.class);
        player2 = mock(Player.class);
        start = mock(Position.class);
        move = mock(Move.class);
        // create a unique CuT for each test
        CuT = new PostSubmitTurn(gson, moveVerifier);
        when(session.attribute("playerlobby")).thenReturn(playerlobby);
        when(playerlobby.getGame()).thenReturn(game);
        when(game.getRedPlayer()).thenReturn(player);
        when(game.getWhitePlayer()).thenReturn(player2);
        when(game.getRedBoard()).thenReturn(board);
        when(game.getWhiteBoard()).thenReturn(board);
        when(moveVerifier.getLastVerifiedMoves()).thenReturn(moves);
        when(moveVerifier.getFirstVerifiedMove()).thenReturn(move);
        when(move.getStart()).thenReturn(start);
        when(board.getPieceForPosition(start)).thenReturn(piece);
        moves.add(move);
    }

    /**
     * Test that the route functions on normal call with red as the active player
     */
    @Test
    public void successful_call_red() {
        when(piece.getColor()).thenReturn(GameColor.RED);
        when(piece.getType()).thenReturn(PieceType.SINGLE);
        when(game.getActiveColor()).thenReturn(GameColor.RED);
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
     * Test that the output functions on normal call with red as the active player
     */
    @Test
    public void successful_call_output_red() {
        when(piece.getColor()).thenReturn(GameColor.RED);
        when(game.getActiveColor()).thenReturn(GameColor.RED);
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
        assert(message.equals(gson.toJson(Message.info("The move has been made."))));
    }

    /**
     * Test that the route functions on normal call with white as the active player
     */
    @Test
    public void successful_call_white() {
        when(game.getActiveColor()).thenReturn(GameColor.WHITE);
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
     * Test that the output functions on normal call with white as the active player
     */
    @Test
    public void successful_call_output_white() {
        when(game.getActiveColor()).thenReturn(GameColor.WHITE);
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
        assert(message.equals(gson.toJson(Message.info("The move has been made."))));
    }


    /**
     * Test that the route functions on jump call with white as the active player
     */
    @Test
    public void successful_jump_call_white() {
        when(game.getActiveColor()).thenReturn(GameColor.WHITE);
        when(moves.get(0).isJump()).thenReturn(true);
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
     * Test that the output functions on jump call with white as the active player
     */
    @Test
    public void successful_jump_call_output_white() {
        when(game.getActiveColor()).thenReturn(GameColor.WHITE);
        when(moves.get(0).isJump()).thenReturn(true);
        String message = null;
        try {
            message = (String)CuT.handle(request, response);
        } catch (HaltException h) {
            System.out.println("halt causes exception");
        } catch (Exception e) {
            assert(false);
            System.out.println("runtime error occurred");
        }
        assert(message.equals(gson.toJson(Message.info("The move has been made."))));
    }

    /**
     * Tests unsuccessful multi-jump for a white player
     */
    @Test
    public void unsuccessful_multi_jump_call_output_white() {
        when(piece.getColor()).thenReturn(GameColor.RED);
        when(game.getActiveColor()).thenReturn(GameColor.WHITE);
        when(moves.get(0).isJump()).thenReturn(true);
        @SuppressWarnings("unchecked")
        Set<Move> jumps = (Set<Move>)mock(Set.class);
        /// this is allow a double jump
        when(jumps.isEmpty()).thenReturn(false).thenReturn(false).thenReturn(true);
        CuT.setJumps(jumps);
        String message = null;
        try {
            message = (String)CuT.handle(request, response);
        } catch (HaltException h) {
            System.out.println("halt causes exception");
        } catch (Exception e) {
            assert(false);
            System.out.println("runtime error occurred");
        }
        assert(message.equals(gson.toJson(Message.error("You have jump moves to make."))));
    }

    /**
     * Tests successful multi-jump for a white player
     */
    @Test
    public void successful_multi_jump_call_output_white(){
        when(piece.getColor()).thenReturn(GameColor.RED);
        when(game.getActiveColor()).thenReturn(GameColor.WHITE);
        when(moves.get(0).isJump()).thenReturn(true);
        @SuppressWarnings("unchecked")
        Set<Move> jumps = (Set<Move>)mock(Set.class);
        /// this is allow a double jump
        when(jumps.isEmpty()).thenReturn(true);
        CuT.setJumps(jumps);
        String message = null;
        try {
            message = (String)CuT.handle(request, response);
        } catch (HaltException h) {
            System.out.println("halt causes exception");
        } catch (Exception e) {
            assert(false);
            System.out.println("runtime error occurred");
        }
        assert(message.equals(gson.toJson(Message.info("The move has been made."))));
    }
}
