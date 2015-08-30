package services;

import com.mongodb.BasicDBObject;
import factories.SessionFactory;
import models.Session;
import models.User;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import play.Logger;
import play.mvc.Http;
import repositories.SessionRepository;

/**
 * SessionState Service Utility
 * ---
 * Facilitates session state logic
 */
public class SessionStateService {

    SessionRepository sessionRepository;

    public SessionStateService() {
        sessionRepository = new SessionRepository();
    }

    /**
     * Persist a session user into the database
     *
     * @param user
     * @return
     */
    public String CreateSession(User user) {

        // create and store the session
        Session session = new SessionFactory().create((ObjectId)user.get_id());
        sessionRepository.insert(session);

        // set in the cookie
        Http.Context.current().session().clear();
        Http.Context.current().session().put("id", session.sessionId);

        // Return the unique id
        return session.sessionId;

    }

    /**
     * Get the session for the current http context
     *
     * @return
     */
    public Session Current() {

        // Get id from context
        String sessionId = getCurrentSessionId();

        // Return null if no session, or find the session and return it
        return sessionId == null ? null : findSessionBySessionId(sessionId);

    }

    /**
     * Session expiry
     */
    public void ExpireCurrentSession() {

        // Un-persist the current session
        String sessionId = getCurrentSessionId();
        ExpireSession(sessionId);

        // Clear the session data on the client side.
        Http.Context.current().session().clear();

    }

    /**
     * Expire a session so that it can no longer be utilized
     * - note: this forces any user with the session to be logged out
     *
     * @param sessionId
     */
    public void ExpireSession(String sessionId) {

        // Expire session by id
        Session session = findSessionBySessionId(sessionId);

        // Remove from database
        sessionRepository.remove(session);

    }

    /**
     * Updates the last access time for the session
     */
    public void UpdateCurrentSession() {

        // Get current session
        String sessionId = getCurrentSessionId();

        // Update last access with new date time
        updateLastAccess(sessionId);

    }

    /**
     * Find a single session based on sessionId
     *
     * @param sessionId
     * @return session from db
     */
    public Session findSessionBySessionId(String sessionId) {

        Session session = sessionRepository.findOne("{sessionId: '"+sessionId+"'}");
        return session;

    }

    /**
     * Update the stored session with a lastAccess of now.
     * @param sessionId
     */
    public void updateLastAccess(String sessionId) {

        // Update the session with a timestamp of now for lastAccess
        sessionRepository.update(String.format("{sessionId: '%s'}", sessionId)
                , new BasicDBObject("lastAccess", DateTime.now().toDate()).toString());

    }

    /**
     * Get the current session uuid from the current http context.
     *
     * @return
     */
    private String getCurrentSessionId() {

        // Get the current session id from http context
        String sessionId = Http.Context.current().session().get("id");
        if (sessionId == null) return null;
        return sessionId;

    }

}