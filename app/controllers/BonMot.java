package controllers;

import models.Session;
import models.User;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import security.Authenticator;
import services.BonMotService;
import services.SessionStateService;

import static play.data.Form.form;

/**
 * BonMot Controller
 * <p>
 * Controller for managing BonMots
 */
@Security.Authenticated(Authenticator.class)
public class BonMot extends BaseController {

    /**
     * POST: Create action
     *
     * @return redirect to the main page
     */
    public static Result create() {

        // Bind data from the request
        Form<models.BonMot> motForm = form(models.BonMot.class).bindFromRequest();
        if (motForm.hasErrors()) {
            return badRequest();
        }

        // Get data from the posted form
        models.BonMot postMot = motForm.get();

        // Prepare data for the bonmot service
        User user = getUser();
        String text = postMot.text;

        // Create new bonmot
        BonMotService service = new BonMotService();
        service.create(user, text);

        // Redirect to main page
        return redirect(routes.Profile.index());

    }

}
