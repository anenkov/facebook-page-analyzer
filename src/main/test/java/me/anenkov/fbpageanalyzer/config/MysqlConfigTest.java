package me.anenkov.fbpageanalyzer.config;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import javax.sql.DataSource;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * me.anenkov.fbpageanalyzer.config.MysqlConfigTest
 */
public class MysqlConfigTest {

    private MysqlConfig mysqlConfig;

    @Before
    public void setUp() {
        MockEnvironment environment = new MockEnvironment()
                .withProperty("db.driver", "com.mysql.jdbc.Driver")
                .withProperty("db.url", "a")
                .withProperty("db.username", "a")
                .withProperty("db.password", "a")
                .withProperty("entitymanager.packagesToScan", "fpa")
                .withProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect")
                .withProperty("hibernate.show_sql", "a")
                .withProperty("hibernate.hbm2ddl.auto", "a");
        mysqlConfig = new MysqlConfig(environment);
    }

    @Test
    public void dataSource() throws Exception {
        assertThat(mysqlConfig.dataSource(), instanceOf(DriverManagerDataSource.class));
    }

    @Test
    public void sessionFactory() throws Exception {
        assertThat(mysqlConfig.sessionFactory(), instanceOf(LocalSessionFactoryBean.class));
    }

    @Test
    public void transactionManager() throws Exception {
        assertThat(mysqlConfig.transactionManager(), instanceOf(HibernateTransactionManager.class));
    }

    @Test
    public void jdbcTemplate() throws Exception {
        assertThat(mysqlConfig.jdbcTemplate(mock(DataSource.class)), instanceOf(JdbcTemplate.class));
    }

}