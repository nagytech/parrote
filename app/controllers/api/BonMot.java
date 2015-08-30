package controllers.api;

import repositories.BonMotRepository;
import repositories.UserRepository;
import security.Authenticator;
import controllers.BaseController;
import models.User;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import services.BonMotService;
import services.PithService;
import services.SessionStateService;
import services.UserService;

import static play.data.Form.form;

/**
 * BonMot API Controller
 * <p>
 * Controller for managing BonMots
 */
@Security.Authenticated(Authenticator.class)
public class BonMot extends BaseController {

    /**
     * POST: Create action (API Based)
     *
     * @return Json object of the new bonmot
     */
    public static Result create(String text) {

        BonMotService service = new BonMotService();

        // Check the text is valid
        if (text == null || text.length() > 129)
            return badRequest("Invalid text length.  Text must be less than 129 characters.");

        // Prepare data for the bonmot service
        User user = getUser();

        // Create new bonmot
        models.BonMot mot = service.create(user, text);

        // Redirect to main page
        return ok(Json.toJson(mot));

    }

    public static Result byPith(String tag) {

        BonMotService bonMotService = new BonMotService();
        java.util.List<models.BonMot> bonMots =  bonMotService.getLatest(0, 25, tag);
        return ok(Json.prettyPrint(Json.toJson(bonMots)));
    }

    public static Result byUser(String username) {

        UserService userService = new UserService();

        User user = userService.findByUsername(username);
        if (user == null)
            return notFound();

        BonMotService bonMotService = new BonMotService();
        java.util.List<models.BonMot> bonMots = bonMotService.getLatestForUser(user.get_id(), 0, 25);
        return ok(Json.prettyPrint(Json.toJson(bonMots)));

    }

}
