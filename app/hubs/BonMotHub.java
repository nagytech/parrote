package hubs;

import models.BonMot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Hub for subscribing to new bonmots
 */
public class BonMotHub {

    /**
     * Internal set of listeners that are subscribed
     */
    List<BonMotHubListener> listeners;

    /**
     * Singleton instance
     */
    static final BonMotHub instance = new BonMotHub();

    /**
     * Access the singleton instance
     * @return
     */
    public static BonMotHub getInstance() {
        return instance;
    }

    /**
     * Initialize the hub
     */
    protected BonMotHub() {
        this.listeners = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Send a bonMot based event to all listeners
     * @param bonMot
     */
    public void send(BonMot bonMot) {

        // Iterate all listeners pass the bonmot for processing
        for (BonMotHubListener listener : listeners) {
            // Note: for large scale applications it could be argued that
            // a dictionary based pub/sub would work better.  If n number of users
            // searched a single topic, say #yolo, then they would be attached to a
            // dictionary entry for #yolo as opposed to having to check with each listener
            // to compare for all piths.  Something to think about anyway...
            listener.processBonMot(bonMot);
        }

    }

    /**
     * Add a listener to the hub
     * @param listener
     */
    public void addListener(BonMotHubListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Remove a listener from the hub by reference
     * @param listener
     */
    public void removeListener(BonMotHubListener listener) {
        this.listeners.remove(listener);
    }

}
