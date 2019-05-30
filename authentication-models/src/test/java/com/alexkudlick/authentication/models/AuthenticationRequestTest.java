package com.alexkudlick.authentication.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AuthenticationRequestTest {

    @Test
    public void testDeserialize() throws IOException {
        String json = "{" +
            "\"userName\":\"testUserName\"," +
            "\"password\":\"testPassword\"" +
        "}";
        AuthenticationRequest deserialized = new ObjectMapper().readValue(json, AuthenticationRequest.class);
        assertEquals(
            new AuthenticationRequest("testUserName", "testPassword"),
            deserialized
        );
    }

}
