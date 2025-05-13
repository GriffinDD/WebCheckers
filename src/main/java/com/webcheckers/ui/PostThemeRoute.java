package com.webcheckers.ui;

import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

/**
 * The UI Controller to POST the chosen theme when in the theme menu
 *
 * @author Griffin Danner-Doran
 */
public class PostThemeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(PostThemeRoute.class.getName());

  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all {@code POST /themes} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public PostThemeRoute(final TemplateEngine templateEngine) {
    Objects.requireNonNull(templateEngine);
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    //
    LOG.config("PostThemeRoute is initialized.");
  }

  /**
   * Render the home page after selecting a theme
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   null, indicating a sucessful redirect
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("PostThemeRoute is invoked.");
    final Session httpSession = request.session();
    final String modeStr = request.queryParams("theme");
    int mode = Integer.parseInt(modeStr);
    String theme;
    //read the submitted number from the selection buttons and change to the corresponding theme
    if(mode == 2){
      theme = "Wattson";
      httpSession.attribute("GameTheme", "Wattson.ftl");
    }else if(mode == 3){
      theme = "Versus";
      httpSession.attribute("GameTheme", "Versus.ftl");
    }else if(mode == 4){
      theme = "Chad";
      httpSession.attribute("GameTheme", "chad.ftl");
    }else if(mode == 5){
      theme = "Terry Davis";
      httpSession.attribute("GameTheme", "terrydavisappreciation.ftl");
    }else{
      theme = "Basic";
      httpSession.attribute("GameTheme", "game.ftl");
    }
    httpSession.attribute("CurrentTheme", theme);
    response.redirect(WebServer.HOME_URL);
    halt();
    return null;
  }
}
