package actions;

import models.User;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

public class UnlogAction extends Action.Simple {

    @Override
    public F.Promise<Result> call(Http.Context context) throws Throwable {
        // If the user does not exist, but is logged in.
        if (User.findByEmail(context.session().get("email")) == null)
            // Clear the session
            context.session().clear();
        return delegate.call(context);
    }
}