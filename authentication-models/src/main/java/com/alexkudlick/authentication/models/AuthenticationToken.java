package com.alexkudlick.authentication.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import java.util.Objects;

public class AuthenticationToken {

    public static final int MIN_LENGTH = 8;

    @JsonProperty("token")
    private String token;

    private AuthenticationToken() {

    }

    public AuthenticationToken(String token) {
        Preconditions.checkArgument(token != null && token.length() >= MIN_LENGTH);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthenticationToken)) return false;
        AuthenticationToken that = (AuthenticationToken) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    @Override
    public String toString() {
        return "AuthenticationToken{" +
            "token='" + "*".repeat(token.length() - 4) + token.substring(token.length() - 4, token.length()) + '\'' +
            "}";
    }

}
