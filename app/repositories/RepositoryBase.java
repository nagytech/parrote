package repositories;

import db.MongoConnection;
import factories.MongoConnectionFactory;
import models.ModelBase;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Base repository class that uses generics to facilitate basic CRUD ops
 * @param <T> generic
 */
public class RepositoryBase<T extends ModelBase> {

    /**
     *  Class type from generic
     */
    private final Class clazz;
    /**
     * Collection name to be used in mongo.  ( ie. db.[collectionName].find({..}) )
     */
    private final String collectionName;
    /**
     * Mongo connection from factory.
     */
    protected final MongoConnection con;

    /**
     * Repository base class
     * @param c class type from generic
     * @param name name of collection for mongo.
     */
    public RepositoryBase(Class c, String name) {

        // get connection from factory.
        this.con = new MongoConnectionFactory().create();

        clazz = c;
        collectionName = name;

    }

    /**
     * List all entities
     *
     * @return list of T entities
     */
    public List<T> all() {

        List<T> list = new ArrayList<>();

        MongoCursor<T> cursor = getCollection().find("{$query: {}, $orderBy: { username: 1 } }").as(clazz);
        while (cursor.hasNext())
            list.add(cursor.next());

        return list;

    }

    /**
     * List entities that match query
     *
     * @param query - mongo json typed query
     * @return
     */
    public List<T> find(String query) {

        List<T> list = new ArrayList<>();

        MongoCursor<T> cursor = getCollection().find(query).as(clazz);
        while (cursor.hasNext())
            list.add(cursor.next());

        return list;

    }

    /**
     * Find the single entity that matches the query
     *
     * @param query
     * @return
     */
    public T findOne(String query) {

        return (T) getCollection().findOne(query).as(clazz);

    }

    /**
     * Find the single entity by object id (_id)
     *
     * @param objId _id object id.
     * @return
     */
    public T findById(ObjectId objId) {

        return (T) getCollection().findOne(objId).as(clazz);

    }

    /**
     * Insert new entity record
     *
     * @param entity
     * @return string value of _id
     */
    public String insert(T entity) {

        getCollection().insert(entity);
        return String.valueOf(entity.get_id());

    }

    /**
     * Remove entity
     *
     * @param entity entity to remove
     */
    public void remove(T entity) {

        getCollection().remove(new ObjectId(entity.get_id().toString()));

    }

    /**
     * Update entity with query
     *
     * @param query query to get object
     * @param obj query object to update (note: you do not need to include $set)
     */
    public void update(String query, String obj) {

        getCollection().update(query).with(String.format("{$set:%s}", obj));

    }

    /**
     * Perform an index query (should be a once off on app start)
     *
     * @param query
     */
    protected void doIndex(String query) {

        getCollection().ensureIndex(query);

    }

    /**
     * Gets the jongo based collection by name
     *
     * @return
     */
    private org.jongo.MongoCollection getCollection() {

        //noinspection deprecation
        Jongo j = new Jongo(con.getDb());
        return j.getCollection(collectionName);

    }

}
