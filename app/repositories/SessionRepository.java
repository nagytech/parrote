package repositories;

import com.mongodb.BasicDBObject;
import models.Session;
import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Created by jnagy on 29/08/15.
 */
public class SessionRepository extends RepositoryBase<Session> {

    public SessionRepository() {
        super(Session.class, "sessions");
    }

    /**
     * Find a single session based on sessionId
     *
     * @param sessionId
     * @return session from db
     */
    public Session findSessionBySessionId(String sessionId) {

        Session session = find("{sessionId: '"+sessionId+"'}");
        return session;

    }

    /**
     * Update the stored session with a lastAccess of now.
     * @param sessionId
     */
    public void updateLastAccess(String sessionId) {

        update(String.format("{sessionId: '%s'}", sessionId), new BasicDBObject("lastAccess", DateTime.now().toDate()));

    }
}
