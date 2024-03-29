package com.alexkudlick.authentication.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testSerialize() throws IOException {
        AuthenticationRequest request = new AuthenticationRequest("testUserName", "testPassword");
        String expectedJson1 = "{" +
                "\"userName\":\"testUserName\"," +
                "\"password\":\"testPassword\"" +
                "}";
        String expectedJson2 = "{" +
                "\"password\":\"testPassword\"," +
                "\"userName\":\"testUserName\"" +
                "}";
        String serialized = new ObjectMapper().writeValueAsString(request);
        assertTrue(expectedJson2.equals(serialized) || expectedJson1.equals(serialized));
    }

}
