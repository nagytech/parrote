package controllers;

import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.profile;

import java.util.List;

/**
 * Profile controller
 */
public class Profile extends BaseController {

    /**
     *  GET: Index page
     *
     * @return Current logged in user's profle page with a list of recent bonmots
     */
    @Security.Authenticated(Authenticator.class)
    public static Result index() {

        User user = User.findByEmail(session().get("email"));
        List<models.BonMot> mots = models.BonMot.getLatestForUser(user, 0, 25);

        return ok(profile.render(user.username, true, mots));

    }

    /**
     * User's
     * @param username
     * @return
     */
    public static Result user(String username) {

        User user = User.findByUsername(username);
        List<models.BonMot> mots = models.BonMot.getLatestForUser(user, 0, 25);

        return ok(profile.render(user.username, false, mots));

    }


}
