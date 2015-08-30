package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * User model
 * <p>
 * Represents a user who has registered in the system
 */
@Entity
public class User extends Audit {

    /**
     * Finder
     */
    static final Model.Finder<Long, User> find = new Model.Finder<>(User.class);

    /**
     * Username (must be unique)
     * <p>
     * NOTE: Stored without the @ prefix
     */
    @Column(length = 64, unique = true)
    public String username;
    /**
     * Email address (must be unique)
     */
    @Constraints.Email
    @Column(unique = true)
    @JsonIgnore
    public String email;
    /**
     * User's password (Stored encrypted)
     */
    @JsonIgnore
    public String password;

    /**
     * Flag to denote if the user is an administrator
     */
    @JsonIgnore
    public boolean admin;
    /**
     * Flag to denote if the user is banned (true) or not (false)
     */
    @JsonIgnore
    public boolean banned;

    /**
     * Authenticate the user using JBCrypt against the plaintext password
     * to match the corresponding cypted entry in the datastore
     *
     * @param email    user's email
     * @param password entered password in plain text
     * @return true if authenticated, false otherwise
     */
    public static boolean authenticate(String email, String password) {

        // Find the user's entry in the data store
        User user = find.where()
                .eq("email", email)
                .isNull("deleted")
                .findUnique();

        if (user.banned) {
            Logger.debug("User [{}] is banned, refusing to authenticate", user.email);
            user = null;
        }

        try {
            // Check that the user exists and the password matches
            if (user == null || !BCrypt.checkpw(password, user.password)) {
                Logger.debug("User failed to authenticate: [{}]", email);
                return false;
            }
        } catch (Exception ex) {
            Logger.error("Failed to authenticate: ", ex);
        }

        Logger.info("User authenticated: {}", user.email);
        return true;

    }

    /**
     * Find the user by their email address
     *
     * @param email
     * @return user, or null if not exist
     */
    public static User findByEmail(String email) {

        User user = find.where()
                .eq("email", email)
                .findUnique();

        if (user != null) {
            Logger.debug("User found by email {}", email);
        }

        return user;
    }

    /**
     * Find the user by their username
     *
     * NOTE: Does not include the @ symbol.
     *
     * @param username
     * @return user, or null if not exist
     */
    public static User findByUsername(String username) {

        return find.where().eq("username", username).findUnique();

    }

    /**
     * Register a user with the given email, username and password
     *
     * @param email email (should be unique)
     * @param username username (should also be unique)
     * @param password user's password - no length / security limitations (will be crypted)
     *
     * @return true if registration complete, otherwise false
     */
    public static boolean register(String email, String username, String password) {

        // Create user object
        User user = new User();
        user.email = email;
        user.username = username;
        user.password = BCrypt.hashpw(password, BCrypt.gensalt());

        // For extracting a crypted password to put in initial-data.yml
        //Logger.info(user.password);

        try {
            // Attempt to save the record
            user.save();
        } catch (Exception ex) {
            // Exception most likely means validation logic failed.  Error message
            // should be generated in the controller since the database isn't always
            // clear on why inserts fail.
            Logger.error("Failed to register new user: [{}]", email);
            Logger.error("Exception:", ex);
            return false;
        }

        Logger.info("Registered new user: [{}]", email);
        return true;

    }

    public static User findById(UUID userId) {
        Logger.warn("REMOVE ME - TEMPORARY METHOD FOR REFACTOR PURPOSES");
        return User.find.where().eq("id", userId).findUnique();
    }

    /**
     * Format username with @ prefix.
     * @return username with @ prefix
     */
    @Override
    public String toString() {
        return String.format("@%s <%s>", username, email);
    }

    /**
     * Find all of the current users (including those who are banned.
     * @return list of users
     */
    public static List<User> findAll() {
        return User.find.orderBy("username").findList();
    }

}
