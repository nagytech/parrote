package factories;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import db.MongoConnection;
import play.Logger;

public class MongoConnectionFactory {

    public MongoConnection create() {

        Config conf = ConfigFactory.load();

        String name = conf.getString("app.mongodb.name");

        MongoClient mc = new MongoClient();
        DB db = mc.getDB(name);

        return new db.MongoConnection(db, mc);

    }

}
