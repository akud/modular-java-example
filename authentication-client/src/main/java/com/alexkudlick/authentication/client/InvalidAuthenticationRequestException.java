package com.alexkudlick.authentication.client;

import com.alexkudlick.authentication.models.ErrorResponse;
import org.apache.http.HttpEntity;

public class InvalidAuthenticationRequestException extends RuntimeException {
    public InvalidAuthenticationRequestException(ErrorResponse errorResponse) {
        super("Invalid request: " + errorResponse);
    }
}
