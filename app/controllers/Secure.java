package controllers;

import models.Session;
import models.User;
import org.joda.time.DateTime;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import security.Authenticator;
import services.SessionStateService;
import viewmodels.Login;
import viewmodels.Signup;
import views.html.admin;
import views.html.login;
import views.html.signup;

import java.util.*;

import static play.data.Form.form;

/**
 * Secure Controller
 * <p>
 * Controller for facilitating user registration and authentication.
 */
public class Secure extends BaseController {

    /**
     * GET: Admin action
     * <p>
     * Restricted to Admin user only.
     *
     * @return render user management page
     */
    @Security.Authenticated(Authenticator.class)
    public static Result admin() {

        // If the current user is not an admin, redirect
        if (!SessionStateService.Current().user.admin)
            redirect(routes.Application.index());

        // Get the current user set
        List<User> users = User.findAll();
        return ok(admin.render(users));

    }

    /**
     * POST: Ban action
     * <p>
     * - Allows the admin user to instantly ban another user
     * - Once banned, the other user will no longer be able to
     * log in.  Their session is terminated.
     * <p>
     * Restricted to Admin user only.
     *
     * @param email
     * @return
     */
    @Security.Authenticated(Authenticator.class)
    public static Result ban(String email) {

        // If the current user is not an admin, redirect
        if (!SessionStateService.Current().user.admin)
            redirect(routes.Application.index());

        // Don't allow banning of an admin
        User user = User.findByEmail(email);
        if (user.admin)
            redirect(routes.Secure.admin());

        // Ban the user
        user.banned = true;
        user.save();

        // post / get
        return redirect(routes.Secure.admin());

    }


    /**
     * Expire the user's session as identified by their email address
     *
     * @param email
     * @return
     */
    public static Result expire(String uuid) {

        // Expire the given session
        SessionStateService.ExpireSession(UUID.fromString(uuid));

        return redirect(routes.Secure.admin());

    }


    /**
     * POST: Authenticate action
     * <p>
     * Attempt to authenticate the user based on the POST data
     *
     * @return redirect to main page on success, bad request revert to form on failure
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

        // Get the user and update their last action
        User user = User.findByEmail(postLogin.email);

        System.out.println("user: " + user.email);

        // Clear any previous session and add current user's email to new session.
        SessionStateService.CreateSession(user);

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

        // redirect if already logged in
        if (SessionStateService.Current() != null)
            return redirect(routes.Application.index());

        // Render login form
        return ok(login.render(form(Login.class)));

    }

    /**
     * POST: Logout action.
     * <p>
     * Handles POST to log current user out
     *
     * @return redirect to main page
     */
    @Security.Authenticated(Authenticator.class)
    public static Result logout() {

        User user = SessionStateService.Current().user;

        if (user == null)
            // Redirect to main page
            return redirect(routes.Application.index());

        SessionStateService.ExpireCurrentSession();

        return redirect(routes.Application.index());

    }

    /**
     * POST: Register action.
     * <p>
     * Handles POST to register user
     *
     * @return redirect to login form on success, bad request revert to form on failure
     */
    public static Result register() {

        // Bind from form request
        Form<Signup> signupForm = form(Signup.class).bindFromRequest();
        if (signupForm.hasErrors()) {
            // Send bad request with form data if invalid
            return badRequest(signup.render(signupForm));
        }

        // Get data from form
        Signup postSignup = signupForm.get();

        // Try to register the user
        if (!User.register(postSignup.email, postSignup.username, postSignup.password)) {
            // If the registration failed, return bad request
            signupForm.data().put("password", "");
            signupForm.reject(Messages.get("signup.error"));
            return badRequest(signup.render(signupForm));
        }

        // Success, redirect to login page
        return redirect(routes.Secure.login());

    }

    /**
     * GET: Signup action
     * <p>
     * Handles GET request for signup form
     *
     * @return render signup form
     */
    public static Result signup() {

        // Render signup form
        return ok(signup.render(form(Signup.class)));

    }

}
