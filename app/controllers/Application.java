package controllers;

import models.User;
import play.Logger;
import play.Routes;
import play.data.Form;
import play.data.validation.Constraints;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.login;
import views.html.signup;

import java.util.List;

import static play.data.Form.form;

public class Application extends Controller {

    public static Result index() {
        if (session("email") != null) {
            User user = User.findByEmail(session("email"));
            Logger.debug("Checking for user: {}", session("email"));
            List<models.BonMot> mots = models.BonMot.getLatestForUser(user, 0, 25);
            return ok(index.render(form(models.BonMot.class), mots));
        }
        return ok(signup.render(form(Signup.class)));
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
