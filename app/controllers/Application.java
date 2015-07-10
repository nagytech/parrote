package controllers;

import actions.UnlogAction;
import play.Routes;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.index;

/**
 * Application Controller.
 * <p>
 * Main landing controller of the application
 */
@With(UnlogAction.class)
public class Application extends Controller {

    /**
     * GET: index action
     *
     * @return Latest bonmot listing for all users.
     */
    public static Result index() {

        // List the latest mots from all users
        java.util.List<models.BonMot> mots = models.BonMot.getLatest(0, 25, new String[]{});
        return ok(index.render(mots));

    }

    public static Result jsRoutes() {
        return ok(Routes
                .javascriptRouter("jsRoutes",
                        routes.javascript.Secure.authenticate(),
                        routes.javascript.Secure.login(),
                        routes.javascript.Secure.register(),
                        routes.javascript.Secure.signup()
                ));
    }
}
