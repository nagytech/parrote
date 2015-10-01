package repositories;

import models.BonMot;

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
