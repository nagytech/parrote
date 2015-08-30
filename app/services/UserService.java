package services;

import com.mongodb.BasicDBObject;
import models.User;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import repositories.UserRepository;

import java.util.List;

/**
 * Created by jnagy on 30/08/15.
 */
public class UserService {

    UserRepository userRepository;

    public UserService() {
        userRepository = new UserRepository();
    }

    /**
     * Authenticate the user using JBCrypt against the plaintext password
     * to match the corresponding cypted entry in the datastore
     *
     * @param email    user's email
     * @param password entered password in plain text
     * @return true if authenticated, false otherwise
     */
    public boolean authenticate(String email, String password) {

        // Find the user's entry in the data store
        User user = findByEmail(email);

        boolean authenticated = false;

        if (user != null) {

            if (user.banned) {

                Logger.debug("User [{}] is banned, refusing to authenticate", user.email);

            } else {

                try {
                    // Check that the user exists and the password matches
                    if (user == null || !BCrypt.checkpw(password, user.password)) {
                        Logger.debug("User failed to authenticate: [{}]", email);
                    } else {
                        authenticated = true;
                        Logger.info("User authenticated: {}", user.email);
                    }
                } catch (Exception ex) {
                    Logger.error("Failed to authenticate: ", ex);
                }

            }
        }

        return authenticated;

    }

    /**
     * Create new user
     *
     * @param user
     */
    public void create(User user) {

        userRepository.insert(user);
        Logger.info("User created: [{}]", user.toString());

    }

    /**
     * Find the user by their email address
     *
     * @param email
     * @return user, or null if not exist
     */
    public User findByEmail(String email) {
        return userRepository.findOne(new BasicDBObject("email", email).toString());
    }

    /**
     * Find the user by their username
     * <p>
     * NOTE: Does not include the @ symbol.
     *
     * @param username
     * @return user, or null if not exist
     */
    public User findByUsername(String username) {
        return userRepository.findOne(new BasicDBObject("username", username).toString());
    }

    /**
     * Find the user by objectId
     *
     * @param objId
     * @return
     */
    public User findById(ObjectId objId) {
        return userRepository.findById(objId);
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
    public boolean register(String email, String username, String password) {

        // Create user object
        User user = new User();
        user.email = email;
        user.username = username;
        user.password = BCrypt.hashpw(password, BCrypt.gensalt());

        // For extracting a crypted password to put in initial-data.yml
        //Logger.info(user.password);

        try {

            // Attempt to save the record
            userRepository.insert(user);

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

    public void index() {
        userRepository.index();
    }

    public void banUser(User user) {

        userRepository.update(String.format("{user: '%s'}", user.email), "{banned: true}");

    }

    public List<User> all() {

        // TODO: Order by...

        return userRepository.all();

    }
}
