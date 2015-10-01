package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import hubs.BonMotHub;
import hubs.BonMotHubListener;
import models.BonMot;
import play.libs.Json;
import services.BonMotService;

import java.util.List;

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

        // Get initial search results
        BonMotService bmsvc = new BonMotService();
        List<BonMot> mots;
        if (token.startsWith("@")) {
            mots = bmsvc.getLatestForUser(token.substring(1));
        } else {
            if (token.startsWith("#")) {
                mots = bmsvc.getLatestForTag(token.substring(1));
            } else {
                mots = bmsvc.getLatestForTag(token);
            }
        }

        // Send the immediate search results as a list
        out.tell(Json.toJson(mots).toString(), self());

        // Prepare delegate for live updates
        this.listener = (e) -> {
            boolean canReturn = false;
            if (token.charAt(0) == '@') {
                if (e.username.equals(token.substring(1))) {
                    canReturn = true;
                }
            } else {
                String subToken = token.charAt(0) == '#' ? token.substring(1) : token;
                if (e.piths.contains(subToken)) {
                    canReturn = true;
                }
            }

            if (canReturn) out.tell(e.toJSONString(), self());

        };

        // Add listener delegate to the hub
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
