package controllers.api;

import controllers.Authenticator;
import controllers.routes;
import models.User;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.BonMotService;

import static play.data.Form.form;

/**
 * BonMot API Controller
 * <p>
 * Controller for managing BonMots
 */
@Security.Authenticated(Authenticator.class)
public class BonMot extends Controller {

    /**
     * POST: Create action (API Based)
     *
     * @return Json object of the new bonmot
     */
    public static Result create(String text) {

        // Check the text is valid
        if (text == null || text.length() > 129)
            return badRequest("Invalid text length.  Text must be less than 129 characters.");

        // Prepare data for the bonmot service
        User user = User.findByEmail(session().get("email"));

        // Create new bonmot
        models.BonMot mot = BonMotService.create(user, text);

        // Redirect to main page
        return ok(Json.toJson(mot));

    }

}
