package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;


import static spark.Spark.halt;

/**
 * The UI Controller to handle POST signout calls, which occur when the signout link is selected
 *
 * @author Griffin Danner-Doran
 */
public class PostSignoutRoute implements Route {
  private static final Logger LOG = Logger.getLogger(PostSignoutRoute.class.getName());
  private final PlayerLobby playerlobby;

  /**
   * Create the Spark Route (UI controller) to handle all {@code POST /signout} HTTP requests.
   *
   * @param playerlobby
   *   the universal player lobby instance
   */
  public PostSignoutRoute(final PlayerLobby playerlobby) {
    Objects.requireNonNull(playerlobby);
    this.playerlobby = playerlobby;
    LOG.config("PostSignoutRoute is initialized.");
  }

  /**
   * Render the home view with relevant messages and update the game if done from in game
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   null, indicating a successful redirect to the home view
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("PostSignoutRoute is invoked.");
    final Session httpSession = request.session();
    //if called from in game, handle win or loss accordingly
    if(httpSession.attribute("gaming") != null && (boolean)httpSession.attribute("gaming")){
      Game game = playerlobby.getGame();
      if(game != null) {
        Player playerToWin = game.getOtherPlayer(httpSession.attribute("currentUser"));
        playerlobby.removePlayerFromGame(playerToWin);
        game.endGame(playerToWin);
        playerlobby.endGame();
      }
      httpSession.attribute("gaming", false);
    }
    playerlobby.removePlayer(httpSession.attribute("currentUser"));
    httpSession.attribute("currentUser", null);
    httpSession.attribute("PlayerName", null);
    httpSession.attribute("playerlobby", playerlobby);
    response.redirect(WebServer.HOME_URL);
    halt();
    return null;
  }
}
