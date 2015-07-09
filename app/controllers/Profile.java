package controllers;

import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.profile;

import java.util.List;

public class Profile extends Controller {

    public static Result index() {

        User user = User.findByEmail(session().get("email"));
        List<models.BonMot> mots = models.BonMot.getLatestForUser(user, 0, 25);

        return ok(profile.render(user.username, true, mots));

    }

    public static Result user(String username) {

        User user = User.findByUsername(username);
        List<models.BonMot> mots = models.BonMot.getLatestForUser(user, 0, 25);

        return ok(profile.render(user.username, false, mots));

    }


}
