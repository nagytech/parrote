package controllers;

import models.User;
import play.data.Form;
import play.data.validation.Constraints;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;
import views.html.signup;

import static play.data.Form.form;

/**
 * Secure Controller
 * <p>
 * Controller for facilitating user registration and authentication.
 */
public class Secure extends Controller {

    /**
     * Login object
     * <p>
     * Viewmodel for login and authentication
     */
    public static class Login {


        /**
         * User's email address
         */
        @Constraints.Required
        @Constraints.Email
        public String email;

        /**
         * User's plain text password
         */
        @Constraints.Required
        public String password;

        /**
         * Model validation used by form binding
         *
         * @returns error message if invalid, or null if valid
         */
        @SuppressWarnings("unused")
        public String validate() {

            // Try to authenticate the user
            if (!User.authenticate(email, password)) {
                // Clean password
                password = "";
                return Messages.get("login.invalid");
            }
            return null;

        }
    }

    /**
     * Signup object
     * <p>
     * Viewmodel for signup and registration
     */
    public static class Signup extends Login {

        /**
         * User's chosen username
         */
        @Constraints.Required
        @Constraints.MaxLength(24)
        public String username;

        /**
         * Password match field
         */
        @Constraints.Required
        public String passwordMatch;

        /**
         * Model validation used by form binding
         *
         * @return error message if invalid, or null if valid
         */
        @Override
        public String validate() {

            // Check that the passwords match
            if (!password.equals(passwordMatch)) {
                return Messages.get("login.invalid");
            }

            // Check that the email does not already exist
            if (User.findByEmail(email) != null) {
                return Messages.get("signup.emailExists");
            }

            // Check that the username does not already exist
            if (User.findByUsername(username) != null) {
                return Messages.get("signup.usernameExists");
            }
            return null;
        }

    }

    /**
     * POST: Authenticate action
     * <p>
     * Attempt to authenticate the user based on the POST data
     *
     * @return redirect to main page on success, revert to form on failure
     */
    public static Result authenticate() {

        // Bind form from request and check for errors
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            // Return if form not valid
            return badRequest(login.render(loginForm));
        }

        // Get data from the post
        Login postLogin = loginForm.get();

        // Clear any previous session and add current user's email to new session.
        session().clear();
        session("email", postLogin.email);

        // Redirect to index
        return redirect(routes.Application.index());

    }

    /**
     * GET: Login action.
     * <p>
     * Handles GET to display login form
     *
     * @return rendered login form
     */
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
