package me.anenkov.fbpageanalyzer.config;

import me.anenkov.fbpageanalyzer.ApplicationException;
import me.anenkov.fbpageanalyzer.services.SimpleSocialUsersDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * Security Configuration
 *
 * @author anenkov
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    /**
     * @param dataSource
     */
    @Autowired
    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @param auth
     * @throws ApplicationException
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws ApplicationException {
        try {
            auth.jdbcAuthentication().dataSource(dataSource);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin().loginPage("/login").loginProcessingUrl("/login/authenticate")
                .failureUrl("/login?param.error=bad_credentials").permitAll().and().logout()
                .logoutUrl("/logout").deleteCookies("JSESSIONID").and().authorizeRequests()
                .antMatchers("/favicon.ico", "/css/**").permitAll().antMatchers("/**").authenticated()
                .and().rememberMe().and()
                .apply(new SpringSocialConfigurer().postLoginUrl("/").alwaysUsePostLoginUrl(true));
    }

    /**
     * Specify "socialUsersDetailService" bean
     *
     * @return
     */
    @Bean
    public SocialUserDetailsService socialUsersDetailService() {
        return new SimpleSocialUsersDetailService(userDetailsService());
    }
}
