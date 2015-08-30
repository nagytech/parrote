package actions;

import controllers.routes;
import models.Session;
import models.User;
import org.joda.time.DateTime;
import play.Logger;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.SessionStateService;

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

    private SessionStateService service;

    @Inject
    public UnlogAction(SessionStateService sss) {
        service = sss;
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

        Session session = service.Current();

        // If the user does not exist, but is logged in.
        if (session == null || session.userId == null)
            return delegate.call(context);

        // TODO: Replace
        Logger.warn("REPLACE ME - TEMPORARY");
        User user = User.findById(session.userId);
        if (user.banned || new DateTime(session.lastAccess).plusHours(6).isBeforeNow()) {
            Logger.warn("TODO");
            // Clear the session if user invalid, banned, or inactive for over 6 hours
            service.ExpireCurrentSession();
            return F.Promise.pure(redirect(routes.Application.index()));
        } else {
            Logger.warn("TODO");
            // Update user activity flag
            service.UpdateCurrentSession();
        }

        context.current().args.put("user", user);
        context.current().args.put("session", session);

        return delegate.call(context);
    }

}