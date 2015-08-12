package security;

import controllers.routes;
import models.Session;
import models.User;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import services.SessionStateService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Authenticator Class : Extends Security.Authenticator
 */
public class Authenticator extends Security.Authenticator {

    /**
     * Get the username from the given context
     *
     * @param context
     * @return UserName (in this case, our user's email - not their actual username)
     */
    @Override
    public String getUsername(Context context) {

        Session session = SessionStateService.Current();
        if (session != null && session.user != null)
            return session.user.email;
        return null;

    }

    /**
     * Handle the event where the user is unauthorized to view
     * a page
     *
     * @param context
     * @return redirect to the login page
     */
    @Override
    public Result onUnauthorized(Context context) {

        return Results.redirect(routes.Secure.login());

    }
}
