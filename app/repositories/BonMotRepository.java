package repositories;

import com.avaje.ebean.ExpressionList;
import com.mongodb.BasicDBObject;
import models.BonMot;
import models.User;
import play.Logger;

import java.util.List;

/**
 * Repository for BonMot CRUD operations
 */
public class BonMotRepository extends RepositoryBase<BonMot> {

    /**
     * BonMotRepository
     */
    public BonMotRepository() {
        super(BonMot.class, "bonmots");
    }

}
