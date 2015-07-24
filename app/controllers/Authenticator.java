package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

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
        return context.session().get("email");
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
        return redirect(routes.Secure.login());
    }
}
