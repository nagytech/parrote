package controllers;

import models.User;
import play.Routes;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.List;

public class Application extends Controller {

    public static Result index() {

        // List the latest mots from all users
        List<models.BonMot> mots = models.BonMot.getLatest(0, 25, new String[]{});
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
