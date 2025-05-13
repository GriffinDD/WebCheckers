package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.util.GameColor;
import com.webcheckers.util.MenuState;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The UI Controller to GET the player help menu
 *
 * @author Griffin Danner-Doran
 */
public class GetHelpRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetHelpRoute.class.getName());

  private final TemplateEngine templateEngine;
  private final PlayerLobby playerlobby;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /help} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   * @param playerlobby
   *    the universal player lobby instance
   */
  public GetHelpRoute(final TemplateEngine templateEngine, final PlayerLobby playerlobby) {
    Objects.requireNonNull(playerlobby, "playerlobby is required");
    Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.templateEngine = templateEngine;
    this.playerlobby = playerlobby;
    LOG.config("GetHelpRoute is initialized.");
  }

  /**
   * Render the WebCheckers help page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the help page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("GetHelpRoute is invoked.");
    final Session httpSession = request.session();
    //updates both the player list and in game registers of menu status to notify others that this player is in the
    //help menu
    if(httpSession.attribute("currentUser") != null){
      playerlobby.setPlayerState(httpSession.attribute("currentUser"), MenuState.INHELP);
    }
    Game game = playerlobby.getGame();
    //if there is a game going on, set the calling players menu status
    if(game != null){
      GameColor myColor = httpSession.attribute("myColor");
      if(myColor.equals(GameColor.RED)){
        game.setRedState(MenuState.INHELP);
      }else{
        game.setWhiteState(MenuState.INHELP);
      }
    }

    Map<String, Object> viewModel = new HashMap<>();

    // display a user message in the Home page
    viewModel.put("title", "Welcome to the help page");
    // render the View
    return templateEngine.render(new ModelAndView(viewModel , "help.ftl"));
  }
}
