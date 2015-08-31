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

        UserService userService = new UserService();

        User user = userService.findByUsername(username);
        if (user == null)
            return notFound();

        java.util.List<models.BonMot> bonMots = uow.getBonMotService().getLatestForUser(user);
        return ok(Json.prettyPrint(Json.toJson(bonMots)));

    }

}
