package controllers;

import actors.LiveSearchResultsActor;
import play.mvc.Controller;
import play.mvc.WebSocket;

/**
 * Controller - Live Search
 *
 * Handles the websocket connections for a live search from the single page application
 */
public class LiveSearch extends Controller {

    /**
     * Connect and open a new WebSocket with the client
     * @param q
     * @return
     */
    public static WebSocket<String> connect(String q) {

        // Let Akka create the actor and return the WebSocket instance
        return WebSocket.<String>withActor((out) -> LiveSearchResultsActor.props(q, out));

    }

}
