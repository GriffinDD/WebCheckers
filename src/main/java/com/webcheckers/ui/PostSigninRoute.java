package com.webcheckers.ui;

import spark.*;
import com.webcheckers.model.Player;
import com.webcheckers.application.PlayerLobby;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.webcheckers.ui.Attributes.PostSigninAttributes;
import static com.webcheckers.ui.Attributes.GetSigninRouteAttributes;
import static spark.Spark.halt;


/**
 * The UI Controller to handle POST signin calls, which occur when a name is submitted in the sign in page
 *
 * @author Griffin Danner-Doran
 * @author Joseph Casale
 */
public class PostSigninRoute implements Route {
  private static final Logger LOG = Logger.getLogger(PostSigninRoute.class.getName());
  private final PlayerLobby playerlobby;
  private final TemplateEngine templateEngine;


  /**
   * Create the Spark Route (UI controller) to handle all {@code POST /signin} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   * @param playerlobby
   *    the universal player lobby instance
   */
  public PostSigninRoute(final TemplateEngine templateEngine, final PlayerLobby playerlobby) {
    Objects.requireNonNull(playerlobby, "playerLobby is required");
    Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.templateEngine = templateEngine;
    this.playerlobby = playerlobby;
    LOG.config("PostSigininRoute is initialized.");
  }

  /**
   * Validate a submitted name, redirect to home if valid otherwise render signin page with error message
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Signin page if name is not valid
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("PostSigininRoute is invoked.");
    Map<String, Object> vm = new HashMap<>();
    final Session httpSession = request.session();
    final String Name = request.queryParams(PostSigninAttributes.PLAYER_NAME);
    httpSession.attribute("playerlobby", playerlobby);
    vm.put("message1", GetSigninRouteAttributes.PLAYER_WELCOME_MESSAGE);
    // assert that name meets standards.
    if(!Name.matches("[a-zA-Z0-9 ]+$") || !Name.matches(".*\\w.*")){ //regex to make sure name has at least one alphanumeric character and no special characters
      //also checks to make sure that it is not just a space
        // If the name is not unique, for now, lets just go home.
        vm.put("message", PostSigninAttributes.INVALID_NAME);
        vm.put("title", "Choose a valid name!"); //we should consider resolving the fact that this happens twice DRY
        vm.put("login", PostSigninAttributes.PLAYER_LOGIN_PROMPT);
        return templateEngine.render(new ModelAndView(vm, "signin.ftl"));
    }

    // playerlobby.addPlayerName will return a true only if it is not already in the name
    if(playerlobby.addPlayerName(Name)){
      Player ex = new Player(Name);
      httpSession.attribute("currentUser", ex);
      httpSession.attribute("PlayerName", Name);
      httpSession.attribute("message", PostSigninAttributes.PLAYER_LOGGED_IN_MESSAGE);
    }else{
      // If the name is not unique, render the sign in again with a relevant warning
      vm.put("message", PostSigninAttributes.NAME_ALREADY_EXISTS_MESSAGE);
      vm.put("title", "That name is taken!");
      vm.put("login", PostSigninAttributes.PLAYER_LOGIN_PROMPT);
      return templateEngine.render(new ModelAndView(vm, "signin.ftl"));
    }
    LOG.log(Level.CONFIG, "Creating Player Object for " + Name);
    // render the Home View
    response.redirect(WebServer.HOME_URL);
    halt();
    return null;
  }
}
