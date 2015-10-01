package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.ReflectionDBObject;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import java.util.Date;

/**
 *  Base class for mongodb persistence
 */
@JsonIgnoreProperties({"_id", "partialObject"})
public class ModelBase extends ReflectionDBObject {

    // Date created
    public Date createdOn;

    /**
     * Model base
     */
    public ModelBase() {

        // Set the _id to the next available
        // Jongo should do this, but we just make sure..
        set_id(ObjectId.get());

        // Update created on date
        createdOn = DateTime.now().toDate();

    }

}
