package org.acme.infrastructure.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.util.Set;

@Provider
@Priority(1)
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Set<String> ALLOWED_ORIGINS = Set.of(
        "http://localhost:8081",
        "http://localhost:8082",
        "http://localhost:19006",
        "http://localhost:3000",
        "https://todo-web-orcin.vercel.app"
    );

    private static final String METHODS = "GET, POST, PUT, PATCH, DELETE, OPTIONS";
    private static final String HEADERS = "Content-Type, Authorization";

    private String resolveOrigin(ContainerRequestContext ctx) {
        String origin = ctx.getHeaderString("Origin");
        return (origin != null && ALLOWED_ORIGINS.contains(origin)) ? origin : ALLOWED_ORIGINS.iterator().next();
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (requestContext.getMethod().equalsIgnoreCase("OPTIONS")) {
            requestContext.abortWith(
                Response.ok()
                    .header("Access-Control-Allow-Origin", resolveOrigin(requestContext))
                    .header("Access-Control-Allow-Methods", METHODS)
                    .header("Access-Control-Allow-Headers", HEADERS)
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Max-Age", "86400")
                    .build()
            );
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", resolveOrigin(requestContext));
        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", METHODS);
        responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", HEADERS);
        responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
    }
}
