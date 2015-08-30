package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.BonMot;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.BonMotService;

import java.util.List;

import static play.libs.Json.toJson;

/**
 * BonMots Controller
 * <p>
 * Handles plural actions relating to BonMots
 */
public class BonMots extends BaseController {

    /**
     * latest Action
     * <p>
     * Get the latest set of bonmots
     * @param page Option: specify the page out of pages
     * @param pageSize Option: specify the pageSize for the pages
     * @param pith Option: specify the pith filter
     * @return Json array of bonmots matching search critieria
     */
    public static Result latest(int page, int pageSize, String pith) {

        // Get the bonmots matching the parameters
        BonMotService service = new BonMotService();
        List<BonMot> mots = service.getLatest(page, pageSize, pith);

        // TODO: properly design the DTO
        // Return the json array
        return ok(Json.prettyPrint(toJson(mots)));

    }

}
