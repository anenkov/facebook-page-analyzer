package me.anenkov.fbpageanalyzer.dao;

import me.anenkov.fbpageanalyzer.model.UserConnection;
import me.anenkov.fbpageanalyzer.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.UUID;

/**
 * UsersDao
 *
 * @author anenkov
 */
@Repository
public class UsersDao {
    private final JdbcTemplate jdbcTemplate;

    /**
     * @param dataSource
     */
    @Autowired
    public UsersDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * @param userId
     * @return
     */
    public UserProfile getUserProfile(final String userId) {
        return jdbcTemplate.queryForObject("SELECT * FROM UserProfile WHERE userId = ?",
                (rs, rowNum) -> new UserProfile(
                        userId,
                        rs.getString("name"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("username")), userId);
    }

    /**
     * @param userId
     * @return
     */
    public UserConnection getUserConnection(final String userId) {
        return jdbcTemplate.queryForObject("SELECT * FROM UserConnection WHERE userId = ?",
                (rs, rowNum) -> new UserConnection(
                        userId,
                        rs.getString("providerUserId"),
                        rs.getString("displayName"),
                        rs.getString("imageUrl"),
                        rs.getString("accessToken")
                ), userId);
    }

    /**
     * @param userId
     * @param profile
     */
    public void createUser(String userId, UserProfile profile) {
        jdbcTemplate.update("INSERT INTO users(username,password,enabled) VALUES(?,?,TRUE)", userId, UUID.fromString(userId).toString());
        jdbcTemplate.update("INSERT INTO authorities(username,authority) VALUES(?,?)", userId, "USER");
        jdbcTemplate.update("INSERT INTO UserProfile(userId, email, firstName, lastName, name, username) VALUES(?,?,?,?,?,?)",
                userId,
                profile.getEmail(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getName(),
                profile.getUsername());
    }
}
