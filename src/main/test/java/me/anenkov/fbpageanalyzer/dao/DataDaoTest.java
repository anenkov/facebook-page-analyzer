package me.anenkov.fbpageanalyzer.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * me.anenkov.fbpageanalyzer.dao.DataDaoTest
 */
public class DataDaoTest {

    private DataDao dataDao;
    private JdbcTemplate jdbcTemplate;
    private String userId = "1234";
    private String data = "";

    @Before
    public void setUp() {
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        dataDao = new DataDao(jdbcTemplate);
    }

    @Test
    public void whenDataExistsReturnIt() throws Exception {
        when(jdbcTemplate.queryForObject("SELECT count(*) FROM Data WHERE userId = ?", Integer.class, userId)).thenReturn(1);
        when(jdbcTemplate.queryForObject(contains("SELECT data"), ArgumentMatchers.<RowMapper<String>>any(), eq(userId))).thenReturn("fake data");
        String returnedData = dataDao.getData(userId);
        assertEquals("fake data", returnedData);
    }

    @Test
    public void whenDataNotExistsReturnEmpty() throws Exception {
        when(jdbcTemplate.queryForObject("SELECT count(*) FROM Data WHERE userId = ?", Integer.class, userId)).thenReturn(0);
        verify(jdbcTemplate, times(0)).queryForObject(contains("SELECT data"), ArgumentMatchers.<RowMapper<String>>any(), eq(userId));
        String returnedData = dataDao.getData(userId);
        assertEquals("", returnedData);
    }

    @Test
    public void whenUserNotExistsInsertData() throws Exception {

        when(jdbcTemplate.queryForObject("SELECT count(*) FROM Data WHERE userId = ?", Integer.class, userId)).thenReturn(0);
        dataDao.setData(userId, data);
        verify(jdbcTemplate, times(1)).update("INSERT INTO Data (userId, data) VALUES(?,?)", userId, data);
        verify(jdbcTemplate, times(0)).update("UPDATE Data SET data = ? WHERE userId = ?", data, userId);
    }


    @Test
    public void whenUserExistsUpdateData() throws Exception {
        when(jdbcTemplate.queryForObject("SELECT count(*) FROM Data WHERE userId = ?", Integer.class, userId)).thenReturn(1);
        dataDao.setData(userId, data);
        verify(jdbcTemplate, times(0)).update("INSERT INTO Data (userId, data) VALUES(?,?)", userId, data);
        verify(jdbcTemplate, times(1)).update("UPDATE Data SET data = ? WHERE userId = ?", data, userId);
    }

}