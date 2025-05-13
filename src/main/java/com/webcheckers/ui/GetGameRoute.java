package com.webcheckers.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.*;
import com.webcheckers.util.GameColor;
import com.webcheckers.util.MenuState;
import com.webcheckers.util.Message;
import com.webcheckers.util.ViewMode;
import java.util.logging.Logger;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

import static spark.Spark.halt;


/**
 * The UI Controller to GET the game view
 * Updates the game board and game view. Called from home menu and works with PostCheckTurnRoute to provide timely updates
 *
 * @author Griffin Danner-Doran
 * @author Joseph Casale
 */
public class GetGameRoute implements Route{
    private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());
    private TemplateEngine templateEngine;
    private MoveVerifier moveVerifier;
    private final PlayerLobby playerlobby;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /game} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     * @param playerlobby
     *    the universal player lobby instance
     * @param moveVerifier
     *    the shared moveVerifier instance used to validate moves
     */
  public GetGameRoute(TemplateEngine templateEngine, final PlayerLobby playerlobby, MoveVerifier moveVerifier){
        Objects.requireNonNull(playerlobby, "playerlobby is required");
        Objects.requireNonNull(templateEngine, "templateEngine is required");
        Objects.requireNonNull(moveVerifier, "moveVerifier is required");
        this.templateEngine = templateEngine;
        this.playerlobby = playerlobby;
        this.moveVerifier = moveVerifier;
        LOG.config("GetGameRoute is initialized.");
    }

    /**
     * Render the WebCheckers Game view
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the game view
     *
     *  Redirects to home view if a win or loss is detected upon board update
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

        LOG.finer("GetGameRoute is invoked.");

        Session httpSession = request.session();
        Player currentPlayer = httpSession.attribute("currentUser");
        BoardView board;
        GameColor myColor = httpSession.attribute("myColor");
        Game game = playerlobby.getGame();
        //checks end game conditions. If game is null, the opponent resigned or lost when their turn started.
        if(game == null) { //game is over
            httpSession.attribute("gaming", false);
            httpSession.attribute("message", Message.info("You Won! Ready to win again?"));
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
        //gets my board based on color
        GameColor color = game.getActiveColor();
        if(myColor.equals(GameColor.RED)){
            board = game.getRedBoard();
        }else{
            board = game.getWhiteBoard();
        }

        //updates board with any submitted moves
        moveVerifier.setBoard(board);
        List<Move> moves = moveVerifier.getLastSubmittedMoves();
        if(moves.size() > 0 && color == myColor){
            moveVerifier.UpdateBoard(board, moves);
        }

        //triggers lose condition if I cannot move, which means either I have no pieces or I am blocked from a move
        if(board.outOfMoves()){
            game.endGame(game.getOtherPlayer(currentPlayer));
            playerlobby.endGame();
            httpSession.attribute("gaming", false);
            httpSession.attribute("message", Message.info("Sorry you lost, up for another game?"));
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
        Map<String, Object> vm = new HashMap<>();
        //checks if the opponent is currently in a menu, adding a specific notification to the game view if they are
        if(myColor.equals(GameColor.RED)){
            if(game.getWhiteState() == MenuState.INHELP){
                vm.put("message", Message.info("The other player is currently in the help menu."));
            }else if(game.getWhiteState() == MenuState.INTHEME){
                vm.put("message", Message.info("The other player is currently in the theme menu."));
            }
        }else{
            if(game.getRedState() == MenuState.INHELP){
                vm.put("message", Message.info("The other player is currently in the help menu."));
            }else if(game.getRedState() == MenuState.INTHEME){
                vm.put("message", Message.info("The other player is currently in the theme menu."));
            }
        }
        
        /// this is in the case that the game is still going
        vm.put("title", "Game");
        vm.put("currentUser", currentPlayer);
        vm.put("viewMode", ViewMode.PLAY);
        vm.put("redPlayer", game.getRedPlayer());
        vm.put("whitePlayer", game.getWhitePlayer());
        //gives the enemy player in menu message when it is your turn so you know what the opponent is doing
        vm.put("activeColor", game.getActiveColor());
        vm.put("board", board);
        return templateEngine.render(new ModelAndView(vm, httpSession.attribute("GameTheme")));
    }
    
}
