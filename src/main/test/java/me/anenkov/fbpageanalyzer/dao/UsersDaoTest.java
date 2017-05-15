package me.anenkov.fbpageanalyzer.dao;

import me.anenkov.fbpageanalyzer.model.UserConnection;
import me.anenkov.fbpageanalyzer.model.UserProfile;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * me.anenkov.fbpageanalyzer.dao.UsersDaoTest
 */
public class UsersDaoTest {

    private final String USER_ID = UUID.randomUUID().toString();
    private UsersDao usersDao;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        usersDao = new UsersDao(jdbcTemplate);
    }


    @Test
    public void getUserProfile() throws Exception {
        usersDao.getUserProfile(USER_ID);
        verify(jdbcTemplate, times(1)).queryForObject(
                eq("SELECT * FROM UserProfile WHERE userId = ?"),
                ArgumentMatchers.<RowMapper<UserProfile>>any(),
                eq(USER_ID)
        );
    }

    @Test
    public void getUserConnection() throws Exception {
        usersDao.getUserConnection(USER_ID);
        verify(jdbcTemplate, times(1)).queryForObject(
                eq("SELECT * FROM UserConnection WHERE userId = ?"),
                ArgumentMatchers.<RowMapper<UserConnection>>any(),
                eq(USER_ID)
        );
    }

    @Test
    public void createUser() throws Exception {
        UserProfile userProfile = new UserProfile(USER_ID, "name", "firstName", "lastName", "email", "username");
        usersDao.createUser(USER_ID, userProfile);
        verify(jdbcTemplate, times(1)).update(
                "INSERT INTO users(username,password,enabled) VALUES(?,?,TRUE)",
                USER_ID,
                UUID.fromString(USER_ID).toString()
        );
        verify(jdbcTemplate, times(1)).update(
                "INSERT INTO authorities(username,authority) VALUES(?,?)", USER_ID, "USER"
        );
        verify(jdbcTemplate, times(1)).update(
                "INSERT INTO UserProfile(userId, email, firstName, lastName, name, username) VALUES(?,?,?,?,?,?)",
                USER_ID,
                userProfile.getEmail(),
                userProfile.getFirstName(),
                userProfile.getLastName(),
                userProfile.getName(),
                userProfile.getUsername()
        );
    }
}
