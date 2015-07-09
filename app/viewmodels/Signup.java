package viewmodels;

import models.User;
import play.data.validation.Constraints;
import play.i18n.Messages;

/**
 * Signup object
 * <p>
 * Viewmodel for signup and registration
 */
public class Signup extends Login {

    /**
     * User's chosen username
     */
    @Constraints.Required
    @Constraints.MaxLength(24)
    public String username;

    /**
     * Password match field
     */
    @Constraints.Required
    public String passwordMatch;

    /**
     * Model validation used by form binding
     *
     * @return error message if invalid, or null if valid
     */
    @Override
    public String validate() {

        // Check that the passwords match
        if (!password.equals(passwordMatch)) {
            return Messages.get("login.invalid");
        }

        // Check that the email does not already exist
        if (User.findByEmail(email) != null) {
            return Messages.get("signup.emailExists");
        }

        // Check that the username does not already exist
        if (User.findByUsername(username) != null) {
            return Messages.get("signup.usernameExists");
        }
        return null;
    }

}

