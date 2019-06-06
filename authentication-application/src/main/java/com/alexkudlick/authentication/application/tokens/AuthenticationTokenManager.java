package com.alexkudlick.authentication.application.tokens;

import com.google.common.cache.Cache;

import java.util.Objects;

public class AuthenticationTokenManager {

    private final Cache<String, String> tokenCache;

    public AuthenticationTokenManager(Cache<String, String> tokenCache) {
        this.tokenCache = Objects.requireNonNull(tokenCache);
    }

    public boolean isValid(String token) {
        return tokenCache.getIfPresent(token) != null;
    }
}
