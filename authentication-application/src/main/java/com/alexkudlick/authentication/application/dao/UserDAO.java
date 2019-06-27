package com.alexkudlick.authentication.application.dao;

import com.alexkudlick.authentication.application.entities.UserEntity;
import com.google.common.base.Preconditions;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

public class UserDAO extends AbstractDAO<UserEntity> {

    private final PasswordEncoder passwordEncoder;

    public UserDAO(SessionFactory sessionFactory, PasswordEncoder passwordEncoder) {
        super(sessionFactory);
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder);
    }

    public void createUser(String userName, String password) {
        Preconditions.checkArgument(userName != null);
        Preconditions.checkArgument(password != null);
        UserEntity.withUnsavedInstance(
            userName,
            passwordEncoder.encode(password),
            this::persist
        );
    }
}
