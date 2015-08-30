package controllers;

import models.*;
import play.mvc.Result;
import play.mvc.Security;
import security.Authenticator;
import services.BonMotService;
import services.SessionStateService;
import services.UserService;
import views.html.profile;

import java.util.ArrayList;
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

        User user = getUser();
        BonMotService service = new BonMotService();
        List<models.BonMot> mots = service.getLatestForUser(user);

        return ok(profile.render(user.username, true, mots));

    }

    /**
     * GET: User Action
     *
     * Shows a specific user's profile.  If it is the current user, then
     * it renders the same page as their user profile.
     *
     * @param username
     * @return
     */
    public static Result user(String username) {

        // Check if the requested profile is the same as the logged in user's profile
        boolean ownProfile = false;
        // If null, render will display 'user not found'
        String nameCheck = null;
        List<models.BonMot> mots = new ArrayList<>();

        // Find the user's latest bonmots if they exist
        UserService userService = new UserService();
        User user = userService.findByUsername(username);
        if (user != null) {
            nameCheck = user.username;
            ownProfile = user.email.equalsIgnoreCase(session().get("email"));
            // TODO: Pagination
            BonMotService bonMotService = new BonMotService();
            mots = bonMotService.getLatestForUser(user);
        }

        return ok(profile.render(nameCheck, ownProfile, mots));

    }


}
