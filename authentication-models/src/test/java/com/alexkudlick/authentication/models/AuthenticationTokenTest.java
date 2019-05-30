package com.alexkudlick.authentication.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AuthenticationTokenTest {


    @Test
    public void testDeserialize() throws IOException {
        String json = "{" +
            "\"token\":\"testToken\"" +
            "}";
        AuthenticationToken deserialized = new ObjectMapper().readValue(json, AuthenticationToken.class);
        assertEquals(
            new AuthenticationToken("testToken"),
            deserialized
        );
    }

    @Test
    public void testSerialize() throws IOException {
        AuthenticationToken token = new AuthenticationToken("testToken");
        String expectedJson = "{" +
            "\"token\":\"testToken\"" +
            "}";
        String serialized = new ObjectMapper().writeValueAsString(token);
        assertEquals(
            expectedJson,
            serialized
        );
    }

}
