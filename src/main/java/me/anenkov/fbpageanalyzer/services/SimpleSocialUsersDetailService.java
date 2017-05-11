package me.anenkov.fbpageanalyzer.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

/**
 * SimpleSocialUsersDetailService
 */
public class SimpleSocialUsersDetailService implements SocialUserDetailsService {
    private final UserDetailsService userDetailsService;

    /**
     * @param userDetailsService
     */
    public SimpleSocialUsersDetailService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public SocialUserDetails loadUserByUserId(String userId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new SocialUser(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }
}
