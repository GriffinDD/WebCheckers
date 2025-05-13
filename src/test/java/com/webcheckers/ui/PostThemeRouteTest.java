package com.webcheckers.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.webcheckers.TemplateEngineTester;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import spark.HaltException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

@Tag("UI-Tier")
public class PostThemeRouteTest {
    private PostThemeRoute CuT;

    private Request request;
    private Session session;
    private TemplateEngine engine;
    private Response response;

    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        engine =mock(TemplateEngine.class);
        response = mock(Response.class);

        CuT = new PostThemeRoute(engine);
    }
    /**
     * Test the constructor.
     */
    @Test
    public void create_posttheme_route(){
        PostThemeRoute p = new PostThemeRoute(engine);
        assertNotNull(p);
    }

    /**
     * Test that the route functions on its only use case
     */
    @Test
    public void full_run() {
        for (int x = 1; x < 6; x++) {
            String tester = String.valueOf(x);
            final TemplateEngineTester testHelper = new TemplateEngineTester();
            when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
            when(request.queryParams("theme")).thenReturn(tester);

            //
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
    }

}
