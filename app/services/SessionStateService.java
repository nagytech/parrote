package services;

import com.mongodb.BasicDBObject;
import factories.SessionFactory;
import models.Session;
import models.User;
import org.joda.time.DateTime;
import play.mvc.Http;
import repositories.SessionRepository;


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
        Session session = new SessionFactory().create(user.id);
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
        return sessionId == null ? null : sessionRepository.findSessionBySessionId(sessionId);

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
        Session session = sessionRepository.findSessionBySessionId(sessionId);

        // Remove from database
        sessionRepository.remove(session);

    }

    /**
     * Get all users in the current session store
     *
     * @return
     */
    public java.util.List<Session> ListAll() {

        return null;

    }

    /**
     * Updates the last access time for the session
     */
    public void UpdateCurrentSession() {

        // Get current session
        String sessionId = getCurrentSessionId();

        // Update last access with new date time
        sessionRepository.updateLastAccess(sessionId);

    }

    /**
     * Get the current session uuid from the current http context.
     *
     * @return
     */
    private String getCurrentSessionId() {

        String sessionId = Http.Context.current().session().get("id");
        if (sessionId == null) return null;
        return sessionId;

    }

}