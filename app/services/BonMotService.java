package services;

import com.mongodb.BasicDBObject;
import models.BonMot;
import models.Pith;
import models.User;
import org.bson.types.ObjectId;
import play.Logger;
import repositories.BonMotRepository;

import java.util.List;
import java.util.regex.Matcher;

/**
 * BonMotService Utility
 * <p>
 * Facilitates BonMot CRUD
 */
public class BonMotService {

    BonMotRepository bonMotRepository;

    public BonMotService() {
        bonMotRepository = new BonMotRepository();
    }

    /**
     * Create method
     * <p>
     * Create a new bonmot for the given user and identify any piths
     *
     * @param user user account
     * @param text submitted text
     * @return new persisted bonmot
     */
    public BonMot create(User user, String text) {

        // Create a new bonmot and transform the data
        models.BonMot newMot = new models.BonMot();
        newMot.userId = (ObjectId)user.get_id();
        newMot.text = text;
        newMot.username = user.username;

        bonMotRepository.insert(newMot);

        Logger.debug("Created new bonmot with text [{}] from user [{}]", newMot.text, user.toString());

        // Iterate the piths and store each one
        Matcher m = newMot.matchPiths();
        Logger.warn("TODO");
//        while (m.find()) {
//            Pith pith = Pith.findOrCreate(m.group(1).toLowerCase());
//            pith.bonMots.add(newMot);
//            pith.save();
//            Logger.debug("Pith [{}] attached to bonmot id [{}]", pith, newMot.get_id());
//        }

        return newMot;

    }

    /**
     * Get the latest bonmots in the system for any piths in the array
     * - returns all piths where no piths are given
     * @param page page number
     * @param pageSize page size
     * @return
     */
    public List<BonMot> getLatest(int page, int pageSize, String tag) {

        //TODO: find relationship
        // TODO: Get tags
        // TODO: Handle null tag

        // Execute the expression query
        List<BonMot> mots = bonMotRepository.find("{}");

        return mots;

    }

    /**
     * Get the latest bonmots for the given user
     *
     * @param userId user to search by
     * @return
     */
    public List<BonMot> getLatestForUser(User user) {

        ObjectId userId = (ObjectId)user.get_id();

        List<BonMot> mots = bonMotRepository.find(new BasicDBObject("userId", userId).toString());

        Logger.debug("Found [{}] bonmots for userId [{}]", mots.size(), userId);

        return mots;
    }
}
