package com.alexkudlick.authentication.client;

import com.alexkudlick.authentication.models.AuthenticationRequest;
import com.alexkudlick.authentication.models.AuthenticationToken;
import com.alexkudlick.authentication.models.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;


public class AuthenticationClient {

    private static final ObjectMapper SERIALIZER = new ObjectMapper();

    private final HttpClient httpClient;
    private final URL baseURL;

    public AuthenticationClient(HttpClient httpClient, URL serviceUrl) {
        this.httpClient = Objects.requireNonNull(httpClient);
        this.baseURL = Objects.requireNonNull(serviceUrl);
    }

    public static AuthenticationClient buildDefaultClient(URL serviceUrl) {
        return new AuthenticationClient(
            HttpClientBuilder.create().build(),
            serviceUrl
        );
    }

    /**
     * Create a user
     *
     * @param userName the user name to use
     * @param password the password to use
     * @throws IOException                           if there is an error connecting to the authentication service
     * @throws DuplicationUserNameException          if the user name provided is a duplicate
     * @throws InvalidAuthenticationRequestException if the supplied userName or password are invalid
     */
    public void createUser(String userName, String password)
        throws IOException, DuplicationUserNameException, InvalidAuthenticationRequestException {
        HttpResponse response = post("/api/users/", new AuthenticationRequest(userName, password));
        validateNoConflict(userName, response);
    }

    /**
     * Attempt to tryLogin with user credentials, getting an {@link AuthenticationToken} if the credentials were correct
     *
     * @param userName the user name to use
     * @param password the password to use
     * @return an {@link AuthenticationToken} for the provided user
     * @throws IOException                           if there is an error connecting to the authentication service
     * @throws InvalidAuthenticationRequestException if the supplied userName or password are invalid
     */
    public Optional<AuthenticationToken> tryLogin(String userName, String password)
        throws IOException, InvalidAuthenticationRequestException {
        HttpResponse response = post("/api/tokens/", new AuthenticationRequest(userName, password));
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return Optional.of(parseResponseEntity(response.getEntity(), AuthenticationToken.class));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Check whether an {@link AuthenticationToken} is valid
     *
     * @param authenticationToken the authentication token to check
     * @return true if the token is valid, false if it is not
     * @throws IOException                           if there is an error connecting to the authentication service
     * @throws InvalidAuthenticationRequestException if the request was invalid
     */
    public boolean isValid(AuthenticationToken authenticationToken)
        throws IOException, InvalidAuthenticationRequestException {
        HttpResponse response = get("/api/tokens/" + authenticationToken.getToken() + "/");
        return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
    }

    private HttpResponse get(String path) throws IOException {
        HttpResponse response = httpClient.execute(new HttpGet(makeUrl(path)));
        validateResponse(response);
        return response;
    }

    private HttpResponse post(String path, Object jsonPayload) throws IOException {
        HttpPost request = new HttpPost(makeUrl(path));
        request.setEntity(
            new StringEntity(
                SERIALIZER.writeValueAsString(jsonPayload),
                ContentType.APPLICATION_JSON
            )
        );
        HttpResponse response = httpClient.execute(request);
        validateResponse(response);
        return response;
    }

    private <T> T parseResponseEntity(HttpEntity entity, Class<T> responseClass) throws IOException {
        return SERIALIZER.readValue(getResponseContent(entity), responseClass);
    }

    private String getResponseContent(HttpEntity entity) throws IOException {
        try (InputStream inputStream = entity.getContent()) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }

    private void validateNoConflict(String userName, HttpResponse response) throws DuplicationUserNameException {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CONFLICT) {
            throw new DuplicationUserNameException(userName);
        }
    }

    private void validateResponse(HttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNPROCESSABLE_ENTITY) {
            throw new InvalidAuthenticationRequestException(
                parseResponseEntity(response.getEntity(), ErrorResponse.class)
            );
        } else if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            throw new AuthenticationServerErrorException();
        }
    }

    private String makeUrl(String path) {
        try {
            return new URL(baseURL, path).toString();
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid makeUrl " + baseURL + "/" + path);
        }
    }
}
