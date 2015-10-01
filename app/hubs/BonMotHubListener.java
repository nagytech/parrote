package hubs;

import models.BonMot;

/**
 * Interface for listening to a new bonmot instance
 */
public interface BonMotHubListener {

    void processBonMot(BonMot bonMot);

}
