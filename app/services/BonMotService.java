package services;

import com.google.common.html.HtmlEscapers;
import com.mongodb.BasicDBObject;
import hubs.BonMotHub;
import models.BonMot;
import models.User;
import org.bson.types.ObjectId;
import play.Logger;
import repositories.BonMotRepository;

import javax.swing.text.html.HTML;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * BonMotService Utility
 * <p>
 * Facilitates BonMot logic operations
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
        newMot.userId = (ObjectId) user.get_id();
        newMot.text = HtmlEscapers.htmlEscaper().escape(text);
        newMot.username = user.username;
        newMot.piths = new ArrayList<>();

        // Iterate the piths and store each one
        Matcher m = newMot.matchPiths();
        while (m.find())
            newMot.piths.add(m.group(1).toLowerCase());

        // Insert into the repository
        bonMotRepository.insert(newMot);

        // Send the newMot to the BonMotHub for live search results update
        BonMotHub.getInstance().send(newMot);

        // Logging
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
     * - NOTE: username should not start with @
     *
     * @param user - user to search by
     * @return
     */
    public List<BonMot> getLatestForUser(String username) {

        List<BonMot> mots = bonMotRepository
                .find(new BasicDBObject("$query", new BasicDBObject("username", username))
                        .append("$orderby", new BasicDBObject("createdOn", -1)).toString());

        Logger.debug("Found [{}] bonmots for username [{}]", mots.size(), username);

        return mots;
    }
}
