package controllers;

import play.mvc.Result;
import views.html.singlepage;

import static play.mvc.Results.ok;

public class SinglePage {

    public static Result index() {

        return ok(singlepage.render());

    }

}
