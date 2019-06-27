package com.alexkudlick.authentication.application.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class UserRequest {

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("password")
    private String password;

    private UserRequest() {

    }

    public UserRequest(String userName, String password) {
        this.userName = Objects.requireNonNull(userName);
        this.password = Objects.requireNonNull(password);
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
        if (!(o instanceof UserRequest)) return false;
        UserRequest that = (UserRequest) o;
        return Objects.equals(userName, that.userName) &&
            Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password);
    }

    @Override
    public String toString() {
        return "UserRequest{" +
            "userName='" + userName + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
