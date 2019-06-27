package com.alexkudlick.authentication.application.dao;

import com.alexkudlick.authentication.application.entities.UserEntity;
import io.dropwizard.testing.junit.DAOTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class UserDAOTest {

    @Rule
    public DAOTestRule database = DAOTestRule.newBuilder()
        .addEntityClass(UserEntity.class)
        .build();

    private PasswordEncoder passwordEncoder;
    private UserDAO dao;

    @Before
    public void setUp() {
        passwordEncoder = mock(PasswordEncoder.class);
        dao = new UserDAO(database.getSessionFactory(), passwordEncoder);
    }

    @Test
    public void testCreateUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("ENCRYPTED");

        database.inTransaction(() -> dao.createUser("user123", "password123"));

        UserEntity entity = database.getSessionFactory().getCurrentSession().get(UserEntity.class, 1L);

        assertEquals("user123", entity.getUserName());
        assertEquals("ENCRYPTED", entity.getEncryptedPassword());

        verify(passwordEncoder).encode("password123");
        verifyNoMoreInteractions(passwordEncoder);
    }
}