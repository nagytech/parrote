package controllers.api;

import controllers.BaseController;
import models.User;
import play.mvc.Result;
import play.mvc.Security;
import repositories.UnitOfWork;
import security.Authenticator;

import javax.inject.Inject;

@Security.Authenticated(Authenticator.class)
public class Profile extends BaseController {

    @Inject
    public Profile(UnitOfWork uow) {
        super(uow);
    }

    @Security.Authenticated(Authenticator.class)
    public Result whoami() {

        User user = getUser();
        return ok(user.username);

    }

}
