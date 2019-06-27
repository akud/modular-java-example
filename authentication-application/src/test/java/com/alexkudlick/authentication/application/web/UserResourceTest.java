package com.alexkudlick.authentication.application.web;

import com.alexkudlick.authentication.application.dao.UserDAO;
import com.alexkudlick.authentication.models.AuthenticationRequest;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class UserResourceTest {

    private UserDAO dao;
    private UserResource resource;

    @Before
    public void setUp() {
        dao = mock(UserDAO.class);
        resource = new UserResource(dao);
    }

    @Test
    public void testExecute() throws Exception {
        resource.createUser(new AuthenticationRequest("foo", "bar"));

        verify(dao).createUser("foo", "bar");
        verifyNoMoreInteractions(dao);
    }
}