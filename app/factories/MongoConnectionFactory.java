package factories;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import db.MongoConnection;
import play.Logger;

import static play.libs.F.Option.Some;
import static play.libs.Scala.Option;

/**
 * Factory for initializing a MongoConnection
 */
public class MongoConnectionFactory {

    /**
     * Create a mongo connection using the 'app.mongodb.*' configuration settings.
     *
     * @return
     */
    public MongoConnection create() {

        // Load app config
        Config conf = ConfigFactory.load();

        // Get config settings
        String name = conf.getString("app.mongodb.name");
        String host = conf.getString("app.mongodb.host");
        int port = conf.getInt("app.mongodb.port");

        // Get connection and database
        MongoClient mc = new MongoClient(host, port);
        DB db = mc.getDB(name);

        // Return factoried object
        return new db.MongoConnection(db, mc);

    }

}
