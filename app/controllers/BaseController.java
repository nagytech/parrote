package controllers;

import actions.UnlogAction;
import play.mvc.Controller;
import play.mvc.With;

/**
 * Created by geoist on 25/07/15.
 */
@With(UnlogAction.class)
public class BaseController extends Controller {


}
