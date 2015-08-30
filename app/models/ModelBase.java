package models;

import com.mongodb.ReflectionDBObject;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class ModelBase extends ReflectionDBObject {

    public Date createdOn;

    public ModelBase() {

        set_id(ObjectId.get());
        createdOn = DateTime.now().toDate();

    }

}
