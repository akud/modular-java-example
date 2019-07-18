package com.alexkudlick.authentication.application.web;

import com.alexkudlick.authentication.models.ErrorResponse;
import org.hibernate.exception.ConstraintViolationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        return Response.status(Response.Status.CONFLICT)
            .entity(new ErrorResponse("duplicate user name"))
            .build();
    }
}
