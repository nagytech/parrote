package repositories;

import com.mongodb.BasicDBObject;
import db.MongoConnection;
import factories.MongoConnectionFactory;
import models.ModelBase;
import org.bson.types.ObjectId;
import org.jongo.Jongo;

/**
 * Created by jnagy on 29/08/15.
 */
public class RepositoryBase<T extends ModelBase> {

    private final Class clazz;
    private final String collectionName;
    protected final MongoConnection con;

    public RepositoryBase(Class c, String name) {

        this.con = new MongoConnectionFactory().create();

        clazz = c;
        collectionName = name;

    }

    public T find(String query) {
        return (T) getCollection().findOne(query).as(clazz);
    }

    public T findById(ObjectId objId) {
        return (T) getCollection().findOne(objId).as(clazz);
    }

    public String insert(T entity) {

        getCollection().insert(entity);
        return String.valueOf(entity.get_id());

    }

    public void remove(T entity) {

        getCollection().remove(new ObjectId(entity.get_id().toString()));

    }

    public void update(String query, BasicDBObject obj) {
        getCollection().update(query).with(obj);
    }

    private org.jongo.MongoCollection getCollection() {

        //noinspection deprecation
        Jongo j = new Jongo(con.getDb());
        return j.getCollection(collectionName);

    }

}
