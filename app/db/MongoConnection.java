package db;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {

    DB db;
    MongoClient client;

    public MongoConnection(DB db, MongoClient client) {
        this.db = db;
        this.client = client;
    }

    public DB getDb() {
        return db;

    }

}
