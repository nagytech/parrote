package controllers;

import play.Routes;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by jnagy on 24/07/15.
 */
public class Reverse extends Controller {
    /**
     * Reverse routing for JavaScript
     * @return
     */
    public static Result jsRoutes() {
        return ok(Routes
                .javascriptRouter("jsRoutes",
                        routes.javascript.Secure.authenticate(),
                        routes.javascript.Secure.login(),
                        routes.javascript.Secure.register(),
                        routes.javascript.Secure.signup(),
                        routes.javascript.LiveSearch.connect()
                ));
    }
}
