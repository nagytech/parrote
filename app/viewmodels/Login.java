package viewmodels;


import play.data.validation.Constraints;
import play.i18n.Messages;
import services.UserService;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Model validation used by form binding
     *
     * @returns error message if invalid, or null if valid
     */
    @SuppressWarnings("unused")
    public String validate() {

        // Try to authenticate the user
        if (new UserService().authenticate(email, password))
            return null;

        password = "";
        return Messages.get("login.invalid");

    }
}
