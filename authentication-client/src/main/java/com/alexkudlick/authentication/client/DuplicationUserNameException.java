package com.alexkudlick.authentication.client;

public class DuplicationUserNameException extends Exception {
    public DuplicationUserNameException(String userName) {
        super("duplication user name '" + userName + "'");
    }
}
