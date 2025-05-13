package com.webcheckers.ui;

import com.webcheckers.TemplateEngineTester;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;


import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

@Tag("UI-tier")
public class GetSigninRouteTest {

    private GetSigninRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;


    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        // create a unique CuT for each test
        CuT = new GetSigninRoute(engine);
    }

    /**
     * Test that the route functions on its only use case
     */
    @Test
    public void full_run() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("title", Attributes.GetSigninRouteAttributes.TITLE);
        testHelper.assertViewModelAttribute("message1", Attributes.GetSigninRouteAttributes.PLAYER_WELCOME_MESSAGE);
        testHelper.assertViewModelAttribute("message", null);
        testHelper.assertViewModelAttribute("login", Attributes.GetSigninRouteAttributes.PLAYER_LOGIN_PROMPT);
        testHelper.assertViewName("signin.ftl");
    }



}
