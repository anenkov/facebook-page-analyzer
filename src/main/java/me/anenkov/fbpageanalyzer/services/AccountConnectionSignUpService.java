package me.anenkov.fbpageanalyzer.services;

import me.anenkov.fbpageanalyzer.dao.UsersDao;
import me.anenkov.fbpageanalyzer.model.UserProfile;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import java.util.UUID;

/**
 * AccountConnectionSignUpService
 */
public class AccountConnectionSignUpService implements ConnectionSignUp {

    private final UsersDao usersDao;

    /**
     * @param usersDao
     */
    public AccountConnectionSignUpService(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @Override
    public String execute(Connection<?> connection) {
        org.springframework.social.connect.UserProfile profile = connection.fetchUserProfile();
        String userId = UUID.randomUUID().toString();
        usersDao.createUser(userId, new UserProfile(userId, profile));
        return userId;
    }
}
