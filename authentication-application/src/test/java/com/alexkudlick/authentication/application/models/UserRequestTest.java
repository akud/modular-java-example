package com.alexkudlick.authentication.application.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class UserRequestTest {

    @Test
    public void testDeserialize() throws IOException {
        String json = "{" +
            "\"userName\":\"user123\"," +
            "\"password\":\"password123\"" +
            "}";
        UserRequest deserialized = new ObjectMapper().readValue(json, UserRequest.class);
        assertEquals(deserialized, new UserRequest("user123", "password123"));
    }
}