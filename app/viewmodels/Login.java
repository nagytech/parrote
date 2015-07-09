package viewmodels;


import models.User;
import play.data.validation.Constraints;
import play.i18n.Messages;

/**
 * Login object
 * <p>
 * Viewmodel for login and authentication
 */
public class Login {

    /**
     * User's email address
     */
    @Constraints.Required
    @Constraints.Email
    public String email;

    /**
     * User's plain text password
     */
    @Constraints.Required
    public String password;

    /**
     * Model validation used by form binding
     *
     * @returns error message if invalid, or null if valid
     */
    @SuppressWarnings("unused")
    public String validate() {

        // Try to authenticate the user
        if (!User.authenticate(email, password)) {
            // Clean password
            password = "";
            return Messages.get("login.invalid");
        }
        return null;

    }
}
