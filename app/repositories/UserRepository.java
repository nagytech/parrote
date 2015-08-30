package repositories;

import com.mongodb.BasicDBObject;
import models.BonMot;
import models.User;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;

import java.util.List;
import java.util.UUID;

/**
 * Created by jnagy on 30/08/15.
 */
public class UserRepository extends RepositoryBase<User> {

    public UserRepository() {
        super(User.class, "users");
    }

    public void index() {
        doIndex("{email:1, username:1},{unique:true}");
    }
}
