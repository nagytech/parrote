package hubs;

import models.BonMot;

/**
 * Interface for listening to a new bonmot instance
 */
public interface BonMotHubListener {

    /**
     * To be implemented anonymously with J8 lambda delegate.
     * @param bonMot
     */
    void processBonMot(BonMot bonMot);

}
