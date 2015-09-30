package hubs;

import models.BonMot;

/**
 * Interface for listening to a new bonmot instance
 */
public interface BonMotHubListener {

    public void processBonMot(BonMot bonMot);

}
