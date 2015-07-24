package controllers;

import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.search;

import java.util.ArrayList;
import java.util.List;

public class Search extends BaseController {

    public static Result index(String q) {

        List<models.BonMot> mots = new ArrayList<>();
        if (q == null) {
            return ok(search.render(q, false, mots));
        } else {

            if (q.startsWith("@")) {
                User user = User.findByUsername(q.substring(1, q.length()));
                if (user == null)
                    return ok(search.render(q, true, mots));
                if (user.email == request().username())
                    return redirect("/profile");
                return redirect("/user/" + user.username);
            } else if (q.startsWith("#")) {
                // assume hash
                return ok(search.render(q, true, models.BonMot.getLatest(0, 25, new String[] { q.substring(1, q.length()) })));
            } else {
                return ok(search.render(q, true, models.BonMot.getLatest(0, 25, new String[] {q})));
            }

        }

    }

}
