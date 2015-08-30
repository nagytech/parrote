package actions;

import controllers.routes;
import models.Session;
import models.User;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import play.Logger;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.SessionStateService;
import services.UserService;

import javax.inject.Inject;

/**
 * UnlogAction - Action extension
 * -----------
 * To be used on a controller with the @With attribute.
 *
 * Implements an session clear policy where the user does not
 * exist, is banned, or has no activity in the last 6 hours.
 *
 * Also updates the user's last activity time stamp so that
 * we can identify the 6 hour timeout.
 */
public class UnlogAction extends Action.Simple {

    private SessionStateService sessionService;
    private UserService userService;

    @Inject
    public UnlogAction(SessionStateService sss, UserService us) {
        sessionService = sss;
        userService = us;
    }

    /**
     * Intercept the http context call / reponse and run the
     * unlog action.
     *
     * @param context
     * @return
     * @throws Throwable
     */
    @Override
    public F.Promise<Result> call(Http.Context context) throws Throwable {

        Session session = sessionService.Current();

        // If the user does not exist, but is logged in.
        if (session == null || session.userId == null)
            return delegate.call(context);

        User user = userService.findById(new ObjectId(session.userId));
        if (user.banned || new DateTime(session.lastAccess).plusHours(6).isBeforeNow()) {
            // Clear the session if user invalid, banned, or inactive for over 6 hours
            sessionService.ExpireCurrentSession();
            return F.Promise.pure(redirect(routes.Application.index()));
        } else {
            // Update user activity flag
            sessionService.UpdateCurrentSession();
        }

        context.current().args.put("user", user);
        context.current().args.put("session", session);

        return delegate.call(context);
    }

}