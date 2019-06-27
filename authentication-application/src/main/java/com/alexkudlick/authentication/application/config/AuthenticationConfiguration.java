package com.alexkudlick.authentication.application.config;

import com.alexkudlick.authentication.application.tokens.AuthenticationTokenManager;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.cache.CacheBuilder;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.SessionFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

public class AuthenticationConfiguration extends Configuration {

    @JsonProperty("cacheSpec")
    private String cacheSpec;

    @JsonProperty("database")
    private DataSourceFactory dataSourceFactory = new DataSourceFactory();

    public ServiceRegistry createServiceRegistry(SessionFactory sessionFactory) {
        return new ServiceRegistry(
            CacheBuilder.from(cacheSpec).build(),
            createPasswordEncoder(),
            sessionFactory
        );
    }

    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    private PasswordEncoder createPasswordEncoder() {
        return new SCryptPasswordEncoder();
    }
}
