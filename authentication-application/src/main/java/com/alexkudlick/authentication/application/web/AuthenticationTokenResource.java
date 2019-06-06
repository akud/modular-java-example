package com.alexkudlick.authentication.application.web;


import com.alexkudlick.authentication.application.tokens.AuthenticationTokenManager;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Path("/api/tokens/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationTokenResource {

    private final AuthenticationTokenManager manager;

    public AuthenticationTokenResource(AuthenticationTokenManager manager) {
        this.manager = Objects.requireNonNull(manager);
    }

    @GET
    @Path("{token}/")
    public Response checkTokenValidity(@PathParam("token") String token) {
        if (manager.isValid(token)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
