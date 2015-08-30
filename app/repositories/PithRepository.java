package repositories;

import com.mongodb.BasicDBObject;
import models.BonMot;
import models.Pith;
import models.User;
import play.Logger;

import java.util.List;

/**
 * Created by jnagy on 30/08/15.
 */
public class PithRepository extends RepositoryBase<BonMot> {

    public PithRepository() {
        super(Pith.class, "piths");
    }


}
