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
public class Admin extends BaseController {

    /**
     * GET: Admin action
     * <p>
     * Restricted to Admin user only.
     *
     * @return render user management page
     */
    @Security.Authenticated(Authenticator.class)
    public static Result index() {

        // Redirect if user is not admin
        User user = getUser();
        if (user == null || !user.admin)
            return redirect(routes.Application.index());

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
        if (!getUser().admin)
            return redirect(routes.Application.index());

        // Don't allow banning of an admin
        // TODO: Old way
        User user = User.findByEmail(email);
        if (user.admin)
            return redirect(routes.Admin.index());

        // Ban the user
        user.banned = true;
        user.save();

        // post / get
        return redirect(routes.Admin.index());

    }

    /**
     * Expire the user's session as identified by their email address
     *
     * @param uuid
     * @return
     */
    public static Result expire(String uuid) {

        // Expire the given session
        SessionStateService service = new SessionStateService();
        service.ExpireSession(uuid);

        return redirect(routes.Admin.index());

    }


}
