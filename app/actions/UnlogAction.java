package actions;

import controllers.routes;
import models.Session;
import models.User;
import org.joda.time.DateTime;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.SessionStateService;

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

        // If the user does not exist, but is logged in.
        Session session = SessionStateService.Current();
        if (session == null || session.user == null)
            return delegate.call(context);

        User user = session.user;
        if (user.banned || new DateTime(session.lastAccess).plusHours(6).isBeforeNow()) {
            // Clear the session if user invalid, banned, or inactive for over 6 hours
            SessionStateService.ExpireCurrentSession();
            return F.Promise.pure(redirect(routes.Application.index()));
        } else {
            // Update user activity flag
            SessionStateService.UpdateCurrentSession();
        }

        return delegate.call(context);
    }
}