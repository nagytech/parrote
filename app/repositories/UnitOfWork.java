package repositories;

import services.BonMotService;
import services.SessionStateService;
import services.UserService;

/**
 * Pattern for lazy lookup of all services
 */
public class UnitOfWork {

    BonMotService bonMotService;
    SessionStateService sessionStateService;
    UserService userService;

    /**
     * Lazy lookup for bonmot service
     * @return
     */
    public BonMotService getBonMotService() {
        if (bonMotService == null)
            bonMotService = new BonMotService();
        return bonMotService;
    }

    /**
     * Lazy lookup for session service
     * @return
     */
    public SessionStateService getSessionStateService() {
        if (sessionStateService == null)
            sessionStateService = new SessionStateService();
        return sessionStateService;
    }

    /**
     * Lazy lookup for user service
     * @return
     */
    public UserService getUserService() {
        if (userService == null)
            userService = new UserService();
        return userService;
    }


}
