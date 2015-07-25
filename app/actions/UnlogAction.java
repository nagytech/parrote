package actions;

import controllers.routes;
import models.User;
import org.joda.time.DateTime;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

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
        String email = context.session().get("email");
        if (email == null || email.isEmpty()) return delegate.call(context);

        User user = User.findByEmail(email);
        if (user == null || user.banned || new DateTime(user.lastAction).plusHours(6).isBeforeNow()) {
            // Clear the session if user invalid, banned, or inactive for over 6 hours
            context.session().clear();
            return F.Promise.pure(redirect(routes.Application.index()));
        } else if (user != null) {
            // Update user activity flag
            user.lastAction = DateTime.now().toDate();
            user.save();
        }

        return delegate.call(context);
    }
}