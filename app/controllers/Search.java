package controllers;

import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.search;

import java.util.ArrayList;
import java.util.List;

public class Search extends Controller {

    public static Result index(String text) {

        // TODO: clean text for post back
        // TODO: search

        List<models.BonMot> mots = new ArrayList<>();
        if (text == null) {
            return ok(search.render(text, false, mots));
        } else {

            if (text.startsWith("@")) {
                User user = User.findByUsername(text.substring(1, text.length()));
                if (user == null)
                    return ok(search.render(text, true, mots));
                return redirect("/user/" + user.username);
            } else {
                // assume hash
                // todo: filterhash.
                return ok(search.render(text, true, models.BonMot.getLatest(0, 25, new String[] { text })));
            }

        }

    }

}
