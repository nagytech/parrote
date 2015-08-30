package models;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Session class - stores session information about a user
 * and their current session
 */
public class Session extends ModelBase {

    /**
     * Id for the session (UUID as string)
     */
    public String sessionId;
    /**
     * User Id
     */
    public ObjectId userId;
    /**
     * Username for session
     */
    public String username;
    /**
     * Last access for given user
     */
    public Date lastAccess;

    /**
     * Get the user's status based on the last action time
     * - Online: Active in last 10 minutes
     * - Idle: Active in the last 60 minutes
     * - Inactive: Active in the last 6 hours
     * - Offline: Not active in the last 6 hours
     *
     * @return status as per above
     */
    public String getStatus() {

        if (lastAccess != null) {

            DateTime date = new DateTime(lastAccess);
            if (date.plusMinutes(10).isAfterNow()) {
                return "Online";
            } else if (date.plusMinutes(60).isAfterNow()) {
                return "Idle";
            } else if (date.plusHours(6).isBeforeNow()) {
                return "Inactive";
            }

        }

        return "Offline";
    }

}
