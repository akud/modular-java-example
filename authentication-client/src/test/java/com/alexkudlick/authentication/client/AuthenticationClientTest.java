package com.alexkudlick.authentication.client;

import com.alexkudlick.authentication.models.AuthenticationRequest;
import com.alexkudlick.authentication.models.AuthenticationToken;
import com.alexkudlick.authentication.models.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.mockito.internal.invocation.ArgumentMatcherAction;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class AuthenticationClientTest {

    private HttpClient httpClient;
    private AuthenticationClient authenticationClient;

    @Before
    public void setUp() throws MalformedURLException {
        httpClient = mock(HttpClient.class);
        authenticationClient = new AuthenticationClient(httpClient, new URL("http://auth.example.com"));
    }

    @Test
    public void testCreateUser() throws IOException, DuplicationUserNameException {
        HttpResponse response = mockResponse();
        when(httpClient.execute(isA(HttpPost.class))).thenReturn(response);

        authenticationClient.createUser("foo", "bar");

        verify(httpClient).execute(
            argThat(isPost("/api/users/", new AuthenticationRequest("foo", "bar")))
        );
        verifyNoMoreInteractions(httpClient);
    }

    @Test
    public void testCreateUserThrowsOn409s() throws IOException, DuplicationUserNameException {
        HttpResponse response = mockResponse(HttpStatus.SC_CONFLICT, new ErrorResponse("duplicate"));
        when(httpClient.execute(isA(HttpPost.class))).thenReturn(response);

        try {
            authenticationClient.createUser("foo1", "bar1");
            fail();
        } catch (DuplicationUserNameException e) {
            // Expected
        }

        verify(httpClient).execute(
            argThat(isPost("/api/users/", new AuthenticationRequest("foo1", "bar1")))
        );
        verifyNoMoreInteractions(httpClient);
    }

    @Test
    public void testSuccessfulLogin() throws IOException {
        AuthenticationToken token = new AuthenticationToken("asdfhijk");
        HttpResponse response = mockResponse(token);
        when(httpClient.execute(isA(HttpPost.class))).thenReturn(response);

        assertEquals(Optional.of(token), authenticationClient.tryLogin("user123", "pass123"));

        verify(httpClient).execute(
            argThat(isPost("/api/tokens/", new AuthenticationRequest("user123", "pass123")))
        );
        verifyNoMoreInteractions(httpClient);
    }

    @Test
    public void testUnsuccessfulLogin() throws IOException {
        HttpResponse response = mockResponse(HttpStatus.SC_NOT_FOUND);
        when(httpClient.execute(isA(HttpPost.class))).thenReturn(response);

        assertEquals(Optional.empty(), authenticationClient.tryLogin("user123", "pass123"));

        verify(httpClient).execute(
            argThat(isPost("/api/tokens/", new AuthenticationRequest("user123", "pass123")))
        );
        verifyNoMoreInteractions(httpClient);
    }

    @Test
    public void testCheckValidityWithValidToken() throws IOException {
        HttpResponse response = mockResponse();
        when(httpClient.execute(isA(HttpGet.class))).thenReturn(response);

        assertTrue(authenticationClient.isValid(new AuthenticationToken("63l45kj235kkj")));

        verify(httpClient).execute(argThat(isGetTo("/api/tokens/63l45kj235kkj/")));
        verifyNoMoreInteractions(httpClient);
    }

    @Test
    public void testCheckValidityWithInvalidToken() throws IOException {
        HttpResponse response = mockResponse(HttpStatus.SC_NOT_FOUND);
        when(httpClient.execute(isA(HttpGet.class))).thenReturn(response);

        assertFalse(authenticationClient.isValid(new AuthenticationToken("3kj312145")));

        verify(httpClient).execute(argThat(isGetTo("/api/tokens/3kj312145/")));
        verifyNoMoreInteractions(httpClient);

    }

    private static ArgumentMatcher<HttpGet> isGetTo(String path) {
        return new HamcrestArgumentMatcher<>(new TypeSafeMatcher<HttpGet>() {
            @Override
            protected boolean matchesSafely(HttpGet item) {
                return URI.create("http://auth.example.com" + path).equals(item.getURI());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Http GET to " + path);
            }
        });
    }

    private static ArgumentMatcher<HttpPost> isPost(String path, Object payload) throws JsonProcessingException {
        String serializedPayload = new ObjectMapper().writeValueAsString(payload);

        return new HamcrestArgumentMatcher<>(new TypeSafeMatcher<HttpPost>() {
            @Override
            protected boolean matchesSafely(HttpPost item) {
                try {
                    return item.getURI().equals(URI.create("http://auth.example.com" + path)) &&
                        serializedPayload.equals(
                            IOUtils.toString(item.getEntity().getContent())
                        );
                } catch (IOException e) {
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Http POST to " + path + " with payload " + serializedPayload);
            }
        });
    }

    private static HttpResponse mockResponse(int statusCode) throws IOException {
        return mockResponse(statusCode, ImmutableMap.of());
    }

    private static HttpResponse mockResponse() throws IOException {
        return mockResponse(HttpStatus.SC_OK, ImmutableMap.of());
    }

    private static HttpResponse mockResponse(Object responseEntity) throws IOException {
        return mockResponse(HttpStatus.SC_OK, responseEntity);
    }

    private static HttpResponse mockResponse(int statusCode, Object responseEntity) throws IOException {
        HttpResponse response = mock(HttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);
        StatusLine statusLine = mock(StatusLine.class);

        when(entity.getContent()).thenReturn(
            new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(responseEntity))
        );
        when(statusLine.getStatusCode()).thenReturn(statusCode);

        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        return response;
    }
}