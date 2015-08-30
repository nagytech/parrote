package repositories;

import com.avaje.ebean.ExpressionList;
import com.mongodb.BasicDBObject;
import models.BonMot;
import models.User;
import play.Logger;

import java.util.List;

/**
 * Created by jnagy on 30/08/15.
 */
public class BonMotRepository extends RepositoryBase<BonMot> {

    public BonMotRepository() {
        super(BonMot.class, "bonmots");
    }

}
