package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import hubs.BonMotHub;
import hubs.BonMotHubListener;

/**
 * Created by jnagy on 30/09/15.
 */
public class LiveSearchResultsActor extends UntypedActor {

    /**
     * Implementation of BonMotHubListener
     */
    private BonMotHubListener listener;

    /**
     * Search token
     */
    private final String token;

    /**
     * Actor reference
     */
    private final ActorRef out;

    /**
     * Static property initializer
     *
     * @param token
     * @param out
     * @return
     */
    public static Props props(String token, ActorRef out) {
        return Props.create(LiveSearchResultsActor.class, token, out);
    }

    /**
     * Live Search Results Actor Constructor
     *
     * Initializes a new live search results actor for the given search token.
     *
     * NOTE: Do not call the constructor directly, Akka will initialize accordingly.
     *
     * @param token
     * @param out
     */
    public LiveSearchResultsActor(String token, ActorRef out) {

        this.token = token;
        this.out = out;

        this.listener = (e) -> out.tell(e, self());

        BonMotHub.getInstance().addListener(this.listener);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        // ignored
    }

    @Override
    public void postStop() throws Exception {

        BonMotHub.getInstance().removeListener(this.listener);

    }
}
