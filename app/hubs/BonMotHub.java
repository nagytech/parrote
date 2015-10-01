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

        for (BonMotHubListener listener : listeners) {
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
