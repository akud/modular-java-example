package com.alexkudlick.authentication.application.config;

import com.alexkudlick.authentication.application.tokens.AuthenticationTokenManager;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.cache.CacheBuilder;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

public class AuthenticationConfiguration extends Configuration {

    @JsonProperty("cacheSpec")
    private String cacheSpec;

    @JsonProperty("database")
    private DataSourceFactory dataSourceFactory = new DataSourceFactory();

    public AuthenticationTokenManager createAuthenticationTokenManager() {
        return new AuthenticationTokenManager(CacheBuilder.from(cacheSpec).build());
    }

    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    public PasswordEncoder createPasswordEncoder() {
        return new SCryptPasswordEncoder();
    }
}
