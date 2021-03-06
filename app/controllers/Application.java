package controllers;

import play.mvc.Result;
import repositories.UnitOfWork;
import views.html.index;

import javax.inject.Inject;

/**
 * Application Controller.
 * <p>
 * Main landing controller of the application
 */
public class Application extends BaseController {

    @Inject
    public Application(UnitOfWork uow) {
        super(uow);
    }

    /**
     * GET: index action
     *
     * @return Latest bonmot listing for all users.
     */
    public Result index() {

        // List the latest mots from all users
        java.util.List<models.BonMot> mots = uow.getBonMotService().getLatest();
        return ok(index.render(mots));

    }

}
