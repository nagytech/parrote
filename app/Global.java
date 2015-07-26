import com.avaje.ebean.Ebean;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Yaml;
import play.mvc.Action;
import play.mvc.Http;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by jnagy on 10/07/15.
 */
public class Global  extends GlobalSettings {

    @Override
    public void onStart(Application app) {

        if (User.findByEmail("admin@admin.com") == null) {
            Logger.info("Loading initial data.");
            Ebean.save((List<?>) Yaml.load("initial-data.yml"));
        }

    }
}
