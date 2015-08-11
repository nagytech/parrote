package viewmodels;


import models.User;
import org.joda.time.DateTime;

import java.util.Date;

public class Session {

    public User user;
    public Date lastAccess;

    /**
     * Get the user's status based on the last action time
     * - Banned: User has been banned
     * - Online: Active in last 10 minutes
     * - Idle: Active in the last 60 minutes
     * - Inactive: Active in the last 6 hours
     * - Offline: Not active in the last 6 hours
     *
     * @return status as per above
     */
    public String getStatus() {

        if (user.banned)
            return "Banned";

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
