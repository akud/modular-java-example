package com.alexkudlick.authentication.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import java.util.Objects;

public class AuthenticationRequest {

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("password")
    private String password;

    private AuthenticationRequest() {

    }

    public AuthenticationRequest(String userName, String password) {
        Preconditions.checkArgument(userName != null && !userName.isEmpty());
        Preconditions.checkArgument(password != null && !password.isEmpty());
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthenticationRequest)) return false;
        AuthenticationRequest that = (AuthenticationRequest) o;
        return Objects.equals(userName, that.userName) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password);
    }

    @Override
    public String toString() {
        return "AuthenticationRequest{" +
                "userName='" + userName + '\'' +
                "password='" + "*".repeat(password.length()) + '\'' +
                "}";
    }
}
