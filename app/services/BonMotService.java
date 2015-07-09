package services;

import models.BonMot;
import models.Pith;
import models.User;
import play.Logger;

import java.util.regex.Matcher;

/**
 * BonMotService Utility
 * <p>
 * Facilitates BonMot CRUD
 */
public class BonMotService {

    /**
     * Create method
     * <p>
     * Create a new bonmot for the given user and identify any piths
     *
     * @param user user account
     * @param text submitted text
     * @return new persisted bonmot
     */
    public static BonMot create(User user, String text) {

        // Create a new bonmot and transform the data
        models.BonMot newMot = new models.BonMot();
        newMot.user = user;
        newMot.text = text;
        newMot.save();
        Logger.debug("Created new bonmot with text [{}] from user [{}]", newMot.text, newMot.user);

        // Iterate the piths and store each one
        Matcher m = newMot.matchPiths();
        while (m.find()) {
            Pith pith = Pith.findOrCreate(m.group(1).toLowerCase());
            pith.bonMots.add(newMot);
            pith.save();
            Logger.debug("Pith [{}] attached to bonmot id [{}]", pith, newMot.id);
        }

        return newMot;

    }

}
