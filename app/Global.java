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
 * GlobalSettings extension
 *
 * Used to override application behaviour
 */
public class Global  extends GlobalSettings {

    /**
     * On application startup
     * @param app
     */
    @Override
    public void onStart(Application app) {

        // If the admin user hasn't been added, then this is probably the
        // first time the app has been started.
        if (User.findByEmail("admin@admin.com") == null) {
            // Add the initial data
            Logger.info("Loading initial data.");
            Ebean.save((List<?>) Yaml.load("initial-data.yml"));
        }

    }
}
