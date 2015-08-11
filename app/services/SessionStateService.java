package services;

import com.avaje.ebean.Model;
import models.Session;
import models.User;
import org.joda.time.DateTime;
import play.mvc.Http;

import java.util.UUID;

/**
 * Created by jnagy on 11/08/15.
 */
public class SessionStateService {

    private static final Model.Finder<UUID, Session> find = new Model.Finder<>(Session.class);

    /**
     * Persist a session user into the database
     * @param user
     * @return
     */
    public static UUID CreateSession(User user) {

        // persist to the database
        Session session = new Session();
        session.user = user;
        session.lastAccess = DateTime.now();
        session.save();

        // set in the cookie
        setCurrentUuid(session.id);

        // Return the unique id
        return session.id;

    }

    public static Session Current() {

        UUID uuid = getCurrentUuid();
        return FindSession(uuid);

    }

    /**
     * Session expiry
     */
    public static void ExpireCurrentSession() {

        // Un-persist the current session
        UUID uuid = getCurrentUuid();
        SessionStateService.ExpireSession(uuid);

        // Clear the session data on the client side.
        Http.Context.current().session().clear();

    }

    /**
     * Expire a session so that it can no longer be utilized
     * - note: this forces any user with the session to be logged out
     *
     * @param uuid
     */
    public static void ExpireSession(UUID uuid) {

        // Expire session by id
        Session session = FindSession(uuid);
        session.delete();

    }

    /**
     * Find a single session based on uuid
     * @param uuid
     * @return
     */
    public static Session FindSession(UUID uuid) {

        return find
                .where()
                .eq("id", uuid)
                .ne("deleted", true)
                .findUnique();

    }

    /**
     * Get all users in the current session store
     * @return
     */
    public static java.util.List<Session> ListAll() {

        return find
                .where()
                .ne("deleted", true)
                .findList();

    }

    /**
     * Updates the last access time for the session
     */
    public static void UpdateCurrentSession() {

        Session session = Current();
        session.lastAccess = DateTime.now();
        session.save();

    }

    /**
     * Get the current session uuid from the current http context.
     * @return
     */
    private static UUID getCurrentUuid() {

        return UUID.fromString(Http.Context.current().session().get("_"));

    }

    /**
     * Set the current session uuid from the given param
     * @param uuid
     */
    private static void setCurrentUuid(UUID uuid) {

        Http.Context.current().session().clear();
        Http.Context.current().session().put("_", uuid.toString());

    }

}