package services;

import com.mongodb.BasicDBObject;
import models.BonMot;
import models.Pith;
import models.User;
import org.bson.types.ObjectId;
import play.Logger;
import repositories.BonMotRepository;
import repositories.PithRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * BonMotService Utility
 * <p>
 * Facilitates BonMot CRUD
 */
public class BonMotService {

    BonMotRepository bonMotRepository;
    PithRepository pithRepository;

    public BonMotService() {
        bonMotRepository = new BonMotRepository();
        pithRepository = new PithRepository();
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
        newMot.userId = (ObjectId) user.get_id();
        newMot.text = text;
        newMot.username = user.username;
        newMot.piths = new ArrayList<>();

        // Iterate the piths and store each one
        Matcher m = newMot.matchPiths();
        while (m.find())
            newMot.piths.add(m.group(1).toLowerCase());

        bonMotRepository.insert(newMot);

        Logger.debug("Created new bonmot with text [{}] from user [{}]", newMot.text, user.toString());

        return newMot;

    }

    /**
     * Simply get the latest bonmots from the system - no filters.
     * @return
     */
    public List<BonMot> getLatest() {

        return bonMotRepository.find("{ $query: { }, $orderby: { createdOn: -1 } }");

    }

    /**
     * Get the latest bonmots in the system for any piths in the array
     * - returns all piths where no piths are given
     *
     * @return
     */
    public List<BonMot> getLatestForTag(String tag) {

        // Execute the expression query
        List<BonMot> mots = bonMotRepository.find(
                "{ $query: { piths: { $elemMatch: { $in: ['" + tag + "'] } } }, $orderby: { createdOn: -1 } }"
        );

        return mots;

    }

    /**
     * Get the latest bonmots for the given user
     *
     * @param user - user to search by
     * @return
     */
    public List<BonMot> getLatestForUser(User user) {

        ObjectId userId = (ObjectId) user.get_id();

        List<BonMot> mots = bonMotRepository
                .find(new BasicDBObject("$query", new BasicDBObject("userId", userId))
                        .append("$orderby", new BasicDBObject("createdOn", -1)).toString());

        Logger.debug("Found [{}] bonmots for userId [{}]", mots.size(), userId);

        return mots;
    }
}
