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
import services.UserService;

import javax.inject.Inject;

/**
 * SessionAction - Action extension
 * -----------
 * To be used on a controller with the @With attribute.
 *
 * Implements an session clear policy where the user does not
 * exist, is banned, or has no activity in the last 6 hours.
 *
 * Also updates the user's last activity time stamp so that
 * we can identify the 6 hour timeout.
 */
public class SessionAction extends Action.Simple {

    /**
     * Session service - injected
     */
    private SessionStateService sessionService;
    /**
     * User service - injected
     */
    private UserService userService;

    @Inject
    public SessionAction(SessionStateService sss, UserService us) {
        sessionService = sss;
        userService = us;
    }

    /**
     * Intercept the http context call / response and run the
     * unlog action.
     *
     * @param context
     * @return
     * @throws Throwable
     */
    @Override
    public F.Promise<Result> call(Http.Context context) throws Throwable {

        // Get the current session object
        Session session = sessionService.Current();

        // If the user does not exist, but is logged in.
        if (session == null || session.userId == null)
            return delegate.call(context);

        // Get the corresponding user
        User user = userService.findById(session.userId);

        // Check for session validity (if user invalid, banned, or inactive for over 6 hours)
        if (user.banned || new DateTime(session.lastAccess).plusHours(6).isBeforeNow()) {
            // Clear the session and redirect to main page
            sessionService.ExpireCurrentSession();
            return F.Promise.pure(redirect(routes.Application.index()));
        }

        // Update user activity flag
        sessionService.UpdateCurrentSession();

        // Store the user and session objects for template usage
        context.current().args.put("user", user);
        context.current().args.put("session", session);

        // Continue with call
        return delegate.call(context);

    }

}