package repositories;

import com.mongodb.BasicDBObject;
import models.Session;
import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Repository for Session CRUD operations
 */
public class SessionRepository extends RepositoryBase<Session> {

    public SessionRepository() {
        super(Session.class, "sessions");
    }

    /**
     * Ensure that the sessionId is unique
     */
    public void index() {
        doIndex("{sessionId: 1},{unique: true}");
    }
}
