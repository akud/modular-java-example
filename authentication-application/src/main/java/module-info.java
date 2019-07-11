module com.alexkudlick.authentication.application {
    requires com.alexkudlick.authentication.models;

    requires jackson.annotations;
    requires com.google.common;
    requires javax.ws.rs.api;
    requires java.naming;

    requires java.sql;
    requires java.xml.bind;

    requires hibernate.jpa;
    requires dropwizard.hibernate;
    requires hibernate.core;
    requires spring.security.crypto;
    requires dropwizard.servlets;
    requires dropwizard.db;
    requires dropwizard.migrations;
    requires com.fasterxml.jackson.databind;

    requires dropwizard.core;
    requires dropwizard.configuration;

    opens com.alexkudlick.authentication.application.config to com.fasterxml.jackson.databind;
    opens com.alexkudlick.authentication.application.models to com.fasterxml.jackson.databind;
    opens com.alexkudlick.authentication.application.web to jersey.server;
    opens com.alexkudlick.authentication.application.entities to hibernate.core, javassist;
}
