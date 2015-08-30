package repositories;

import services.BonMotService;
import services.SessionStateService;
import services.UserService;

/**
 * Created by jnagy on 30/08/15.
 */
public class UnitOfWork {

    BonMotService bonMotService;
    SessionStateService sessionStateService;
    UserService userService;

    public BonMotService getBonMotService() {
        if (bonMotService == null)
            bonMotService = new BonMotService();
        return bonMotService;
    }

    public SessionStateService getSessionStateService() {
        if (sessionStateService == null)
            sessionStateService = new SessionStateService();
        return sessionStateService;
    }

    public UserService getUserService() {
        if (userService == null)
            userService = new UserService();
        return userService;
    }


}
