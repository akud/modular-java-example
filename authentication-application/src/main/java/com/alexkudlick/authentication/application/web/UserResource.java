package com.alexkudlick.authentication.application.web;

import com.alexkudlick.authentication.application.dao.UserDAO;
import com.alexkudlick.authentication.application.models.UserRequest;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Path("/api/users/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserDAO userDAO;

    public UserResource(UserDAO userDAO) {
        this.userDAO = Objects.requireNonNull(userDAO);
    }

    @POST
    @UnitOfWork
    public Response createUser(UserRequest userRequest) {
        userDAO.createUser(userRequest.getUserName(), userRequest.getPassword());
        return Response.status(Response.Status.CREATED).build();
    }
}
