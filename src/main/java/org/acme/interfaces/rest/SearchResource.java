package org.acme.interfaces.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.application.usecase.SearchUseCase;
import org.acme.infrastructure.security.AuthContext;

import java.util.List;
import java.util.Map;

@Path("/api/search")
@Produces(MediaType.APPLICATION_JSON)
public class SearchResource {

    @Inject AuthContext authContext;
    @Inject SearchUseCase searchUseCase;

    @GET
    public Response search(@QueryParam("q") String q) {
        if (q == null || q.trim().length() < 2) {
            return Response.ok(Map.of("lists", List.of(), "tasks", List.of())).build();
        }
        return Response.ok(searchUseCase.execute(q.trim(), authContext.getUser().getId())).build();
    }
}
