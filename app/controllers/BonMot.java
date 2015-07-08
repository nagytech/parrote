package controllers;

import models.Pith;
import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.regex.Matcher;

import static play.data.Form.form;

public class BonMot extends Controller {

    public static Result create() {

        Form<models.BonMot> motForm = form(models.BonMot.class).bindFromRequest();

        models.BonMot postMot = motForm.get();
        postMot.clean();

        models.BonMot newMot = new models.BonMot();

        newMot.user = User.findByEmail(session().get("email"));
        newMot.text = postMot.text;

        Logger.debug("Request to create new bonmot with text [{}] from user [{}]", newMot.text, newMot.user);

        // TODO: Don't block client, use promise to return then process piths
        newMot.save();

        Matcher m = newMot.matchPiths();
        while (m.find()) {
            Pith pith = Pith.findOrCreate(m.group(1).toLowerCase());
            pith.bonMots.add(newMot);
            pith.save();
            Logger.debug("Pith [{}] attached to bonmot id [{}]", pith, newMot.id);
        }

        Logger.debug("New bonmot with id [{}] created for user [{}]", newMot.id, newMot.user);

        return redirect(routes.Application.index());

    }

}
