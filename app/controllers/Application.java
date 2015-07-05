package controllers;

import models.*;

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

import java.util.ArrayList;
import java.util.List;

import static play.data.Form.form;

public class Application extends Controller {

    public static class Login {

        @Constraints.Required
        @Constraints.Email
        public String email;
        @Constraints.Required
        public String password;

        public String validate() {

            if (!User.authenticate(email, password)) {
                password = "";
                return Messages.get("login.invalid");
            }
            return null;
        }
    }

    public static class Signup extends Login{

        @Constraints.Required
        @Constraints.MaxLength(24)
        public String username;
        @Constraints.Required
        public String passwordMatch;

        public String validate() {
            if (!password.equals(passwordMatch)) {
                return Messages.get("login.invalid");
            }
            if (User.findByEmail(email) != null) {
                return Messages.get("signup.emailExists");
            }
            if (User.findByUsername(username) != null) {
                return Messages.get("signup.usernameExists");
            }
            return null;
        }

    }

    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        }
        Login postLogin = loginForm.get();
        session().clear();
        session("email", postLogin.email);
        return redirect(routes.Application.index());
    }

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
                        routes.javascript.Application.authenticate(),
                        routes.javascript.Application.login(),
                        routes.javascript.Application.register(),
                        routes.javascript.Application.signup()
                ));
    }

    public static Result login() {
        return ok(login.render(form(Login.class)));
    }

    public static Result logout() {
        session().clear();
        return redirect(routes.Application.index());
    }

    public static Result register() {
        Form<Signup> signupForm = form(Signup.class).bindFromRequest();
        if (signupForm.hasErrors()) {
            return badRequest(signup.render(signupForm));
        }
        Signup postSignup = signupForm.get();
        if (!User.register(postSignup.email, postSignup.username, postSignup.password)) {
            signupForm.data().put("password", "");
            signupForm.reject(Messages.get("signup.error"));
            return badRequest(signup.render(signupForm));
        }
        return redirect(routes.Application.login());
    }

    public static Result signup() {
        return ok(signup.render(form(Signup.class)));
    }
}
