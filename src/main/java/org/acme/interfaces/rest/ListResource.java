package org.acme.interfaces.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.application.dto.CreateTaskListDto;
import org.acme.application.dto.UpdateTaskListDto;
import org.acme.application.usecase.*;
import org.acme.infrastructure.security.AuthContext;

import java.util.Map;
import java.util.UUID;

@Path("/api/lists")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ListResource {

    @Inject AuthContext authContext;
    @Inject GetListsUseCase getListsUseCase;
    @Inject GetListByIdUseCase getListByIdUseCase;
    @Inject CreateListUseCase createListUseCase;
    @Inject UpdateListUseCase updateListUseCase;
    @Inject DeleteListUseCase deleteListUseCase;

    @GET
    public Response getLists() {
        return Response.ok(getListsUseCase.execute(authContext.getUser().getId())).build();
    }

    @GET
    @Path("/{id}")
    public Response getListById(@PathParam("id") UUID id) {
        return getListByIdUseCase.execute(id, authContext.getUser().getId())
                .map(list -> Response.ok(list).build())
                .orElse(Response.status(404).entity(Map.of("message", "Lista no encontrada")).build());
    }

    @POST
    public Response createList(@Valid CreateTaskListDto dto) {
        return Response.status(201).entity(createListUseCase.execute(dto)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateList(@PathParam("id") UUID id, UpdateTaskListDto dto) {
        return updateListUseCase.execute(id, dto, authContext.getUser().getId())
                .map(list -> Response.ok(list).build())
                .orElse(Response.status(404).entity(Map.of("message", "Lista no encontrada")).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteList(@PathParam("id") UUID id) {
        boolean deleted = deleteListUseCase.execute(id, authContext.getUser().getId());
        return deleted
                ? Response.noContent().build()
                : Response.status(404).entity(Map.of("message", "Lista no encontrada")).build();
    }
}
