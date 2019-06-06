package com.alexkudlick.authentication.application;

import com.alexkudlick.authentication.application.config.AuthenticationConfiguration;
import com.alexkudlick.authentication.application.web.AuthenticationTokenResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AuthenticationApplication extends Application<AuthenticationConfiguration> {

    public static void main(String[] args) throws Exception {
        new AuthenticationApplication().run(args);
    }

    @Override
    public void run(AuthenticationConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(
            new AuthenticationTokenResource(configuration.createAuthenticationTokenManager())
        );
    }

    @Override
    public void initialize(Bootstrap<AuthenticationConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
    }
}
