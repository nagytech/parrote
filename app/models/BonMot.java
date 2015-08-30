package models;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import org.bson.types.ObjectId;
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
public class BonMot extends ModelBase {

    /**
     * Text message
     */
    public String text;
    /**
     * User object _id
     */
    public ObjectId userId;
    /**
     * Username - (denormalized)
     */
    public String username;
    /**
     * Piths that were found via matchPiths method
     */
    public List<String> piths;

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

}
