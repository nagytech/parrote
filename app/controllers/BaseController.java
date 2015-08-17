package controllers;

import actions.UnlogAction;
import play.mvc.Controller;
import play.mvc.With;

/**
 * Base controller with session activity handler as UnlogAction class
 */
@With(UnlogAction.class)
public class BaseController extends Controller {

    // Nothing to see here, move along

}
