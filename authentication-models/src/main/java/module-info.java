open module com.alexkudlick.authentication.models {
    requires jackson.annotations;
    requires com.google.common;

    requires validation.api;
    requires hibernate.validator;

    exports com.alexkudlick.authentication.models;
}
