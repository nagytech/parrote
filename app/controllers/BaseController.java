package controllers;

import actions.UnlogAction;
import play.mvc.Controller;
import play.mvc.With;


@With(UnlogAction.class)
public class BaseController extends Controller {

    // Nothing to see here, move along

}
