package controllers;

import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Search controller
 *
 * Used for searching and displaying search results
 */
public abstract class Search extends BaseController {

    /**
     * GET: Index action
     *
     * @param q Query string can either be a '#pith' or '@user'. It is
     *          neccessary to include the special character prefix. (ie. @/#)
     *
     * @return display of search results
     */
    public static Result index(String q) {

        List<models.BonMot> mots = new ArrayList<>();

//        // Check the query is not empty
//        if (q != null && !q.isEmpty()) {
//            // Check for prefix that denotes user search
//            if (q.startsWith("@")) {
//                // Check that the user exists
//                User user = User.findByUsername(q.substring(1, q.length()));
//                // If the user does not exist, then just render the empty list
//                if (user == null)
//                    return ok(search.render(q, true, mots, "User not found."));
//                // Get user's recent bonmots
//                return ok(search.render(q, true, models.BonMot.getLatestForUser(user, 0, 25), "No results."));
//            // Check for prefix that denotes pith search
//            } else if (q.startsWith("#")) {
//                // assume hash
//                return ok(search.render(q, true, models.BonMot.getLatest(0, 25, new String[]{q.substring(1, q.length())}), "Pith not found."));
//            } else {
//                return ok(search.render(q, true, mots, "Search not valid. Prefix with '@' for user search or '#' for pith search"));
//            }
//        }

        // Search could not be deemed as valid
        return ok(search.render(q, false, mots, ""));
    }
}
