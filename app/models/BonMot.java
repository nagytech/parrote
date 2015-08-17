package models;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import play.Logger;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BonMot model
 *
 * An instance of a user's message within the system
 */
@Entity
public class BonMot extends Audit {

    /**
     * Finder
     */
    static final Finder<Long, BonMot> find = new Finder<>(BonMot.class);

    /**
     * User association
     */
    @ManyToOne
    public User user;
    /**
     * Message
     */
    @Constraints.Required
    @Constraints.MaxLength(128)
    public String text;
    /**
     * Associated Piths
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    public List<Pith> piths = new ArrayList<>();

    /**
     * Find any Piths within the text using the regex \B#(\w\w+)
     *
     * @return Matches
     */
    public Matcher matchPiths() {
        final String r = "\\B#(\\w\\w+)";
        final Pattern p = Pattern.compile(r);
        Matcher m = p.matcher(text);
        return m;
    }

    /**
     * Get the latest bonmots in the system for any piths in the array
     * - returns all piths where no piths are given
     * @param page page number
     * @param pageSize page size
     * @param pithArray array of piths (sans '#' for each entry)
     * @return
     */
    public static List<BonMot> getLatest(int page, int pageSize, String[] pithArray) {

        // Build expression list to include pith query if pith search provided
        ExpressionList<BonMot> motsEl = find.where();
        String pithOut = "";
        if (pithArray != null && pithArray.length > 0) {
            motsEl = motsEl.in("piths.code", (Object[])pithArray);
            pithOut = String.join(", ", pithArray);
        }

        // Execute the expression query
        final List<BonMot> mots = motsEl.isNull("deleted")
                .orderBy("createdOn DESC")
                .findPagedList(page, pageSize)
                .getList();

        if (pithOut.length() > 0)
            Logger.debug("Found [{}] bonmots for piths [{}]", mots.size(), pithOut);
        else
            Logger.debug("Found [{}] bonmots", mots.size());

        return mots;

    }

    /**
     * Get the latest bonmots for the given user
     *
     * @param user user to search by
     * @param page page number
     * @param pageSize page size
     * @return
     */
    public static List<BonMot> getLatestForUser(final User user, int page, int pageSize) {

        // Build and execute expression to find all bonmots for user
        final List<BonMot> mots = find.where()
                .eq("user.id", user.id)
                .isNull("deleted")
                .orderBy("createdOn DESC")
                .findPagedList(page, pageSize)
                .getList();

        Logger.debug("Found [{}] bonmots for user [{}]", mots.size(), user);

        return mots;
    }

}
