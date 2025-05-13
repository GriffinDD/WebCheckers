package com.webcheckers.ui;

import spark.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import com.webcheckers.ui.Attributes.GetSigninRouteAttributes;

/**
 * The UI Controller to GET the Siginin page.
 *
 * @author Griffin Danner-Doran
 * @author Joseph Casale
 */
public class GetSigninRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetSigninRoute.class.getName());

  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /signin} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetSigninRoute(final TemplateEngine templateEngine) {
    Objects.requireNonNull(templateEngine);
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    //
    LOG.config("GetSigininRoute is initialized.");
  }

  /**
   * Render the WebCheckers Signin page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Signin page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("GetSigininRoute is invoked.");

    Map<String, Object> viewModel = new HashMap<>();

    // display a user message in the Home page
    viewModel.put("title", GetSigninRouteAttributes.TITLE);
    viewModel.put("message1", GetSigninRouteAttributes.PLAYER_WELCOME_MESSAGE);
    viewModel.put("login", GetSigninRouteAttributes.PLAYER_LOGIN_PROMPT);

    // render the View
    return templateEngine.render(new ModelAndView(viewModel , "signin.ftl"));
  }
}
