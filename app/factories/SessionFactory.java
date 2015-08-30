package factories;

import models.Session;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Created by jnagy on 29/08/15.
 */
public class SessionFactory {

    public Session create(String userId) {

        Session session = new Session();

        session.sessionId = UUID.randomUUID().toString();
        session.userId = userId;
        session.lastAccess = DateTime.now().toDate();

        return session;

    }

}