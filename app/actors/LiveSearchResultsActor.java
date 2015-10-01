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

        BonMotService bmsvc = new BonMotService();
        List<BonMot> mots;

        // Check for a user based search
        if (token.startsWith("@")) {
            // Return latest for user
            mots = bmsvc.getLatestForUser(token.substring(1));
        } else {
            // Default to a pith based search
            String subtoken = token.charAt(0) == '#' ? token.substring(1) : token;
            mots = bmsvc.getLatestForTag(subtoken);
        }

        // Send the immediate search results as a list
        out.tell(Json.toJson(mots).toString(), self());

        // Prepare delegate for live updates
        this.listener = (e) -> {
            boolean canReturn = false;
            // Handle a user based submission
            if (token.charAt(0) == '@') {
                // Check the user matches the token
                if (e.username.equals(token.substring(1))) {
                    canReturn = true;
                }
            // Otherwise, default to a pith based search
            } else if (e.piths != null && !e.piths.isEmpty()) {
                // Check the prefix and resolve to a normalized search
                String subToken = token.charAt(0) == '#' ? token.substring(1) : token;
                // check the piths contain the token
                if (e.piths.contains(subToken)) {
                    canReturn = true;
                }
            }

            // Return the JSON representation of the bonmot post to the current websocket client
            if (canReturn) out.tell(e.toJSONString(), self());

        };

        // Add listener delegate to the hub
        BonMotHub.getInstance().addListener(this.listener);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        // ignored since we aren't sending anything from the client
    }

    @Override
    public void postStop() throws Exception {
        BonMotHub.getInstance().removeListener(this.listener);
    }
}
