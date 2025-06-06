package com.webcheckers.application;

import java.io.InputStream;
import java.util.Objects;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.model.MoveVerifier;
import com.webcheckers.ui.WebServer;

import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;


/**
 * The entry point for the WebCheckers web application.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public final class Application {
  private static final Logger LOG = Logger.getLogger(Application.class.getName());

  /**
   * Queries whether the application is being run in demo-mode.
   *
   * <p>
   *   This method uses the memoization idiom so the calculation of
   *   this value is only done once.
   * @see <a href="https://en.wikipedia.org/wiki/Memoization">Memoization (wikipedia)</a>
   * </p>
   *
   * @return true if 'demoMode' property is equal to 'true'; false if the
   * property isn't set or is not 'true'
   */
  public static boolean isInDemoMode() {
    if (inDemoMode == null) {
      inDemoMode = _isInDemoMode();
    }
    return inDemoMode;
  }
  private static final String DEMO_MODE_PROPERTY = "demoMode";
  private static Boolean inDemoMode = null;

  /**
   * The explicit, private property lookup method.
   */
  private static boolean _isInDemoMode() {
    final String demoModeStr = System.getProperty(DEMO_MODE_PROPERTY);
    if (demoModeStr == null) {
      return false;
    } else {
      try {
        return Boolean.parseBoolean(demoModeStr);
      } catch (Exception e) {
        LOG.warning(String.format("Bad '%s' value, '%s'; must be a boolean.",
            DEMO_MODE_PROPERTY, demoModeStr));
        return false;
      }
    }
  }

  //
  // Application Launch method
  //

  /**
   * Entry point for the WebCheckers web application.
   *
   * <p>
   * It wires the application components together.  This is an example
   * of <a href='https://en.wikipedia.org/wiki/Dependency_injection'>Dependency Injection</a>
   * </p>
   *
   * @param args
   *    Command line arguments; none expected.
   */
  public static void main(String[] args) {
    // initialize Logging
    try {
      ClassLoader classLoader = Application.class.getClassLoader();
      final InputStream logConfig = classLoader.getResourceAsStream("log.properties");
      LogManager.getLogManager().readConfiguration(logConfig);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Could not initialize log manager because: " + e.getMessage());
    }

    //Make the only playerlobby
    final PlayerLobby playerlobby = new PlayerLobby();

    // Make the only active game... this will NOT work if we try to implement the multiple games
    // If multiple games were to be implemented we would need some kind of set or map of games
//    final Game activeGame = new Game();

    // The application uses FreeMarker templates to generate the HTML
    // responses sent back to the client. This will be the engine processing
    // the templates and associated data.
    final TemplateEngine templateEngine = new FreeMarkerEngine();

    // The application uses Gson to generate JSON representations of Java objects.
    // This should be used by your Ajax Routes to generate JSON for the HTTP
    // response to Ajax requests.
    final Gson gson = new Gson();


    final MoveVerifier moveVerifier = new MoveVerifier();

    // inject the game center and freemarker engine into web server
//    final WebServer webServer = new WebServer(templateEngine, gson, playerlobby, activeGame);
    final WebServer webServer = new WebServer(templateEngine, gson, playerlobby, moveVerifier);

    // inject web server into application
    final Application app = new Application(webServer);


    // start the application up
    app.initialize();
  }

  //
  // Attributes
  //
  private final WebServer webServer;

  //
  // Constructor
  //
  private Application(final WebServer webServer) {
    // validation
    Objects.requireNonNull(webServer, "webServer must not be null");
    //
    this.webServer = webServer;
  }

  public Application(){webServer = null;};


  //
  // Private methods
  //

  private void initialize() {
    LOG.config("WebCheckers is initializing.");

    // configure Spark and startup the Jetty web server
    webServer.initialize();

    // other applications might have additional services to configure

    LOG.config("WebCheckers initialization complete.");
  }

}
