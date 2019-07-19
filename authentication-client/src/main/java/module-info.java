module com.alexkudlick.authentication.client {
    requires com.alexkudlick.authentication.models;

    requires com.google.common;

    requires transitive org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.commons.io;

    requires com.fasterxml.jackson.databind;

    exports com.alexkudlick.authentication.client;
}