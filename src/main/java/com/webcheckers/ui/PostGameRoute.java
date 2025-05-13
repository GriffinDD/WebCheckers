package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.GameColor;
import com.webcheckers.util.MenuState;
import com.webcheckers.util.ViewMode;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;


/**
 * The UI Controller to POST a player challenge made in the home page and render a game accordingly
 *
 * @author Griffin Danner-Doran
 * @author Joseph Casale
 * @author Benjamin LaGreca
 */
public class PostGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostGameRoute.class.getName());
    private final TemplateEngine templateEngine;
    private final PlayerLobby playerlobby;
    static final String OPPOSING_PLAYER_NAME = "OpposingPlayerName";

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /game} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     * @param playerlobby
     *    the universal player lobby instance
     */
    public PostGameRoute(TemplateEngine templateEngine, final PlayerLobby playerlobby) {
        Objects.requireNonNull(playerlobby, "playerlobby is required");
        Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.templateEngine = templateEngine;
        this.playerlobby = playerlobby;
        LOG.config("PostGameRoute is initialized.");
    }

    /**
     * Render the Web Checkers Game page if the challenge is valid
     * @param request
     *  the HTTP request
     * @param response
     *  the HTTP response
     * @return
     *  the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("PostGameRoute is invoked.");

        final Session httpSession = request.session();

        Map<String, Object> vm = new HashMap<>();

        Game game = playerlobby.getGame();
        Player p1 =  httpSession.attribute("currentUser");
        final String opponentName = request.queryParams(OPPOSING_PLAYER_NAME);
        Player p2 = null;


        if(playerlobby.hasPlayer(opponentName) && p1 != null && !(p1.getName().equals(opponentName))){
            p2 = playerlobby.getPlayer(opponentName);
            //sends back to home view with an error message if that player was already in a game
            if(playerlobby.inGame(p2)){
                httpSession.attribute("message", Message.error("That player is already in a game"));
                response.redirect(WebServer.HOME_URL);
                halt();
                return null;
                //sends back to home view with an error message if that player is in a menu
            }else if(p2.getMenuState() == MenuState.INHELP || p2.getMenuState() == MenuState.INTHEME){
                httpSession.attribute("message", Message.error("That player is in a menu, try challenging them" +
                        " again soon"));
                response.redirect(WebServer.HOME_URL);
                halt();
                return null;
            }
            playerlobby.putInGame(p1);
            playerlobby.putInGame(p2);
        } else{
            //sends back to home view with an error message if the entered player does not exist or is the current user
            httpSession.attribute("message", Message.error("Please choose a valid opponent from the list"));
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }

        //creates a game with the current player as red
        BoardView myBoard = new BoardView(false);
        if(game==null){
            playerlobby.addGame();
            game = playerlobby.getGame();
            game.setRedPlayer(p1);
            game.setWhitePlayer(p2);
            game.setRedBoard(myBoard);
            vm.put("title", "Game");
            vm.put("currentUser", p1);
            vm.put("viewMode", ViewMode.PLAY);
            vm.put("redPlayer", p1);
            vm.put("whitePlayer", p2);
            vm.put("activeColor", GameColor.RED);
            vm.put("board", myBoard);
            httpSession.attribute("currentUser", p1);
            httpSession.attribute("backupboard", myBoard);
            game.setActiveColor(GameColor.RED);
            httpSession.attribute("myColor", GameColor.RED);
            // render the view
            httpSession.attribute("gaming", true);
            
            // attempt to use threading to resolve wait
            synchronized(playerlobby){
                playerlobby.wait(8000); // will wait and timeout at 8 seconds.
            }
        }

        
        return templateEngine.render(new ModelAndView(vm,  httpSession.attribute("GameTheme")));
    }
}
