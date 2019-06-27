package com.alexkudlick.authentication.application.tokens;

import com.alexkudlick.authentication.application.dao.UserDAO;
import com.alexkudlick.authentication.models.AuthenticationToken;
import com.google.common.cache.Cache;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class AuthenticationTokenManager {

    private final Cache<String, String> tokenCache;
    private final UserDAO userDAO;
    private final Supplier<UUID> tokenGenerator;

    public AuthenticationTokenManager(Cache<String, String> tokenCache, UserDAO userDAO, Supplier<UUID> tokenGenerator) {
        this.tokenCache = Objects.requireNonNull(tokenCache);
        this.userDAO = Objects.requireNonNull(userDAO);
        this.tokenGenerator = Objects.requireNonNull(tokenGenerator);
    }

    public boolean isValid(String token) {
        return tokenCache.getIfPresent(token) != null;
    }

    public Optional<AuthenticationToken> login(String userName, String password) {
        if (userDAO.isValidLogin(userName, password)) {
            String token = tokenGenerator.get().toString();
            tokenCache.put(token, token);
            return Optional.of(new AuthenticationToken(token));
        } else {
            return Optional.empty();
        }
    }
}
