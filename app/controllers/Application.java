package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import repositories.BonMotRepository;
import repositories.UnitOfWork;
import services.BonMotService;
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
        java.util.List<models.BonMot> mots = uow.getBonMotService().getLatestForTag("");
        return ok(index.render(mots));

    }

}
