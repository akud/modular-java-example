package com.alexkudlick.authentication.application.web;


import com.alexkudlick.authentication.application.tokens.AuthenticationTokenManager;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class AuthenticationTokenResourceTest {

    private AuthenticationTokenManager manager;
    private AuthenticationTokenResource resource;

    @Before
    public void setUp() {
        manager = mock(AuthenticationTokenManager.class);
        resource = new AuthenticationTokenResource(manager);
    }

    @Test
    public void testCheckTokenValidityReturnsOkIfTokenIsValid() {
        when(manager.isValid(anyString())).thenReturn(true);

        Response response = resource.checkTokenValidity("21l2kjh253424");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        verify(manager).isValid("21l2kjh253424");
        verifyNoMoreInteractions(manager);
    }

    @Test
    public void testCheckTokenValidityReturnsNotFoundIfTokenIsNotValid() {
        when(manager.isValid(anyString())).thenReturn(false);

        Response response = resource.checkTokenValidity("lky4hq32kk");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        verify(manager).isValid("lky4hq32kk");
        verifyNoMoreInteractions(manager);
    }
}