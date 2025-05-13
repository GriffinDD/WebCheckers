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
 * The UI Controller to GET the theme page
 *
 * @author Griffin Danner-Doran
 */
public class GetThemeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetThemeRoute.class.getName());

  private final TemplateEngine templateEngine;
  private final PlayerLobby playerlobby;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /themes} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   * @param playerlobby
   *    the universal player lobby instance
   */
  public GetThemeRoute(final TemplateEngine templateEngine, final PlayerLobby playerlobby) {
    Objects.requireNonNull(playerlobby, "playerlobby is required");
    Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.templateEngine = templateEngine;
    this.playerlobby = playerlobby;
    LOG.config("GetThemeRoute is initialized.");
  }

  /**
   * Render the WebCheckers theme page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the theme page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("GetThemeRoute is invoked.");
    final Session httpSession = request.session();
    Map<String, Object> viewModel = new HashMap<>();
    //updates both the player list and in game registers of menu status to notify others that this player is in the
    //theme menu
    if(httpSession.attribute("currentUser") != null){
      playerlobby.setPlayerState(httpSession.attribute("currentUser"), MenuState.INTHEME);
    }
    Game game = playerlobby.getGame();
    //if a game is in progress, update the calling players status
    if(game != null){
      GameColor myColor = httpSession.attribute("myColor");
      if(myColor.equals(GameColor.RED)){
        game.setRedState(MenuState.INTHEME);
      }else{
        game.setWhiteState(MenuState.INTHEME);
      }
    }
    // display a user message in the Home page
    viewModel.put("title", "Welcome to the theme selection page");
    viewModel.put("message", "Please choose a theme for your game");
    viewModel.put("message2", "Current theme: " + httpSession.attribute("CurrentTheme"));
    // render the View
    return templateEngine.render(new ModelAndView(viewModel , "theme.ftl"));
  }
}
