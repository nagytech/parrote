package factories;

import models.Session;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Session factory
 */
public class SessionFactory {

    /**
     * Create a new Session object with new sessionId and lastAccess of now.
     *
     * @param userId - primary _id of mongo user object
     * @return
     */
    public Session create(ObjectId userId) {

        Session session = new Session();

        session.sessionId = UUID.randomUUID().toString();
        session.userId = userId;
        session.lastAccess = DateTime.now().toDate();

        return session;

    }

}
