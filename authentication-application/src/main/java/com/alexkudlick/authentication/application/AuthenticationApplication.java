package com.alexkudlick.authentication.application;

import com.alexkudlick.authentication.application.config.AuthenticationConfiguration;
import com.alexkudlick.authentication.application.config.ServiceRegistry;
import com.alexkudlick.authentication.application.entities.UserEntity;
import com.alexkudlick.authentication.application.web.AuthenticationTokenResource;
import com.alexkudlick.authentication.application.web.ConstraintViolationExceptionMapper;
import com.alexkudlick.authentication.application.web.UserResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AuthenticationApplication extends Application<AuthenticationConfiguration> {

    private static final HibernateBundle<AuthenticationConfiguration> HIBERNATE_BUNDLE = new HibernateBundle<>(
        UserEntity.class
    ) {
        @Override
        public PooledDataSourceFactory getDataSourceFactory(AuthenticationConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    private static final MigrationsBundle<AuthenticationConfiguration> MIGRATIONS_BUNDLE = new MigrationsBundle<>() {
        @Override
        public PooledDataSourceFactory getDataSourceFactory(AuthenticationConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }

        @Override
        public String getMigrationsFileName() {
            return "migrations.yml";
        }
    };

    public static void main(String[] args) throws Exception {
        new AuthenticationApplication().run(args);
    }

    @Override
    public void run(AuthenticationConfiguration configuration, Environment environment) throws Exception {
        ServiceRegistry serviceRegistry = configuration.createServiceRegistry(HIBERNATE_BUNDLE.getSessionFactory());
        environment.jersey().register(
            new AuthenticationTokenResource(serviceRegistry.getTokenManager())
        );
        environment.jersey().register(new UserResource(serviceRegistry.getUserDAO()));
        environment.jersey().register(new ConstraintViolationExceptionMapper());
    }

    @Override
    public void initialize(Bootstrap<AuthenticationConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
            new SubstitutingSourceProvider(
                new ResourceConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor()
            )
        );
        bootstrap.addBundle(HIBERNATE_BUNDLE);
        bootstrap.addBundle(MIGRATIONS_BUNDLE);
    }
}
