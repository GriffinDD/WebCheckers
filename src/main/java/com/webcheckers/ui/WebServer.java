package com.webcheckers.ui;

import static spark.Spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.MoveVerifier;

import spark.TemplateEngine;


/**
 * The server that initializes the set of HTTP request handlers.
 * This defines the <em>web application interface</em> for this
 * WebCheckers application.
 *
 * <p>
 * There are multiple ways in which you can have the client issue a
 * request and the application generate responses to requests. If your team is
 * not careful when designing your approach, you can quickly create a mess
 * where no one can remember how a particular request is issued or the response
 * gets generated. Aim for consistency in your approach for similar
 * activities or requests.
 * </p>
 *
 * <p>Design choices for how the client makes a request include:
 * <ul>
 *     <li>Request URL</li>
 *     <li>HTTP verb for request (GET, POST, PUT, DELETE and so on)</li>
 *     <li><em>Optional:</em> Inclusion of request parameters</li>
 * </ul>
 * </p>
 *
 * <p>Design choices for generating a response to a request include:
 * <ul>
 *     <li>View templates with conditional elements</li>
 *     <li>Use different view templates based on results of executing the client request</li>
 *     <li>Redirecting to a different application URL</li>
 * </ul>
 * </p>
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class WebServer {
  private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

  //
  // Constants
  //

  /**
   * The URL pattern to request the Home page.
   */
  public static final String HOME_URL = "/";


  /**
   * The URL pattern to request the Sign in page.
   */
  public static final String SIGNIN_URL = "/signin";


  /**
   * The URL pattern to request sign out and return to home page
   */
  public static final String SIGNOUT_URL = "/signout";


  /**
   *  The URL pattern to request the game page
   */
  public static final String GAME_URL = "/game";


  /**
   * The URL pattern to resign the game
   */
  private static final String RESIGN_URL = "/resignGame";

  /**

   * The URL pattern to enter the theme page screen
   */
  private static final String THEME_URL = "/themes";

  /**
   * The URL pattern to open the help menu
   */
  private static final String HELP_URL = "/help";

  /**
   * The URL pattern to validate a move.
   */
  private static final String VALIDATE_URL = "/validateMove";

  /**
   * The URL pattern to submit a turn
   */
  private static final String SUBMIT_TURN_URL = "/submitTurn";

  /**
   * The URL pattern to check for a turn
   */
  private static final String CHECK_TURN_URL = "/checkTurn";

  /**
   * The URL pattern to backup a verified turn.
   */
  private static final String BACKUP_URL = "/backupMove";
  //
  // Attributes
  //

  private final TemplateEngine templateEngine;
  private final Gson gson;
  private final PlayerLobby playerLobby;
  private final MoveVerifier moveVerifier;
//  private final Game activeGame;

  //
  // Constructor
  //

  /**
   * The constructor for the Web Server.
   *
   * @param templateEngine
   *    The default {@link TemplateEngine} to render page-level HTML views.
   * @param gson
   *    The Google JSON parser object used to render Ajax responses.
   *
   * @throws NullPointerException
   *    If any of the parameters are {@code null}.
   */
//  public WebServer(final TemplateEngine templateEngine, final Gson gson, final PlayerLobby playerLobby, final Game activeGame) {
  public WebServer(final TemplateEngine templateEngine, final Gson gson, final PlayerLobby playerLobby, MoveVerifier moveVerifier) {
    // validation
    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    Objects.requireNonNull(gson, "gson must not be null");
    Objects.requireNonNull(playerLobby);
    Objects.requireNonNull(moveVerifier);
    //
    this.templateEngine = templateEngine;
    this.gson = gson;
    this.playerLobby = playerLobby;
    this.moveVerifier = moveVerifier;
  }

  //
  // Public methods
  //

  /**
   * Initialize all of the HTTP routes that make up this web application.
   *
   * <p>
   * Initialization of the web server includes defining the location for static
   * files, and defining all routes for processing client requests. The method
   * returns after the web server finishes its initialization.
   * </p>
   */
  public void initialize() {

    // Configuration to serve static files
    staticFileLocation("/public");

    //// Setting any route (or filter) in Spark triggers initialization of the
    //// embedded Jetty web server.

    //// A route is set for a request verb by specifying the path for the
    //// request, and the function callback (request, response) -> {} to
    //// process the request. The order that the routes are defined is
    //// important. The first route (request-path combination) that matches
    //// is the one which is invoked. Additional documentation is at
    //// http://sparkjava.com/documentation.html and in Spark tutorials.

    //// Each route (processing function) will check if the request is valid
    //// from the client that made the request. If it is valid, the route
    //// will extract the relevant data from the request and pass it to the
    //// application object delegated with executing the request. When the
    //// delegate completes execution of the request, the route will create
    //// the parameter map that the response template needs. The data will
    //// either be in the value the delegate returns to the route after
    //// executing the request, or the route will query other application
    //// objects for the data needed.

    //// FreeMarker defines the HTML response using templates. Additional
    //// documentation is at
    //// http://freemarker.org/docs/dgui_quickstart_template.html.
    //// The Spark FreeMarkerEngine lets you pass variable values to the
    //// template via a map. Additional information is in online
    //// tutorials such as
    //// http://benjamindparrish.azurewebsites.net/adding-freemarker-to-java-spark/.

    //// These route definitions are examples. You will define the routes
    //// that are appropriate for the HTTP client interface that you define.
    //// Create separate Route classes to handle each route; this keeps your
    //// code clean; using small classes.

    // Shows the Checkers game Home page.
    get(HOME_URL, new GetHomeRoute(templateEngine, playerLobby));

    // Shows the sign in page
    get(SIGNIN_URL, new GetSigninRoute(templateEngine));

    // Shows the theme selector page.
    get(THEME_URL, new GetThemeRoute(templateEngine, playerLobby));

    // Shows the home view as the calling player would see it
    get(GAME_URL, new GetGameRoute(templateEngine, playerLobby, moveVerifier));

    //shows the help menu view
    get(HELP_URL, new GetHelpRoute(templateEngine, playerLobby));

    // Posts the selected theme and returns home
    post(THEME_URL, new PostThemeRoute(templateEngine));

    // shows the game page
    post(GAME_URL, new PostGameRoute(templateEngine, playerLobby));

    // Shows the Checkers game Home page
    post(SIGNIN_URL, new PostSigninRoute(templateEngine, playerLobby));

    //shows the Checkers game Home page
    post(SIGNOUT_URL, new PostSignoutRoute(playerLobby));

    //handles resignation logic
    post(RESIGN_URL, new PostResignRoute(gson, playerLobby));

    // handles Move verification logic
    post(VALIDATE_URL, new PostValidateMove(gson, moveVerifier));

    // handles submission of a turn to cause a change in turn, and update the board
    post(SUBMIT_TURN_URL, new PostSubmitTurn(gson, moveVerifier));

    // to check to see if the other player made a move, and to update the board
    post(CHECK_TURN_URL, new PostCheckTurnRoute(gson, moveVerifier, playerLobby));

    // backs up the last verified move pre-submission.
    post(BACKUP_URL, new PostBackupRoute(gson, moveVerifier));
    //
    LOG.config("WebServer is initialized.");
  }

}