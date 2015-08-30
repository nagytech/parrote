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
public class PithRepository extends RepositoryBase<Pith> {

    public PithRepository() {
        super(Pith.class, "piths");
    }

    public Pith findOrInsert(String code) {

        Pith pith;
        pith = findOne(new BasicDBObject("code", code).toString());

        if (pith == null)
        {
            pith = new Pith();
            pith.code = code;
            insert(pith);
        }

        return pith;
    }
}
