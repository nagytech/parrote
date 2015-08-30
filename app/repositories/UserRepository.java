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
 * Repository for User CRUD operations
 */
public class UserRepository extends RepositoryBase<User> {

    public UserRepository() {
        super(User.class, "users");
    }

    /**
     * Ensure the username and email columns are unique
     */
    public void index() {
        doIndex("{email:1, username:1},{unique:true}");
    }
}
