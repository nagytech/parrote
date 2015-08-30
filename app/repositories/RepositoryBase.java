package repositories;

import com.mongodb.BasicDBObject;
import db.MongoConnection;
import factories.MongoConnectionFactory;
import models.ModelBase;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCursor;
import play.Logger;

import java.util.ArrayList;
import java.util.List;

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

    public List<T> all() {
        List<T> list = new ArrayList<>();
        MongoCursor<T> cursor = getCollection().find().as(clazz);
        while (cursor.hasNext())
            list.add(cursor.next());
        return list;
    }

    public List<T> find(String query) {
        List<T> list = new ArrayList<>();
        MongoCursor<T> cursor = getCollection().find(query).as(clazz);
        while (cursor.hasNext())
            list.add(cursor.next());
        return list;
    }

    public T findOne(String query) {
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

    public void update(String query, String obj) {
        getCollection().update(query).with(String.format("{$set:%s}", obj));
    }

    protected void doIndex(String query) {
        getCollection().ensureIndex(query);
    }

    private org.jongo.MongoCollection getCollection() {

        //noinspection deprecation
        Jongo j = new Jongo(con.getDb());
        return j.getCollection(collectionName);

    }

}
