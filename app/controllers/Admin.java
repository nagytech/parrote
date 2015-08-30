package controllers;

import models.User;
import play.mvc.Result;
import play.mvc.Security;
import repositories.UnitOfWork;
import security.Authenticator;
import views.html.admin;

import javax.inject.Inject;
import java.util.List;

/**
 * Secure Controller
 * <p>
 * Controller for facilitating user registration and authentication.
 */
public class Admin extends BaseController {

    @Inject
    public Admin(UnitOfWork uow) {
        super(uow);
    }

    /**
     * GET: Admin action
     * <p>
     * Restricted to Admin user only.
     *
     * @return render user management page
     */
    @Security.Authenticated(Authenticator.class)
    public Result index() {

        // Redirect if user is not admin
        User user = getUser();
        if (user == null || !user.admin)
            return redirect(routes.Application.index());

        // Get the current user set
        List<User> users = uow.getUserService().all();
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
    public Result ban(String email) {

        // If the current user is not an admin, redirect
        if (!getUser().admin)
            return redirect(routes.Application.index());

        // Don't allow banning of an admin
        User user = uow.getUserService().findByEmail(email);
        if (user.admin)
            return redirect(routes.Admin.index());

        // Ban the user
        uow.getUserService().banUser(user);

        // post / get
        return redirect(routes.Admin.index());

    }

    /**
     * Expire the user's session as identified by their email address
     *
     * @param uuid
     * @return
     */
    public Result expire(String uuid) {

        // Expire the given session
        uow.getSessionStateService().ExpireSession(uuid);

        return redirect(routes.Admin.index());

    }


}
