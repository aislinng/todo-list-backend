package org.acme.interfaces.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.application.dto.CreateTaskDto;
import org.acme.application.dto.ToggleTaskDto;
import org.acme.application.dto.UpdateTaskDto;
import org.acme.application.usecase.*;
import org.acme.infrastructure.security.AuthContext;

import java.util.Map;
import java.util.UUID;

@Path("/api/lists/{listId}/tasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {

    @Inject AuthContext authContext;
    @Inject GetTasksUseCase getTasksUseCase;
    @Inject CreateTaskUseCase createTaskUseCase;
    @Inject UpdateTaskUseCase updateTaskUseCase;
    @Inject ToggleTaskUseCase toggleTaskUseCase;
    @Inject DeleteTaskUseCase deleteTaskUseCase;

    @GET
    public Response getTasks(@PathParam("listId") UUID listId) {
        return getTasksUseCase.execute(listId, authContext.getUser().getId())
                .map(tasks -> Response.ok(tasks).build())
                .orElse(Response.status(404).entity(Map.of("message", "Lista no encontrada")).build());
    }

    @POST
    public Response createTask(@PathParam("listId") UUID listId, @Valid CreateTaskDto dto) {
        return createTaskUseCase.execute(listId, dto, authContext.getUser().getId())
                .map(task -> Response.status(201).entity(task).build())
                .orElse(Response.status(404).entity(Map.of("message", "Lista no encontrada")).build());
    }

    @PUT
    @Path("/{taskId}")
    public Response updateTask(@PathParam("listId") UUID listId,
                               @PathParam("taskId") UUID taskId,
                               UpdateTaskDto dto) {
        return updateTaskUseCase.execute(listId, taskId, dto, authContext.getUser().getId())
                .map(task -> Response.ok(task).build())
                .orElse(Response.status(404).entity(Map.of("message", "Tarea no encontrada")).build());
    }

    @PATCH
    @Path("/{taskId}")
    public Response toggleTask(@PathParam("listId") UUID listId,
                               @PathParam("taskId") UUID taskId,
                               ToggleTaskDto dto) {
        return toggleTaskUseCase.execute(listId, taskId, dto, authContext.getUser().getId())
                .map(task -> Response.ok(task).build())
                .orElse(Response.status(404).entity(Map.of("message", "Tarea no encontrada")).build());
    }

    @DELETE
    @Path("/{taskId}")
    public Response deleteTask(@PathParam("listId") UUID listId,
                               @PathParam("taskId") UUID taskId) {
        boolean deleted = deleteTaskUseCase.execute(listId, taskId, authContext.getUser().getId());
        return deleted
                ? Response.noContent().build()
                : Response.status(404).entity(Map.of("message", "Tarea no encontrada")).build();
    }
}
