package controllers;

import actions.UnlogAction;
import models.Session;
import models.User;
import org.bson.types.ObjectId;
import play.mvc.Controller;
import play.mvc.With;
import services.SessionStateService;
import services.UserService;

/**
 * Base controller with session activity handler as UnlogAction class
 */
@With(UnlogAction.class)
public class BaseController extends Controller {

    public BaseController() {

    }

    public static User getUser() {
        SessionStateService service = new SessionStateService();
        UserService userservice = new UserService();
        Session session = service.Current();
        if (session == null) return null;
        User user = userservice.findById(new ObjectId(session.userId));
        return user;
    }

    public static Session getSession() {
        SessionStateService service = new SessionStateService();
        Session session = service.Current();
        return session;
    }

}
