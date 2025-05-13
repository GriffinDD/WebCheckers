package com.webcheckers.ui;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.*;
import com.webcheckers.util.GameColor;
import com.webcheckers.util.Message;


import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;


/**
 * The UI Controller to handle POST validate move calls, which occur when a piece is dragged and needs to be confirmed
 * as valid
 *
 * @author Griffin Danner-Doran
 * @author Joseph Casale
 */
public class PostValidateMove implements Route{
    private static final Logger LOG = Logger.getLogger(PostValidateMove.class.getName());
    private Gson gson;
    private MoveVerifier moveVerifier;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /validateMove} HTTP requests.
     *
     * @param gson
     *   gson object used to coordinate with the given JS scripts
     * @param moveVerifier
     *    the shared moveVerifier instance used to validate moves
     */
    public PostValidateMove(Gson gson, MoveVerifier moveVerifier){
        Objects.requireNonNull(gson);
        Objects.requireNonNull(moveVerifier);
        this.moveVerifier = moveVerifier;
        this.gson = gson;
        LOG.config("PostValidateMove is initialized.");
    }

    /**
     * Check that the most recently attempted move is valid, and return a result
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   a status update on whether a move was valid
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("PostValidateMove is invoked.");
        Session httpSession = request.session();
        PlayerLobby playerLobby = httpSession.attribute("playerlobby");
        Game game = playerLobby.getGame();
        BoardView board;
        if(game == null) { //in the case where the opponent resigns on your turn, a back-up of the board can be accessed for a final move before wincon is checked
            board = httpSession.attribute("backupboard");
        }else {
            //gets this player's board
            GameColor color = game.getActiveColor();
            if (color.equals(GameColor.RED)) {
                board = game.getRedBoard();
            } else {
                board = game.getWhiteBoard();
            }
        }

        this.moveVerifier.setBoard(board);
        /// terrible json parsing that gets the startrow, startcell -> endrow, endcell data
        String data = request.queryParams("actionData");
        JsonObject jObject = gson.fromJson(data, JsonObject.class);
        String start = jObject.get("start").toString();
        String end = jObject.get("end").toString();
        int startRow = Integer.parseInt(gson.fromJson(start, JsonObject.class).get("row").toString());
        int startCell = Integer.parseInt(gson.fromJson(start, JsonObject.class).get("cell").toString());
        int endRow = Integer.parseInt(gson.fromJson(end, JsonObject.class).get("row").toString());
        int endCell = Integer.parseInt(gson.fromJson(end, JsonObject.class).get("cell").toString());


        Move move = new Move(new Position(startRow, startCell), new Position(endRow, endCell));
        
        if(board.getPieceForPosition(move.getStart()) == null){ //is multi-jump
            Set<Move> jumps = new HashSet<>();
            Space space = board.getSpace(move.getStartRow(), move.getStartCell());
            board.getJumpsForSpace(jumps, space, board.getPieceForPosition(moveVerifier.getFirstVerifiedMove().getStart()));
            if(jumps.contains(move)){
                moveVerifier.addVerifiedMove(move);
                return gson.toJson(Message.info("The move is valid"));
            }
        }
        
        if(moveVerifier.moveIsValid(move)){
            // set a piece for algorithm, remember to remove this intermediate piece.
            return gson.toJson(Message.info("The move is valid"));
        }

        //only not valid if it was not found in the set of jumps or moves
        return gson.toJson(Message.error("Not valid"));
    }
    
}
