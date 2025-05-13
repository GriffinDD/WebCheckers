package com.webcheckers.ui;


import java.util.Objects;
import java.util.logging.Logger;

import spark.*;

import com.google.gson.Gson;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import spark.Request;
import spark.Response;
import spark.Route;


/**
 * The UI Controller to handle POST resign calls from the game view
 * This class is called only when a player selects the resign button while in a game and will end the game and return
 * the player to the home view with a corresponding message
 *
 * @author Griffin Danner-Doran
 * @author Joseph Casale
 */
public class PostResignRoute  implements Route{
    private static final Logger LOG = Logger.getLogger(PostResignRoute.class.getName());
    private PlayerLobby playerlobby;
    private Gson gson;
    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /resignGame} HTTP requests.
     *
     * @param gson
     *   gson object used to coordinate with the given JS scripts
     * @param playerlobby
     *    the universal player lobby instance
     */
    public PostResignRoute(Gson gson, PlayerLobby playerlobby){
        Objects.requireNonNull(gson);
        Objects.requireNonNull(playerlobby);

        this.playerlobby = playerlobby;
        this.gson = gson;
        LOG.config("PostResignRoute is initialized.");
    }


    /**
     * Handle the resignation of a player and return them to the home view
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the success status indicating a completed resignation
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

        LOG.finer("PostResignRoute is invoked.");
        final Session httpSession = request.session();

        Game game = playerlobby.getGame();
        //checks if game is over, in the rare case that you resign after your opponent but before your board updates
        if(game == null) { //game is over
            httpSession.attribute("gaming", false);
            httpSession.attribute("message", Message.info("You Won! Ready to win again?"));
            return gson.toJson(Message.info("success"));
        }

        //ends the game and updates relevant attributes
        Player playerToResign = httpSession.attribute("currentUser");
        Player playerToWin = game.getOtherPlayer(playerToResign);
        playerlobby.removePlayerFromGame(playerToResign);
        playerlobby.removePlayerFromGame(playerToWin);
        game.endGame(playerToWin);
        playerlobby.endGame();

        httpSession.attribute("playerlobby", playerlobby);
        httpSession.attribute("gaming", false);
        httpSession.attribute("message", Message.info("Sorry you lost, up for another game?"));
        
        // line 96 of AjexUtils is throwing the exception with json.parse.
        return gson.toJson(Message.info("success"));
        //return templateengine.render(new ModelAndView(vm, "home.ftl"));
    }
    
}
