package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.BonMot;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

import static play.libs.Json.toJson;

public class BonMots extends Controller {

    public static Result latest(int page, int pageSize, String piths) {

        String[] codesArray = piths != null && piths.length() > 0 ? piths.split(",") : new String[] {};
        List<BonMot> mots = models.BonMot.getLatest(page, pageSize, codesArray);
        // TODO: properly design the DTO
        return ok(Json.prettyPrint(toJson(mots)));

    }

}
