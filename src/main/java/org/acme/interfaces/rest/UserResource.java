package org.acme.interfaces.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.application.dto.RegisterUserDto;
import org.acme.application.usecase.RegisterUserUseCase;
import org.acme.domain.models.User;
import org.acme.domain.repository.UserRepository;
import org.acme.infrastructure.security.AuthContext;

import java.util.Map;

@Path("/api/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    RegisterUserUseCase registerUserUseCase;

    @Inject
    AuthContext authContext;

    @Inject
    UserRepository userRepository;

    public UserResource(RegisterUserUseCase registerUserUseCase, AuthContext authContext, UserRepository userRepository){
        this.registerUserUseCase = registerUserUseCase;
        this.authContext = authContext;
        this.userRepository = userRepository;
    }

    @POST
    public Response registerUser(@Valid RegisterUserDto registerUserDto) {
        try {
            return Response.ok(registerUserUseCase.execute(registerUserDto)).build();
        }catch (Exception e){
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/me")
    public Response getMe() {
        User u = authContext.getUser();
        Map<String, Object> body = new java.util.LinkedHashMap<>();
        body.put("id", u.getId().toString());
        body.put("email", u.getEmail());
        body.put("fullName", u.getFullName());
        body.put("role", u.getRole());
        body.put("active", u.isActive());
        body.put("avatarUrl", u.getAvatarUrl());
        return Response.ok(body).build();
    }

    @PATCH
    @Path("/avatar")
    public Response updateAvatar(Map<String, String> body) {
        String avatarUrl = body.get("avatarUrl");
        if (avatarUrl == null || avatarUrl.isBlank()) {
            return Response.status(400).entity(Map.of("message", "avatarUrl es obligatorio")).build();
        }
        return userRepository.updateAvatar(authContext.getUser().getId(), avatarUrl)
                .map(u -> {
                    Map<String, Object> res = new java.util.LinkedHashMap<>();
                    res.put("id", u.getId().toString());
                    res.put("email", u.getEmail());
                    res.put("fullName", u.getFullName());
                    res.put("role", u.getRole());
                    res.put("active", u.isActive());
                    res.put("avatarUrl", u.getAvatarUrl());
                    return Response.ok(res).build();
                })
                .orElse(Response.status(404).entity(Map.of("message", "Usuario no encontrado")).build());
    }
}
