package com.webcheckers.ui;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.*;
import com.webcheckers.util.GameColor;
import com.webcheckers.util.MenuState;
import com.webcheckers.util.Message;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

/**
 * Notifies the game view when an update is required. Called periodically when a player is waiting for their turn
 *
 * @author Griffin Danner-Doran
 * @author Joseph Casale
 */
public class PostCheckTurnRoute implements Route{
    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());
    private MoveVerifier moveVerifier;
    private Gson gson;
    private final PlayerLobby playerlobby;


    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /checkTurn} HTTP requests.
     *
     * @param gson
     *   gson object used to coordinate with the given JS scripts
     * @param playerlobby
     *    the universal player lobby instance
     * @param moveVerifier
     *    the shared moveVerifier instance used to validate moves
     */
  public PostCheckTurnRoute(Gson gson, MoveVerifier moveVerifier,  final PlayerLobby playerlobby){
        Objects.requireNonNull(playerlobby, "playerlobby is required");
        this.moveVerifier = Objects.requireNonNull(moveVerifier, "move verifier must exist to check for turns");
        this.gson = Objects.requireNonNull(gson, "gson must not be null");
        this.playerlobby = playerlobby;
        LOG.config("PostCheckTurnRoute is initialized.");
    }

    /**
     * Check if the board needs to update and notify gson of the result
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the status message indicating if an update needs to occur
     *      true for a board refresh, false for none
     *
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("PostCheckTurnRoute is invoked.");
        Session httpSession = request.session();
        Game game = playerlobby.getGame();
        /// If the game is null, it's over; send true, and refresh the game route.
        if(playerlobby.getGame() == null){
            return gson.toJson(Message.info("true"));
        }
        BoardView board;
        GameColor color = game.getActiveColor();
        GameColor myColor = httpSession.attribute("myColor");
        //checks if my opponent is in a menu, refreshes if they are
        //the transition state represents having just exited a menu, so the refresh clears the notification
        // by refreshing a final time
        if(myColor.equals(GameColor.RED)){
            board = game.getRedBoard();
        }else{
            board = game.getWhiteBoard();
        }
        httpSession.attribute("backupboard", board);
        /// make sure move verifier is working with the correct session board
        moveVerifier.setBoard(board);

        List<Move> moves = moveVerifier.getLastSubmittedMoves();
        //tells the game view that it needs to update to accommodate a recently made move
        if(moves.size() > 0 && color == myColor){
                return gson.toJson(Message.info("true"));
        }

        //checks if the other player is in a menu, which warrants an update
        //if the menu state is TRANSITION, the state should be set to the default HOME state and cleared with one last
        //refresh
        if(myColor.equals(GameColor.RED)){
            if(game.getWhiteState() != MenuState.HOME){
                if(game.getWhiteState() == MenuState.TRANSITION) {
                    game.setWhiteState(MenuState.HOME);
                }
                return gson.toJson(Message.info("true"));
            }
        }else{
            if(game.getRedState() != MenuState.HOME){
                if(game.getRedState() == MenuState.TRANSITION) {
                    game.setRedState(MenuState.HOME);
                }
                return gson.toJson(Message.info("true"));
            }
        }
        return gson.toJson(Message.info("false"));
    }
    
}
