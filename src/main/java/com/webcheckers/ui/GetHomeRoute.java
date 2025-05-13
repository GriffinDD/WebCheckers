package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;


import com.webcheckers.model.BoardView;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.GameColor;
import com.webcheckers.util.MenuState;
import com.webcheckers.util.ViewMode;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

import com.webcheckers.application.PlayerLobby;

import static spark.Spark.halt;


/**
 * The UI Controller to GET the Home page.
 *
 * @author Griffin Danner-Doran
 * @author Joseph Casale
 */
public class GetHomeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  private final PlayerLobby playerlobby;
  private final TemplateEngine templateEngine;
  boolean init = false;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   * @param playerlobby
   *    the universal player lobby instance
   */
  public GetHomeRoute(final TemplateEngine templateEngine, final PlayerLobby playerlobby) {
    Objects.requireNonNull(playerlobby, "playerlobby is required");
    Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.templateEngine = templateEngine;
    this.playerlobby = playerlobby;
    LOG.config("GetHomeRoute is initialized.");
  }


  /**
   * Render the WebCheckers Home page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Home page
   *
   *  Redirects to game view when the session is marked as in game or a challenge has been issued
   */
  @Override
  public Object handle(Request request, Response response) {
  

    LOG.finer("GetHomeRoute is invoked.");
    //
    Map<String, Object> vm = new HashMap<>();
    final Session httpSession = request.session();
    httpSession.attribute("playerlobby", playerlobby);
    //sets the player to be out of menu whenever they enter the home screen, which ensures that they are in the correct
    //state relative to menu status
    if(httpSession.attribute("currentUser") != null){
      playerlobby.setPlayerState(httpSession.attribute("currentUser"), MenuState.HOME);
      Game g = playerlobby.getGame();
      if(g != null) {
        Player c = httpSession.attribute("currentUser");
        if (c.equals(g.getWhitePlayer())) {
          g.setWhiteState(MenuState.TRANSITION);
        } else if (c.equals(g.getRedPlayer())) {
          g.setRedState(MenuState.TRANSITION);
        }
      }
    }else{
      httpSession.attribute("message", Attributes.GetHomeRouteAttributes.WELCOME_MSG);
    }
    if(!init){//sets base values on startup
      init = true;
      vm.put("playerlobby", httpSession.attribute("playerlobby"));
      httpSession.attribute("CurrentTheme", "Basic");
      httpSession.attribute("GameTheme", "game.ftl");

      // display a user message in the Home page
      vm.put("playerName", "");
      vm.put("message",  httpSession.attribute("message"));
    }else{
      //send player back to game view if they are in a game
      if(httpSession.attribute("gaming") != null){
        if((boolean)httpSession.attribute("gaming")){
          // test for wait idea
          synchronized(playerlobby){
              playerlobby.notifyAll();
          }
          response.redirect(WebServer.GAME_URL);
          halt();
          return null;
        }
      }
      Player currentUser = httpSession.attribute("currentUser");
      //checks if the current user has been challenged, pulls them into the game if so
      if(currentUser != null) {
        Game game = playerlobby.getGame();
        if (game != null) {
          Player whitePlayer = game.getWhitePlayer();
          // Render board 'flipped' with white at bottom
          if (whitePlayer != null && whitePlayer.equals(currentUser)) {
            BoardView wboard = new BoardView(true);
            vm.put("redPlayer", game.getRedPlayer());
            vm.put("whitePlayer", game.getWhitePlayer());
            vm.put("board", wboard);
            game.setWhiteBoard(wboard);
            vm.put("currentUser", game.getWhitePlayer());
            vm.put("title", "Game");
            vm.put("viewMode", ViewMode.PLAY);
            vm.put("activeColor", GameColor.RED);
            httpSession.attribute("myColor", GameColor.WHITE);
            httpSession.attribute("gaming", true);
            httpSession.attribute("backupboard", wboard);
            synchronized(playerlobby){
              playerlobby.notifyAll();
            }  
            return templateEngine.render(new ModelAndView(vm, httpSession.attribute("GameTheme")));
          }
        }
      }

      //makes sure that default theme is set
      if(httpSession.attribute("CurrentTheme") == null){
        httpSession.attribute("CurrentTheme", "Basic");
        httpSession.attribute("GameTheme", "game.ftl");
      }
      vm.put("playerlobby", playerlobby);
      vm.put("playerName", httpSession.attribute("PlayerName"));
      vm.put("currentUser", httpSession.attribute("currentUser"));
      vm.put("message", httpSession.attribute("message"));
      httpSession.attribute("message", Attributes.GetHomeRouteAttributes.PLAY_A_GAME_MESSAGE);
    }
    vm.put("title", Attributes.GetHomeRouteAttributes.WELCOME_TITLE);
    vm.put("challenge", Attributes.GetHomeRouteAttributes.CHALLENGE);

    // render the View
    return templateEngine.render(new ModelAndView(vm , "home.ftl"));
  }
}
