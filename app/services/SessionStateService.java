package services;

import com.avaje.ebean.Model;
import models.Session;
import models.User;
import org.joda.time.DateTime;
import play.Logger;
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

        Logger.debug("Persisted session to database: " + session.id.toString());

        // set in the cookie
        setCurrentUuid(session.id);

        // Return the unique id
        return session.id;

    }

    public static Session Current() {

        UUID uuid = getCurrentUuid();
        if (uuid != null)
        return FindSession(uuid);
        return null;
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

        Session session = find
                .where()
                .eq("id", uuid)
                .findUnique();

        return session;

    }

    /**
     * Get all users in the current session store
     * @return
     */
    public static java.util.List<Session> ListAll() {

        return find
                .where()
                .findList();

    }

    /**
     * Updates the last access time for the session
     */
    public static void UpdateCurrentSession() {

        Session session = Current();
        session.lastAccess = DateTime.now();
        session.update();

    }

    /**
     * Get the current session uuid from the current http context.
     * @return
     */
    private static UUID getCurrentUuid() {

        String uuid = Http.Context.current().session().get("id");
        if (uuid == null) return null;
        return UUID.fromString(uuid);

    }

    /**
     * Set the current session uuid from the given param
     * @param uuid
     */
    private static void setCurrentUuid(UUID uuid) {

        Http.Context.current().session().clear();
        System.out.println(uuid.toString());
        Http.Context.current().session().put("id", uuid.toString());

        Logger.debug("Set current session to: " + uuid.toString());

    }

}