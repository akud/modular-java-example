package com.alexkudlick.authentication.application.tokens;

import com.google.common.cache.LoadingCache;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class AuthenticationTokenManagerTest {

    private LoadingCache<String, String> cache;
    private AuthenticationTokenManager manager;

    @Before
    public void setUp() {
        cache = mock(LoadingCache.class);
        manager = new AuthenticationTokenManager(cache);
    }

    @Test
    public void testTokenIsValidIfInCache() {
        when(cache.getIfPresent(anyString())).thenReturn("asdfasdfasdf");

        assertTrue(manager.isValid("asdfasdfasdf"));

        verify(cache).getIfPresent("asdfasdfasdf");
        verifyNoMoreInteractions(cache);
    }

    @Test
    public void testTokenIsInValidIfNotInCache() {
        when(cache.getIfPresent(anyString())).thenReturn(null);

        assertFalse(manager.isValid("asdfasdfasdf"));

        verify(cache).getIfPresent("asdfasdfasdf");
        verifyNoMoreInteractions(cache);
    }

}