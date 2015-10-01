package models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * User model
 * <p>
 * Represents a user who has registered in the system
 */
public class User extends ModelBase {

    /**
     * Username (must be unique)
     * <p>
     * NOTE: Stored without the @ prefix
     */
    public String username;
    /**
     * Email address (must be unique)
     */
    public String email;
    /**
     * User's password (Stored encrypted)
     */
    public String password;
    /**
     * Flag to denote if the user is an administrator
     */
    public boolean admin;
    /**
     * Flag to denote if the user is banned (true) or not (false)
     */
    public boolean banned;
    /**
     * Sessions for the user - non-normalized
     */
    public List<UUID> sessions = new ArrayList<>();

    /**
     * Format username with @ prefix.
     *
     * @return username with @ prefix
     */
    @Override
    public String toString() {
        return String.format("@%s <%s>", username, email);
    }


}
