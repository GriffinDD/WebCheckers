package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Game;
import com.webcheckers.model.Move;
import com.webcheckers.model.MoveVerifier;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Position;
import com.webcheckers.util.GameColor;
import com.webcheckers.util.Message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Tag("UI-tier")
public class PostValidateMoveTest {

    private PostValidateMove CuT;

    // friendly
    private MoveVerifier moveVerifier;
    private Move move;
    private Move jump;

    private Request request;
    private Session session;
    private Response response;
    private Game game;
    private Gson gson;
    private BoardView board;
    private PlayerLobby playerlobby;
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        gson = new Gson();
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        playerlobby = mock(PlayerLobby.class);
        when(session.attribute("playerlobby")).thenReturn(playerlobby);
        game = mock(Game.class);
        when(playerlobby.getGame()).thenReturn(game);
        board = mock(BoardView.class);
        move = new Move(new Position(5,0), new Position(4,1));
        jump = new Move(new Position(5,0), new Position(3,2));
        Set<Move> moves = new HashSet<>();
        moves.add(move);
        Set<Move> jumps = new HashSet<>();
        jumps.add(jump);
        List<Set<Move>> actions = new ArrayList<>();
        actions.add(moves);
        actions.add(jumps);
        when(board.getMoves()).thenReturn(actions);
        moveVerifier = mock(MoveVerifier.class);
        when(moveVerifier.moveIsValid(move)).thenReturn(true);
        // create a unique CuT for each test
        CuT = new PostValidateMove(gson, moveVerifier);
        when(request.queryParams("actionData"))
                    .thenReturn("{\"start\":{\"row\":5,\"cell\":0},\"end\":{\"row\":4,\"cell\":1}}");
        when(game.getRedBoard()).thenReturn(board);
        when(game.getWhiteBoard()).thenReturn(board);
        when(session.attribute("backupboard")).thenReturn(board);
    }

    /**
     * Test that the route functions on normal call with successful context for a red player
     */
    @Test
    public void test_successful_call_red() {
        when(game.getActiveColor()).thenReturn(GameColor.RED);
        when(board.getPieceForPosition(move.getStart())).thenReturn(mock(Piece.class));
        try {
            CuT.handle(request, response);
            assert true;
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    
    }
    /**
     * Test that the output is correct on a successful context for a red player
     */
    @Test
    public void test_successful_call_output_red() {
        when(game.getActiveColor()).thenReturn(GameColor.RED);
        when(board.getPieceForPosition(move.getStart())).thenReturn(mock(Piece.class));
        String message = null;
        try {
            message = (String)CuT.handle(request, response);
            assert true;
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
        assert(message.equals(gson.toJson(Message.info("The move is valid"))));
    
    }

    /**
     * Test that the route functions on normal call with successful context for a white player
     */
    @Test
    public void test_successful_call_white() {
        when(board.getPieceForPosition(move.getStart())).thenReturn(mock(Piece.class));
        when(game.getActiveColor()).thenReturn(GameColor.WHITE);
        try {
            CuT.handle(request, response);
            assert true;
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }

    }
    /**
     * Test that the output is correct on a successful context for a white player
     */
    @Test
    public void test_successful_call_output_white() {
        when(board.getPieceForPosition(move.getStart())).thenReturn(mock(Piece.class));
        when(game.getActiveColor()).thenReturn(GameColor.WHITE);
        String message = null;
        try {
            message = (String)CuT.handle(request, response);
            assert true;
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
        assert(message.equals(gson.toJson(Message.info("The move is valid"))));

    }


    /**
     * Test that the route functions on an invalid move with white player
     */
    @Test
    public void test_invalid_call_white() {
        when(board.getPieceForPosition(move.getStart())).thenReturn(mock(Piece.class));
        when(game.getActiveColor()).thenReturn(GameColor.WHITE);
        when(moveVerifier.moveIsValid(move)).thenReturn(false);
        try {
            CuT.handle(request, response);
            assert true;
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }

    }
    /**
     * Test that the output is correct on an invalid move for a white player
     */
    @Test
    public void test_invalid_call_output_white() {
        when(board.getPieceForPosition(move.getStart())).thenReturn(mock(Piece.class));
        when(game.getActiveColor()).thenReturn(GameColor.WHITE);
        when(moveVerifier.moveIsValid(move)).thenReturn(false);
        String message = null;
        try {
            message = (String)CuT.handle(request, response);
            assert true;
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
        assert(message.equals(gson.toJson(Message.error("Not valid"))));

    }


    /**
     * Test that the route functions on a valid move made with the backup board with white player
     */
    @Test
    public void test_successful_gamover_call_white() {
        when(board.getPieceForPosition(move.getStart())).thenReturn(mock(Piece.class));
        when(game.getActiveColor()).thenReturn(GameColor.WHITE);
        when(playerlobby.getGame()).thenReturn(null);
        try {
            CuT.handle(request, response);
            assert true;
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }

    }
    /**
     * Test that the output is correct on a valid move made with the backup for a white player
     */
    @Test
    public void test_successful_gameover_call_output_white() {
        when(board.getPieceForPosition(move.getStart())).thenReturn(mock(Piece.class));
        when(game.getActiveColor()).thenReturn(GameColor.WHITE);
        when(playerlobby.getGame()).thenReturn(null);
        String message = null;
        try {
            message = (String)CuT.handle(request, response);
            assert true;
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
        assert(message.equals(gson.toJson(Message.info("The move is valid"))));

    }
}
