package com.alexkudlick.authentication.application.config;

import com.alexkudlick.authentication.application.tokens.AuthenticationTokenManager;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.cache.CacheBuilder;
import io.dropwizard.Configuration;

public class AuthenticationConfiguration extends Configuration {

    @JsonProperty("cacheSpec")
    private String cacheSpec;


    public AuthenticationTokenManager createAuthenticationTokenManager() {
        return new AuthenticationTokenManager(CacheBuilder.from(cacheSpec).build());
    }

}
