package com.alexkudlick.authentication.application.config;

import com.alexkudlick.authentication.application.dao.UserDAO;
import com.alexkudlick.authentication.application.tokens.AuthenticationTokenManager;
import com.google.common.cache.Cache;
import org.hibernate.SessionFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class ServiceRegistry {

    private final UserDAO userDAO;
    private final AuthenticationTokenManager tokenManager;

    public ServiceRegistry(Cache<String, String> tokenCache, PasswordEncoder passwordEncoder,
                           SessionFactory sessionFactory) {
        userDAO = new UserDAO(sessionFactory, passwordEncoder);
        tokenManager = new AuthenticationTokenManager(tokenCache, userDAO, UUID::randomUUID);
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public AuthenticationTokenManager getTokenManager() {
        return tokenManager;
    }
}
