package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

/**
 * Application Controller.
 * <p>
 * Main landing controller of the application
 */
public class Application extends BaseController {

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

}
