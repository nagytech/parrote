package controllers;

import actions.SessionAction;
import models.Session;
import models.User;
import play.mvc.Controller;
import play.mvc.With;
import repositories.UnitOfWork;
import services.SessionStateService;
import services.UserService;

import javax.inject.Inject;

/**
 * Base controller with session activity handler as SessionAction class
 */
@With(SessionAction.class)
public class BaseController extends Controller {

    private User user;
    private boolean userCheck;
    private Session session;
    private boolean sessionCheck;

    protected final UnitOfWork uow;

    public BaseController(UnitOfWork uow) {
        this.uow = uow;
    }

    public User getUser() {

        // Utilize user cache
        if (user == null && userCheck) return null;
        userCheck = true;

        // Utilize session cache
        if (session == null && sessionCheck) return null;
        getSession();
        if (session == null) return null;

        // Lookup user
        user = uow.getUserService().findById(session.userId);
        return user;
    }

    public Session getSession() {

        // Utilize session cache
        if (session == null && sessionCheck) return null;
        sessionCheck = true;

        // Lookup session
        session = uow.getSessionStateService().Current();
        return session;

    }

}
