package me.anenkov.fbpageanalyzer.controllers.util;

import me.anenkov.fbpageanalyzer.dao.DataDao;
import me.anenkov.fbpageanalyzer.dao.UsersDao;
import me.anenkov.fbpageanalyzer.model.UserConnection;
import me.anenkov.fbpageanalyzer.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * SocialControllerUtil
 *
 * @author anenkov
 */
@Component
public class SocialControllerUtil {
    private static final String USER_CONNECTION = "MY_USER_CONNECTION";
    private static final String USER_PROFILE = "MY_USER_PROFILE";

    private final DataDao dataDao;
    private final UsersDao usersDao;

    /**
     * @param dataDao
     * @param usersDao
     */
    @Autowired
    public SocialControllerUtil(DataDao dataDao, UsersDao usersDao) {
        this.dataDao = dataDao;
        this.usersDao = usersDao;
    }

    /**
     * @param request
     * @param currentUser
     * @param model
     */
    public void setModel(HttpServletRequest request, Principal currentUser, Model model) {
        String userId = currentUser == null ? null : currentUser.getName();
        HttpSession session = request.getSession();

        UserConnection connection = null;
        UserProfile profile = null;
        String displayName = null;
        String data = null;

        // Collect info if the user is logged in, i.e. userId is set
        if (userId != null) {

            // Get the current UserConnection from the http session
            connection = getUserConnection(session, userId);

            // Get the current UserProfile from the http session
            profile = getUserProfile(session, userId);

            // Compile the best display name from the connection and the profile
            displayName = getDisplayName(connection, profile);

            // Get user data from persistence storage
            data = dataDao.getData(userId);
        }

        Throwable exception = (Throwable) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        // Update the model with the information we collected
        model.addAttribute("exception", exception == null ? null : exception.getMessage());
        model.addAttribute("currentUserId", userId);
        model.addAttribute("currentUserProfile", profile);
        model.addAttribute("currentUserConnection", connection);
        model.addAttribute("currentUserDisplayName", displayName);
        model.addAttribute("currentData", data);
    }

    /**
     * Get the current UserProfile from the http session
     *
     * @param session
     * @param userId
     * @return
     */
    private UserProfile getUserProfile(HttpSession session, String userId) {
        UserProfile profile = (UserProfile) session.getAttribute(USER_PROFILE);

        // Reload from persistence storage if not set or invalid (i.e. no valid userId)
        if (profile == null || !userId.equals(profile.getUserId())) {
            profile = usersDao.getUserProfile(userId);
            session.setAttribute(USER_PROFILE, profile);
        }
        return profile;
    }

    /**
     * Get the current UserConnection from the http session
     *
     * @param session
     * @param userId
     * @return
     */
    private UserConnection getUserConnection(HttpSession session, String userId) {
        UserConnection connection;
        connection = (UserConnection) session.getAttribute(USER_CONNECTION);

        // Reload from persistence storage if not set or invalid (i.e. no valid userId)
        if (connection == null || !userId.equals(connection.getUserId())) {
            connection = usersDao.getUserConnection(userId);
            session.setAttribute(USER_CONNECTION, connection);
        }
        return connection;
    }

    /**
     * Compile the best display name from the connection and the profile
     *
     * @param connection
     * @param profile
     * @return
     */
    private String getDisplayName(UserConnection connection, UserProfile profile) {
        // The name is set differently in different providers so we better look in both places...
        if (connection.getDisplayName() != null) {
            return connection.getDisplayName();
        } else {
            return profile.getName();
        }
    }
}
