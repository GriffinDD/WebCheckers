package com.webcheckers.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Game;
import com.webcheckers.model.Move;
import com.webcheckers.model.MoveVerifier;
import com.webcheckers.model.Piece;
import com.webcheckers.util.GameColor;
import com.webcheckers.util.Message;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

/**
 * The UI Controller to handle POST submit turn calls, which occur when a move is submitted in the game view.
 * Only called after moves have been validated, applies the moves to the current board then prepares them to
 * be reflected on the opponents board.
 *
 * @author Griffin Danner-Doran
 * @author Joseph Casale
 */
public class PostSubmitTurn implements Route{
    private static final Logger LOG = Logger.getLogger(PostSubmitTurn.class.getName());
    private Gson gson;
    private MoveVerifier moveVerifier;
    private Set<Move> jumps;
    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /submitTurn} HTTP requests.
     *
     * @param gson
     *   gson object used to coordinate with the given JS scripts
     * @param moveVerifier
     *    the shared moveVerifier instance used to validate moves
     */
    public PostSubmitTurn(Gson gson, MoveVerifier moveVerifier){
        Objects.requireNonNull(moveVerifier);
        Objects.requireNonNull(gson);
        this.gson = gson;
        this.moveVerifier = moveVerifier;
        LOG.config("PostSubmitTurn is initialized.");
        this.jumps = new HashSet<>();
    }

    /**
     * WARNING : DANGEROUS
     * Mostly for testing purposes, seed with jumps.
     * @param jumps set of jumps
     */
    public void setJumps(Set<Move> jumps){
        this.jumps = jumps;
    }


    /**
     * Enact a set of valid moves or jumps, update the users board, and switch the turn
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   a status update on whether a move was successfully submitted
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("PostSubmitTurn is invoked.");
        Map<String, Object> vm = new HashMap<>();

        Session httpSession = request.session();
        PlayerLobby playerLobby = httpSession.attribute("playerlobby");
        Game game = playerLobby.getGame();
        BoardView board;
        /// if you go to submit a move and the game is over, redirect to game to handle exit/
        if(playerLobby.getGame() == null){
            jumps.clear();
            moveVerifier.clearLastVerifiedMoves();
            return gson.toJson(Message.info("true"));
        }

        //gets the submitting player's board
        GameColor color = game.getActiveColor();
        if(color.equals(GameColor.RED)){
            board = game.getRedBoard();
        }else{
            board = game.getWhiteBoard();
        }

        this.moveVerifier.setBoard(board);
        moveVerifier.clearLastSubmittedMoves();



        /// switch the active color to change turns
        if(color.equals(GameColor.RED)){
            color = GameColor.WHITE;
        }else{
            color = GameColor.RED;
        }

        //makes every move on this board, then sets up a list of moves to make on the opponents
        List<Move> moves = moveVerifier.getLastVerifiedMoves();
        Move last = moves.get(moves.size() - 1);
        Piece firstVerifiedMovePiece = board.getPieceForPosition(moveVerifier.getFirstVerifiedMove().getStart());
        if(last.isJump()){
            board.getJumpsForSpace(jumps, board.getSpace(last.getEndRow(), last.getEndCell()), 
                                    firstVerifiedMovePiece);            
            // if there is a king and it makes a move, the ability to jump back must not be considered a valid jump.
            moves.forEach((move) -> jumps.remove(move.reverse())); 
            if(!jumps.isEmpty()){
                jumps.clear();
                return gson.toJson(Message.error("You have jump moves to make."));
            }
        }
        Move move = moves.remove(0);
        board.makeMove(move);

        /// add it to the backlog for submitted moves for the other player to reflect
        moveVerifier.addLastSubmittedMove(move);


        while(!moves.isEmpty()){
            move = moves.stream().findFirst().get();
            moves.remove(move);
            board.makeMove(move);
            moveVerifier.addLastSubmittedMove(move);
        }

        vm.put("redPlayer", game.getRedPlayer());
        vm.put("whitePlayer", game.getWhitePlayer());
        vm.put("activeColor", color);
        game.setActiveColor(color);
         
        /// just incase
        jumps.clear();
        /// this will now end the turn.
        return gson.toJson(Message.info("The move has been made."));
    }
    
}
