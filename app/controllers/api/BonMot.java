package controllers.api;

import controllers.BaseController;
import models.User;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import repositories.UnitOfWork;
import security.Authenticator;
import services.UserService;

import javax.inject.Inject;

/**
 * BonMot API Controller
 * <p>
 * Controller for managing BonMots
 */
@Security.Authenticated(Authenticator.class)
public class BonMot extends BaseController {

    @Inject
    public BonMot(UnitOfWork uow) {
        super(uow);
    }

    /**
     * POST: Create action (API Based)
     *
     * @return Json object of the new bonmot
     */
    public Result create() {

        String text = request().body().asFormUrlEncoded().get("message")[0];

        // Check the text is valid
        if (text == null || text.length() > 129)
            return badRequest("Invalid text length.  Text must be less than 129 characters.");

        // Prepare data for the bonmot service
        User user = getUser();

        // Create new bonmot
        models.BonMot mot = uow.getBonMotService().create(user, text);

        // Redirect to main page
        return ok(Json.toJson(mot));

    }

    public Result byPith(String tag) {

        java.util.List<models.BonMot> bonMots =  uow.getBonMotService().getLatestForTag(tag);
        return ok(Json.prettyPrint(Json.toJson(bonMots)));
    }

    public Result byUser(String username) {

        // Check that the user exists before returning result
        UserService userService = new UserService();
        if (userService.findByUsername(username) == null)
            return notFound();

        // Get the latest set of mots and return as Json
        java.util.List<models.BonMot> bonMots = uow.getBonMotService().getLatestForUser(username);
        return ok(Json.toJson(bonMots));

    }

}
