package me.anenkov.fbpageanalyzer.model;

import java.io.Serializable;

/**
 * UserConnection
 *
 * @author anenkov
 */
public class UserConnection implements Serializable {
    private final String userId;
    private final String providerUserId;
    private final String displayName;
    private final String imageUrl;
    private final String accessToken;

    /**
     * @param userId
     * @param providerUserId
     * @param displayName
     * @param imageUrl
     * @param accessToken
     */
    public UserConnection(String userId, String providerUserId, String displayName, String imageUrl, String accessToken) {
        this.userId = userId;
        this.providerUserId = providerUserId;
        this.displayName = displayName;
        this.imageUrl = imageUrl;
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return String.format("userId = %s, providerUserId = %s, displayName = %s, accessToken = %s",
                userId, providerUserId, displayName, accessToken
        );
    }

    public String getUserId() {
        return userId;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
