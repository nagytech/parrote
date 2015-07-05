package models;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;

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

    public static boolean authenticate(String email, String password) {

        User user = find.where()
                .eq("email", email)
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

}
