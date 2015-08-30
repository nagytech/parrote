package db;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * To hold mongo connection objects.
 */
public class MongoConnection {

    /**
     * Database object
     * --
     * Uses deprecated class, but Jongo does not support
     * the most latest revision.
     */
    DB db;
    /**
     * Mongo client for connection to mongod instance
     */
    MongoClient client;

    /**
     * Mongo connection
     *
     * @param db database
     * @param client client
     */
    public MongoConnection(DB db, MongoClient client) {
        this.db = db;
        this.client = client;
    }

    /**
     * Get the database instance
     * @return
     */
    public DB getDb() {
        return db;
    }

    @Override
    protected void finalize() throws Throwable {
        if (client != null)
            try {
                client.close();
            } catch (Exception ex) {

            }
    }
}
