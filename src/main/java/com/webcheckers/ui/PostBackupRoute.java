package com.webcheckers.ui;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.model.MoveVerifier;
import com.webcheckers.util.Message;

import spark.Request;
import spark.Response;
import spark.Route;


/**
 * The UI Controller tied to the Ajax call to POST a move backup
 *
 * @author Joseph Casale
 */
public class PostBackupRoute implements Route{
    private static final Logger LOG = Logger.getLogger(PostBackupRoute.class.getName());
    private Gson gson;
    private MoveVerifier moveVerifier;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /backupMove} HTTP requests.
     *
     * @param gson
     *   gson object used to coordinate with the given JS scripts
     * @param moveVerifier
     *    the shared moveVerifier instance used to validate moves
     */
    public PostBackupRoute(Gson gson, MoveVerifier moveVerifier){
        this.gson = Objects.requireNonNull(gson, "Gson object needed");
        this.moveVerifier = Objects.requireNonNull(moveVerifier, "Move Verifier is needed");
        LOG.config("PostBackupRoute is initialized.");
    }
    /**
     * Undo the last move and update the gson
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the status message indicating an undone move
     *
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("PostBackupRoute is invoked.");
        moveVerifier.undoLastVerify();
        return gson.toJson(Message.info("Move undone"));
    }
    
}
