module com.alexkudlick.authentication.application {
    requires com.alexkudlick.authentication.models;

    requires jackson.annotations;
    requires com.google.common;
    requires javax.ws.rs.api;

    requires java.sql;

    requires dropwizard.core;
    requires dropwizard.configuration;
}
