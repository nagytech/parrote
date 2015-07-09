package controllers;

import models.User;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.BonMotService;

import static play.data.Form.form;

/**
 * BonMot Controller
 * <p>
 * Controller for managing BonMots
 */
@Security.Authenticated(Authenticator.class)
public class BonMot extends Controller {

    /**
     * POST: Create action
     *
     * @return redirect to the main page
     */
    public static Result create() {

        // Bind data from teh request
        Form<models.BonMot> motForm = form(models.BonMot.class).bindFromRequest();
        if (motForm.hasErrors()) {
            return badRequest(); // TODO: redo the form
        }

        // Get data from the posted form
        models.BonMot postMot = motForm.get();
        postMot.clean();

        // Prepare data for the bonmot service
        User user = User.findByEmail(session().get("email"));
        String text = postMot.text;

        // Create new bonmot
        BonMotService.create(user, text);

        // Redirect to main page
        return redirect(routes.Profile.index());

    }

}
