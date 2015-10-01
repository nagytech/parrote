package controllers;

import models.User;
import play.mvc.Result;
import play.mvc.Security;
import repositories.UnitOfWork;
import security.Authenticator;

import javax.inject.Inject;

/**
 * BonMot Controller
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
     * POST: Create action
     *
     * @return redirect to the main page
     */
    public Result create() {

        // Prepare data for the bonmot service
        User user = getUser();

        String text = request().body().asFormUrlEncoded().get("text")[0].toString();

        // Create new bonmot
        uow.getBonMotService().create(user, text);

        // Redirect to main page
        return redirect(routes.Profile.index());

    }

}
