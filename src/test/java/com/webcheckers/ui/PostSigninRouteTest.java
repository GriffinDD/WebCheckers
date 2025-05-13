package com.webcheckers.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.TemplateEngineTester;
import com.webcheckers.model.Player;
import com.webcheckers.ui.Attributes.PostSigninAttributes;
import com.webcheckers.ui.Attributes.GetSigninRouteAttributes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import spark.HaltException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;


@Tag("UI-tier")
public class PostSigninRouteTest {
    private static final String EXAMPLE_NAME = "Steve";
    private static final String BAD_NAME = "4_Steve";
    private static final String EXAMPLE_NAME2 = "4 Steve";
    private static final String BAD_NAME2= "  ";

    private PostSigninRoute CuT;

    // Friendly Objects
    private PlayerLobby playerLobby;
    // Mock Objects for untested classes
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;

    /**
     * Players themselves will have to be made in the individual tests, since there can be a bad name.
     * currentUser is the session attribute that will return this.
     */
    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        // Sessions are always created as the following:
        when(request.session()).thenReturn(session);
        engine = mock(TemplateEngine.class);
        

        // Creating a new player lobby to inject
        playerLobby = new PlayerLobby();
        when(session.attribute("playerlobby")).thenReturn(playerLobby);

        CuT = new PostSigninRoute(engine, playerLobby);
    }

    /**
     * Test the constructor.
     */
    @Test
    public void create_signin_route(){
        PostSigninRoute postSigninRoute = new PostSigninRoute(engine, playerLobby);
        assertNotNull(postSigninRoute);
    }

    /**
     * Testing that when a player name is invalid because it has a non-alphanumeric character, it is detected, and handled properly.
     */
    @Test
    public void bad_player_name_nonalphanumeric(){
        Player player = new Player(BAD_NAME);
        when(session.attribute("currentUser")).thenReturn(player);
        when(request.queryParams(PostSigninAttributes.PLAYER_NAME)).thenReturn(player.getName());
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invocation
        CuT.handle(request, response);

        //Analyze
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("signin.ftl");

        // If the name is invalid, the following should be the case:
        testHelper.assertViewModelAttribute("message1", GetSigninRouteAttributes.PLAYER_WELCOME_MESSAGE);
        testHelper.assertViewModelAttribute("message", PostSigninAttributes.INVALID_NAME);
        testHelper.assertViewModelAttribute("title", "Choose a valid name!");
        testHelper.assertViewModelAttribute("login", PostSigninAttributes.PLAYER_LOGIN_PROMPT);

        assert(playerLobby.getNumberOfPlayers() == 0);
    }

    /**
     * Testing that when a player name is invalid because it has only spaces, it is detected, and handled properly.
     */
    @Test
    public void bad_player_name_empty(){
        Player player = new Player(BAD_NAME2);
        when(session.attribute("currentUser")).thenReturn(player);
        when(request.queryParams(PostSigninAttributes.PLAYER_NAME)).thenReturn(player.getName());
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invocation
        CuT.handle(request, response);

        //Analyze
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("signin.ftl");

        // If the name is invalid, the following should be the case:
        testHelper.assertViewModelAttribute("message1", GetSigninRouteAttributes.PLAYER_WELCOME_MESSAGE);
        testHelper.assertViewModelAttribute("message", PostSigninAttributes.INVALID_NAME);
        testHelper.assertViewModelAttribute("title", "Choose a valid name!");
        testHelper.assertViewModelAttribute("login", PostSigninAttributes.PLAYER_LOGIN_PROMPT);

        assert(playerLobby.getNumberOfPlayers() == 0);
    }

    /**
     * Testing the success case function for a single alphabet only name
     */
    @Test
    public void good_player_name(){
        Player player = new Player(EXAMPLE_NAME);
        when(session.attribute("currentUser")).thenReturn(player);
        when(request.queryParams(PostSigninAttributes.PLAYER_NAME)).thenReturn(player.getName());
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //
        Response response = mock(Response.class);
        
        // Invocation
        // It should halt since it redirects.
        try {
            CuT.handle(request, response);
            fail("Redirects invoke halt exceptions.");
          } catch (HaltException e) {
            // expected
          }

        //Analyze
        testHelper.assertViewModelDoesNotExist();
    }

    /**
     * Testing the success case function for a name with numbers and spaces
     */
    @Test
    public void good_player_name_with_space_and_numbers(){
        Player player = new Player(EXAMPLE_NAME2);
        when(session.attribute("currentUser")).thenReturn(player);
        when(request.queryParams(PostSigninAttributes.PLAYER_NAME)).thenReturn(player.getName());
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //
        Response response = mock(Response.class);

        // Invocation
        // It should halt since it redirects.
        try {
            CuT.handle(request, response);
            fail("Redirects invoke halt exceptions.");
        } catch (HaltException e) {
            // expected
        }

        //Analyze
        testHelper.assertViewModelDoesNotExist();
    }

    /**
     * Testing the fail case where a name is already reserved
     */
    @Test
    public void duplicate_player_name(){
        // The player is already in the lobby
        playerLobby.addPlayerName(EXAMPLE_NAME);

        Player player = new Player(EXAMPLE_NAME);
        when(session.attribute("currentUser")).thenReturn(player);
        when(request.queryParams(PostSigninAttributes.PLAYER_NAME)).thenReturn(player.getName());
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invocation
        CuT.handle(request, response);

        //Analyze
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("signin.ftl");

        // If the name is invalid, the following should be the case:
        testHelper.assertViewModelAttribute("message1", GetSigninRouteAttributes.PLAYER_WELCOME_MESSAGE);
        testHelper.assertViewModelAttribute("message", PostSigninAttributes.NAME_ALREADY_EXISTS_MESSAGE);
        testHelper.assertViewModelAttribute("title", "That name is taken!");
        testHelper.assertViewModelAttribute("login", PostSigninAttributes.PLAYER_LOGIN_PROMPT);

        assert(playerLobby.getNumberOfPlayers() == 1);
    }
}
