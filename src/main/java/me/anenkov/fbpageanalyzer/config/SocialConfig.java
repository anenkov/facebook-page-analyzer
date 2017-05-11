package me.anenkov.fbpageanalyzer.config;

import me.anenkov.fbpageanalyzer.dao.UsersDao;
import me.anenkov.fbpageanalyzer.services.AccountConnectionSignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.security.AuthenticationNameUserIdSource;

import javax.sql.DataSource;

/**
 * Social Configuration
 *
 * @author anenkov
 */
@Configuration
@EnableSocial
@ComponentScan("me.anenkov.fbpageanalyzer")
public class SocialConfig extends SocialConfigurerAdapter {

    private final DataSource dataSource;
    private final UsersDao usersDao;

    /**
     * @param dataSource
     * @param usersDao
     */
    @Autowired
    public SocialConfig(DataSource dataSource, UsersDao usersDao) {
        this.dataSource = dataSource;
        this.usersDao = usersDao;
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
                connectionFactoryLocator, Encryptors.noOpText());
        repository.setConnectionSignUp(new AccountConnectionSignUpService(usersDao));
        return repository;
    }
}
