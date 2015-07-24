package models;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class User extends Audit {

    static final Model.Finder<Long, User> find = new Model.Finder<>(User.class);

    @Column(length = 64, unique = true)
    public String username;
    @Constraints.Email
    @Column(unique = true)
    @JsonIgnore
    public String email;
    @JsonIgnore
    public String password;
    @JsonIgnore
    public Date lastAction;
    @JsonIgnore
    public boolean banned;
    @JsonIgnore
    public List<User> crowd = new ArrayList<>();

    public String getStatus() {

        if (banned)
          return "Banned";

        DateTime  date = new DateTime(lastAction);
        if (date.plusMinutes(10).isAfterNow()) {
            return "Online";
        } else if (date.plusMinutes(60).isAfterNow()) {
            return "Idle";
        } else if (date.plusHours(6).isBeforeNow()) {
            return "Inactive";
        }

        return "Offline";
    }

    public static boolean authenticate(String email, String password) {

        User user = find.where()
                .eq("email", email)
                .ne("banned", true)
                .isNull("deleted")
                .findUnique();

        try {
            if (user == null || !BCrypt.checkpw(password, user.password)) {
                Logger.debug("User failed to authenticate: [{}]", email);
                return false;
            }
        } catch (Exception ex) {
            Logger.error("Failed to authenticate: ", ex);
        }

        Logger.info("User authenticated: {}", user.email);
        return true;

    }

    public static User findByEmail(String email) {

        User user = find.where()
                .eq("email", email)
                .findUnique();

        if (user != null) {
            Logger.debug("User found by email {}", email);
        }

        return user;
    }

    public static User findByUsername(String username) {

        ExpressionList<User> users = find.where()
                .eq("username", username);

        if (users.findRowCount() == 1)
            return users.findUnique();
        return null;

    }

    public static boolean register(String email, String username, String password) {

        User user = new User();
        user.email = email;
        user.username = username;
        user.password = BCrypt.hashpw(password, BCrypt.gensalt());

        Logger.info(user.password);

        try {
            user.save();
        } catch (Exception ex) {
            Logger.error("Failed to register new user: [{}]", email);
            Logger.error("Exception:", ex);
            return false;
        }
        Logger.info("Registered new user: [{}]", email);
        return true;

    }

    @Override
    public String toString() {
        return String.format("@%s <%s>", username, email);
    }

    public static List<User> findAll() {
        return User.find.orderBy("username").findList();
    }
}
