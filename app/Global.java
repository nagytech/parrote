import com.avaje.ebean.Ebean;
import db.MongoConnection;
import factories.MongoConnectionFactory;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Yaml;
import play.mvc.Action;
import play.mvc.Http;
import repositories.UserRepository;
import services.BonMotService;
import services.UserService;

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

        BonMotService bonMotService = new BonMotService();
        UserService userService = new UserService();
        userService.index();

        // If the admin user hasn't been added, then this is probably the
        // first time the app has been started.
        if (userService.findByEmail("admin@admin.com") == null) {

            User user = new User();
            user.email = "admin@admin.com";
            user.username = "admin";
            user.password = "$2a$10$6qcM9wmSFGeiTN2hwhIE5OJ0emrAOf1juJx8TA7oywQDvEdS6Wn4K";
            user.banned = false;
            user.admin = true;

            userService.create(user);
            bonMotService.create(user, "Beware the banhammer! #first");

            user = new User();
            user.email = "test@test.com";
            user.username = "test";
            user.password = "$2a$10$6qcM9wmSFGeiTN2hwhIE5OJ0emrAOf1juJx8TA7oywQDvEdS6Wn4K";
            user.banned = false;
            user.admin = false;

            userService.create(user);
            bonMotService.create(user, "Hello, world! #second");

        }



    }
}
