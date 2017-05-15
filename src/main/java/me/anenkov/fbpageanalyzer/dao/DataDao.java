package me.anenkov.fbpageanalyzer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * DataDao
 *
 * @author anenkov
 */
@Repository
public class DataDao {

    private final JdbcTemplate jdbcTemplate;

    /**
     * @param jdbcTemplate
     */
    @Autowired
    public DataDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * @param userId
     * @return
     */
    public String getData(String userId) {
        if (userDataExists(userId)) {
            return jdbcTemplate
                    .queryForObject("SELECT data FROM Data WHERE userId = ?", (rs, rowNum) -> rs.getString("data"), userId);
        } else {
            return "";
        }
    }

    /**
     * @param userId
     * @return
     */
    private boolean userDataExists(String userId) {
        Integer cnt = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM Data WHERE userId = ?", Integer.class, userId);
        return cnt != null && cnt > 0;
    }

    /**
     * @param userId
     * @param data
     */
    public void setData(String userId, String data) {
        if (!userDataExists(userId)) {
            jdbcTemplate.update("INSERT INTO Data (userId, data) VALUES(?,?)", userId, data);
        } else {
            jdbcTemplate.update("UPDATE Data SET data = ? WHERE userId = ?", data, userId);
        }
    }
}
